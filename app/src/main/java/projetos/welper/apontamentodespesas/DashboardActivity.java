package projetos.welper.apontamentodespesas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class DashboardActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
    }

    public void selecionarOpcao(View view) {
        TextView textView = (TextView) view;
        String opcao = "Opção: " + textView.getText().toString();

        if( textView.getText().toString().equals(getString(R.string.nova_despesa)) ){
            startActivity(new Intent(this,DespesaActivity.class));
        } else if(textView.getText().toString().equals(getString(R.string.list_despesas))  ) {
            startActivity(new Intent(this,DespesasListActivity.class));
        } else if(textView.getText().toString().equals(getString(R.string.relatorios))  ) {
            startActivity(new Intent(this,RelatorioActivityActivity.class));
        } else if(textView.getText().toString().equals(getString(R.string.categorias))  ) {
            startActivity(new Intent(this,CategoriaActivity.class));
        } else {
            startActivity(new Intent(this, AlterarSenhaActivity.class));
        }
    }

    public void sair(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN); finish();
    }

}
