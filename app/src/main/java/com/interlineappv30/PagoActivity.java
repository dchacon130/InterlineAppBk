package com.interlineappv30;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PagoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spTipoBanco,
            spTipoCuenta,
            spTipoPago;
    Button btnDate, btnAgregarPago;
    private EditText txtFecha;
    private int mYear, mMonth, mDay;
    TextView tvNumeroCuenta, tvValorPago, tvDate;
    private String txtSpinnerTipoPago;
    String nomPago,
            sTipoPago,
            sNumeroCuenta,
            sTipoBanco,
            sTipoCuenta,
            sFecha,
            sValorPago,
            codEmp,codEje,nombreEje,nifEmp,nombreEmp,direccionEmp;
    DbAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        /*SE RECIBEN LOS DATOS DE RECAUDO ACTIVITY*/
        codEje = getIntent().getStringExtra("codEje");
        nombreEje = getIntent().getStringExtra("nombreEje");
        nifEmp = getIntent().getStringExtra("nifEmp");
        codEmp = getIntent().getStringExtra("codEmpr");
        nombreEmp = getIntent().getStringExtra("nombreEmp");
        direccionEmp = getIntent().getStringExtra("direccionEmp");

        db = new DbAdapter(getApplicationContext());
        /*RELACIONO LOS CAMPOS EXISTENTES*/
        //COMBO BANCO
        spTipoPago = (Spinner) findViewById(R.id.spnTipoPago);
        spTipoBanco = (Spinner) findViewById(R.id.spnTipoBanco);
        spTipoCuenta = (Spinner) findViewById(R.id.spnCodigoCuenta);
        tvNumeroCuenta = (TextView)findViewById(R.id.idNumeroCuenta);
        tvValorPago = (EditText)findViewById(R.id.idValorPago);
        txtFecha = (EditText)findViewById(R.id.txtDate);

        btnAgregarPago = (Button)findViewById(R.id.btnAgregarPago);
        btnDate = (Button)findViewById(R.id.btn_date);

        loadSpinnerBancos();
        /*BOTÓN FECHA*/
        btnDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(PagoActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year,
                                                      int monthOfYear,
                                                      int dayOfMonth) {
                                    txtFecha.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }catch (Exception e){
                    Log.i("Exception",""+e);
                }
            }
        });

        /*BOTÓN AGREGAR PAGO*/
        btnAgregarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*Se validan campos obligatorios del cliente menos las observaciones*/
                sNumeroCuenta = tvNumeroCuenta.getText().toString();
                sValorPago = tvValorPago.getText().toString();
                sFecha = txtFecha.getText().toString();
                sTipoPago = spTipoPago.getSelectedItem().toString();
                sTipoBanco = spTipoBanco.getSelectedItem().toString();
                sTipoCuenta = spTipoCuenta.getSelectedItem().toString();

                Log.i("sNumeroCuenta",""+sNumeroCuenta);
                Log.i("sValorPago",""+sValorPago);
                Log.i("sFecha",""+sFecha);
                Log.i("sTipoPago",""+sTipoPago);
                Log.i("sTipoBanco",""+sTipoBanco);
                Log.i("sTipoCuenta",""+sTipoCuenta);

                int id = db.agregarPago(codEmp,
                        nifEmp,
                        sTipoPago,
                        sNumeroCuenta,
                        sTipoBanco,
                        sTipoCuenta,
                        sFecha,
                        sValorPago);
                Log.i("Insert id", "" + id);
                if (id>0){
                    Intent intent = new Intent(PagoActivity.this, GenerarRecaudoActivity.class);
                    intent.putExtra("codEje",codEje);
                    intent.putExtra("nombreEje",nombreEje);
                    intent.putExtra("nifEmp", nifEmp);
                    intent.putExtra("codEmp", codEmp);
                    intent.putExtra("nombreEmp", nombreEmp);
                    intent.putExtra("direccionEmp", direccionEmp);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(PagoActivity.this,
                            "Ha ocurrido un error al ingresar el recaudo: "+id,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Populate the Spinner.
     */
    private void loadSpinnerBancos() {
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spnTipoBanco, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spTipoBanco.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.spTipoBanco.setOnItemSelectedListener(PagoActivity.this);
        this.spTipoCuenta.setOnItemSelectedListener(PagoActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        String itm = parent.getItemAtPosition(pos).toString();
        nomPago = itm;
        switch (parent.getId()) {
            case R.id.spnTipoBanco:
                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.array_bancos);
                CharSequence[] localidades = arrayLocalidades.getTextArray(pos);
                arrayLocalidades.recycle();
                // Create an ArrayAdapter using the string array and a default
                // spinner layout
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, localidades);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                this.spTipoCuenta.setAdapter(adapter);
                break;
            case R.id.spnCodigoCuenta:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }
    /**
     * Calendar picker.
     */
    @Override
    public void onClick(View v) {

    }
}
