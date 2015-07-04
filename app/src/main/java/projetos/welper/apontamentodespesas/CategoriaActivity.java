package projetos.welper.apontamentodespesas;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projetos.welper.apontamentodespesas.adapter.CategoriaAdapter;
import projetos.welper.apontamentodespesas.dao.CategoriaDao;
import projetos.welper.apontamentodespesas.model.Categoria;


public class CategoriaActivity extends ListActivity implements DialogInterface.OnClickListener{

    public final int REMOVER = 0;
    private android.app.AlertDialog alertDialog;
    private List<Categoria> listaCategorias = new ArrayList<Categoria>();
    private CategoriaDao dao;
    public Categoria catSelecionada;
    private Button btnNovaCat;
    private CategoriaAdapter adapter;
    final Context context = this;

    private int posCatSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoria_activity);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        dao = new CategoriaDao(this);
        inicializaListView();
        this.alertDialog = criaAlertDialog();

        btnNovaCat = (Button) findViewById(R.id.btnNovaCat);
        btnNovaCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.cad_categoria, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);

                final EditText categoria = (EditText) promptView.findViewById(R.id.userInput);
                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Categoria objCategoria = new Categoria();
                                objCategoria.setDescricao(categoria.getText().toString());
                                salvar(objCategoria);
                            }
                        })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
            }
        });
    }

    @Override
    protected void onListItemClick(ListView listView, View v, int pos, long id) {
        super.onListItemClick(listView, v, pos, id);
        catSelecionada = listaCategorias.get(pos);
        posCatSelecionada = pos;
        this.alertDialog.show();
    }

    private android.app.AlertDialog criaAlertDialog(){
        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {"Excluir", "Fechar"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Selecione");
        builder.setItems(items, this);
        return builder.create();
    }

    private void inicializaListView() {
        listaCategorias = new ArrayList<Categoria>();
        listaCategorias.addAll(dao.getCategorias());
        adapter = new CategoriaAdapter(listaCategorias,this);
        setListAdapter(adapter);
    }

    public CategoriaDao getDao() {
        return dao;
    }

    public void salvar(Categoria categoria_) {
        if (getDao().inserir(categoria_).intValue() != -1) {
            Toast.makeText(this, "Categoria gravada com sucesso", Toast.LENGTH_LONG).show();
            inicializaListView();
        } else {
            Toast.makeText(this, "Erro ao gravar despesa", Toast.LENGTH_LONG).show();
        }
    }

    public void menu(View view){
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item){
            case REMOVER:
                dao.remover(catSelecionada.getId().toString());
                listaCategorias.remove(posCatSelecionada);
                getListView().invalidateViews();
                break;
        }
    }
}
