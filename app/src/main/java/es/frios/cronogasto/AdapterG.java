package es.frios.cronogasto;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterG extends ArrayAdapter<G>{
    Context context;
    int resource;
    List<G> data=null;

    public AdapterG(Context context, int resource, List<G> data) {
        super(context, resource, data);
        this.context=context;
        this.resource=resource;
        this.data=data;

    }


    public View getView(int position,View convertView,ViewGroup parent){
        //ATRIBUTOS
        View row = convertView; //view row
        Holder holder = null;//contenedor

        //1- REUTILIZACIÓN DE CELDAS
        if(row == null){
            //tendremos que crear filas/celdas

            //?????
            //INFLATE ES EL ENCARGADO DE UNIR layout -> información a mostrar
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row=inflater.inflate(resource, parent,false);





            holder = new Holder();

            //UNIMOS==> vista con controlador
            holder.textConceptoG = (TextView) row.findViewById(R.id.textConceptoRow_g);
            holder.textImporteG = (TextView) row.findViewById(R.id.textImporteRow_g);
            holder.textFechaG = (TextView) row.findViewById(R.id.textFechaRow_g);


            row.setTag(holder);


        }else{
            //la reutilizaos
            holder=(Holder) row.getTag();
        }




        //PONEMOS LOS TEXTOS EN LOS ELEMENTOS DEL LAYOUT
        holder.textConceptoG.setText(data.get(position).getConcepto());
        holder.textImporteG.setText(data.get(position).getImporte());
        holder.textFechaG.setText(data.get(position).getFecha());

        return row;
    }



    //UNA CLASE QUE REPRESENTA A NUESTRO LAYOUT ROW
    static class Holder{
        //CREAMOS TANTOS ATRIBUTOS COMO OBJETOS TENGA LA VISTA ROW
        TextView textConceptoG;
        TextView textImporteG;
        TextView textFechaG;
    }


}





