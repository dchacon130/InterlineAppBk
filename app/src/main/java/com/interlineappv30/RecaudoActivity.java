package com.interlineappv30;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RecaudoActivity extends AppCompatActivity {

    /*VARIABLES EJECUTIVO*/
    String codEje, nombreEje;
    /*VARIABLES EMPRESA*/
    String nifEmp, nombreEmp, direccionEmp, codEmp;
    TextView tvCodEje, tvNombreEje, tvDireccionEmp, tvNombreEmp, tvPesosTotal, tvPesos30,
            tvPesos51, tvPesosVencido;
    Button pagos, recaudar;
    Boolean consultaRecaudo;
    String total_carteraf, total_treintaf, total_cincuentaynuevef, total_vencidaf;
    Integer total_cartera, total_treinta, total_cincuentaynueve, total_vencida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recaudo);

        /*SE RECIBEN LOS DATOS*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        nifEmp = getIntent().getStringExtra("nifEmpresa");
        codEmp = getIntent().getStringExtra("codEmpresa");
        nombreEmp = getIntent().getStringExtra("nombreEmpresa");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        /*ASIGNO LOS VALORES*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);
        /*IDENTIFICO LOS TV DE LOS VALORES*/
        tvPesosTotal = (TextView) findViewById(R.id.idPesosTotal);
        tvPesos30 = (TextView) findViewById(R.id.idPesos30);
        tvPesos51 = (TextView) findViewById(R.id.idPesos51);
        tvPesosVencido = (TextView) findViewById(R.id.idPesosVencido);

        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);
        tvNombreEmp.setText(nombreEmp);
        tvDireccionEmp.setText(direccionEmp);

        /*CONSULTAMOS LA INFORMACIÓN DE CARTERA*/
        consultaRecaudo = getRecaudoEmpresa(codEmp);
        Log.i("consultaRecaudo",""+consultaRecaudo);

        pagos = (Button)findViewById(R.id.btnPagos);
        recaudar = (Button)findViewById(R.id.btnRecaudar);
        /*CONSULTAR PAGOS*/
        pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecaudoActivity.this, PagosActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmp", nifEmp);
                intent.putExtra("codEmp", codEmp);
                intent.putExtra("nombreEmp", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                startActivity(intent);
            }
        });
        /*GENERAR RECAUDOS*/
        recaudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecaudoActivity.this, GenerarRecaudoActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmp", nifEmp);
                intent.putExtra("codEmp", codEmp);
                intent.putExtra("nombreEmp", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                startActivity(intent);
            }
        });
    }

    /*SE CONSULTA LA INFORMACIÓN DE LOS SALDOS Y SE MUESTRA*/
    protected Boolean getRecaudoEmpresa(String codEmpr) {
        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();
        String accion = "consultarRecaudoId";
        String URLcompleta = IP+accion+"/"+codEmpr+"/0/0";
        Log.i("URLcompleta",""+URLcompleta);

        JsonArrayRequest req = new JsonArrayRequest(URLcompleta, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonobject = response.getJSONObject(i);
                        /*VARIABLE STRING FORMATEADA*/
                        total_carteraf = jsonobject.getString("total_carteraf");
                        total_treintaf = jsonobject.getString("total_treintaf");
                        total_cincuentaynuevef = jsonobject.getString("total_cincuentaynuevef");
                        total_vencidaf = jsonobject.getString("total_vencidaf");
                        /*VARIABLES ENTERAS SIN FORMATEAR*/
                        total_cartera = jsonobject.getInt("total_cartera");
                        total_treinta = jsonobject.getInt("total_treinta");
                        total_cincuentaynueve = jsonobject.getInt("total_cincuentaynueve");
                        total_vencida = jsonobject.getInt("total_vencida");

                        /*LOG INFORMANDO LOS REGISTROS*/
                        Log.i("For**************",""+i);
                        Log.i("total_cartera",""+total_cartera);
                        Log.i("total_treinta",""+total_treinta);
                        Log.i("total_cincuentaynueve",""+total_cincuentaynueve);
                        Log.i("total_vencida",""+total_vencida);

                        tvPesosTotal.setText(total_carteraf);
                        tvPesos30.setText(total_treintaf);
                        tvPesos51.setText(total_cincuentaynuevef);
                        tvPesosVencido.setText(total_vencidaf);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        },   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecaudoActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
        return true;
    }
}
