package com.interlineappv30;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by INTEL on 12/08/2017.
 */

public class BaseDatosClass extends SQLiteOpenHelper {
    private static final String TAG = "MSN";
    private Context contexto;
    private BaseDatosClass dbHelper;
    private SQLiteDatabase db;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbinterline";
    private static final String DATABASE_TABLE = "detalle_producto_final";
    /*datos de los campos*/
    public static final String KEY_ID = "_id";
    public static final String KEY_CODEJE = "codEje";
    public static final String KEY_NOMBREEJE = "nombreEje";
    public static final String KEY_CODEMPRESA = "codEmpresa";
    public static final String KEY_NIFEMP = "nifEmp";
    public static final String KEY_NOMBREEMPRESA = "nombreEmpresa";
    public static final String KEY_DIRECCIONEMP = "direccionEmp";
    public static final String KEY_ID_CAUSAL = "idCausal";
    public static final String KEY_NOMCAUSAL = "nomCausal";
    public static final String KEY_OBCLIENTE = "observacionCli";
    public static final String KEY_NOMCLI = "nomCli";
    public static final String KEY_TELCLI = "telCli";
    public static final String KEY_EMAILCLI = "emailCli";
    public static final String KEY_CODPROD = "codProd";
    public static final String KEY_NOMPROD = "nomProd";
    public static final String KEY_LOTEPROD = "loteProd";
    public static final String KEY_FECHAEXPPROD = "fechaExpProd";
    public static final String KEY_CANTIDADDEV = "cantdadDev";
    public static final String KEY_OBSERVACIONDEV = "observacionDev";
    public static final String KEY_NUMBOLETO = "numeroBoleto";
    public static final String KEY_PRECINTO = "numeroPrecinto";
    public static final String KEY_FECHASYS = "fecha_sys";
    public static final String KEY_ESTADO = "estado";


    private static final String SQL_CREAR = "create table " + DATABASE_TABLE +" ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CODEJE + " text not null, "
            + KEY_NOMBREEJE + " text not null, "
            + KEY_CODEMPRESA + " text not null, "
            + KEY_NIFEMP + " text not null, "
            + KEY_NOMBREEMPRESA + " text not null, "
            + KEY_DIRECCIONEMP + " text not null, "
            + KEY_ID_CAUSAL + " text not null, "
            + KEY_NOMCAUSAL + " text not null, "
            + KEY_OBCLIENTE + " text not null, "
            + KEY_NOMCLI + " text not null, "
            + KEY_TELCLI + " text not null, "
            + KEY_EMAILCLI + " text not null, "
            + KEY_CODPROD + " text not null, "
            + KEY_NOMPROD + " text not null, "
            + KEY_LOTEPROD + " text not null, "
            + KEY_FECHAEXPPROD + " text not null, "
            + KEY_CANTIDADDEV + " text not null, "
            + KEY_OBSERVACIONDEV + " text not null, "
            + KEY_NUMBOLETO + " text not null, "
            + KEY_PRECINTO + " text not null, "
            + KEY_FECHASYS + " text not null, "
            + KEY_ESTADO + " text not null); ";

    public BaseDatosClass(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int agregar(String dbcodEje, String dbnombreEje, String dbcodEmpresa, String dbnifEmp, String dbnombreEmpresa,
                        String dbdireccionEmp, String dbidCausal, String dbnomCausal, String dbObservacionCli,
                        String dbNomCli, String dbTelCli, String dbEmailCli, String dbcodProd, String dbnomProd,
                        String dbloteProd, String dbfechaExpProd, String dbcantdadDev, String dbobservacionDev,
                        String dbfecha_sys, String dbestado){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CODEJE,dbcodEje);
        cv.put(KEY_NOMBREEJE,dbnombreEje);
        cv.put(KEY_CODEMPRESA,dbcodEmpresa);
        cv.put(KEY_NIFEMP,dbnifEmp);
        cv.put(KEY_NOMBREEMPRESA,dbnombreEmpresa);
        cv.put(KEY_DIRECCIONEMP,dbdireccionEmp);
        cv.put(KEY_ID_CAUSAL,dbidCausal);
        cv.put(KEY_NOMCAUSAL,dbnomCausal);
        cv.put(KEY_OBCLIENTE,dbObservacionCli);
        cv.put(KEY_NOMCLI,dbNomCli);
        cv.put(KEY_TELCLI,dbTelCli);
        cv.put(KEY_EMAILCLI,dbEmailCli);
        cv.put(KEY_CODPROD,dbcodProd);
        cv.put(KEY_NOMPROD,dbnomProd);
        cv.put(KEY_LOTEPROD,dbloteProd);
        cv.put(KEY_FECHAEXPPROD,dbfechaExpProd);
        cv.put(KEY_CANTIDADDEV,dbcantdadDev);
        cv.put(KEY_OBSERVACIONDEV,dbobservacionDev);
        cv.put(KEY_NUMBOLETO,"0");
        cv.put(KEY_PRECINTO,"0");
        cv.put(KEY_FECHASYS,dbfecha_sys);
        cv.put(KEY_ESTADO,dbestado);

        long newRowId;
        newRowId  = db.insert(DATABASE_TABLE, null,cv);
        Log.i("newRowId",""+newRowId);
        db.close();
        return (int) newRowId;
    }


