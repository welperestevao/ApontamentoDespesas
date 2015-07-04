package projetos.welper.apontamentodespesas;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import projetos.welper.apontamentodespesas.adapter.AdapterRelatorio;
import projetos.welper.apontamentodespesas.dao.DespesaDao;
import projetos.welper.apontamentodespesas.model.Relatorio;


public class RelatorioActivityActivity extends ListActivity {

    private AdapterRelatorio adapterRelatorio;
    private DespesaDao dao;
    private List<Relatorio> relatorios;
    private Double total = 0.0;
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatorio_activity);
        relatorios = new ArrayList<Relatorio>();
        montaResultado();
    }

    private void montaResultado() {
        dao = new DespesaDao(this);
        dao.abreConexao();
        relatorios.clear();
        relatorios.addAll(dao.getResumoDespesas());

        for(Relatorio r : relatorios){
           String v = r.getValor().replace("R$","").replace(",",".");
           total += new Double(v.trim());
        }

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotal.setText(NumberFormat.getCurrencyInstance().format(total));
        adapterRelatorio = new AdapterRelatorio(relatorios,this);

        setListAdapter(adapterRelatorio);
    }

    public void menu(View view){
        startActivity(new Intent(this, DashboardActivity.class));
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        dao.fechaConexao();
    }

}
