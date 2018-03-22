package com.interlineappv30;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by INTEL on 20/08/2017.
 */

public class EnviarDevolucionesDBClass extends AppCompatActivity {

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

    public void enviarDevolucionesDBLocal(String codEje,String codEmpresa){

    }

}
