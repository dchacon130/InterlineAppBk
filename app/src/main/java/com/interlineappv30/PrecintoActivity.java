package com.interlineappv30;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrecintoActivity extends AppCompatActivity {

    private static final String CONSECUTIVO = "getConsecutivo";

    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;
    String consecutivo_boleto;
    String  URLcompleta,URLcompleta2,
            accion = "consultarNumeroPrecinto",cadena="",accion2 = "consultarBoletoById",
            IP,URLconsultarDatosEmpresa,
            EmailCli, idCausal, nomCausal, ObservacionCli, DocumentoCli, NomCli, TelCli;
    TextView tvCodEje,tvNombreEje,tvNitEmp,tvNombreEmp;
    Activity activity;
    LinearLayout layout_base;
    Button btnFinalEnviar;
    ArrayList<Integer> checkedlist = new ArrayList<Integer>();
    DbAdapter db;
    EnviarEmail eEmail;

    /*inicia variables para el pdf*/
    InvoiceObject invoiceObject = new InvoiceObject();
    private String INVOICES_FOLDER = "Devoluciones";

    //Declaramos la clase PdfActivity
    private PdfActivity pdfManager = null;

    //Constante para la solicitud del permiso en ejecucion
    final private int REQUEST_PERMISSION_READ = 101; //Lectura
    final private int REQUEST_PERMISSION_WRITE = 102; //Escritura

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precinto);

        activity = this;

         /*Recibo los datos*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");

        idEmp = getIntent().getStringExtra("idEmp");
        codEmp = getIntent().getStringExtra("codEmp");
        nifEmp = getIntent().getStringExtra("nifEmp");
        nombreEmp = getIntent().getStringExtra("nombreEmp");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        ciudadEmp = getIntent().getStringExtra("ciudadEmp");
        departamentoEmp = getIntent().getStringExtra("departamentoEmp");
        representanteEmp = getIntent().getStringExtra("representanteEmp");
        telefonoEmp = getIntent().getStringExtra("telefonoEmp");
        correoEmp = getIntent().getStringExtra("correoEmp");

        idCausal = getIntent().getStringExtra("idCausal");
        nomCausal = getIntent().getStringExtra("nomCausal");

        ObservacionCli = getIntent().getStringExtra("ObservacionCli");
        DocumentoCli = getIntent().getStringExtra("DocumentoCli");
        NomCli = getIntent().getStringExtra("NomCli");
        TelCli = getIntent().getStringExtra("TelCli");
        EmailCli = getIntent().getStringExtra("EmailCli");
        if (ObservacionCli.equals("")) {
            ObservacionCli = "No se ingresaron observaciones.";
        }


        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvNitEmp = (TextView) findViewById(R.id.idNif);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);

        tvCodEje.setText(codEje);
        tvNombreEje.setText(codEmp);
        tvNitEmp.setText(nifEmp);
        tvNombreEmp.setText(nombreEmp);

        layout_base = (LinearLayout)findViewById(R.id.base_layout);

        DireccionClass dir = new DireccionClass();
        IP = dir.direccionURL();

        URLcompleta = IP+accion+"/"+codEje+"/0/0";
        Log.i("URLcompleta",""+URLcompleta);
        //CONSULTA PRECINTOS
        getPrecinto(URLcompleta);

        btnFinalEnviar = (Button)findViewById(R.id.btnFinalEnviar);
        db = new DbAdapter(getApplicationContext());
        db.open();

        /* Verificando si tienen los permisos y de lo contrario solicito los mismos al usuario.*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(PrecintoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrecintoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            //dialog.dismiss();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(PrecintoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrecintoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ);
            //dialog.dismiss();
            return;
        }

        btnFinalEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer marcados:checkedlist){
                    cadena = cadena+marcados+"|";
                }
                if (cadena.equals("")){
                    Toast.makeText(PrecintoActivity.this,"Porfavor seleccione un precinto"  ,Toast.LENGTH_SHORT).show();
                }else{
                    /*GUARDA LOS REGISTROS SELECCIONADOS EN DB*/
                    int res = db.agregarPrecito(cadena, codEmp);
                    Log.i("res",""+res);

                     /*CONSULTA NUMERO DE BOLETO*/
                    DireccionClass dir = new DireccionClass();
                    String IP = dir.direccionURL();
                    URLcompleta2 = IP+accion2+"/"+codEje+"/0/0";
                    Log.i("URLcompleta2:",""+URLcompleta2);
                    String finalConsecutivo = getBoletoData(URLcompleta2);
                    consecutivo_boleto = finalConsecutivo;
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

    /*CONSULTA CONSECTIVO DE BOLETO*/
    protected String getBoletoData(String URL) {
        try{
            JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.i("response",""+response);

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            consecutivo_boleto = jsonobject.getString("consecutivo_boleto");
                            HashMap<String, String> j = new HashMap<String, String>();
                            j.put(CONSECUTIVO, consecutivo_boleto);

                            Log.i("For**************",""+i);
                            Log.i("consecutivo",""+consecutivo_boleto);

                            creareDialog(consecutivo_boleto);
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
                    Toast.makeText(activity,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
            Log.i("req",""+req);
        }catch (Exception e){
            Log.i("e",""+e);
        }
        return consecutivo_boleto;
    }

    /*ENVIA EL CONSECUTIVO PARA AGREGARLE +1*/
    public void enviarBoletoMasUno(String URL, final String consecutivo){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("consecutivo_boleto",consecutivo);

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
                    params.put("consecutivo_boleto", consecutivo);
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

    /*ENVIA EL Precinto usado*/
    public void enviarPrecintoUsado(String URL, final String precinto){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("consecutivo_precinto",precinto);

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
                    params.put("consecutivo_precinto", precinto);
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


    /*MUESTRA LA VENTANA DE ALERTA*/
    public void creareDialog(final String consecutivo){
        String MSJ = "Alerta!";
        String INF = "Se genera el consecutivo "+consecutivo+" para la devolución.\n\n" +
                "Esta seguro de finalizar la devolución?";
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(MSJ)
                .setMessage(INF)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cadena = "";
                        for (Integer marcados:checkedlist){
                            cadena = cadena+marcados+"|";
                            Log.i("Los marcados, id:",""+cadena);
                            URLcompleta = IP+"actualizarPrecinto"+"/"+codEje+"/0/0";
                            Log.i("URLcompleta:",""+URLcompleta);
                            /*GUARDAR PRECINTO COMO USADO*/
                            enviarPrecintoUsado(URLcompleta, String.valueOf(marcados));
                        }
                        DireccionClass dir = new DireccionClass();
                        String IP = dir.direccionURL();
                        URLcompleta = IP+"actualizarBoleto"+"/"+codEje+"/0/0";
                        Log.i("URLcompleta:",""+URLcompleta);
                        /*GUARDAR BOLETO +1 EN EL SERVIDOR*/
                        enviarBoletoMasUno(URLcompleta, consecutivo);

                        /*GUARDA EL BOLETO EN LA DB LOCAL*/
                        int res1 = db.agregarBoleto(consecutivo, codEmp);
                        Log.i("res1",""+res1);
                        Log.i("res1",""+res1);
                        if (res1>0){
                            Toast.makeText(getApplicationContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
                            try {
                                /*GUARDAR LOS DATOS DE LA DEVOLUCIÓN EN LA BD LOCAL*/
                                Boolean valor = enviarDevolucionesDBLocal(codEmp);
                                /*CREAR PDF*/
                                //Creamos una factura desde nuestro código solo para poder generar el documento PDF
                                //con esta información
                                createInvoiceObject();
                                //Instanciamos la clase PdfActivity
                                pdfManager = new PdfActivity(PrecintoActivity.this);
                                //Create PDF document
                                assert pdfManager != null;
                                pdfManager.createPdfDocument(invoiceObject);
                                /*ENVIAR CORREO ELECTRONICO*/
                                if (EmailCli != null){
                                    Boolean email =  getEmailCC();
                                    Log.i("email",""+email);
                                    /*ENVIAR A LA PANTALLA DE CLIENTES*/
                                    Intent intent = new Intent(PrecintoActivity.this, ClientesActivity.class);
                                    intent.putExtra("codEje", codEje);
                                    intent.putExtra("nombreEje", nombreEje);
                                    CausalActivity.fa.finish();
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"No hay destinatario!",Toast.LENGTH_SHORT).show();
                                }
                                Log.i("valor",""+valor);
                            }catch(Exception e){
                                Log.i("ExceptionIntent",""+e);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No se pudo guardar la información",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }


    /*GUARDA LAS DEVOLUCIONES CREADAS*/
    public Boolean enviarDevolucionesDBLocal(final String codEmpresa) throws JSONException {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String url = IP + "guardarDevolucion/0/0/0";
        Log.i("url", "" + url);

        try {
            Cursor cursor = db.getAllContacts(codEmpresa);
            JSONObject jo = null;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    jo = new JSONObject();

                    jo.put("observacion_causal", cursor.getString(0));
                    jo.put("documento_contacto", cursor.getString(1));
                    jo.put("nombre_contacto", cursor.getString(2));
                    jo.put("telefono_contacto", cursor.getString(3));
                    jo.put("email_contacto", cursor.getString(4));
                    jo.put("cantidad_devolucion", cursor.getString(5));
                    jo.put("observacion_cantidad", cursor.getString(6));
                    jo.put("numero_boleto", cursor.getString(7));
                    jo.put("numero_precinto", cursor.getString(8));
                    jo.put("estado", cursor.getString(9));
                    jo.put("ejecutivo_codigo", cursor.getString(10));
                    jo.put("causales_devolucion_id", cursor.getString(11));
                    jo.put("detalle_lotes_lote", cursor.getString(12));
                    jo.put("detalle_lotes_producto", cursor.getString(13));
                    jo.put("proveedores_codigo_cliente", cursor.getString(14));
                    jo.put("proveedores_nif", cursor.getString(15));

                    final String id = cursor.getString(16);
                    Log.i("id", "" + id);

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jo, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("response", "" + response.toString());
                            Boolean delete = db.eliminarRegistro(id);
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
                } while (cursor.moveToNext());
                db.close();
                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "No hay devoluciones para guardar", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(JSONException je){
            Log.i("JSONException", "" + je);
        }
        return true;
    }

    /*CONSULTA EL CONSECUTIVO*/
    protected Void getPrecinto(String URL) {
        try{
            JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("response",""+response);
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            String consecutivo = jsonobject.getString("consecutivo");

                            HashMap<String, String> j = new HashMap<String, String>();
                            j.put(CONSECUTIVO, consecutivo);

                            Log.i("For**************",""+i);
                            Log.i("consecutivo",""+consecutivo);

                            CheckBox cb = new CheckBox(activity);
                            cb.setId(Integer.parseInt(consecutivo));
                            cb.setText(consecutivo);
                            cb.setOnClickListener(ckListener);
                            layout_base.addView(cb);
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
                    Toast.makeText(activity,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
            Log.i("req",""+req);
        }catch (Exception e){
            Log.i("e",""+e);
        }
        return null;
    }

    private View.OnClickListener ckListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            boolean checked = ((CheckBox) view).isChecked();
            if(checked){
                checkedlist.add(id);
            }else{
                checkedlist.remove(new Integer(id));
            }
        }
    };

    /*MENU SUPERIOR*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /*OPCIONES DEL MENU SUPERIOR*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                //metodoAdd()
                finalActivity();
                return true;
            case R.id.idPrecinto:
                //metodoAdd()
                precintoActivity();
                return true;
            case R.id.Exit:
                //metodoSearch()
                exitActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void precintoActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /*ENVIA A LA OPCIÓN DE DETALLE*/
    private void finalActivity() {
        try{
            Toast.makeText(getApplicationContext(), "Go to Detalle devolución", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(activity, FinalActivity.class);
            /*DATOS DEL USUARIO*/
            intent.putExtra("codEje", codEje);
            intent.putExtra("nombreEje", nombreEje);
            /*DATOS DE LA EMPRESA*/
            intent.putExtra("codEmp", codEmp);
            intent.putExtra("nifEmp", nifEmp);
            intent.putExtra("nombreEmp", nombreEmp);
            intent.putExtra("direccionEmp", direccionEmp);
            intent.putExtra("ciudadEmp", ciudadEmp);
            intent.putExtra("departamentoEmp", departamentoEmp);
            intent.putExtra("representanteEmp", representanteEmp);
            intent.putExtra("telefonoEmp", telefonoEmp);
            intent.putExtra("correoEmp", correoEmp);

            intent.putExtra("idCausal", idCausal);
            intent.putExtra("nomCausal", nomCausal);

            intent.putExtra("DocumentoCli", DocumentoCli);
            intent.putExtra("ObservacionCli", ObservacionCli);
            intent.putExtra("NomCli", NomCli);
            intent.putExtra("TelCli", TelCli);
            intent.putExtra("EmailCli", EmailCli);

            startActivity(intent);
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
            Log.i("Error:",""+e);
        }
    }

    /*CIERRA LA APLICACIÓN*/
    @SuppressLint("NewApi")
    private void exitActivity() {
        finishAffinity();
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
                            final String asunto = "Boleto de devolución: "+consecutivo_boleto;
                            final String mensaje = "Apreciado cliente,\n" +
                                    "\n" +
                                    "Anexo encontrará boleto de devolución N°  "+consecutivo_boleto+". Le recordamos que esta dirección de e-mail es utilizada solamente para los envíos de la información solicitada. " +
                                    "\n" +
                                    "Por favor no responder este correo.\n" +
                                    "\n" +
                                    "Cordialmente,\n" +
                                    "\n" +
                                    "ENLACE INTERNACIONAL";
                            assert pdfManager != null;
                            String FILENAME = "Devolucion_"+consecutivo_boleto+".pdf";

                            /*ENVIAR CORREO MANERA TRADICIONAL*/
                            pdfManager.sendPdfByEmail(INVOICES_FOLDER + File.separator + FILENAME,emailTo+", "+EmailCli,emailCC, PrecintoActivity.this, asunto, mensaje);
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
                    Toast.makeText(activity,error.getMessage(),Toast.LENGTH_LONG).show();
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

    //Creando la factura por hard code
    private void createInvoiceObject(){

        invoiceObject.boletoDevolucion = consecutivo_boleto;

        /*DATOS DE LA EMPRESA*/
        invoiceObject.codigoEmpresa = codEmp;
        invoiceObject.direccionEmpresa = direccionEmp;
        invoiceObject.municipioEmpresa = ciudadEmp;
        invoiceObject.departamentoEmpresa = departamentoEmp;
        invoiceObject.nombreEmpresa = nombreEmp;
        invoiceObject.telefonoEmpresa = telefonoEmp;
        invoiceObject.contactoEmpresa = representanteEmp;
        invoiceObject.emailEmpresa = correoEmp;
        invoiceObject.precintos = cadena;

        /*DATOS CLIENTE - DEVOLUCIÓN*/
        invoiceObject.documentoCliente = DocumentoCli;
        invoiceObject.nombreCliente = NomCli;
        invoiceObject.telefonoCliente = TelCli;
        invoiceObject.emailCliente = EmailCli;

        invoiceObject.causal = nomCausal;
        invoiceObject.observacion = ObservacionCli;

        try{
            invoiceObject.invoiceDetailsList = new ArrayList<InvoiceDetails>();

            Cursor cursor = db.getAllContacts(codEmp);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String codigoPro = cursor.getString(13);
                    String nombrePro = cursor.getString(17);
                    String lotePro = cursor.getString(12);
                    String fechaPro = cursor.getString(18);
                    String cantidadPro = cursor.getString(5);
                    String observacionPro = cursor.getString(6);

                    InvoiceDetails invoiceDetailsl = new InvoiceDetails();
                    invoiceDetailsl.itemCode = codigoPro;
                    invoiceDetailsl.itemName = nombrePro;
                    invoiceDetailsl.itemLote = lotePro;
                    invoiceDetailsl.itemFecha = fechaPro;
                    invoiceDetailsl.itemCantidad = cantidadPro;
                    invoiceDetailsl.itemObservacion = observacionPro;

                    invoiceObject.invoiceDetailsList.add(invoiceDetailsl);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
