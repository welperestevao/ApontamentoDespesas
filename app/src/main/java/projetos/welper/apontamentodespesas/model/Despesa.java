package projetos.welper.apontamentodespesas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by welper on 22/06/2015.
 */
public class Despesa implements Serializable{

    private String categoria;
    private String descricao;
    private String formaPgto;
    private Double valor;
    private Date data;
    private Long id;
    private int imagem;

    public Despesa(){

    }

    public Despesa(String categoria, String descricao, String formaPgto, Double valor, Date data) {
        this.categoria = categoria;
        this.descricao = descricao;
        this.formaPgto = formaPgto;
        this.valor = valor;
        this.data = data;
    }

    public Despesa(Long id, String categoria, String descricao, String formaPgto, Double valor, Date data) {
        this.categoria = categoria;
        this.descricao = descricao;
        this.formaPgto = formaPgto;
        this.valor = valor;
        this.data = data;
        this.id = id;
        this.imagem = imagem;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(String formaPgto) {
        this.formaPgto = formaPgto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataFormatada(){
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
