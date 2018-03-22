package com.interlineappv30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocumentosActivity extends AppCompatActivity {

    private String documento, referencia_factura, fecha_documento, fecha_vc_mto, saldo, saldoint,
    codEmp, codEje, id, iddb;
    private String nombreEje,
                    nifEmp,
                    nombreEmp,
                    direccionEmp;
    private int total = 0;
    ArrayList<Integer> checkedlist = new ArrayList<Integer>();
    LinearLayout layout_base;
    Activity activity;
    Button btnAgregarDocumentos;
    DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos);
        try{
            /*SE RECIBEN LOS DATOS DE RECAUDO ACTIVITY*/
            codEje = getIntent().getStringExtra("codEje");
            nombreEje = getIntent().getStringExtra("nombreEje");
            nifEmp = getIntent().getStringExtra("nifEmp");
            codEmp = getIntent().getStringExtra("codEmp");
            nombreEmp = getIntent().getStringExtra("nombreEmp");
            direccionEmp = getIntent().getStringExtra("direccionEmp");
            activity = DocumentosActivity.this;
            getDescuentos();
            layout_base = (LinearLayout)findViewById(R.id.base_layout);
            db = new DbAdapter(getApplicationContext());
            btnAgregarDocumentos = (Button)findViewById(R.id.btnAgregarDocumentos);

            btnAgregarDocumentos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        for (Integer marcados:checkedlist){
                            Log.i("marcados:",""+marcados);
                            total = total+marcados;
                            consultarDocumentosMarcados(marcados);
                        }
                        if (total == 0){
                            Toast.makeText(DocumentosActivity.this,
                                    "No se ha seleccionado ningun documento",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(DocumentosActivity.this,
                                "No se ha seleccionado ningun documento",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }catch (Exception e){
            Log.i("Exception",""+e);
        }
    }

    protected Void getDescuentos() {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String accion = "consultarDocumento";
        String URLcompleta = IP+accion+"/"+codEje+"/"+codEmp+"/0";
        Log.i("URLcompleta",""+URLcompleta);
        try {
            JsonArrayRequest req = new JsonArrayRequest(URLcompleta, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("response", "" + response);
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            id = jsonobject.getString("id");
                            documento = jsonobject.getString("documento");
                            referencia_factura = jsonobject.getString("referencia_factura");
                            fecha_documento = jsonobject.getString("fecha_documento");
                            fecha_vc_mto = jsonobject.getString("fecha_vc_mto");
                            saldo = jsonobject.getString("saldo");

                            Log.i("For**************", "" + i);
                            Log.i("id", "" + id);
                            Log.i("documento", "" + documento);
                            Log.i("referencia_factura", "" + referencia_factura);
                            Log.i("fecha_documento", "" + fecha_documento);
                            Log.i("fecha_vc_mto", "" + fecha_vc_mto);
                            Log.i("saldo", "" + saldo);
//
//                            HashMap<String, String> j = new HashMap<String, String>();
//                            j.put(id, id);
//                            j.put(documento, documento);
//                            j.put(referencia_factura, referencia_factura);
//                            j.put(fecha_documento, fecha_documento);
//                            j.put(fecha_vc_mto, fecha_vc_mto);
//                            j.put(saldo, saldo);

                            CheckBox cb = new CheckBox(activity);
                            cb.setId(Integer.parseInt(id));
                            cb.setText(
                                "Documento: " + documento + "\n" +
                                "Referencia: " + referencia_factura + "\n" +
                                "FD: " + fecha_documento +  "   " +
                                "FV: " + fecha_vc_mto +  "\n" +
                                "Saldo: " + saldo
                            );

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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DocumentosActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        }catch (Exception e){
            Log.i("Exception", "" + e);
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
                Log.i("id", "" + id);
            }else{
                checkedlist.remove(new Integer(id));
                Log.i("-id", "" + id);
            }
        }
    };

    /*CONSULTA LOS DATOS DE LOS DOCUMENTOS MARCADOS Y LOS GUARDA EN LA BASE DE DATOS*/
    protected Void consultarDocumentosMarcados(int marcado) {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String accion = "consultarDocumentosMarcados";
        String URLcompleta = IP+accion+"/"+codEje+"/"+codEmp+"/"+marcado;
        Log.i("URLcompleta",""+URLcompleta);
        try {
            JsonArrayRequest req = new JsonArrayRequest(URLcompleta, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("response", "" + response);
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonobject = response.getJSONObject(i);
                            iddb = jsonobject.getString("id");
                            documento = jsonobject.getString("documento");
                            referencia_factura = jsonobject.getString("referencia_factura");
                            fecha_documento = jsonobject.getString("fecha_documento");
                            fecha_vc_mto = jsonobject.getString("fecha_vc_mto");
                            saldo = jsonobject.getString("saldo");

                            Log.i("For marcados *******", "" + i);
                            Log.i("id", "" + iddb);
                            Log.i("documento", "" + documento);
                            Log.i("referencia_factura", "" + referencia_factura);
                            Log.i("fecha_documento", "" + fecha_documento);
                            Log.i("fecha_vc_mto", "" + fecha_vc_mto);
                            Log.i("saldo", "" + saldo);

                            int id = db.agregarRecaudo(iddb,
                                    codEmp,
                                    nifEmp,
                                    documento,
                                    referencia_factura,
                                    fecha_documento,
                                    fecha_vc_mto,
                                    saldo);
                            Log.i("Insert id", "" + id);
                            if (id>=0){
                                Intent intent = new Intent(DocumentosActivity.this, GenerarRecaudoActivity.class);
                                intent.putExtra("codEje",codEje);
                                intent.putExtra("nombreEje",nombreEje);
                                intent.putExtra("nifEmp", nifEmp);
                                intent.putExtra("codEmp", codEmp);
                                intent.putExtra("nombreEmp", nombreEmp);
                                intent.putExtra("direccionEmp", direccionEmp);
                                finish();
                                startActivity(intent);
                            }else {
                                Toast.makeText(DocumentosActivity.this,
                                        "Ha ocurrido un error al ingresar el recaudo: "+id,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DocumentosActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(req);
        }catch (Exception e){
            Log.i("Exception", "" + e);
        }
        return null;
    }
}
