package es.frios.cronogasto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;


public class Balance extends Activity {
    //VBLES GLOBALES
    private SQLiteDatabase bd;
    private CG cg;
    private int gastosT, ingresosT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_balance);

        //CREAR / ABRIR LA BBDD EN MODO ESCRITURA
        CGSQLite cgsqlite = new CGSQLite(this, "DBCG", null, 1);
        bd = cgsqlite.getWritableDatabase();


        //Obtener los datos del cronogasto en el que se ha entrado. Se lo envía el activity DetalleCG
        Intent i = getIntent();
        cg = (CG)i.getSerializableExtra("CG");

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.textNombreCGBalanceCG)).setText(cg.getNombre());
        calcularTotales();
        ((TextView)findViewById(R.id.textIngresosBalance)).setText(String.valueOf(ingresosT)+"€");
        ((TextView)findViewById(R.id.textGastosBalance)).setText(String.valueOf(gastosT)+"€");
        ((TextView)findViewById(R.id.textTotalBalance)).setText(String.valueOf(ingresosT-gastosT)+"€");
        if (ingresosT-gastosT >=0){
            ((TextView)findViewById(R.id.textTotalBalance)).setTextColor(getResources().getColor(R.color.ingreso));
        }else{
            ((TextView)findViewById(R.id.textTotalBalance)).setTextColor(getResources().getColor(R.color.gasto));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    public void back (View v){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    public void calcularTotales(){
        //Sentencia de balance: String sql = "select (select sum(importe) from ingresos where idCG="+cg.getId()+") - (select sum (importe) from gastos where idCG="+cg.getId()+")";

        //Cálculo de Ingresos
        String sql = "select sum(importe) from ingresos where idCG="+cg.getId();
        Cursor cursor = bd.rawQuery(sql, null);
        cursor.moveToFirst();
        ingresosT=cursor.getInt(0);

        //Cálculo de Gastos
        sql = "select sum(importe) from gastos where idCG="+cg.getId();
        cursor = bd.rawQuery(sql, null);
        cursor.moveToFirst();
        gastosT=cursor.getInt(0);
        cursor.close();
    }

    public void irIngresos(View v){
        Intent i = new Intent(this, IngresosCG.class);
        i.putExtra("CG", cg);
        startActivity(i);
    }

    public void irGastos(View v){
        Intent i = new Intent(this, DetalleCG.class);
        i.putExtra("CG", cg);
        startActivity(i);
    }

}
