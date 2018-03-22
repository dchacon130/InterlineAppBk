package com.interlineappv30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;

public class ClientesActivity extends AppCompatActivity {

    private static final String ID = "id";
    private static final String COD_CLIENTE = "codigo_cliente";
    private static final String NIF = "nif";
    private static final String NOMBRE = "nombre";
    private static final String DIR = "direccion";

    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, nomCausal, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;
    TextView txtCodEje, txtNomEje;
    ListView lv;
    ArrayList<EmpresaClass> countries = new ArrayList<EmpresaClass>();
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        activity = this;

        /*Tomo la variable de LoginActivity*/
        //cedulaEje = getIntent().getStringExtra("cedulaEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        codEje = getIntent().getStringExtra("codigoEje");
        /*se la asigno al TextView idEjecutivo*/
        txtCodEje = (TextView)findViewById(R.id.txtCodEje);
        txtNomEje = (TextView)findViewById(R.id.txtNomEje);

        txtCodEje.setText(codEje);
        txtNomEje.setText(nombreEje);

        lv = (ListView) findViewById(android.R.id.list);

        getWebServiceResponseData();

    }

    protected Void getWebServiceResponseData() {

        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String accion = "consultarCliente";
        String URLcompleta = IP+accion+"/"+codEje+"/0/0";
        Log.i("URLcompleta",""+URLcompleta);

        JsonArrayRequest req = new JsonArrayRequest(URLcompleta, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("response",""+response);
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonobject = response.getJSONObject(i);

                        idEmp = jsonobject.getString("id");
                        codEmp = jsonobject.getString("codigo_cliente");
                        nifEmp = jsonobject.getString("nif");
                        nombreEmp = jsonobject.getString("nombre");
                        direccionEmp = jsonobject.getString("direccion");
                        ciudadEmp = jsonobject.getString("ciudad_id");
                        departamentoEmp = jsonobject.getString("departamento");
                        representanteEmp = jsonobject.getString("nombre_representante");
                        telefonoEmp = jsonobject.getString("telefono");
                        correoEmp = jsonobject.getString("correo");

                        Log.i("For**************",""+i);
                        Log.i("id",""+idEmp);
                        Log.i("codigo_cliente",""+codEmp);
                        Log.i("nif",""+nifEmp);
                        Log.i("nombreEmpresa",""+nombreEmp);
                        Log.i("direccion",""+direccionEmp);

                        HashMap<String, String> j = new HashMap<String, String>();
                        j.put(ID, idEmp);
                        j.put(COD_CLIENTE, codEmp);
                        j.put(NIF, nifEmp);
                        j.put(NOMBRE, nombreEmp);
                        j.put(DIR, direccionEmp);

                        EmpresaClass Obj = new EmpresaClass(idEmp, codEmp, nifEmp, nombreEmp, direccionEmp,
                                ciudadEmp, departamentoEmp, representanteEmp, telefonoEmp, correoEmp);
                        countries.add(Obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                ListEmpresasClass ListEmp = new ListEmpresasClass(activity, countries);
                lv.setAdapter(ListEmp);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Toast.makeText(getApplicationContext(),"Ingreso a Lote Producto...",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ClientesActivity.this, MenuActivity.class);

                        intent.putExtra("codEje",codEje);
                        intent.putExtra("nombreEje",nombreEje);

                        intent.putExtra("idEmp", countries.get(position).getId());
                        intent.putExtra("codEmpresa", countries.get(position).getCodEmpresa());
                        intent.putExtra("nifEmpresa", countries.get(position).getnif());
                        intent.putExtra("nombreEmpresa", countries.get(position).getnombreEmpresa());
                        intent.putExtra("direccionEmp", countries.get(position).getdireccionEmp());
                        intent.putExtra("ciudadEmp", countries.get(position).getciudadEmp());
                        intent.putExtra("departamentoEmp", countries.get(position).getdepartamentoEmp());
                        intent.putExtra("representanteEmp", countries.get(position).getrepresentanteEmp());
                        intent.putExtra("telefonoEmp", countries.get(position).gettelefonoEmp());
                        intent.putExtra("correoEmp", countries.get(position).getcorreoEmp());

                        startActivity(intent);
                    }
                });
            }
        },   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ClientesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        return null;
    }
}
