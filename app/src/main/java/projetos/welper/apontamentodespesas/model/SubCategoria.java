package projetos.welper.apontamentodespesas.model;

import java.io.Serializable;

/**
 * Created by welper on 28/06/2015.
 */
public class SubCategoria implements Serializable {

    private Long id;
    private String descricao;
    private Categoria categoria;

    public SubCategoria() {  }

    public SubCategoria(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public SubCategoria(Long id, String descricao, Categoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
