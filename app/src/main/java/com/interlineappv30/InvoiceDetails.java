package com.interlineappv30;

/**
 * Created by INTEL on 7/09/2017.
 */

class InvoiceDetails {
    public String itemCode;
    public String itemName;
    public String itemLote;
    public String itemFecha;
    public String itemCantidad;
    public String itemObservacion;

    /*DATOS DOCUMENTOS*/
    public String itemDocumento;
    public String itemFactura;
    public String itemFechaDoc;
    public String itemFechaVenc;
    public String itemSaldo;

    /*DATOS PAGOS*/
    public String tipoPago;
    public String numeroCuenta;
    public String tipoBanco;
    public String codigoCuenta;
    public String fechaPago;
    public String valorPago;


    /*DATOS DESCUENTOS*/
    public String odis;
    public String tipoDescuento;
    public String valorDescuento;
    public String obsDescuento;
}
