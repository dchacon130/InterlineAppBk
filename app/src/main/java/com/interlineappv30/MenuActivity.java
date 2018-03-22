package com.interlineappv30;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    /*VARIABLES EJECUTIVO*/
    String codEje, nombreEje;
    /*VARIABLES EMPRESA*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;
    TextView tvCodEje, tvNombreEje, tvDireccionEmp, tvNombreEmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*SE RECIBEN LOS DATOS*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        idEmp = getIntent().getStringExtra("idEmp");
        codEmp = getIntent().getStringExtra("codEmpresa");
        nifEmp = getIntent().getStringExtra("nifEmpresa");
        nombreEmp = getIntent().getStringExtra("nombreEmpresa");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        ciudadEmp = getIntent().getStringExtra("ciudadEmp");
        departamentoEmp = getIntent().getStringExtra("departamentoEmp");
        representanteEmp = getIntent().getStringExtra("representanteEmp");
        telefonoEmp = getIntent().getStringExtra("telefonoEmp");
        correoEmp = getIntent().getStringExtra("correoEmp");
        /*ASIGNO LOS VALORES*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);

        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);
        tvNombreEmp.setText(nombreEmp);
        tvDireccionEmp.setText(direccionEmp);

        final ImageButton btnDevolucion = (ImageButton)findViewById(R.id.idBtnDevolucion);
        final ImageButton btnRecaudo = (ImageButton)findViewById(R.id.idBtnRecaudo);
        /*BOTÓN DEVOLUCIONES*/
        btnDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ingreso a Devolución",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, CausalActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("idEmp", idEmp);
                intent.putExtra("codEmpresa", codEmp);
                intent.putExtra("nifEmpresa", nifEmp);
                intent.putExtra("nombreEmpresa", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                intent.putExtra("ciudadEmp", ciudadEmp);
                intent.putExtra("departamentoEmp", departamentoEmp);
                intent.putExtra("representanteEmp", representanteEmp);
                intent.putExtra("telefonoEmp", telefonoEmp);
                intent.putExtra("correoEmp", correoEmp);
                startActivity(intent);
            }
        });
        /*BOTÓN RECAUDOS*/
        btnRecaudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ingreso a Recaudo",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, RecaudoActivity.class);
                intent.putExtra("codEje",codEje);
                intent.putExtra("nombreEje",nombreEje);
                intent.putExtra("nifEmpresa", nifEmp);
                intent.putExtra("codEmpresa", codEmp);
                intent.putExtra("nombreEmpresa", nombreEmp);
                intent.putExtra("direccionEmp", direccionEmp);
                startActivity(intent);
            }
        });
    }
}
