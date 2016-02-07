package es.frios.cronogasto;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class InsertarCG extends Activity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //eliminamos título
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_insertar_cg);


        //Fecha introducida automáticamente
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int nummes = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        //Formateamos la hora para el estandar HH:MM
        String hora_formateada = String.format("%02d:%02d", hora, min);
        String mes = mes(nummes);
        String fecha = dia+" "+mes+" "+year+" "+hora_formateada+"h";
        ((TextView)findViewById(R.id.textFechaCGInsertarCG)).setText(fecha);


        //Abrir la BBDD
        CGSQLite cgsqlite = new CGSQLite(this, "DBCG", null, 1);
        db = cgsqlite.getWritableDatabase();


    }


    public String mes(int nummes){
        switch (nummes){
            case 1: return "ENE";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "ABR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AGO";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DIC";
            default: return null;
        }
    }

    public boolean comprobarCampos(){
        String nombre = ((EditText)findViewById(R.id.editNombreCGInsertarCG)).getText().toString().trim();
        String descripcion = ((EditText)findViewById(R.id.editDescripcionCGInsertarCG)).getText().toString().trim();
        if ((nombre.isEmpty()) || (descripcion.isEmpty())){
            return false;
        }else{
            return true;
        }
    }

    public void backButton(View v){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    public void insertarCG(View v){
    /*
    * Poner la fecha automática
    * Comprobar y coger los datos introducidos
    * Abrir la BBDD
    * Hacer la inserción con los datos introducidos en la BBDD. En la tabla CG's
    * Irse al main, que listará todos los CG's disponibles*/
        if (comprobarCampos()){
            String nombre = ((EditText)findViewById(R.id.editNombreCGInsertarCG)).getText().toString();
            String descripcion = ((EditText)findViewById(R.id.editDescripcionCGInsertarCG)).getText().toString();
            String fecha = ((TextView)findViewById(R.id.textFechaCGInsertarCG)).getText().toString();

            //Para la inserción necesitamos un objeto 'ContentValues'
            ContentValues datos = new ContentValues();
            datos.put("nombre", nombre);
            datos.put("descripcion", descripcion);
            datos.put("fecha", fecha);

            db.insert("cronogastos", null, datos);
            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);

        }else{
            Toast toast = Toast.makeText(this, "Campos obligatorios", Toast.LENGTH_SHORT);
            toast.show();
        }


    }



}
