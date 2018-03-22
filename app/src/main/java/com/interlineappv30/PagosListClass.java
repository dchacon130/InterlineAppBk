package com.interlineappv30;

/**
 * Created by INTEL on 6/11/2017.
 */

public class PagosListClass {

    String codigo_cliente, consecutivo, fecha_sys, pago;

    public PagosListClass(String codigo_cliente, String consecutivo, String fecha_sys, String pago) {
        super();
        this.codigo_cliente = codigo_cliente;
        this.consecutivo = consecutivo;
        this.fecha_sys = fecha_sys;
        this.pago = pago;
    }

    public String getcodigo_cliente() {
        return codigo_cliente;
    }
    public void setcodigo_cliented(String codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }
    public String getconsecutivo() {
        return consecutivo;
    }
    public void setconsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }
    public String getfecha_sys() {
        return fecha_sys;
    }
    public void setfecha_sys(String fecha_sys) {
        this.fecha_sys = fecha_sys;
    }
    public String getpago() {
        return pago;
    }
    public void setpago(String pago) {
        this.pago = pago;
    }
}
