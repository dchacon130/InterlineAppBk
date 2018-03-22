package com.interlineappv30;

import android.content.Intent;
import android.database.SQLException;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetProductoActivity extends AppCompatActivity {
    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp;
    String nomCausal, idCausal,
            ObservacionCli, NomCli, TelCli, EmailCli, codProd,nomProd,loteProd,fechaExpProd, ObservacionCant, DocumentoCli;
    TextView tvCodEje, tvNombreEje, tvCodEmp, tvNitEmp, tvNombreEmp, tvDireccionEmp, tvNombreCausal, tvDetObservacion, tvCodProducto,tvNombreProducto,
            tvLoteProd,tvFechaVenc;
    EditText tvCantidad, tvObsCantidad;
    Button btnAdd, btnPrecinto;
    DbAdapter db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_producto);


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

        ObservacionCli = getIntent().getStringExtra("ObservacionCli");
        DocumentoCli = getIntent().getStringExtra("DocumentoCli");
        NomCli = getIntent().getStringExtra("NomCli");
        TelCli = getIntent().getStringExtra("TelCli");
        EmailCli = getIntent().getStringExtra("EmailCli");

        codProd = getIntent().getStringExtra("codProd");
        nomProd = getIntent().getStringExtra("nomProd");
        loteProd = getIntent().getStringExtra("loteProd");
        fechaExpProd = getIntent().getStringExtra("fechaExpProd");

        /*Inicializo los textos del xml*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);

        tvCodEmp = (TextView) findViewById(R.id.idCodigo);
        tvNitEmp = (TextView) findViewById(R.id.idNif);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvDireccionEmp = (TextView) findViewById(R.id.idDireccionEmp);

        tvNombreCausal = (TextView) findViewById(R.id.idCausal);
        tvDetObservacion = (TextView) findViewById(R.id.idDetObservacion);

        tvCodProducto = (TextView) findViewById(R.id.idCodProducto);
        tvNombreProducto = (TextView) findViewById(R.id.idNombreProducto);
        tvLoteProd = (TextView) findViewById(R.id.idLoteProd);
        tvFechaVenc = (TextView) findViewById(R.id.idFechaVenc);

        /*Asigno los valores*/
        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);

        tvCodEmp.setText(codEmp);
        tvNitEmp.setText(nifEmp);
        tvNombreEmp.setText(nombreEmp);
        tvDireccionEmp.setText(direccionEmp);

        tvNombreCausal.setText(nomCausal);
        tvDetObservacion.setText(ObservacionCli);

        tvCodProducto.setText(codProd);
        tvNombreProducto.setText(nomProd);
        tvLoteProd.setText(loteProd);
        tvFechaVenc.setText(fechaExpProd);

        tvCantidad = (EditText)findViewById(R.id.idCantidadDev);
        tvObsCantidad = (EditText)findViewById(R.id.idObservacion);

        btnAdd = (Button) findViewById(R.id.btnAdicionar);
        btnPrecinto = (Button) findViewById(R.id.btnFinalizarDev);

        db = new DbAdapter(getApplicationContext());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try{
                    if(tvCantidad.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Por favor ingrese la cantidad",Toast.LENGTH_SHORT).show();
                    }else{
                        saveState();
                        setResult(RESULT_OK);
                        Toast.makeText(getApplicationContext(),"Registro guardado correctamente!",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DetProductoActivity.this, LoteActivity.class);
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

                        intent.putExtra("DocumentoCli", DocumentoCli);
                        intent.putExtra("ObservacionCli", ObservacionCli);
                        intent.putExtra("NomCli", NomCli);
                        intent.putExtra("TelCli", TelCli);
                        intent.putExtra("EmailCli", EmailCli);
                        finish();
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Log.i("saveState Error:",""+e);
                }
            }
        });

        btnPrecinto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try{
                    if(tvCantidad.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Por favor ingrese la cantidad",Toast.LENGTH_SHORT).show();
                    }else {
                        saveState();
                        setResult(RESULT_OK);
                        Toast.makeText(getApplicationContext(), "Registro guardado correctamente!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DetProductoActivity.this, PrecintoActivity.class);
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

                        intent.putExtra("DocumentoCli", DocumentoCli);
                        intent.putExtra("ObservacionCli", ObservacionCli);
                        intent.putExtra("NomCli", NomCli);
                        intent.putExtra("TelCli", TelCli);
                        intent.putExtra("EmailCli", EmailCli);
                        finish();
                        startActivity(intent);
                    }
                }catch (SQLException e){
                    Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
                    Log.i("Error:",""+e);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveState(){

        try{
            String dbcodEje = codEje;
            String dbnombreEje = nombreEje;
            String dbcodEmpresa = codEmp;
            String dbnifEmp = nifEmp;
            String dbnombreEmpresa = nombreEmp;
            String dbdireccionEmp = direccionEmp;
            String dbidCausal = idCausal;
            String dbnomCausal = nomCausal;
            String dbObservacionCli = ObservacionCli;
            String dbDocumentoCli = DocumentoCli;
            String dbNomCli = NomCli;
            String dbTelCli = TelCli;
            String dbEmailCli = EmailCli;
            String dbcodProd = codProd;
            String dbnomProd = nomProd;
            String dbloteProd = loteProd;
            String dbfechaExpProd = fechaExpProd;
            String dbcantdadDev = tvCantidad.getText().toString();

            if (tvObsCantidad.getText().toString().equals("")) {
                ObservacionCant = "No se ingresaron observaciones.";
            } else {
                ObservacionCant = tvObsCantidad.getText().toString();
            }
            System.out.println(ObservacionCant);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDate = sdf.format(cal.getTime());
            String dbfecha_sys =strDate;

            String dbestado = "1";

            int id = db.agregarRegistro(dbcodEje,dbnombreEje,dbcodEmpresa,dbnifEmp,dbnombreEmpresa,dbdireccionEmp,dbidCausal,
                    dbnomCausal,dbObservacionCli,dbDocumentoCli,dbNomCli,dbTelCli,dbEmailCli,dbcodProd,dbnomProd,dbloteProd,dbfechaExpProd,
                    dbcantdadDev,ObservacionCant,dbfecha_sys,dbestado);
            System.out.println(id);

            tvCantidad.setText("");

        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),"Ups, ha ocurrido un error, "+e,Toast.LENGTH_SHORT).show();
            Log.i("Exception ",""+e);
        }
    }
}
