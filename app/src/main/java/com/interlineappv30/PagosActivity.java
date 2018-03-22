package com.interlineappv30;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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

public class PagosActivity extends AppCompatActivity {
    String codigo_cliente, consecutivo, fecha_sys, pago,
            codEmp, codEje;
    String nombreEje,
            nifEmp,
            nombreEmp,
            direccionEmp;
    TextView tvCodEje, tvNombreEje;
    ArrayList<PagosListClass> countries = new ArrayList<PagosListClass>();
    Activity activity;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        activity = this;
        /*SE RECIBEN LOS DATOS DE RECAUDO ACTIVITY*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        nifEmp = getIntent().getStringExtra("nifEmp");
        codEmp = getIntent().getStringExtra("codEmp");
        nombreEmp = getIntent().getStringExtra("nombreEmp");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        /*ASIGNO LOS VALORES*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);
        lv = (ListView) findViewById(android.R.id.list);
        getDescuentosPagos();
    }

    protected Void getDescuentosPagos() {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String accion = "consultarDocumentoPagos";
        String URLcompleta = IP+accion+"/"+codEje+"/"+codEmp+"/0";
        Log.i("URLcompleta",""+URLcompleta);
        JsonArrayRequest req = new JsonArrayRequest(URLcompleta, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("response",""+response);
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonobject = response.getJSONObject(i);
                        codigo_cliente = jsonobject.getString("codigo_cliente");
                        consecutivo = jsonobject.getString("consecutivo");
                        fecha_sys = jsonobject.getString("fecha_sys");
                        pago = jsonobject.getString("pago");
                        Log.i("For**************",""+i);
                        Log.i("codigo_cliente",""+codigo_cliente);
                        Log.i("consecutivo",""+consecutivo);
                        Log.i("fecha_sys",""+fecha_sys);
                        Log.i("pago",""+pago);
//                        HashMap<String, String> j = new HashMap<String, String>();
//                        j.put(ID, idEmp);
//                        j.put(COD_CLIENTE, codEmp);
//                        j.put(NIF, nifEmp);
//                        j.put(NOMBRE, nombreEmp);
//                        j.put(DIR, direccionEmp);
                        PagosListClass Obj = new PagosListClass(codigo_cliente, consecutivo, fecha_sys, pago);
                        countries.add(Obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                ListoPagosClass ListEmp = new ListoPagosClass(activity, countries);
                lv.setAdapter(ListEmp);
            }
        },   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PagosActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        return null;
    }
}
