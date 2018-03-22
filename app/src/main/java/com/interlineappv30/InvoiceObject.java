package com.interlineappv30;

import java.util.List;

/**
 * Created by INTEL on 7/09/2017.
 */

class InvoiceObject {
    /*DATOS DEVOLUCIÓN*/
    public String boletoDevolucion;
    public String documentoCliente;
    public String telefonoCliente;
    public String emailCliente;
    public String nombreCliente;
    public String causal;
    public String observacion;

    /*DATOS DE LA EMPRESA*/
    public String codigoEmpresa;
    public String direccionEmpresa;
    public String municipioEmpresa;
    public String departamentoEmpresa;
    public String nombreEmpresa;
    public String telefonoEmpresa;
    public String contactoEmpresa;
    public String emailEmpresa;
    public String precintos;

    public int id;
    public String companyName;
    public String companyAddress;
    public String companyCountry;
    public String clientName;
    public String clientAddress;
    public String clientTelephone;
    public String date;
    public double total;

    public List<InvoiceDetails> invoiceDetailsList;

    public float clientCountry;

    /*DATOS RECAUDO*/
    public String ascesor;
    public String transferencia;
    public String notaCredito;
    public String TotalRecaudo;

    public String codigoempresa;
    public String nitempresa;
    public String nombreempresa;

    public List<InvoiceDetails> invoiceDocumentosList;
    public List<InvoiceDetails> invoicePagosList;
    public List<InvoiceDetails> invoiceDescuentoList;
}
