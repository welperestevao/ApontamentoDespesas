package projetos.welper.apontamentodespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import projetos.welper.apontamentodespesas.bd.BdCore;
import projetos.welper.apontamentodespesas.model.Usuario;

/**
 * Created by welper on 02/07/2015.
 */
public class LoginDao {

    private SQLiteDatabase bd;

    public LoginDao(Context context) {
        BdCore auxBd = new BdCore(context);
        bd = auxBd.getWritableDatabase();
    }


    public Long insertEntry(String userName,String password)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("login", userName);
        newValues.put("senha",password);

        // Insert the row into your table
        return bd.insert("usuario", null, newValues);
    }
    public int deleteEntry(String UserName)
    {
        //String id=String.valueOf(ID);
        String where="login=?";
        int numberOFEntriesDeleted= bd.delete("usuario", where, new String[]{UserName}) ;
        return numberOFEntriesDeleted;
    }
    public Usuario getUsuario(String userName){
        boolean existe = true;
        Cursor cursor = bd.rawQuery("SELECT _idUsuario, login, senha FROM usuario ", null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            Usuario u = new Usuario( cursor.getLong(0), cursor.getString(2), cursor.getString(1));
            cursor.close();
            return u;
        }
        cursor.close();
        return null;
    }

    public int  updateEntry(String senha,String login){
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("senha", senha);
        String where="login = ?";
        return bd.update("usuario", updatedValues, where, new String[]{login});
    }

    public void fecharDB(){
        bd.close();
    }

}
