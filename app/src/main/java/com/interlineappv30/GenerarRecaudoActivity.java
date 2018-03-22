package com.interlineappv30;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenerarRecaudoActivity extends AppCompatActivity {

    /*SE DECLARAN LAS VARIABLES QUE SE RECIBEN DE RECAUDO ACTIVITY*/
    private String cantidadDoc, cantidadPag, cantidadDesc;
    String codEje, nombreEje, nifEmp, codEmp, nombreEmp, direccionEmp, sTotalSaldo,
            consecutivo_recaudo;
    TextView tvCodEje, tvNombreEje, tvDireccionEmp, tvNombreEmp,tvTotalDocumento, tvTotalPagos,
            tvTotalDescuentos, tvTotalSaldo;
    Button btnDoc, btnPag, btnDesc, btnSave;
    DbAdapter db;
    int tDocumentos, tPagos, tDescuentos, tTotalSaldo;
    private static final String CONSECUTIVO = "getConsecutivo";
    /*inicia variables para el pdf*/
    InvoiceObject invoiceObject = new InvoiceObject();
    private String INVOICES_FOLDER = "Recaudos";
    //Declaramos la clase PdfActivity
    private PdfActivityRecaudo pdfManager = null;
    //Constante para la solicitud del permiso en ejecucion
    final private int REQUEST_PERMISSION_READ = 101; //Lectura
    final private int REQUEST_PERMISSION_WRITE = 102; //Escritura
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_recaudo);
        db = new DbAdapter(getApplicationContext());
        /*SE RECIBEN LOS DATOS DE RECAUDO ACTIVITY*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        nifEmp = getIntent().getStringExtra("nifEmp");
        codEmp = getIntent().getStringExtra("codEmp");
        nombreEmp = getIntent().getStringExtra("nombreEmp");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        /*MUESTRA LAS VARIABLES EN EL LOG*/
        imprimirVariables();
        /*INICIALIZO LOS TEXTOS*/
        tvCodEje = (TextView) findViewById(R.id.idCodEje);
        tvNombreEje = (TextView) findViewById(R.id.idNomEje);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);
        tvTotalDocumento = (TextView) findViewById(R.id.idTotalDocumentos);
        tvTotalPagos = (TextView) findViewById(R.id.idTotalPagos);
        tvTotalDescuentos = (TextView) findViewById(R.id.idTotalDescuentos);
        tvTotalSaldo = (TextView)findViewById(R.id.idTotalSaldo);
        try{
            /*ASIGNO LOS VALORES*/
            tvCodEje.setText(codEje);
            tvNombreEje.setText(nombreEje);
            tvNombreEmp.setText(nombreEmp);
            tvDireccionEmp.setText(direccionEmp);
                /*SE SUMAN LOS VALORES DE LOS DOCUMENTOS*/
            tDocumentos = totalDocumentos(codEmp);
                /*SE SUMAN LOS VALORES DE LOS PAGOS*/
            tPagos = totalPagos(codEmp);
                /*SE SUMAN LOS VALORES DE LOS PAGOS*/
            tDescuentos = totalDescuentos(codEmp);
            int tDocumentosSumados=tDocumentos;
            tTotalSaldo = tDocumentosSumados-tPagos-tDescuentos;
            sTotalSaldo = String.valueOf(tTotalSaldo);
            tvTotalSaldo.setText(sTotalSaldo);

            Log.i("tvTotalSaldo db: ", "" + tvTotalSaldo);
            /* Verificando si tienen los permisos y de lo contrario solicito los mismos al usuario.*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ContextCompat.checkSelfPermission(GenerarRecaudoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GenerarRecaudoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_WRITE);
                //dialog.dismiss();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ContextCompat.checkSelfPermission(GenerarRecaudoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GenerarRecaudoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ);
                //dialog.dismiss();
                return;
            }
        }catch(Exception e){
            Log.i("Exception db: ", "" + e);
        }
        btnDoc = (Button)findViewById(R.id.btnDocumentos);
        btnPag = (Button)findViewById(R.id.btnPago);
        btnDesc = (Button)findViewById(R.id.btnDescuentos);
        btnSave = (Button)findViewById(R.id.btnGuardarPago);

        /*BOTÓN DOCUMENTOS*/
        btnDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerarRecaudoActivity.this, DocumentosActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmp", nifEmp);
                intent.putExtra("codEmp", codEmp);
                intent.putExtra("nombreEmp", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                finish();
                startActivity(intent);
            }
        });

        /*BOTÓN PAGOS*/
        btnPag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerarRecaudoActivity.this, PagoActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmp", nifEmp);
                intent.putExtra("codEmpr", codEmp);
                intent.putExtra("nombreEmp", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                finish();
                startActivity(intent);
            }
        });
        /*BOTÓN DESCUENTOS*/
        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerarRecaudoActivity.this, DescuentosActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmp", nifEmp);
                intent.putExtra("codEmpr", codEmp);
                intent.putExtra("nombreEmp", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                finish();
                startActivity(intent);
            }
        });

        /*BOTÓN GUARDAR*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tTotalSaldo!=0){
                    Toast.makeText(getApplicationContext(),
                            "No es posible guardar, el saldo debe ser $0",
                            Toast.LENGTH_SHORT).show();
                }else{
                    /*CONSULTAR EL CONSECUTIVO DE RECAUDO*/
                    String resp = getConsecutivoRecaudo();
                }
            }
        });
    }
    /*las respuesta de estas solicitud las capturo en la clase sobreescrita onRequestPermissionsResult.*/
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this,"Permission granted"  ,Toast.LENGTH_SHORT).show();

            } else {
                // User refused to grant permission.
                Toast.makeText(this,"Permission refused"  ,Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_PERMISSION_WRITE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this,"Permission granted"  ,Toast.LENGTH_SHORT).show();
            } else {
                // User refused to grant permission.
                Toast.makeText(this,"Permission refused"  ,Toast.LENGTH_SHORT).show();
            }
        }
    }
    private int totalDocumentos(String empresa){
        cantidadDoc = db.countDocumentos(empresa);
        try{
            Log.i("cantidad db: ", "" + cantidadDoc);
            if(cantidadDoc==null){
                tvTotalDocumento.setText("0");
            }else{
                tvTotalDocumento.setText(cantidadDoc);
            }
        }catch (Exception e){
            Log.i("Exception db: ", "" + e);
            tvTotalDocumento.setText("0");
        }
        return Integer.parseInt(cantidadDoc);
    }
    private int totalPagos(String empresa){
        cantidadPag = db.countPagos(empresa);
        try{
            Log.i("cantidadPag: ", "" + cantidadPag);
            if(cantidadPag==null){
                tvTotalPagos.setText("0");
            }else {
                tvTotalPagos.setText(cantidadPag);
            }
        }catch (Exception e){
            Log.i("Exception db: ", "" + e);
            tvTotalPagos.setText("0");
        }
        return Integer.parseInt(cantidadPag);
    }
    private int totalDescuentos(String empresa){
        cantidadDesc = db.countDescuentos(empresa);
        try{
            Log.i("cantidadDesc: ", "" + cantidadDesc);
            if(cantidadDesc==null){
                tvTotalDescuentos.setText("0");
            }else {
                tvTotalDescuentos.setText(cantidadDesc);
            }
        }catch (Exception e){
            Log.i("Exception db: ", "" + e);
            tvTotalDescuentos.setText("0");
        }
        return Integer.parseInt(cantidadDesc);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menurecaudo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idDocumentos:
                //metodoAdd()
                documentosCreados();
                return true;
            case R.id.Exit:
                //metodoSearch()
                exitActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void exitActivity() {
        finishAffinity();
    }
    private void documentosCreados() {
        try{
            Toast.makeText(getApplicationContext(), "Documentos seleccionados", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GenerarRecaudoActivity.this, DocumentosCreados.class);
            intent.putExtra("codEje",codEje);
            intent.putExtra("nombreEje",nombreEje);
            intent.putExtra("nifEmpresa", nifEmp);
            intent.putExtra("codEmp", codEmp);
            intent.putExtra("nombreEmpresa", nombreEmp);
            intent.putExtra("direccionEmp", direccionEmp);
            startActivity(intent);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
            Log.i("Error:",""+e);
        }
    }
    private void imprimirVariables(){
        Log.i("*** VARIABLES EN RECAUDAR ACTIVITY ***",""+codEje);
        Log.i("nombreEje",""+nombreEje);
        Log.i("nifEmp",""+nifEmp);
        Log.i("codEmp",""+codEmp);
        Log.i("nombreEmp",""+nombreEmp);
        Log.i("direccionEmp",""+direccionEmp);
    }
    /*CONSULTA CONSECTIVO DE BOLETO*/
    protected String getConsecutivoRecaudo() {
        try{
            DireccionClass dir = new DireccionClass();
            String IP = dir.direccionURL();
            String accion = "consultarRecaudoById";
            String URL2 = IP+accion+"/"+codEje+"/0/0";
            Log.i("URLcompleta",""+URL2);
            JsonArrayRequest req = new JsonArrayRequest(URL2, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("response",""+response);
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            consecutivo_recaudo = jsonobject.getString("consecutivo_recaudo");
                            HashMap<String, String> j = new HashMap<String, String>();
                            j.put(CONSECUTIVO, consecutivo_recaudo);
                            Log.i("For**************",""+i);
                            Log.i("consecutivo",""+consecutivo_recaudo);
                            creareDialog(consecutivo_recaudo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GenerarRecaudoActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
            Log.i("req",""+req);
        }catch (Exception e){
            Log.i("e",""+e);
        }
        return consecutivo_recaudo;
    }

    /*MUESTRA LA VENTANA DE ALERTA*/
    public void creareDialog(final String consecutivo){
        String MSJ = "Alerta!";
        String INF = "Se genera el consecutivo "+consecutivo+".\n\n" +
                "Esta seguro de finalizar el recaudo?";
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(MSJ)
                .setMessage(INF)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DireccionClass dir = new DireccionClass();
                        String IP = dir.direccionURL();
                        try {
                            String url = IP+"actualizarConsecutivoRecaudo"+"/"+codEje+"/0/0";
                            Log.i("URLcompleta:",""+url);
                            /*GUARDAR BOLETO +1 EN EL SERVIDOR*/
                            enviarRecaudoMasUno(url, consecutivo);
                            /*GUARDA EL BOLETO EN LA DB LOCAL*/
                            int res1 = db.agregarConsecutivoDocumentos(consecutivo, codEmp);
                            int res2 = db.agregarConsecutivoPagos(consecutivo, codEmp);
                            int res3 = db.agregarConsecutivoDescuentos(consecutivo, codEmp);
                            if(res1==0 || res2==0 || res3==0){
                                Log.i("res1",""+res1);
                                Log.i("res2",""+res2);
                                Log.i("res3",""+res3);
                                Toast.makeText(getApplicationContext(),"Se genera un error guardando en la DB local.",Toast.LENGTH_SHORT).show();
                            }else{
                                try {
                                        /*CREAR PDF*/
                                        //Creamos una factura desde nuestro código solo para poder generar el documento PDF
                                        //con esta información
                                        createInvoiceObject();
                                        //Instanciamos la clase PdfActivity
                                        pdfManager = new PdfActivityRecaudo(GenerarRecaudoActivity.this);
                                        //Create PDF document
                                        assert pdfManager != null;
                                        pdfManager.createPdfDocument(invoiceObject);
                                        /*ENVIAR CORREO ELECTRONICO*/
                                        Boolean email =  getEmailCC();
                                        Log.i("email",""+email);
                                        if(email){
                                            /*GUARDAR LOS DATOS DE LOS RECAUDOS EN LA BD LOCAL*/
                                            Boolean valor1 = enviarDocumentosDBLocal(codEmp);
                                            Boolean valor2 = enviarPagosDBLocal(codEmp);
                                            Boolean valor3 = enviarDescuentosDBLocal(codEmp);
                                            Log.i("valor",""+valor1);
                                            if (!valor1 || !valor2 || !valor3){
                                                Toast.makeText(getApplicationContext(),"No se pudo guardar la información el sel servidor",Toast.LENGTH_SHORT).show();
                                                Log.i("valor1",""+valor1);
                                                Log.i("valor2",""+valor2);
                                                Log.i("valor3",""+valor3);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
                                                /*ENVIAR A LA PANTALLA DE CLIENTES*/
                                                Intent intent = new Intent(GenerarRecaudoActivity.this, RecaudoActivity.class);
                                                intent.putExtra("codEje",codEje);
                                                intent.putExtra("nombreEje",nombreEje);
                                                intent.putExtra("nifEmpresa", nifEmp);
                                                intent.putExtra("codEmpresa", codEmp);
                                                intent.putExtra("nombreEmpresa", nombreEmp);
                                                intent.putExtra("direccionEmp", direccionEmp);
                                                finish();
                                                startActivity(intent);
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(),"El correo no se envio correctamente",Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (IOException e) {
                                       e.printStackTrace();
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    //Creando la factura por hard code
    private void createInvoiceObject(){

        invoiceObject.boletoDevolucion = consecutivo_recaudo;
        /*DATOS ENCABEZADOS*/
        invoiceObject.ascesor = nombreEje;
        invoiceObject.transferencia = cantidadDoc;
        invoiceObject.notaCredito = cantidadDesc;
        invoiceObject.TotalRecaudo = cantidadDoc;

        /*DATOS DEL CLIENTE*/
        invoiceObject.codigoempresa = codEmp;
        invoiceObject.direccionEmpresa = direccionEmp;
        invoiceObject.nitempresa = nifEmp;
        invoiceObject.nombreempresa = nombreEmp;

        /*DETALLE DOCUMENTOS*/
        try{
            invoiceObject.invoiceDocumentosList = new ArrayList<InvoiceDetails>();

            Cursor cursor = db.getDocumentsData(codEmp);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String documento = cursor.getString(3);
                    String rfactura = cursor.getString(4);
                    String fechadocumento = cursor.getString(5);
                    String fechavencimiento = cursor.getString(6);
                    String saldo = cursor.getString(7);

                    InvoiceDetails invoiceDetailsl = new InvoiceDetails();
                    invoiceDetailsl.itemDocumento = documento;
                    invoiceDetailsl.itemFactura = rfactura;
                    invoiceDetailsl.itemFechaDoc = fechadocumento;
                    invoiceDetailsl.itemFechaVenc = fechavencimiento;
                    invoiceDetailsl.itemSaldo = saldo;

                    invoiceObject.invoiceDocumentosList.add(invoiceDetailsl);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        /*DETALLE PAGO*/
        try{
            invoiceObject.invoicePagosList = new ArrayList<InvoiceDetails>();

            Cursor cursor = db.getPagosData(codEmp);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String tipopago = cursor.getString(2);
                    String numerocuenta = cursor.getString(3);
                    String tipobanco = cursor.getString(4);
                    String codigocuenta = cursor.getString(5);
                    String fechapago = cursor.getString(6);
                    String valorpago = cursor.getString(7);

                    InvoiceDetails invoiceDetails2 = new InvoiceDetails();
                    invoiceDetails2.tipoPago = tipopago;
                    invoiceDetails2.numeroCuenta = numerocuenta;
                    invoiceDetails2.tipoBanco = tipobanco;
                    invoiceDetails2.codigoCuenta = codigocuenta;
                    invoiceDetails2.fechaPago = fechapago;
                    invoiceDetails2.valorPago = valorpago;

                    invoiceObject.invoicePagosList.add(invoiceDetails2);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        /*DETALLE DESCUENTO*/
        try{
            invoiceObject.invoiceDescuentoList = new ArrayList<InvoiceDetails>();
            String observacionesQuemadoByEdward = "Negociacion ventas 2er trimestre 2017";
            int j=1;
            Cursor cursor = db.getDescuentosData(codEmp);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    String tipodescuento = cursor.getString(2);
                    String valordescuento = cursor.getString(3);
                    String observaciones = cursor.getString(4);

                    InvoiceDetails invoiceDetails3 = new InvoiceDetails();
                    invoiceDetails3.odis = String.valueOf(j);
                    invoiceDetails3.tipoDescuento = tipodescuento;
                    invoiceDetails3.valorDescuento = valordescuento;
                    invoiceDetails3.obsDescuento = observacionesQuemadoByEdward;

                    invoiceObject.invoiceDescuentoList.add(invoiceDetails3);
                    j++;
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*ENVIA EL CORREO ELECTRONICO POR DEBAJO*/
    private boolean getEmailCC() {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String url = IP + "consultarEmailCC/"+codEje+"/"+codEmp+"/0";
        try{
            JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            String emailCC = jsonobject.getString("distribucion_recibos_caja");
                            String emailTo = jsonobject.getString("distribucion_boletos_devolucion");

                            /*CUERPO DEL CORREO*/
                            String user = "dchacon130@gmail.com";
                            String passwd = "F34relfo.,";
                            final String asunto = "Boleto de Recaudo: "+consecutivo_recaudo;
                            final String mensaje = "Apreciado cliente,\n" +
                                    "\n" +
                                    "Anexo encontrará boleto de recaudo N°  "+consecutivo_recaudo+". Le recordamos que esta dirección de e-mail es utilizada solamente para los envíos de la información solicitada. " +
                                    "\n" +
                                    "Por favor no responder este correo.\n" +
                                    "\n" +
                                    "Cordialmente,\n" +
                                    "\n" +
                                    "ENLACE INTERNACIONAL";
                            assert pdfManager != null;
                            String FILENAME = "Recaudo_"+consecutivo_recaudo+".pdf";

                            /*ENVIAR CORREO MANERA TRADICIONAL*/
                            pdfManager.sendPdfByEmail(INVOICES_FOLDER + File.separator + FILENAME,emailTo,emailCC, GenerarRecaudoActivity.this, asunto, mensaje);
                            /*ENVIAR CORREO AUTOMATICAMENTE*/
                            /*new EnviarEmail(user, passwd).execute(
                                    new EnviarEmail.Mail(emailTo+", "+EmailCli, emailCC, asunto, mensaje, FILENAME));*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GenerarRecaudoActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
            Log.i("req",""+req);

        }catch (Exception e){
            Log.i("e",""+e);
        }
        return true;
    }

    /*ENVIA EL CONSECUTIVO PARA AGREGARLE +1*/
    public void enviarRecaudoMasUno(String URL, final String consecutivo){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("consecutivo_recaudo",consecutivo);

            jsonArray.put(jsonObject);
            Log.i("jsonString", jsonObject.toString());
        }catch(Exception e){
            Log.i("Exception", ""+e);
        }
        try{
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, URL,
                    jsonObject,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    // response
                    Log.i("Response","" + response);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Log.i("Error.Response", ""+String.valueOf(error));
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("consecutivo_recaudo", consecutivo);
                    Log.i("params",""+params);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(putRequest);
            Log.i("putRequest",""+putRequest);
        }catch (Exception e){
            Log.i("Exception...",""+e);
        }
    }

    /*GUARDA LAS DEVOLUCIONES CREADAS*/
    public Boolean enviarDocumentosDBLocal(final String codEmpresa) throws JSONException {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String url = IP + "guardarRecaudoDocumentos/0/0/0";
        Log.i("url", "" + url);
        try {
            Cursor cursor = db.getDocumentsData(codEmpresa);
            JSONObject jo = null;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jo = new JSONObject();
                    jo.put("codigo_cliente", cursor.getString(0));
                    jo.put("nif", cursor.getString(1));
                    jo.put("recaudo", cursor.getString(2));
                    jo.put("documento", cursor.getString(3));
                    jo.put("factura", cursor.getString(4));
                    jo.put("fecha_documento", cursor.getString(5));
                    jo.put("fecha_vencimiento", cursor.getString(6));
                    jo.put("saldo", cursor.getString(7));
                    jo.put("consecutivo", cursor.getString(8));

                    final String id = cursor.getString(9);
                    Log.i("id", "" + id);

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("response", "" + response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", "" + error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            return headers;
                        }
                    };
                    requestQueue.add(jsonArrayRequest);
                    Log.i("jsonArrayRequest", "" + jsonArrayRequest.toString());
                    Boolean delete = db.eliminarRegistroDocumentos(id);
                    Log.i("delete", "" + delete);
                } while (cursor.moveToNext());
                db.close();
                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "No hay recaudos para guardar", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(JSONException je){
            Log.i("JSONException", "" + je);
        }
        return true;
    }

    /*GUARDA LOS PAGOS CREADOS*/
    public Boolean enviarPagosDBLocal(final String codEmpresa) throws JSONException {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String url = IP + "guardarRecaudoPagos/0/0/0";
        Log.i("url", "" + url);

        try {
            Cursor cursor = db.getPagosData(codEmpresa);
            JSONObject jo = null;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jo = new JSONObject();

                    jo.put("codigo_cliente", cursor.getString(0));
                    jo.put("nif", cursor.getString(1));
                    jo.put("tipo_pago_recaudo", cursor.getString(2));
                    jo.put("numero_cuenta", cursor.getString(3));
                    jo.put("tipo_banco_recaudo", cursor.getString(4));
                    jo.put("codigo_cuenta", cursor.getString(5));
                    jo.put("fecha_pago", cursor.getString(6));
                    jo.put("valor_pago", cursor.getString(7));
                    jo.put("consecutivo", cursor.getString(8));
                    jo.put("fecha_sys", cursor.getString(9));
                    jo.put("estado", cursor.getString(10));

                    final String id = cursor.getString(11);
                    Log.i("id", "" + id);

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("response", "" + response.toString());
                            Boolean delete = db.eliminarRegistroPagos(id);
                            Log.i("delete", "" + delete);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", "" + error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            return headers;
                        }
                    };
                    requestQueue.add(jsonArrayRequest);
                    Log.i("jsonArrayRequest", "" + jsonArrayRequest.toString());
                    Boolean delete = db.eliminarRegistroPagos(id);
                    Log.i("delete", "" + delete);
                } while (cursor.moveToNext());
                db.close();
                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "No hay recaudos para guardar", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(JSONException je){
            Log.i("JSONException", "" + je);
        }
        return true;
    }

    /*GUARDA LOS DESCUENTOS CREADOS*/
    public Boolean enviarDescuentosDBLocal(final String codEmpresa) throws JSONException {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String url = IP + "guardarRecaudoDescuentos/0/0/0";
        Log.i("url", "" + url);

        try {
            Cursor cursor = db.getDescuentosData(codEmpresa);
            JSONObject jo = null;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jo = new JSONObject();

                    jo.put("codigo_cliente", cursor.getString(0));
                    jo.put("nif", cursor.getString(1));
                    jo.put("tipo_descuento_recaudo", cursor.getString(2));
                    jo.put("valor_descuento", cursor.getString(3));
                    jo.put("observaciones", cursor.getString(4));
                    jo.put("consecutivo", cursor.getString(5));
                    jo.put("fecha_sys", cursor.getString(6));
                    jo.put("estado", cursor.getString(7));

                    final String id = cursor.getString(8);
                    Log.i("id", "" + id);

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("response", "" + response.toString());
                            Boolean delete = db.eliminarRegistroDescuentos(id);
                            Log.i("delete", "" + delete);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", "" + error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            return headers;
                        }
                    };
                    requestQueue.add(jsonArrayRequest);
                    Boolean delete = db.eliminarRegistroDescuentos(id);
                    Log.i("delete", "" + delete);
                } while (cursor.moveToNext());
                db.close();
                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "No hay recaudos para guardar", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(JSONException je){
            Log.i("JSONException", "" + je);
        }
        return true;
    }
}
