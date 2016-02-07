package es.frios.cronogasto;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;



public class IngresosCG extends Activity {
    //VBLES GLOBALES DE LA CLASE
    private SQLiteDatabase bd;
    private ArrayList<G> listaI = new ArrayList<>();
    private CG cg;
    private int totalI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ingresos_cg);

        //CREAR / ABRIR LA BBDD EN MODO ESCRITURA
        CGSQLite cgsqlite = new CGSQLite(this, "DBCG", null, 1);
        bd = cgsqlite.getWritableDatabase();


        //Obtener los datos del cronogasto en el que se ha entrado. Se lo envía el activity DetalleCG
        Intent i = getIntent();
        cg = (CG)i.getSerializableExtra("CG");


        //Asignamos al textView de la fecha la fecha Actual
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
        ((TextView)findViewById(R.id.textFechaCGIngresosCG)).setText(fecha);

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


    @Override
    protected void onResume() {
        super.onResume();
        //Deberíamos de coger aquí tb el getIntent enviado de MainActivity?????
        actualizar();
        //Listar los gastos correspondientes a este CG en la lista

    }

    public void actualizar(){
        obtenerI();
        montarListaI();
        totalI = totalIngresos();
        ((TextView)findViewById(R.id.textTotalIngresado)).setText(String.valueOf(totalI)+"€");
        ((TextView)findViewById(R.id.textNombreCGIngresosCG)).setText(cg.getNombre());
    }

    public void obtenerI(){
        listaI.clear();
        //Hacer el SELECT
        String[] columnas = new String[]{"id", "idCG","concepto","importe","fecha"};
        //PAra recoger los datos necesitamos un objeto Cursor.
        //La consulta tiene el filtro del cronogasto seleccionado. Hay que tener en cuenta el id del CG en el que se ha entrado
        String sql = "SELECT * FROM ingresos WHERE idCG=" +cg.getId().toString()+ " ORDER BY id DESC";
        Cursor datos = bd.rawQuery(sql, null);
        if (datos.moveToFirst()){
            do{
                String id = datos.getString(0);
                String idCG = datos.getString(1);
                String concepto = datos.getString(2);
                String importe = datos.getString(3);
                String fecha = datos.getString(4);
                //Cuando tenemos todos los datos de un mismo ingreso creamos el objeto y lo añadimos al array
                //En este array, debido al filtro hecho en la consulta, sólo estarán los gastos del CG seleccionado
                G i = new G(id, idCG, concepto, importe, fecha);
                listaI.add(i);
            }while(datos.moveToNext());
        }else{
            Toast toast = Toast.makeText(this, "Aún no se han introducido gastos", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void montarListaI(){
        //Montamos la lista asociándole el adapter
        AdapterI adapter = new AdapterI(this, R.layout.row_i, listaI);
        ((ListView)findViewById(R.id.listI)).setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    public String calcularFecha(){
        //Asignamos al textView de la fecha la fecha Actual
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int nummes = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        //Formateamos la hora para el estandar HH:MM
        String hora_formateada = String.format("%02d:%02d", hora, min);
        String mes = mes(nummes);
        return (dia+" "+mes+" "+year+" "+hora_formateada+"h");

    }

    public void back (View v){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_slide_out_bottom);
    }

    public void insertarIngreso(View v){
        String fecha = ((TextView)findViewById(R.id.textFechaCGIngresosCG)).getText().toString();
        String concepto = ((EditText)findViewById(R.id.editConceptoCGIngresosCG)).getText().toString();
        String importe = ((EditText)findViewById(R.id.editImporteCGIngresosCG)).getText().toString();

        //Añadir el control de campos

        //Para insertar en la BBDD necesitamos un ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put ("idCG", cg.getId());
        contentValues.put("concepto", concepto);
        contentValues.put("importe", importe);
        contentValues.put("fecha", fecha);
        bd.insert("ingresos", null, contentValues);
        //cerrar la ventana para volver a la vista anterior
        /*finish();
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_slide_out_bottom);*/

        //Una vez hecha la inserción ponemos los editText a 0 y actualizamos la fecha y la lista de ingresos
        ((EditText)findViewById(R.id.editImporteCGIngresosCG)).setText("");
        ((EditText)findViewById(R.id.editConceptoCGIngresosCG)).setText("");
        ((TextView)findViewById(R.id.textFechaCGIngresosCG)).setText(calcularFecha());

        actualizar();

    }


    //En caso de pulsar sobre Gastos, ¿debemos enviarle los datos del CG seleccionado o ya Gastos lo tiene por ser la vista predeterminada por ser en la que entra al seleccionar un CG?
    public void irGastos(View v){
        Intent i = new Intent(this, DetalleCG.class);
        i.putExtra("CG", cg);
        startActivity(i);
    }

    public int totalIngresos(){
        String sql = "SELECT SUM (importe) from ingresos where idCG="+cg.getId();
        Cursor cursor = bd.rawQuery(sql, null);
        //Log.i("myapp","Total Gastos: Antes de entrar en el if"+cursor.getString(0));
        if (cursor.moveToFirst()){
            //cursor.close(); para cerrarlo tendría que usar un int auxiliar
            return cursor.getInt(0);
        }else{
            return -1;
        }
    }


    //En caso de pulsar sobre Balance debemos enviarle los datos del CG seleccionado
    public void irBalance(View v){
        Intent i = new Intent(this, Balance.class);
        i.putExtra("CG", cg);
        startActivity(i);
    }




}













