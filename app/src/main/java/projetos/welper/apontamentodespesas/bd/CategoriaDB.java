package projetos.welper.apontamentodespesas.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import projetos.welper.apontamentodespesas.model.Categoria;

/**
 * Created by welper on 06/07/2015.
 */
public class CategoriaDB {
    private SQLiteDatabase bd;

    public CategoriaDB(Context context)
    {
        BdCore auxBd = new BdCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public Long inserir(Categoria categoria) {
        ContentValues valores = new ContentValues();
        valores.put("descricaoCatagoria", categoria.getDescricao());
        return bd.insert("categoria",null, valores);
    }

    public void atualizar(Categoria categoria){
        ContentValues valores = new ContentValues();
        valores.put("descricaoCatagoria", categoria.getDescricao());
        bd.update("categoria", valores, "_idCategoria = ?", new String[]{"" + categoria.getId()});
    }

    public void excluir(Categoria categoria){
        Categoria cat = categoria;
        bd.delete("categoria", "_idCategoria = " + categoria.getId(), null);
    }

    public List<Categoria> buscar()
    {
        List<Categoria> list = new ArrayList<Categoria>();
        String[] colunas = new String[]{"_idCategoria", "descricaoCatagoria"};
        Cursor cursor = bd.query("categoria",colunas, null, null, null, null, null);

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            do
            {
                Categoria cat = new Categoria();
                cat.setId(cursor.getLong(0));
                cat.setDescricao(cursor.getString(1));
                list.add(cat);

            }while (cursor.moveToNext());
        }
        return (list);
    }

    public void fecharDB(){
        bd.close();
    }
}
