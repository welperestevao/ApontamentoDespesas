package projetos.welper.apontamentodespesas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import projetos.welper.apontamentodespesas.dao.LoginDao;
import projetos.welper.apontamentodespesas.model.Usuario;


public class LoginActivity extends Activity {

    private static Usuario usuarioLogado;
    private EditText usuario;
    private EditText senha;

    private LoginDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        usuario = (EditText) findViewById(R.id.txtUsuario);
        senha = (EditText) findViewById(R.id.txtSenha);

        dao = new LoginDao(this);
        dao.abreConexao();
    }

    public void entrarOnClick(View view){
        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();

        Usuario user_ = dao.getUsuario(usuarioInformado);
        usuarioLogado = user_;
        if( user_ != null ){

            if( usuarioInformado.equals(user_.getLogin()) && senhaInformada.equals(user_.getSenha()) ){
                startActivity(new Intent(this,DashboardActivity.class));
            } else {
                String mensagemDeErro = getString(R.string.erro_autenticacao);
                Toast toast = Toast.makeText(this, mensagemDeErro, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            startActivity(new Intent(this,UsuarioActivity.class));
        }
    }

    public static Usuario getUsuario(){
        return usuarioLogado;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        dao.fechaConexao();
    }
}
