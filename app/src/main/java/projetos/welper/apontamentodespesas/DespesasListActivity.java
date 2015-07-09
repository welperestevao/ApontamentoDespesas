package projetos.welper.apontamentodespesas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projetos.welper.apontamentodespesas.adapter.AdapterDespesa;
import projetos.welper.apontamentodespesas.bd.CategoriaDB;
import projetos.welper.apontamentodespesas.bd.DespesaDB;
import projetos.welper.apontamentodespesas.model.Categoria;
import projetos.welper.apontamentodespesas.model.Despesa;


public class DespesasListActivity extends ListActivity implements DialogInterface.OnClickListener {

    public final int EDITAR = 0;
    public final int REMOVER = 1;
    private int ano, mes, dia;
    private SimpleDateFormat dateFormat;
    private AlertDialog alertDialog;
    private AlertDialog cadDespesaDialog;
    private Integer posDespesaSelecionada;

    private List<Despesa> listaDespesas;
    private Despesa despesaSelecionada;
    private Despesa despesa;
    private AdapterDespesa adapter;

    private List<Categoria> categorias;
    private Categoria catSelecionada;
    private CategoriaDB catDb;
    private DespesaDB despesaDB;

    private Spinner categoriaCbo;
    private Spinner spinnerFormaPgto;
    private Button dtCadastro;
    private Button btnCadDespesa;
    private EditText valor;
    private EditText descricao;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despesas_list_activity);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        btnCadDespesa = (Button) findViewById(R.id.btnCadDesp);
        btnCadDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirModalCadDespesa();
            }
        });

        this.alertDialog = criaAlertDialog();

        catDb = new CategoriaDB(this);
        despesaDB = new DespesaDB(this);
    }

    private void abrirModalCadDespesa() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View dialogView = layoutInflater.inflate(R.layout.dialog_cad_despesa, null);

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(dialogView);
        abrirCadDespesa(dialogView);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final android.support.v7.app.AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gravarDespesa(alertD)) {
                    alertD.dismiss();
                    inicializaListView();
                    despesaSelecionada = null;
                }
            }
        });
    }

    public void abrirCadDespesa(View v){
        categoriaCbo = (Spinner) v.findViewById(R.id.cboCategorias);
        categoriaCbo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catSelecionada = categorias.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        popularCboCategorias();
        descricao = (EditText) v.findViewById(R.id.txtDescricao);
        valor = (EditText) v.findViewById(R.id.txtValor);
        dtCadastro = (Button) v.findViewById(R.id.data);
        spinnerFormaPgto = (Spinner) v.findViewById(R.id.txtFormaPgto);

        ArrayAdapter<CharSequence> adapterForPgto = getArrayAdapterCategorias(R.array.formaPgto_array);
        adapterForPgto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPgto.setAdapter(adapterForPgto);
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dtCadastro.setText(dia + "/" + (mes + 1) + "/" + ano);
        if( despesaSelecionada != null ){
            preparaEditar();
        }
    }

    private void inicializaListView() {
        listaDespesas = new ArrayList<Despesa>();
        listaDespesas.addAll(despesaDB.buscar());
        adapter = new AdapterDespesa(listaDespesas,this);
        setListAdapter(adapter);
    }

    private void popularCboCategorias() {
        categorias = new ArrayList<Categoria>();
        Categoria c = new Categoria(null, "");
        categorias.add(c);
        categorias.addAll(catDb.buscar());
        categoriaCbo.setAdapter(new ArrayAdapter<Categoria>(getBaseContext(), android.R.layout.simple_spinner_item, categorias));
    }

    private ArrayAdapter<CharSequence> getArrayAdapterCategorias(int categorias_array) {
        return ArrayAdapter.createFromResource(this,
                categorias_array, android.R.layout.simple_spinner_item);
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int pos, long id) {
        super.onListItemClick(listView, v, pos, id);
        despesaSelecionada = listaDespesas.get(pos);
        posDespesaSelecionada = pos;
        this.alertDialog.show();
    }

    private void removerDespesa(Despesa despesa_) {
        if( despesaDB.excluir(despesa_) ){
            Toast.makeText(this, "Despesa excluida com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao excluir despesa", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item){
            case EDITAR:
                abrirModalCadDespesa();
                break;

            case REMOVER:
                removerDespesa(despesaSelecionada);
                listaDespesas.remove(posDespesaSelecionada.intValue());
                getListView().invalidateViews();
                break;

            case 2:
                despesaSelecionada = null;
                break;
        }
    }

    private AlertDialog criaAlertDialog(){
        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {" Editar","Excluir"," Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione");
        builder.setItems(items, this);
        return builder.create();
    }

    @Override
    public void onResume(){
        super.onResume();
        inicializaListView();
    }

    private void preparaEditar() {
        popularCboCategorias();
        int cont = 0;
        for(Categoria cat_ : categorias ){
            if( cat_.getId() != null && cat_.getDescricao().equals(despesaSelecionada.getCategoria())){
                categoriaCbo.setSelection(cont);
                catSelecionada = cat_;
                break;
            }
            cont++;
        }

        ArrayAdapter<CharSequence> adapterForPgto = getArrayAdapterCategorias(R.array.formaPgto_array);
        spinnerFormaPgto.setSelection(adapterForPgto.getPosition(despesaSelecionada.getFormaPgto()));
        descricao.setText(despesaSelecionada.getDescricao());
        valor.setText(despesaSelecionada.getValor().toString());
        dtCadastro.setText(despesaSelecionada.getDataFormatada());
    }

    public void selecionarData(View view){
        Dialog d = new DatePickerDialog(this, datePickerListener, ano, mes,dia);
        d.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,  int selectedMonth, int selectedDay) {
            ano = selectedYear;
            mes = selectedMonth;
            dia = selectedDay;
            dtCadastro.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    public void menu(View view){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public boolean gravarDespesa(android.support.v7.app.AlertDialog dialogView) {
        Spinner categoria = (Spinner) dialogView.findViewById(R.id.cboCategorias);
        EditText valor = (EditText) dialogView.findViewById(R.id.txtValor);
        EditText desc = (EditText) dialogView.findViewById(R.id.txtDescricao);
        Spinner formaPgto = (Spinner) dialogView.findViewById(R.id.txtFormaPgto);

        Categoria cat = (Categoria) categoria.getSelectedItem();

        if( cat.getDescricao().trim().isEmpty()){
            Toast.makeText(this, "Preencha uma Categoria", Toast.LENGTH_LONG).show();
            return false;
        }

        if( valor.getText().toString().length() == 0   ){
            Toast.makeText(this, "Preencha o valor", Toast.LENGTH_LONG).show();
            return false;
        }

        if( desc.getText().toString().trim().isEmpty()  ){
            Toast.makeText(this, "Preencha a descricao", Toast.LENGTH_LONG).show();
            return false;
        }

        if( ( (String) formaPgto.getSelectedItem() ).trim().isEmpty()  ){
            Toast.makeText(this, "Preencha a Forma de Pagamento", Toast.LENGTH_LONG).show();
            return false;
        }

        Calendar c = Calendar.getInstance();
        c.set(ano, (mes), dia);
        Date data = c.getTime();

        criaDespesa(categoria, valor, desc, formaPgto, data);

        Long resultado = null;

        if( despesa.getId() != null ){
            resultado = new Long( despesaDB.atualizar(despesa) );
        } else{
            resultado = despesaDB.inserir(despesa);
            valor.setText("");
            desc.setText("");
        }

        if( resultado != null && resultado.intValue() != -1 ){
            Toast.makeText(this, "Despesa gravada com sucesso", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Erro ao gravar despesa", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
        despesaDB.fecharDB();
    }
}
