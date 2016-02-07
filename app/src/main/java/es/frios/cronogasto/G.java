package es.frios.cronogasto;

import java.io.Serializable;

public class G implements Serializable {
    private String id, idCG, concepto, importe, fecha;

    //CONSTRUCTOR
    public G(String id, String idCG, String concepto, String importe, String fecha) {
        this.id = id;
        this.idCG = idCG;
        this.concepto = concepto;
        this.importe = importe;
        this.fecha = fecha;
    }


    //GET & SET
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCG() {
        return idCG;
    }

    public void setIdCG(String idCG) {
        this.idCG = idCG;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

