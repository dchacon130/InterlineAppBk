package com.interlineappv30;

/**
 * Created by INTEL on 21/10/2017.
 */

public class ValoresClass {

    String documento, referencia_factura, fecha_documento, fecha_vc_mto, saldo;

    public ValoresClass(String documento,
                        String referencia_factura,
                        String fecha_documento,
                        String fecha_vc_mto,
                        String saldo) {
        super();
        this.documento = documento;
        this.referencia_factura = referencia_factura;
        this.fecha_documento = fecha_documento;
        this.fecha_vc_mto = fecha_vc_mto;
        this.saldo = saldo;
    }

    public String getdocumento() {
        return documento;
    }
    public void setdocumento(String documento) {
        this.documento = documento;
    }

    public String getreferencia_factura() {
        return referencia_factura;
    }
    public void setreferencia_factura(String referencia_factura) {
        this.referencia_factura = referencia_factura;
    }

    public String getfecha_documento() {
        return fecha_documento;
    }
    public void setfecha_documento(String fecha_documento) {
        this.fecha_documento = fecha_documento;
    }

    public String getfecha_vc_mto() {
        return fecha_vc_mto;
    }
    public void setfecha_vc_mto(String fecha_vc_mto) {
        this.fecha_vc_mto = fecha_vc_mto;
    }

    public String getsaldo() {
        return saldo;
    }
    public void setsaldo(String saldo) {
        this.saldo = saldo;
    }
}