    public void abrir(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {KEY_ID, KEY_CODEJE};

        Cursor cursor =
                db.query(DATABASE_TABLE,
                        projection,
                        " _id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();

        System.out.println("El nombre es " +  cursor.getString(1) );

        db.close();

    }

    public boolean eliminar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(DATABASE_TABLE,
                    " _id = ?",
                    new String[] { String.valueOf (id ) });
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }


    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{KEY_ID_CAUSAL, KEY_NOMCAUSAL, KEY_CODPROD, KEY_NOMPROD,
            KEY_LOTEPROD, KEY_CANTIDADDEV } ;

    public BaseDatosClass abrir() throws SQLException{
        dbHelper = new BaseDatosClass(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar(){
        dbHelper.close();
    }

    /**
     * Devuelve cursor con todos las columnas de la tabla
     */
    public Cursor getCursor() throws SQLException
    {
        Cursor c = db.query( true, DATABASE_TABLE, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor fetchAllCountries() {

        Cursor mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ID_CAUSAL,
                        KEY_NOMCAUSAL, KEY_CODPROD, KEY_NOMPROD, KEY_LOTEPROD,KEY_CANTIDADDEV   },
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = db.query(DATABASE_TABLE, new String[]{KEY_ID_CAUSAL,
                            KEY_NOMCAUSAL, KEY_CODPROD, KEY_NOMPROD, KEY_LOTEPROD, KEY_CANTIDADDEV},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ID_CAUSAL,
                            KEY_NOMCAUSAL, KEY_CODPROD, KEY_NOMPROD, KEY_LOTEPROD, KEY_CANTIDADDEV},
                    KEY_CODEMPRESA + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor consultaDevolucionByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(DATABASE_TABLE, new String[] {KEY_ID,
                            KEY_CODEJE, KEY_NOMBREEJE, KEY_CODEMPRESA, KEY_NIFEMP, KEY_NOMBREEMPRESA, KEY_DIRECCIONEMP,
                            KEY_ID_CAUSAL, KEY_NOMCAUSAL, KEY_OBCLIENTE, KEY_NOMCLI, KEY_TELCLI, KEY_EMAILCLI, KEY_CODPROD,
                            KEY_NOMPROD, KEY_LOTEPROD, KEY_FECHAEXPPROD, KEY_CANTIDADDEV, KEY_OBSERVACIONDEV, KEY_FECHASYS,
                            KEY_ESTADO},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                            KEY_CODEJE, KEY_NOMBREEJE, KEY_CODEMPRESA, KEY_NIFEMP, KEY_NOMBREEMPRESA, KEY_DIRECCIONEMP,
                            KEY_ID_CAUSAL, KEY_NOMCAUSAL, KEY_OBCLIENTE, KEY_NOMCLI, KEY_TELCLI, KEY_EMAILCLI, KEY_CODPROD,
                            KEY_NOMPROD, KEY_LOTEPROD, KEY_FECHAEXPPROD, KEY_CANTIDADDEV, KEY_OBSERVACIONDEV, KEY_FECHASYS,
                            KEY_ESTADO},
                    KEY_CODEMPRESA + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

}
