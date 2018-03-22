package com.interlineappv30;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DescuentosActivity extends AppCompatActivity {

    String codEje, nombreEje, nifEmp, codEmp, nombreEmp, direccionEmp;
    Spinner spTipoDescuento;
    EditText valorDescuento, observDesc;
    Button btnAdicionarDescuento;
    String sTipoDescuento, sValorDescuento, sObsDesc;
    DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descuentos);
        db = new DbAdapter(getApplicationContext());
        /*SE RECIBEN LOS DATOS DE RECAUDO ACTIVITY*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        nifEmp = getIntent().getStringExtra("nifEmp");
        codEmp = getIntent().getStringExtra("codEmpr");
        nombreEmp = getIntent().getStringExtra("nombreEmp");
        direccionEmp = getIntent().getStringExtra("direccionEmp");

        spTipoDescuento = (Spinner)findViewById(R.id.spnTipoDescuento);
        valorDescuento = (EditText)findViewById(R.id.idValorDescuento);
        observDesc = (EditText)findViewById(R.id.idObservaciones);

        btnAdicionarDescuento = (Button)findViewById(R.id.btnAgregarDescuento);
        btnAdicionarDescuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTipoDescuento = spTipoDescuento.getSelectedItem().toString();
                sValorDescuento = valorDescuento.getText().toString();
                sObsDesc = observDesc.getText().toString();

                Log.i("sTipoDescuento",""+sTipoDescuento);
                Log.i("sValorDescuento",""+sValorDescuento);
                Log.i("sObsDesc",""+sObsDesc);

                int id = db.agregarDescuento(codEmp,
                        nifEmp,
                        sTipoDescuento,
                        sValorDescuento,
                        sObsDesc);
                Log.i("Insert id", "" + id);
                if (id>0){
                    Intent intent = new Intent(DescuentosActivity.this, GenerarRecaudoActivity.class);
                    intent.putExtra("codEje",codEje);
                    intent.putExtra("nombreEje",nombreEje);
                    intent.putExtra("nifEmp", nifEmp);
                    intent.putExtra("codEmp", codEmp);
                    intent.putExtra("nombreEmp", nombreEmp);
                    intent.putExtra("direccionEmp", direccionEmp);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(DescuentosActivity.this,
                            "Ha ocurrido un error al ingresar el recaudo: "+id,
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
