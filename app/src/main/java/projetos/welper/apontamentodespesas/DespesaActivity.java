package projetos.welper.apontamentodespesas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projetos.welper.apontamentodespesas.dao.CategoriaDao;
import projetos.welper.apontamentodespesas.dao.DespesaDao;
import projetos.welper.apontamentodespesas.model.Categoria;
import projetos.welper.apontamentodespesas.model.Despesa;


public class DespesaActivity extends FragmentActivity {

    private int ano, mes, dia;
    private List<Despesa> despesas = new ArrayList<Despesa>();
    private List<Categoria> categorias;
    private Spinner categoriaCbo;
    private Button dtCadastro;
    private Spinner spinnerFormaPgto;
    private EditText valor;
    private EditText descricao;
    private DespesaDao dao;
    private CategoriaDao categoriaDao;
    private Despesa despesa;
    private Despesa despesaSelecionada;
    private Categoria catSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despesa_activity);

        despesa = new Despesa();
        dao = new DespesaDao(this);
        categoriaDao = new CategoriaDao(this);

        categoriaCbo = (Spinner) findViewById(R.id.cboCategorias);
        populaCategorias();
        categoriaCbo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catSelecionada = categorias.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        descricao = (EditText) findViewById(R.id.txtDescricao);

        spinnerFormaPgto = (Spinner) findViewById(R.id.txtFormaPgto);
        ArrayAdapter<CharSequence> adapterForPgto = getArrayAdapterCategorias(R.array.formaPgto_array);
        adapterForPgto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPgto.setAdapter(adapterForPgto);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dtCadastro = (Button) findViewById(R.id.data);
        dtCadastro.setText(dia + "/" + (mes + 1) + "/" + ano);

        if( (Despesa) getIntent().getSerializableExtra("despesa") != null ){
            despesaSelecionada = (Despesa) getIntent().getSerializableExtra("despesa");
            preparaEditar(despesaSelecionada);
        }
    }

    private void populaCategorias() {
        categoriaDao.abreConexao();
        categorias = new ArrayList<Categoria>();
        Categoria c = new Categoria(null, "");
        categorias.add(c);
        categorias.addAll(categoriaDao.getCategorias());
        categoriaCbo.setAdapter(new ArrayAdapter<Categoria>(getBaseContext(), android.R.layout.simple_spinner_item, categorias));
        categoriaDao.fechaConexao();
    }

    private ArrayAdapter<CharSequence> getArrayAdapterCategorias(int categorias_array) {
        return ArrayAdapter.createFromResource(this,
                categorias_array, android.R.layout.simple_spinner_item);
    }

    private void preparaEditar(Despesa despesa) {
        categoriaCbo = (Spinner) findViewById(R.id.cboCategorias);
        int cont = 0;
        for(Categoria cat_ : categorias ){
            if( cat_.getId() != null && cat_.getDescricao().equals(despesa.getCategoria())){
                categoriaCbo.setSelection(cont);
                catSelecionada = cat_;
                break;
            }
            cont++;
        }

        ArrayAdapter<CharSequence> adapterForPgto = getArrayAdapterCategorias(R.array.formaPgto_array);
        spinnerFormaPgto.setSelection(adapterForPgto.getPosition(despesa.getFormaPgto()));

        descricao = (EditText) findViewById(R.id.txtDescricao);
        descricao.setText(despesa.getDescricao());

        valor = (EditText) findViewById(R.id.txtValor);
        valor.setText(despesa.getValor().toString());
        dtCadastro.setText(despesa.getDataFormatada());
    }

    public void selecionarData(View view){
        Dialog d = new DatePickerDialog(this, datePickerListener, ano, mes,dia);
        d.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            ano = selectedYear;
            mes = selectedMonth;
            dia = selectedDay;
            dtCadastro.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    public void menu(View view){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void registrarDespesa(View view) {
        dao.abreConexao();
        Spinner categoria = (Spinner) findViewById(R.id.cboCategorias);
        EditText valor = (EditText) findViewById(R.id.txtValor);
        EditText desc = (EditText) findViewById(R.id.txtDescricao);
        Spinner formaPgto = (Spinner) findViewById(R.id.txtFormaPgto);

        Categoria cat = (Categoria) categoria.getSelectedItem();
        if( cat.getDescricao().isEmpty()){
            Toast.makeText(this, "Preencha uma Categoria", Toast.LENGTH_LONG).show();
            return;
        }

        if( valor.getText().toString().isEmpty()  ){
            Toast.makeText(this, "Preencha o valor", Toast.LENGTH_LONG).show();
            return;
        }

       if( desc.getText().toString().isEmpty()  ){
            Toast.makeText(this, "Preencha a descricao", Toast.LENGTH_LONG).show();
            return;
        }

        if( ( (String) formaPgto.getSelectedItem() ).isEmpty()  ){
            Toast.makeText(this, "Preencha a Forma de Pagamento", Toast.LENGTH_LONG).show();
            return;
        }

        Calendar c = Calendar.getInstance();
        c.set(ano, (mes - 1), dia);
        Date data = c.getTime();

        criaDespesa(categoria, valor, desc, formaPgto, data);

        Long resultado = null;

        if( despesa.getId() != null ){
           resultado = new Long( dao.atualizarDespesa(despesa) );
        } else{
            resultado = dao.inserir(despesa);
            valor.setText("");
            desc.setText("");
        }

        if( resultado != null && resultado.intValue() != -1 ){
            Toast.makeText(this, "Despesa gravada com sucesso", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Erro ao gravar despesa", Toast.LENGTH_LONG).show();
        }
    }

    private void criaDespesa(Spinner categoria, EditText valor, EditText desc, Spinner formaPgto, Date data) {
        despesa = new Despesa(despesaSelecionada != null ? despesaSelecionada.getId() : null,
                            ((Categoria)categoria.getSelectedItem()).getDescricao(),
                            desc.getText().toString(),
                            (String) formaPgto.getSelectedItem(),
                            Double.parseDouble(valor.getText().toString()),
                            data);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        dao.fechaConexao();
    }

}
