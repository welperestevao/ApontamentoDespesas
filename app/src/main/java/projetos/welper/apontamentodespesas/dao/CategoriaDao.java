package projetos.welper.apontamentodespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import projetos.welper.apontamentodespesas.helper.DatabaseHelper;
import projetos.welper.apontamentodespesas.model.Categoria;

/**
 * Created by welper on 28/06/2015.
 */
public class CategoriaDao  {

    private DatabaseHelper helper;

    public CategoriaDao(Context context){
        helper = new DatabaseHelper(context);
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
        return helper.getReadableDatabase().rawQuery("SELECT " + DatabaseHelper.Categoria._ID
                                            + ", " + DatabaseHelper.Categoria.DESCRICAO
                                            + " FROM categoria "
                                            + " ORDER BY " + DatabaseHelper.Categoria.DESCRICAO, null);
    }


    public boolean remover(String id_) {
        String whereClause = DatabaseHelper.Categoria._ID + " = ?";
        String[] whereArgs = new String[]{ id_ };
        int removidos = helper.getWritableDatabase().delete(DatabaseHelper.TB_CATEGORIA, whereClause, whereArgs);
        return removidos > 0;
    }

    private Categoria criaCategoria(Cursor cursor) {
        Categoria c = new Categoria(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Categoria._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Categoria.DESCRICAO)));
        return c;
    }

    public Long inserir(Categoria cat){
        return helper.getWritableDatabase().insert(DatabaseHelper.TB_CATEGORIA, null, getContentValues(cat));
    }

    private ContentValues getContentValues(Categoria cat) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Categoria.DESCRICAO, cat.getDescricao());
        values.put(DatabaseHelper.Categoria._ID, cat.getId());
        return values;
    }

    public int atualizar(Categoria cat){
        return helper.getReadableDatabase().update(DatabaseHelper.TB_CATEGORIA,
                getContentValues(cat),
                DatabaseHelper.Categoria._ID + " = ?",
                new String[]{cat.getId().toString()});
    }

}
