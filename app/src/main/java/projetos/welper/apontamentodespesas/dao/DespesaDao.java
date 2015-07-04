package projetos.welper.apontamentodespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projetos.welper.apontamentodespesas.R;
import projetos.welper.apontamentodespesas.helper.DatabaseHelper;
import projetos.welper.apontamentodespesas.model.Despesa;
import projetos.welper.apontamentodespesas.model.Relatorio;

/**
 * Created by welper on 25/06/2015.
 */
public class DespesaDao extends AbstractDao{

    public DespesaDao(Context context){
        helper = new DatabaseHelper(context);
    }


    public List<Despesa> getDespesas(){
        List<Despesa> despesas = new ArrayList<Despesa>();
        Cursor cursor = findAllDespesas();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Despesa d = criaDespesa(cursor);
            despesas.add(d);
        }
        cursor.close();
        Log.i("@@@ QTDE DESPESAS", despesas.size() + "");
        return despesas;
    }

    private Cursor findAllDespesas() {
        return db.rawQuery("SELECT _id, categoria, data, forma_pgto, descricao, valor FROM despesa " +
                "ORDER BY data DESC, categoria ASC", null);
    }

    public List<Map<String,Object>> getMapDespesas(){
        List<Map<String,Object>> despesas = new ArrayList<Map<String, Object>>();
        Cursor cursor = findAllDespesas();
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            Map<String, Object> item = new HashMap<String,Object>();

            item.put("id", cursor.getString(0));
            item.put("despesa", cursor.getString(1));
            Long data_ = cursor.getLong(2);
            Date data = new Date(data_);
            item.put("data", new SimpleDateFormat("dd/MM/yyyy").format(data));
            item.put("forma_pgto", cursor.getString(3));
            item.put("descricao", cursor.getString(4));
            item.put("total", cursor.getDouble(5));
            item.put("imagem", R.drawable.config);

            despesas.add(item);
            cursor.moveToNext();
        }
        cursor.close();
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

    public long inserir(Despesa despesa){
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

}

