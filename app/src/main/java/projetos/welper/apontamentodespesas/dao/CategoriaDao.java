package projetos.welper.apontamentodespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projetos.welper.apontamentodespesas.helper.DatabaseHelper;
import projetos.welper.apontamentodespesas.model.Categoria;

/**
 * Created by welper on 28/06/2015.
 */
public class CategoriaDao extends AbstractDao {

    public CategoriaDao(Context context){
        helper = new DatabaseHelper(context);
        abreConexao();
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

    public List<Categoria> getConsultaPaginada(int qtdeRegistros,int lasstInScreen){
        Log.i(" ######## QtdeRegistros", "" + qtdeRegistros);
        Log.i(" @@@@@@@@@ LastInScreen", "" + lasstInScreen);
        List<Categoria> categorias = new ArrayList<Categoria>();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.Categoria._ID
                                                    + ", " + DatabaseHelper.Categoria.DESCRICAO
                                                    + " FROM categoria "
                                                    + " ORDER BY " + DatabaseHelper.Categoria.DESCRICAO
                                                    + " LIMIT " + qtdeRegistros + " OFFSET " + lasstInScreen, null);
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
        int removidos = abreConexao().delete(DatabaseHelper.TB_CATEGORIA, whereClause, whereArgs);
        return removidos > 0;
    }

    private Categoria criaCategoria(Cursor cursor) {
        Categoria c = new Categoria(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Categoria._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Categoria.DESCRICAO)));
        return c;
    }

    public Long inserir(Categoria cat){
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
