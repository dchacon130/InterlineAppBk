package com.interlineappv30;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /*Declarar variables*/
    EditText usuarioBox, passBox;
    Button loginButton;
    String accion = "consultarEjecutivo", usuario, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Asignar variables de las cajas de texto*/
        usuarioBox = (EditText)findViewById(R.id.usuarioBox);
        passBox = (EditText)findViewById(R.id.passBox);
        loginButton = (Button)findViewById(R.id.loginButton);


        usuarioBox.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    usuario = usuarioBox.getText().toString();
                    password = passBox.getText().toString();

                    Log.i("IP",""+accion);
                    Log.i("usuarioBox",""+usuario);
                    Log.i("passBox",""+password);

                        /*new ConsultarRest(accion, usuario, password);
                        cr.*/getConsultarEjecutivo(accion, usuario, password);
                }catch (Exception e){
                    Log.i("Error",""+e);
                }
            }
        });
    }

    private void getConsultarEjecutivo(String accion, String usuario, final String password){

        DireccionClass dir = new DireccionClass();
        String IP = dir.direccionURL();

        String URLcompleta = IP+accion+"/"+usuario+"/"+password+"/0";

        Log.i("accion",""+accion);
        Log.i("usuario",""+usuario);
        Log.i("password",""+password);
        Log.i("URLcompleta",""+URLcompleta);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URLcompleta,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    String cedula = "",nombre = "", codigo="", pass="";
                    /*Convierto el arreglo a objeto*/
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        /*Leo el objeto*/
                        JSONObject jo = ja.getJSONObject(i);
                        cedula = jo.getString("cedula");
                        nombre = jo.getString("nombre");
                        codigo = jo.getString("codigo");
                        pass = jo.getString("pass");

                        Log.i("cedula",""+cedula);
                        Log.i("nombre",""+nombre);
                        Log.i("codigo",""+codigo);
                    }
                    if (pass.equals(passBox.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                        //intent.putExtra("cedulaEje",cedula);
                        intent.putExtra("nombreEje",nombre);
                        intent.putExtra("codigoEje",codigo);

                        usuarioBox.setText("");
                        passBox.setText("");

                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Verifique Usuario y Contraseña", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error: "+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
 }

