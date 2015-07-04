package projetos.welper.apontamentodespesas.model;

import java.io.Serializable;

/**
 * Created by welper on 29/06/2015.
 */
public class Relatorio implements Serializable {

    private String categoria;
    private String valor;

    public Relatorio(String categoria, String valor) {
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
