package com.interlineappv30;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FinalActivity extends AppCompatActivity {

    /*Variables ejecutivo*/
    String codEje, nombreEje;
    /*Variables de la empresa*/
    String idEmp, codEmp, nifEmp, nombreEmp, direccionEmp, ciudadEmp, departamentoEmp,
            representanteEmp,telefonoEmp,correoEmp, cantidadProd;
    String idCausal, nomCausal,DocumentoCli,NomCli,TelCli,EmailCli,ObservacionCli;
    TextView tvCodEje, tvNombreEje, tvNitEmp, tvNombreEmp, tvCodEmp, tvCantidad;
    Button btnEnd;

    private DbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

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
        EmailCli = getIntent().getStringExtra("EmailCli");
        ObservacionCli = getIntent().getStringExtra("ObservacionCli");


        /*Inicializo los textos del xml*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvCodEmp = (TextView) findViewById(R.id.idCodigo);
        tvNitEmp = (TextView) findViewById(R.id.idNif);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);
        tvCantidad = (TextView) findViewById(R.id.idCantidadProductosDevolver);


        /*Asigno los valores*/
        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);
        tvCodEmp.setText(codEmp);
        tvNitEmp.setText(nifEmp);
        tvNombreEmp.setText(nombreEmp);
        dbHelper = new DbAdapter(this);
        dbHelper.open();

        String cantidadProd = dbHelper.countCantidadLotes(codEmp);
        tvCantidad.setText(cantidadProd);

        displayListView();
    }

    private void displayListView() {
        try{
            final Cursor[] cursor = {dbHelper.consultarRegistrosByEmpresa(codEje,codEmp)};
            String[] columnas = new String[]{
                    dbHelper.KEY_CODPROD,
                    dbHelper.KEY_NOMPROD,
                    dbHelper.KEY_LOTEPROD,
                    dbHelper.KEY_CANTIDADDEV
            };

            // the XML defined views which the data will be bound to
            int[] to = new int[] {
                    R.id.codProducto,
                    R.id.nombreProducto,
                    R.id.loteProducto,
                    R.id.cantidadDev,
            };

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.list_item_final,
                    cursor[0],
                    columnas,
                    to,
                    0);
            Log.i("dataAdapter",""+dataAdapter);
            ListView listView = (ListView) findViewById(android.R.id.list);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,int position, long id) {

                    cursor[0] = (Cursor) listView.getItemAtPosition(position);
                    Log.i("cursor",""+ cursor[0]);
                    Log.i("position",""+position);
                    Log.i("id",""+id);

                    // Se genera el mensaje de alerta al borrar un registro
                    String MSJ = "Alerta!";
                    String INF = "Esta seguro de borrar el registro?";
                    AlertDialog alert;
                    alert = new AlertDialog.Builder(FinalActivity.this)
                        .setTitle(MSJ)
                        .setMessage(INF)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String countryCode = cursor[0].getString(cursor[0].getColumnIndexOrThrow("_id"));

                                int cr = dbHelper.deleteById(countryCode);
                                Log.i("cr",""+cr);
                                Toast.makeText(getApplicationContext(),"El registro se elimino correctamente, "+cr,Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                }
            });

            //EditText myFilter = (EditText) findViewById(R.id.);
            tvNitEmp.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }
                public void beforeTextChanged(CharSequence s, int start,int count, int after) {

                }
                public void onTextChanged(CharSequence s, int start,int before, int count) {
                    dataAdapter.getFilter().filter(s.toString());
                }
            });

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return dbHelper.consultarRegistrosByEmpresa(codEje,constraint.toString());
                }
            });
        }catch (SQLException e){
            Log.i("Error",""+e);
        }
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

    private void precintoActivity() {
        try{
            Toast.makeText(getApplicationContext(), "Asignar precinto", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FinalActivity.this, PrecintoActivity.class);
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
