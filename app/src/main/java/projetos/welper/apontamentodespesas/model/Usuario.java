package projetos.welper.apontamentodespesas.model;

/**
 * Created by welper on 02/07/2015.
 */
public class Usuario {

    private Long id;
    private String login;
    private String senha;

    public Usuario(String senha, String login) {
        this.senha = senha;
        this.login = login;
    }

    public Usuario(Long id, String senha, String login) {
        this.id = id;
        this.senha = senha;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
