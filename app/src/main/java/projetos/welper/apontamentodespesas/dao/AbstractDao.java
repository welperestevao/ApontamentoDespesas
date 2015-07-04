package projetos.welper.apontamentodespesas.dao;

import android.database.sqlite.SQLiteDatabase;

import projetos.welper.apontamentodespesas.helper.DatabaseHelper;

/**
 * Created by welper on 28/06/2015.
 */
public class AbstractDao {

    public DatabaseHelper helper;
    public SQLiteDatabase db;

    public SQLiteDatabase abreConexao() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }
/*
    protected SQLiteDatabase getReadableDb() {
        if (db == null) {
            db = helper.getReadableDatabase();
        }
        return db;
    }
*/
    public void fechaConexao(){
        if( db != null && db.isOpen() ){
            db.close();
        }
    }

}
