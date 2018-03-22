package com.interlineappv30;

/**
 * Created by INTEL on 11/08/2017.
 */

public class LoteClass {

    String producto;
    String nombrePro;
    String lote;
    String fechaExp;
    int mesesAtras;
    int mesesAdelante;

    public LoteClass(String producto, String nombrePro, String lote, String fechaExp, int mesesAtras,
                     int mesesAdelante) {
        super();
        this.producto = producto;
        this.nombrePro = nombrePro;
        this.lote = lote;
        this.fechaExp = fechaExp;
        this.mesesAtras = mesesAtras;
        this.mesesAdelante = mesesAdelante;
    }

    public String getproducto() {
        return producto;
    }
    public void setproducto(String producto) {
        this.producto = producto;
    }
    public String getnombrePro() {
        return nombrePro;
    }
    public void setnombrePro(String nombrePro) {
        this.nombrePro = nombrePro;
    }
    public String getlote() {
        return lote;
    }
    public void setlote(String lote) {
        this.lote = lote;
    }
    public String getfechaExp() {
        return fechaExp;
    }
    public void setfechaExp(String fechaExp) {
        this.fechaExp = fechaExp;
    }

    public int getmesesAtras() {
        return mesesAtras;
    }
    public void setmesesAtras(int mesesAtras) {
        this.mesesAtras = mesesAtras;
    }
    public int getmesesAdelante() {
        return mesesAdelante;
    }
    public void setmesesAdelante(int mesesAdelante) {
        this.mesesAdelante = mesesAdelante;
    }

}

