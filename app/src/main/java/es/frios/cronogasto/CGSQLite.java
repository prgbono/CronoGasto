package es.frios.cronogasto;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CGSQLite extends SQLiteOpenHelper {

    //SENTENCIAS SQL DE LAS TABLAS
    String tablaCG = "CREATE TABLE IF NOT EXISTS cronogastos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, descripcion VARCHAR, fecha VARCHAR)";
    String tablagastos = "CREATE TABLE IF NOT EXISTS gastos (id INTEGER PRIMARY KEY AUTOINCREMENT, idCG VARCHAR, concepto VARCHAR, importe VARCHAR, fecha VARCHAR)";
    String tablaingresos = "CREATE TABLE IF NOT EXISTS ingresos (id INTEGER PRIMARY KEY AUTOINCREMENT, idCG VARCHAR, concepto VARCHAR, importe VARCHAR, fecha VARCHAR)";


    public CGSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaCG);
        db.execSQL(tablagastos);
        db.execSQL(tablaingresos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
