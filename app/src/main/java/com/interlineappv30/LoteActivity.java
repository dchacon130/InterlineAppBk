package com.interlineappv30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LoteActivity extends AppCompatActivity {

    private static final String PROD = "producto";
    private static final String NOMBRE = "nombre";
    private static final String LOTE = "lote";
    private static final String FECHA = "fecha_expiracion";
    private static final String MESESATRAS = "meses_atras";
    private static final String MESESADELANTE = "meses_adelante";

    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;

    String nomCausal, idCausal,
            ObservacionCli, NomCli, TelCli, EmailCli, accion = "consultarLote", DocumentoCli;
    TextView tvCodEje, tvNombreEje, tvCodEmp, tvNitEmp, tvNombreEmp, tvDireccionEmp, tvNombreCausal, tvDetObservacion, tvBuscar;
    ListView lv;
    ArrayList<LoteClass> countries = new ArrayList<LoteClass>();
    Activity activity;
    Button btnBuscar;
    DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lote);

        activity = this;
        db = new DbAdapter(getApplicationContext());

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

        DocumentoCli = getIntent().getStringExtra("DocumentoCli");
        NomCli = getIntent().getStringExtra("NomCli");
        TelCli = getIntent().getStringExtra("TelCli");
        ObservacionCli = getIntent().getStringExtra("ObservacionCli");
        EmailCli = getIntent().getStringExtra("EmailCli");

        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);

        /*El ID de la empresa (base de datos) no se asigna a ningun texto */
        tvCodEmp = (TextView) findViewById(R.id.idCodigo);
        tvNitEmp = (TextView) findViewById(R.id.idNif);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);

        /*Se muestra solo el nombre de la causal y la observación si existe*/
        tvNombreCausal = (TextView) findViewById(R.id.idCausal);
        tvDetObservacion = (TextView) findViewById(R.id.idDetObservacion);

        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);

        tvCodEmp.setText(codEmp);
        tvNitEmp.setText(nifEmp);
        tvNombreEmp.setText(nombreEmp);
        tvDireccionEmp.setText(direccionEmp);

        tvNombreCausal.setText(nomCausal);
        if (ObservacionCli.equals("")) {
            ObservacionCli = "No se ingresaron observaciones.";
            tvDetObservacion.setText(ObservacionCli);
        } else {
            tvDetObservacion.setText(ObservacionCli);
        }
        Log.i("NomCli", "" + NomCli);
        Log.i("TelCli", "" + TelCli);
        Log.i("EmailCli", "" + EmailCli);

        lv = (ListView) findViewById(android.R.id.list);
        Log.i("lv", "" + lv);

        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        tvBuscar = (TextView)findViewById(R.id.txtBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvBuscar.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Por favor ingrese datos de busqueda",Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        DireccionClass dir = new DireccionClass();
                        String IP = dir.direccionURL();

                        String URLcompleta = IP+accion+"/"+codEmp+"/"+tvBuscar.getText().toString()+"/0";
                        Log.i("URLcompleta",""+URLcompleta);

                        int respuesta = getWebServiceResponseData(URLcompleta);
                        Log.i("respuesta",""+respuesta);
                    }catch (Exception e){
                        Log.i("Exception",""+e);
                        Toast.makeText(getApplicationContext(), "Lote no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

   protected int getWebServiceResponseData(String URL) {

       int respuesta = 0;
       try{
           JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

               @Override
               public void onResponse(JSONArray response) {
                    /*MENSAJE INDICANDO QUE EL ARREGLO NO TIENE INFORMACIÓN*/
                   if(response.isNull(0)){
                       Toast.makeText(getApplicationContext(), "Lote no encontrado", Toast.LENGTH_SHORT).show();
                   }else {
                    /*CONTINUA MOSTRANDO LA INFORMACIÓN*/
                       Log.i("response", "" + response);
                       try {
                           for (int i = 0; i < response.length(); i++) {
                               JSONObject jsonobject = response.getJSONObject(i);
                               String producto = jsonobject.getString("producto");
                               String nombrePro = jsonobject.getString("nombre");
                               String lote = jsonobject.getString("lote");
                               String fechaExp = jsonobject.getString("fecha_expiracion");
                               String fechaCreacion = jsonobject.getString("fecha_creacion");
                               int mesesAtras = jsonobject.getInt("meses_atras");
                               int mesesAdelante = jsonobject.getInt("meses_adelante");

                               Log.i("For**************", "" + i);
                               Log.i("producto", "" + producto);
                               Log.i("nombre", "" + nombrePro);
                               Log.i("lote", "" + lote);
                               Log.i("fecha_expiracion", "" + fechaExp);
                               Log.i("fechaCreacion", "" + fechaCreacion);
                               Log.i("mesesAtras", "" + mesesAtras);
                               Log.i("mesesAdelante", "" + mesesAdelante);

                               HashMap<String, String> j = new HashMap<String, String>();
                               j.put(PROD, producto);
                               j.put(NOMBRE, nombrePro);
                               j.put(LOTE, lote);
                               j.put(FECHA, fechaExp);

                               LoteClass Obj = new LoteClass(producto,
                                       nombrePro,
                                       lote,
                                       fechaExp,
                                       mesesAtras,
                                       mesesAdelante);
                               countries.add(Obj);
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                           Toast.makeText(getApplicationContext(),
                                   "Error: " + e.getMessage(),
                                   Toast.LENGTH_LONG).show();
                       }

                       LoteListaClass ListEmp = new LoteListaClass(activity, countries);
                       lv.setAdapter(ListEmp);

                       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                               try {
                           /*CONSULTAR CANTIDAD DE REGISTROS CON ESE PRODUCTO Y LOTE*/
                                   String cProducto = countries.get(position).getproducto();
                                   String cLote = countries.get(position).getlote();
                                   String cfechaExp = countries.get(position).getfechaExp();
                                   int cMesAtras = countries.get(position).getmesesAtras();
                                   int cMesAdelante = countries.get(position).getmesesAdelante();
                                   Log.i("cProducto: ", "" + cProducto);
                                   Log.i("cLote: ", "" + cLote);
                                   Log.i("cMesAtras: ", "" + cMesAtras);
                                   Log.i("cMesAdelante: ", "" + cMesAdelante);
                                   /*VALIDACIÓN DE FECHAS*/
                                   Boolean fechaMa = consultaFechaMayor(cfechaExp, cMesAdelante);
                                   Log.i("fechaMa: ", "" + fechaMa);
                                   Boolean fechaMe = consultaFechaMenor(cfechaExp, cMesAtras);
                                   Log.i("fechaMe: ", "" + fechaMe);
                                   /*CONDICIÓN SI SE PUEDE DEVOLVER EL PRODUCTO*/
                                   if (fechaMa && fechaMe){
                                       String cantidad = db.countLoteProducto(cLote, cProducto);
                                       Log.i("cantidad db: ", "" + cantidad);
                                       /*MENSAJE SI EL RESULTADO ANTERIOR ES > 0*/
                                       if (Integer.parseInt(cantidad) > 0) {
                                           Log.i("cantidad if: ", "" + cantidad);
                                           Toast.makeText(getApplicationContext(), "Producto ya ingresado!",
                                                   Toast.LENGTH_SHORT).show();
                                       } else {
                                        /*CONTINUAR SI EL RESTULTADO ES = 0*/
                                           Toast.makeText(getApplicationContext(), "Ingreso al Detalle del Producto",
                                                   Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(LoteActivity.this,
                                                   DetProductoActivity.class);
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

                                           intent.putExtra("ObservacionCli", ObservacionCli);
                                           intent.putExtra("DocumentoCli", DocumentoCli);
                                           intent.putExtra("NomCli", NomCli);
                                           intent.putExtra("TelCli", TelCli);
                                           intent.putExtra("EmailCli", EmailCli);

                                           intent.putExtra("codProd", countries.get(position).getproducto());
                                           intent.putExtra("nomProd", countries.get(position).getnombrePro());
                                           intent.putExtra("loteProd", countries.get(position).getlote());
                                           intent.putExtra("fechaExpProd", countries.get(position).getfechaExp());
                                           finish();
                                           startActivity(intent);
                                       }
                                   }else{
                                       Toast.makeText(getApplicationContext(),
                                               "El lote no se encuentra entre el rango de fechas: "+cfechaExp,
                                               Toast.LENGTH_SHORT).show();
                                   }
                               } catch (Exception e) {
                                   Log.i("Exception if: ", "" + e);
                               }
                           }
                       });
                   }
               }
           },   new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Toast.makeText(LoteActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
               }
           });
           tvBuscar.setText("");
           RequestQueue requestQueue = Volley.newRequestQueue(this);
           requestQueue.add(req);
           Log.i("req",""+req);

       }catch (Exception e){
           Log.i("e",""+e);
       }
       return respuesta;
   }

   private Boolean consultaFechaMayor(String fecha, int fechaMayor){
       /*CONVERTIR FECHA A DATE*/
       Date date=null;
       String mes;
       SimpleDateFormat dFormate = new SimpleDateFormat("yyyy-MM-dd");
       try {
           date = dFormate.parse(fecha);
           dFormate.applyPattern("MMM");
           Log.i("Muestra mes",""+dFormate.format(date));
       } catch (ParseException e) {
           e.printStackTrace();
       }
       /*AUMENTO UN MES LA FECHA DE HOY*/
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(calendar.getTime()); // Configuramos la fecha que se recibe
       calendar.add(Calendar.MONTH, fechaMayor);  // numero de meses a añadir, o restar en caso de días<0

       Log.i("FechaMas1Mes",""+calendar.getTime()); // Devuelve el objeto Date con los nuevos días añadidos
       Log.i("FechaLote",""+date); //Devuelve la fecha formateada
       return date.before(calendar.getTime());
       //return calendar.getTime().after(date);
   }

    private Boolean consultaFechaMenor(String fecha, int fechaMenor){
       /*CONVERTIR FECHA A DATE*/
        Date date=null;
        SimpleDateFormat dFormate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dFormate.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /*AUMENTO UN MES LA FECHA DE HOY*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime()); //Configuramos la fecha que se recibe
        calendar.add(Calendar.MONTH, fechaMenor);  // numero de meses a añadir, o restar en caso de días<0

        Log.i("Calendar",""+calendar.getTime()); // Devuelve el objeto Date con los nuevos días añadidos
        Log.i("date",""+date); //Devuelve la fecha formateada
        return date.after(calendar.getTime());
        //return calendar.getTime().after(date);
    }

}
