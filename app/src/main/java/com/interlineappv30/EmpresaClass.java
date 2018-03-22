package com.interlineappv30;

/**
 * Created by INTEL on 11/08/2017.
 */

public class EmpresaClass {

    String id, codEmpresa, nif, nombreEmpresa, direccionEmp, ciudadEmp,departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;

    public EmpresaClass(String id, String codEmpresa, String nif, String nombreEmpresa, String direccionEmp,
                        String ciudadEmp, String departamentoEmp, String representanteEmp, String telefonoEmp,
                        String correoEmp) {
        super();
        this.id = id;
        this.codEmpresa = codEmpresa;
        this.nif = nif;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionEmp = direccionEmp;
        this.ciudadEmp = ciudadEmp;
        this.departamentoEmp = departamentoEmp;
        this.representanteEmp = representanteEmp;
        this.telefonoEmp = telefonoEmp;
        this.correoEmp = correoEmp;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCodEmpresa() {
        return codEmpresa;
    }
    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }
    public String getnif() {
        return nif;
    }
    public void setnif(String nif) {
        this.nif = nif;
    }
    public String getnombreEmpresa() {
        return nombreEmpresa;
    }
    public void setnombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    public String getdireccionEmp() {
        return direccionEmp;
    }
    public void setdireccionEmp(String direccionEmp) {
        this.direccionEmp = direccionEmp;
    }
    public String getciudadEmp() {
        return ciudadEmp;
    }
    public void setCiudadEmp(String ciudadEmp) {
        this.ciudadEmp = ciudadEmp;
    }
    public String getdepartamentoEmp() {
        return departamentoEmp;
    }
    public void setdepartamentoEmp(String departamentoEmp) {
        this.id = departamentoEmp;
    }
    public String getrepresentanteEmp() {
        return representanteEmp;
    }
    public void setrepresentanteEmp(String representanteEmp) {
        this.representanteEmp = representanteEmp;
    }
    public String gettelefonoEmp() {
        return telefonoEmp;
    }
    public void settelefonoEmp(String telefonoEmp) {
        this.telefonoEmp = telefonoEmp;
    }
    public String getcorreoEmp() {
        return correoEmp;
    }
    public void setcorreoEmp(String correoEmp) {
        this.correoEmp = correoEmp;
    }

}
