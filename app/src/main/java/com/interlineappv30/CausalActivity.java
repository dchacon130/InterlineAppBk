package com.interlineappv30;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CausalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;
    String nomCausal;
    TextView tvDocumentoCliente, tvCodEje, tvNombreEje, tvCodEmp, tvNitEmp, tvNombreEmp, tvDireccionEmp, tvObservacionCli,
            tvNomCli, tvTelCli, tvEmailCli;
    int idCausal;
    Button btnSiguiente;
    Spinner spnCausal;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_causal);

        fa = this;

        /*Recibo los datos*/
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

        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);

        /*El ID de la empresa (base de datos) no se asigna a ningun texto */
        tvCodEmp = (TextView) findViewById(R.id.idCodigo);
        tvNitEmp = (TextView) findViewById(R.id.idNif);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);

        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);

        tvCodEmp.setText(codEmp);
        tvNitEmp.setText(nifEmp);
        tvNombreEmp.setText(nombreEmp);
        tvDireccionEmp.setText(direccionEmp);

        /*Selecciono el tem del Spinner*/
        spnCausal = (Spinner) findViewById(R.id.spnCausal);
        spnCausal.setOnItemSelectedListener(this);

        /*TOMO LOS DATOS DE LOS CAMPOS DE TEXTO*/
        tvDocumentoCliente = (TextView)findViewById(R.id.txtDocumentoCliente);
        tvObservacionCli = (TextView) findViewById(R.id.idObservacion);
        tvNomCli = (TextView) findViewById(R.id.txtNombreCliente);
        tvTelCli = (TextView) findViewById(R.id.txtTelCliente);
        tvEmailCli = (TextView) findViewById(R.id.txtEmailCliente);

        btnSiguiente = (Button) findViewById(R.id.nextButton);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Se validan campos obligatorios del cliente menos las observaciones*/
                String sDocCli = tvDocumentoCliente.getText().toString();
                String sNomCli = tvNomCli.getText().toString();
                String sTelCli = tvTelCli.getText().toString();
                String sEmaiCli = tvEmailCli.getText().toString();

                if (sNomCli.equals("") || sTelCli.equals("") || sEmaiCli.equals("") || sDocCli.equals("")) {
                    Toast.makeText(getApplicationContext(), "Por favor complete los datos del cliente.", Toast.LENGTH_SHORT).show();
                }
                else if (!validarEmail(tvEmailCli.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Por favor verifique el Correo.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(CausalActivity.this, LoteActivity.class);
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

                    /*Convierte el idCausal a String*/
                    String idCausalS = String.valueOf(idCausal);
                    intent.putExtra("idCausal", idCausalS);
                    intent.putExtra("nomCausal", nomCausal);

                    intent.putExtra("DocumentoCli", tvDocumentoCliente.getText().toString());
                    intent.putExtra("ObservacionCli", tvObservacionCli.getText().toString());
                    intent.putExtra("NomCli", tvNomCli.getText().toString());
                    intent.putExtra("TelCli", tvTelCli.getText().toString());
                    intent.putExtra("EmailCli", tvEmailCli.getText().toString());

                    Log.i("Pos", "" + idCausal);
                    Log.i("itm", "" + nomCausal);

                    startActivity(intent);
                }
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itm = parent.getItemAtPosition(position).toString();
        nomCausal = itm;
        idCausal = position + 1;
        Log.i("Pos", "" + position);
        Log.i("itm", "" + itm);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /*No hay nada para no seleccionar*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

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

    private void finalActivity() {
        try{

            /*Se validan campos obligatorios del cliente menos las observaciones*/
            String sDocCli = tvDocumentoCliente.getText().toString();
            String sNomCli = tvNomCli.getText().toString();
            String sTelCli = tvTelCli.getText().toString();
            String sEmaiCli = tvEmailCli.getText().toString();

            if (sNomCli.equals("") || sTelCli.equals("") || sEmaiCli.equals("") || sDocCli.equals("")) {
                Toast.makeText(getApplicationContext(), "Por favor complete los datos del cliente.", Toast.LENGTH_SHORT).show();
            }
            else if (!validarEmail(tvEmailCli.getText().toString())){
                Toast.makeText(getApplicationContext(), "Por favor verifique el Correo.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CausalActivity.this, FinalActivity.class);
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
                /*DATOS DE CAUSAL*/
                /*Convierte el idCausal a String*/
                String idCausalS = String.valueOf(idCausal);
                intent.putExtra("idCausal", idCausalS);
                intent.putExtra("nomCausal", nomCausal);
                /*DATOS DEL CLIENTE*/
                intent.putExtra("DocumentoCli", tvDocumentoCliente.getText().toString());
                intent.putExtra("ObservacionCli", tvObservacionCli.getText().toString());
                intent.putExtra("NomCli", tvNomCli.getText().toString());
                intent.putExtra("TelCli", tvTelCli.getText().toString());
                intent.putExtra("EmailCli", tvEmailCli.getText().toString());

                Log.i("Pos", "" + idCausal);
                Log.i("itm", "" + nomCausal);

                startActivity(intent);
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
            Log.i("Error:",""+e);
        }
    }

    private void precintoActivity(){
        try{

            /*Se validan campos obligatorios del cliente menos las observaciones*/
            String sDocCli = tvDocumentoCliente.getText().toString();
            String sNomCli = tvNomCli.getText().toString();
            String sTelCli = tvTelCli.getText().toString();
            String sEmaiCli = tvEmailCli.getText().toString();

            if (sNomCli.equals("") || sTelCli.equals("") || sEmaiCli.equals("") || sDocCli.equals("")) {
                Toast.makeText(getApplicationContext(), "Por favor complete los datos del cliente.", Toast.LENGTH_SHORT).show();
            }
            else if (!validarEmail(tvEmailCli.getText().toString())){
                Toast.makeText(getApplicationContext(), "Por favor verifique el Correo.", Toast.LENGTH_SHORT).show();
            }
            else {

                Intent intent = new Intent(CausalActivity.this, PrecintoActivity.class);
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

            /*DATOS DEL CLIENTE*/
                intent.putExtra("DocumentoCli", tvDocumentoCliente.getText().toString());
                intent.putExtra("ObservacionCli", tvObservacionCli.getText().toString());
                intent.putExtra("NomCli", tvNomCli.getText().toString());
                intent.putExtra("TelCli", tvTelCli.getText().toString());
                intent.putExtra("EmailCli", tvEmailCli.getText().toString());
                finish();
                startActivity(intent);
            }
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
            Log.i("Error:",""+e);
        }
    }

    @SuppressLint("NewApi")
    private void exitActivity() {
        finishAffinity();
    }
}