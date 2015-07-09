package projetos.welper.apontamentodespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projetos.welper.apontamentodespesas.helper.DatabaseHelper;
import projetos.welper.apontamentodespesas.model.Categoria;
import projetos.welper.apontamentodespesas.model.Despesa;
import projetos.welper.apontamentodespesas.model.Relatorio;

/**
 * Created by welper on 25/06/2015.
 */
public class DespesaDao extends AbstractDao{

    public DespesaDao(Context context){
        helper = new DatabaseHelper(context);
        abreConexao();
    }

    public List<Despesa> getDespesas(){
        List<Despesa> despesas = new ArrayList<Despesa>();
        Cursor cursor = db.rawQuery("SELECT _id, categoria, data, forma_pgto, descricao, valor FROM despesa " +
                "ORDER BY data DESC, categoria ASC", null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Despesa d = criaDespesa(cursor);
            despesas.add(d);
        }
        cursor.close();
        Log.i("@@@ QTDE DESPESAS", despesas.size() + "");
        return despesas;
    }


    public boolean removerDespesa(String idDespSelecionada) {
        String whereClause = DatabaseHelper.Despesa._ID + " = ?";
        String[] whereArgs = new String[]{ idDespSelecionada };
        int removidos = db.delete(DatabaseHelper.TB_DESPESA, whereClause, whereArgs);
        return removidos > 0;
    }

    private Despesa criaDespesa(Cursor cursor) {
        Despesa despesa = new Despesa(
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Despesa._ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Despesa.CATEGORIA)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Despesa.DESCRICAO)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Despesa.FORMA_PGTO)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Despesa.VALOR)),
                        new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Despesa.DATA))));
        return despesa;
    }

    public long inserirCategoria(Despesa despesa){
        long r = db.insert(DatabaseHelper.TB_DESPESA, null, getContentValuesDespesa(despesa));
        return r;
    }

    private ContentValues getContentValuesDespesa(Despesa despesa) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Despesa.CATEGORIA, despesa.getCategoria());
        values.put(DatabaseHelper.Despesa.DESCRICAO, despesa.getDescricao());
        values.put(DatabaseHelper.Despesa.DATA, despesa.getData().getTime());
        values.put(DatabaseHelper.Despesa.FORMA_PGTO, despesa.getFormaPgto());
        values.put(DatabaseHelper.Despesa.VALOR,  despesa.getValor());
        values.put(DatabaseHelper.Despesa._ID, despesa.getId());
        return values;
    }

    public int atualizarDespesa(Despesa despesa){
        int retorno = db.update(DatabaseHelper.TB_DESPESA,
                getContentValuesDespesa(despesa),
                DatabaseHelper.Despesa._ID + " = ?",
                new String[]{despesa.getId().toString()});
        return retorno;
    }

    public Despesa getUltimoRegistro() {
        Cursor cursor = db.rawQuery("SELECT _id, categoria, data, forma_pgto, descricao, valor FROM despesa " +
                "ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        Despesa d = null;
        while(cursor.moveToNext()){
            d = criaDespesa(cursor);
        }
        cursor.close();
        return d;
    }

    public List<Relatorio> getResumoDespesas() {
        List<Relatorio> relatorios = new ArrayList<Relatorio>();
        Cursor cursor =  db.rawQuery("select categoria, sum(valor) from despesa group by categoria order by categoria", null);
        Log.i("TOTAL REL", cursor.getCount() + "");
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Relatorio r = new Relatorio(
                    cursor.getString(0),
                    NumberFormat.getCurrencyInstance().format(cursor.getDouble(1))
            );
            relatorios.add(r);
        }
        cursor.close();
        return relatorios;
    }

    public List<Categoria> getCategorias(){
        List<Categoria> categorias = new ArrayList<Categoria>();
        Cursor cursor = findAll();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Categoria c = criaCategoria(cursor);
            categorias.add(c);
        }
        cursor.close();
        return categorias;
    }

    private Cursor findAll() {
        return db.rawQuery("SELECT " + DatabaseHelper.Categoria._ID
                + ", " + DatabaseHelper.Categoria.DESCRICAO
                + " FROM categoria "
                + " ORDER BY " + DatabaseHelper.Categoria.DESCRICAO, null);
    }

    private Categoria criaCategoria(Cursor cursor) {
        Categoria c = new Categoria(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Categoria._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Categoria.DESCRICAO)));
        return c;
    }

    public List<Map<String,Object>> getMapObjeto(){
        List<Map<String,Object>> maps = new ArrayList<Map<String, Object>>();
        Cursor cursor = findAll();
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            Map<String, Object> item = new HashMap<String,Object>();
            item.put(DatabaseHelper.Categoria._ID, cursor.getString(0));
            item.put(DatabaseHelper.Categoria.DESCRICAO, cursor.getString(1));
            maps.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return maps;
    }

    public boolean remover(String id_) {
        String whereClause = DatabaseHelper.Categoria._ID + " = ?";
        String[] whereArgs = new String[]{ id_ };
        int removidos = db.delete(DatabaseHelper.TB_CATEGORIA, whereClause, whereArgs);
        return removidos > 0;
    }

    public Long inserirCategoria(Categoria cat){
        return db.insert(DatabaseHelper.TB_CATEGORIA, null, getContentValues(cat));
    }

    private ContentValues getContentValues(Categoria cat) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Categoria.DESCRICAO, cat.getDescricao());
        values.put(DatabaseHelper.Categoria._ID, cat.getId());
        return values;
    }

    public int atualizar(Categoria cat){
        return db.update(DatabaseHelper.TB_CATEGORIA,
                getContentValues(cat),
                DatabaseHelper.Categoria._ID + " = ?",
                new String[]{cat.getId().toString()});
    }

}

