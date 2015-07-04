package projetos.welper.apontamentodespesas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import projetos.welper.apontamentodespesas.dao.LoginDao;


public class AlterarSenhaActivity extends ActionBarActivity {

    private LoginDao dao;

    private EditText senha;
    private EditText confSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterarsenha_activity);

        dao = new LoginDao(this);
        dao.abreConexao();

        senha = (EditText) findViewById(R.id.txtSenha);
        confSenha = (EditText) findViewById(R.id.txtConfSenha);
    }

    public void gravar(View view){

        String senhaInformada = senha.getText().toString();
        String confSenhaInformada = confSenha.getText().toString();

        if( senhaInformada.equals(confSenhaInformada) ){
            if( dao.updateEntry(senhaInformada, LoginActivity.getUsuario().getLogin()) == 1){
                startActivity(new Intent(this,DashboardActivity.class));
            } else {
                Toast toast = Toast.makeText(this, "Erro ao atualizar senha", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "Senhas diferentes. Digite novamente", Toast.LENGTH_SHORT);
            toast.show();
        }
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
