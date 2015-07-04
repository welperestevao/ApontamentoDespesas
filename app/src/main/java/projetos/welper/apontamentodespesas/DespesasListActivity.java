package projetos.welper.apontamentodespesas;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projetos.welper.apontamentodespesas.adapter.AdapterDespesa;
import projetos.welper.apontamentodespesas.dao.DespesaDao;
import projetos.welper.apontamentodespesas.model.Despesa;


public class DespesasListActivity extends ListActivity implements DialogInterface.OnClickListener {

    public final int EDITAR = 0;
    public final int REMOVER = 1;
    private List< Map<String, Object> > despesas;
    private SimpleDateFormat dateFormat;
    private AlertDialog alertDialog;
    private String idDespSelecionada;
    private Integer posDespesaSelecionada;
    private Map<String, Object> despesaSelecionada;
    private DespesaDao dao;
    private List<Despesa> listaDespesas;
    private Despesa despesaSelected;
    private AdapterDespesa adapter;
    private long TotalRowCount;
    private int totalRecordPerCall = 20;
    boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.despesas_list_activity);

        dao = new DespesaDao(this);
        dao.abreConexao();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        despesaSelecionada = new HashMap<String,Object>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        inicializaListView();

        this.alertDialog = criaAlertDialog();
    }

    private void inicializaListView() {
        listaDespesas = new ArrayList<Despesa>();
        listaDespesas.addAll(dao.getDespesas());
        adapter = new AdapterDespesa(listaDespesas,this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int pos, long id) {
        super.onListItemClick(listView, v, pos, id);
        despesaSelected = listaDespesas.get(pos);
        posDespesaSelecionada = pos;
        this.alertDialog.show();
    }

    private void removerDespesa(Despesa despesa_) {
        if( dao.removerDespesa(despesa_.getId().toString()) ){
            Toast.makeText(this, "Despesa excluida com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao excluir despesa", Toast.LENGTH_LONG).show();
        }

    }

    private List<Map<String, Object>> listarDespesas(){
        despesas = dao.getMapDespesas();
        return despesas;
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {

        Intent intent;
        switch (item){
            case EDITAR:
                intent = new Intent(this,DespesaActivity.class);
                intent.putExtra("despesa", despesaSelected);
                startActivity(intent);
                break;

            case REMOVER:
                removerDespesa(despesaSelected);
                listaDespesas.remove(posDespesaSelecionada.intValue());
                getListView().invalidateViews();
                break;

            case 2:
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        dao.fechaConexao();
    }

    public void menu(View view){
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
