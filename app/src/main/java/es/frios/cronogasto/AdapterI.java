package es.frios.cronogasto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class AdapterI extends ArrayAdapter<G> {
    Context context;
    int resource;
    List<G> data=null;

    public AdapterI(Context context, int resource, List<G> data) {
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
            holder.textConceptoI = (TextView) row.findViewById(R.id.textConceptoRow_i);
            holder.textImporteI = (TextView) row.findViewById(R.id.textImporteRow_i);
            holder.textFechaI = (TextView) row.findViewById(R.id.textfechaRow_i);


            row.setTag(holder);


        }else{
            //la reutilizaos
            holder=(Holder) row.getTag();
        }


        //PONEMOS LOS TEXTOS EN LOS ELEMENTOS DEL LAYOUT
        holder.textConceptoI.setText(data.get(position).getConcepto());
        holder.textImporteI.setText(data.get(position).getImporte());
        holder.textFechaI.setText(data.get(position).getFecha());

        return row;
    }



    //UNA CLASE QUE REPRESENTA A NUESTRO LAYOUT ROW
    static class Holder{
        //CREAMOS TANTOS ATRIBUTOS COMO OBJETOS TENGA LA VISTA ROW
        TextView textConceptoI;
        TextView textImporteI;
        TextView textFechaI;
    }

}