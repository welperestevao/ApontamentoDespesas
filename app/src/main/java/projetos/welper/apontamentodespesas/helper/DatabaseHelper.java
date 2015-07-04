package projetos.welper.apontamentodespesas.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by welper on 22/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "despesas";
    private static final int VERSAO = 1;

    public static final String TB_DESPESA = "despesa";
    public static final String TB_CATEGORIA = "categoria";
    public static final String TB_SUBCATEGORIA = "subcategoria";
    public static final String TB_USUARIO = "usuario";

    public static class Usuario {
        public static final String _ID = "_idUsuario";
        public static final String LOGIN = "login";
        public static final String SENHA = "senha";

        public static final String[] COLUNAS = new String[]{ _ID, LOGIN, SENHA };
    }

    public static class Despesa {
        public static final String _ID = "_id";
        public static final String CATEGORIA = "categoria";
        public static final String VALOR = "valor";
        public static final String FORMA_PGTO = "forma_pgto";
        public static final String DESCRICAO = "descricao";
        public static final String DATA = "data";
        public static final String OBSERVACAO = "observacao";

        public static final String[] COLUNAS = new String[]{ _ID, CATEGORIA, VALOR, DATA, FORMA_PGTO, DESCRICAO };
    }

    public static class Categoria {
        public static final String _ID = "_idCategoria";
        public static final String DESCRICAO = "descricaoCatagoria";

        public static final String[] COLUNAS = new String[]{ _ID, DESCRICAO };
    }


    public static class Relatorio {
        public static final String CATEGORIA = "categoria";
        public static final String TOTAL = "total";

        public static final String[] COLUNAS = new String[]{ CATEGORIA, TOTAL };
    }


    public DatabaseHelper(Context context){
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tabelaDespesa(db);
        tabelaCategoria(db);
        tabelaUsuario(db);
    }


    private void tabelaCategoria(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TB_CATEGORIA +" (" +
                Categoria._ID + " INTEGER, " +
                Categoria.DESCRICAO + " TEXT NOT NULL, " +
                " PRIMARY KEY("+ Categoria._ID +") );");
    }

    private void tabelaDespesa(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_DESPESA
                + "(" + Despesa._ID + " INTEGER PRIMARY KEY, "
                + Despesa.CATEGORIA + " TEXT, "
                + Despesa.VALOR + " DOUBLE, "
                + Despesa.DATA + " DATE, forma_pgto TEXT, "
                + Despesa.DESCRICAO + " TEXT, ");
    }

    private void tabelaUsuario (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TB_USUARIO +" (" +
                DatabaseHelper.Usuario._ID + " INTEGER, " +
                Usuario.LOGIN + " TEXT NOT NULL, " +
                Usuario.SENHA + " TEXT NOT NULL, " +
                " PRIMARY KEY("+ DatabaseHelper.Usuario._ID +") );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
