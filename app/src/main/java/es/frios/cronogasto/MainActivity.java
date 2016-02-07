package es.frios.cronogasto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    //VBLES GLOBALES DE LA CLASE
    private SQLiteDatabase bd;
    private ArrayList<CG> listaCG = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        //CREAR / ABRIR LA BBDD EN MODO ESCRITURA
        CGSQLite cgsqlite = new CGSQLite(this, "DBCG", null, 1);
        bd = cgsqlite.getWritableDatabase();


        //Código para poner el texto del TextView que contiene el título en distintos colores
        TextView txtCGMain = (TextView)findViewById(R.id.textCGMain);
        SpannableString tituloApp = new SpannableString(txtCGMain.getText().toString());
        tituloApp.setSpan(new ForegroundColorSpan(R.color.ppal), 5, 9, 0);



        //EVENTOS
        findViewById(R.id.btnNuevoCGMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, InsertarCG.class));
                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
            }
        });


        //Evento onClick a la lista de Cronogastos. Detalle de cada cronogasto
        ListView listadoCG = (ListView)findViewById(R.id.listCG);
        listadoCG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Obtener cronogasto seleccionado
                CG cgSeleccionado = listaCG.get(position);
                //Pasar cronogasto a la otra activity con los extras
                Intent i = new Intent(MainActivity.this, DetalleCG.class);
                i.putExtra("CG", cgSeleccionado);
                //lanzar la activity
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtener todos los CG's creados
        obtenerCG();

        //Montar la lista de CG's
        montarListaCG();
    }


    public void obtenerCG(){

        //Vaciamos el array
        listaCG.clear();

        Log.i("myapp", "En obtenerCG");

        //Obtenemos los datos
        String[] columnas = new String[]{"id", "nombre", "descripcion", "fecha"};

        //LAnzamos la consulta. En Cursor obtendremos los datos
        Cursor datosCG = bd.query("cronogastos", columnas, null, null, null, null, "id DESC");

        Log.i("myapp", "En obtenerCG antes del recorrido de la tabla cronogastos");

        //Recorrer los datos guardados en datosCG. DatosCG es un array
        if (datosCG.moveToFirst()){
            int i=0;
            do{
                String id = datosCG.getString(0);
                String nombre = datosCG.getString(1);
                String descripcion = datosCG.getString(2);
                String fecha = datosCG.getString(3);
                //Una vez obtenidos los datos creamos el objeto CG y lo añadimos a la lista de CG's
                CG cg = new CG(id, nombre, descripcion, fecha);
                listaCG.add(cg);
                i+=i;
                Log.i("myapp", "Dentro del bucle. i="+i);

            }while (datosCG.moveToNext());

        }else{
            Toast toast = Toast.makeText(this,"No existen datos. ¡Crea tu primer 'CronoGasto' en la parte inferior y empieza a controlar tus cuentas!", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void montarListaCG(){
        //Se crea el adapter y se asocia a la lista
        AdapterCG adapterCG = new AdapterCG(this, R.layout.row_cg, listaCG);
        ((ListView)findViewById(R.id.listCG)).setAdapter(adapterCG);

    }

}
