package projetos.welper.apontamentodespesas.dao;

import android.content.Context;
import android.database.Cursor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import projetos.welper.apontamentodespesas.helper.DatabaseHelper;
import projetos.welper.apontamentodespesas.model.Relatorio;

/**
 * Created by welper on 28/06/2015.
 */
public class RelatorioDao extends AbstractDao {

    public RelatorioDao(Context context){
        helper = new DatabaseHelper(context);
    }

    public List<Relatorio> getRelatorio(){
        List<Relatorio> rels = new ArrayList<Relatorio>();
        Cursor cursor = db.rawQuery(getSQLResumoDespesas(), null);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Relatorio r = criaRelatorio(cursor);
            rels.add(r);
        }
        cursor.close();
        return rels;
    }

    private Relatorio criaRelatorio(Cursor cursor) {
        Relatorio r = new Relatorio(
                cursor.getString(0),
                NumberFormat.getCurrencyInstance().format(cursor.getDouble(1))
        );

        return r;
    }

    private String getSQLResumoDespesas(){
            return "select categoria, sum(valor) from despesa\n" +
                    "group by categoria\n" +
                    "order by categoria" ;
    }
}
