package projetos.welper.apontamentodespesas.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projetos.welper.apontamentodespesas.model.Despesa;

/**
 * Created by welper on 06/07/2015.
 */
public class DespesaDB {
    private SQLiteDatabase bd;

    public DespesaDB(Context context)
    {
        BdCore auxBd = new BdCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public Long inserir(Despesa despesa) {
        ContentValues valores = new ContentValues();
        valores.put("categoria", despesa.getCategoria());
        valores.put("descricao", despesa.getDescricao());
        valores.put("forma_pgto", despesa.getFormaPgto());
        valores.put("valor", despesa.getValor());
        valores.put("data", despesa.getData().getTime());
        return bd.insert("despesa",null, valores);
    }

    public int atualizar(Despesa despesa){
        ContentValues valores = new ContentValues();
        valores.put("categoria", despesa.getCategoria());
        valores.put("descricao", despesa.getDescricao());
        valores.put("forma_pgto", despesa.getFormaPgto());
        valores.put("valor", despesa.getValor());
        valores.put("data", despesa.getData().getTime());
        return bd.update("despesa", valores, "_id = ?", new String[]{"" + despesa.getId()});
    }

    public boolean excluir(Despesa despesa){
        Despesa des = despesa;
        int resultado = bd.delete("despesa", "_id = " + despesa.getId(), null);
        return resultado > 0;
    }

    public List<Despesa> buscar() {
        List<Despesa> list = new ArrayList<Despesa>();
        String[] colunas = new String[]{"_id", "descricao","valor","data","forma_pgto","categoria"};
        Cursor cursor = bd.query("despesa", colunas, null, null, null, null, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                Despesa despesa = new Despesa(
                        cursor.getLong(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("categoria")),
                        cursor.getString(cursor.getColumnIndex("descricao")),
                        cursor.getString(cursor.getColumnIndex("forma_pgto")),
                        cursor.getDouble(cursor.getColumnIndex("valor")),
                        new Date(cursor.getLong(cursor.getColumnIndex("data")))
                        );
                list.add(despesa);
            }while (cursor.moveToNext());
        }
        return (list);
    }

    public void fecharDB(){
        bd.close();
    }
}
