package com.interlineappv30;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DocumentosCreados extends AppCompatActivity {
    /*Variables ejecutivo*/
    private String codEje, nombreEje;
    /*Variables de la empresa*/
    private String codEmp, nifEmp, nombreEmp, direccionEmp;
    TextView tvCodEje, tvNombreEje, tvNitEmp, tvNombreEmp;

    private DbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_creados);
        /*Recibo los datos*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        codEmp = getIntent().getStringExtra("codEmp");
        nifEmp = getIntent().getStringExtra("nifEmp");
        nombreEmp = getIntent().getStringExtra("nombreEmpresa");
        direccionEmp = getIntent().getStringExtra("direccionEmp");
        /*Inicializo los textos del xml*/
        tvCodEje = (TextView) findViewById(R.id.txtCodEje);
        tvNombreEje = (TextView) findViewById(R.id.txtNomEje);
        tvNombreEmp = (TextView) findViewById(R.id.idNombreEmp);

        /*Asigno los valores*/
        tvCodEje.setText(codEje);
        tvNombreEje.setText(nombreEje);
        tvNombreEmp.setText(nombreEmp);

        dbHelper = new DbAdapter(this);
        dbHelper.open();
        displayListView();
    }

    private void displayListView() {
        try{
            final Cursor[] cursor = {dbHelper.consultarDocumentosByEmpresa(codEmp)};
            String[] columnas = new String[]{
                    dbHelper.KEY_DOCUMENTO_R,
                    dbHelper.KEY_RFACTURA_R,
                    dbHelper.KEY_FD_R,
                    dbHelper.KEY_FV_R,
                    dbHelper.KEY_SALDO_R
            };
            // the XML defined views which the data will be bound to
            int[] to = new int[] {
                    R.id.iddocumento,
                    R.id.idreferencia_factura,
                    R.id.idfecha_documento,
                    R.id.idfecha_vc_mto,
                    R.id.idsaldo
            };

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.list_item_delete_documentos,
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
                public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                    cursor[0] = (Cursor) listView.getItemAtPosition(position);
                    Log.i("cursor",""+ cursor[0]);
                    Log.i("position",""+position);
                    Log.i("id",""+id);

                    // Se genera el mensaje de alerta al borrar un registro
                    String MSJ = "Alerta!";
                    String INF = "Esta seguro de borrar el registro?";
                    AlertDialog alert;
                    alert = new AlertDialog.Builder(DocumentosCreados.this)
                            .setTitle(MSJ)
                            .setMessage(INF)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String countryCode = cursor[0].getString(cursor[0].getColumnIndexOrThrow("_id"));

                                    int cr = dbHelper.eliminarDocumento(countryCode);
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


            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                public Cursor runQuery(CharSequence constraint) {
                    return dbHelper.consultarRegistrosByEmpresa(codEje,constraint.toString());
                }
            });
        }catch (SQLException e){
            Log.i("Error",""+e);
        }
    }
}
