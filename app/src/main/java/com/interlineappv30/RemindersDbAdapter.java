package com.interlineappv30;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by INTEL on 12/08/2017.
 */

public class RemindersDbAdapter {

    /*datos de la db*/
    private static final String DATABASE_NAME = "interlinedb";
    private static final String DATABASE_TABLE = "detallefinal";
    private static final int DATABASE_VERSION = 1;

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
    public static final String KEY_OBSERVACIONCLI = "ObservacionCli";
    public static final String KEY_NOMCLI = "NomCli";
    public static final String KEY_TELCLI = "TelCli";
    public static final String KEY_EMAILCLI = "EmailCli";
    public static final String KEY_CODPROD = "codProd";
    public static final String KEY_NOMPROD = "nomProd";
    public static final String KEY_LOTEPROD = "loteProd";
    public static final String KEY_FECHAEXPPROD = "fechaExpProd";
    public static final String KEY_CANTIDADDEV = "cantdadDev";
    public static final String KEY_OBSERVACIONDEV = "observacionDev";
    public static final String KEY_FECHASYS = "fecha_sys";
    public static final String KEY_ESTADO = "estado";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create_table" + DATABASE_TABLE +"("
            + KEY_ID + "integer primary key autoincrement,"
            + KEY_CODEJE + "text not null,"
            + KEY_NOMBREEJE + "text not null,"
            + KEY_CODEMPRESA + "text not null,"
            + KEY_NIFEMP + "text not null,"
            + KEY_NOMBREEMPRESA + "text not null,"
            + KEY_DIRECCIONEMP + "text not null,"
            + KEY_ID_CAUSAL + "text not null,"
            + KEY_NOMCAUSAL + "text not null,"
            + KEY_OBSERVACIONCLI + "text not null,"
            + KEY_NOMCLI + "text not null,"
            + KEY_TELCLI + "text not null,"
            + KEY_EMAILCLI + "text not null,"
            + KEY_CODPROD + "text not null,"
            + KEY_NOMPROD + "text not null,"
            + KEY_LOTEPROD + "text not null,"
            + KEY_FECHAEXPPROD + "text not null,"
            + KEY_CANTIDADDEV + "text not null,"
            + KEY_OBSERVACIONDEV + "text not null,"
            + KEY_FECHASYS + "text not null,"
            + KEY_ESTADO + " text not null);";

    private final Context mCtx;

    public RemindersDbAdapter(Context ctx) {
        this.mCtx  = ctx;
    }

    public RemindersDbAdapter open() throws android.database.SQLException{
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDbHelper.close();
    }

    public long createReminder(String dbcodEje, String dbnombreEje, String dbcodEmpresa, String dbnifEmp, String dbnombreEmpresa,
                               String dbdireccionEmp, String dbidCausal, String dbnomCausal, String dbObservacionCli,
                               String dbNomCli, String dbTelCli, String dbEmailCli, String dbcodProd, String dbnomProd,
                               String dbloteProd, String dbfechaExpProd, String dbcantdadDev, String dbobservacionDev,
                               String dbfecha_sys, String dbestado){
        ContentValues cv = new ContentValues();
            try{
                cv.put(KEY_CODEJE,dbcodEje);
                cv.put(KEY_NOMBREEJE,dbnombreEje);
                cv.put(KEY_CODEMPRESA,dbcodEmpresa);
                cv.put(KEY_NIFEMP,dbnifEmp);
                cv.put(KEY_NOMBREEMPRESA,dbnombreEmpresa);
                cv.put(KEY_DIRECCIONEMP,dbdireccionEmp);
                cv.put(KEY_ID_CAUSAL,dbidCausal);
                cv.put(KEY_NOMCAUSAL,dbnomCausal);
                cv.put(KEY_OBSERVACIONCLI,dbObservacionCli);
                cv.put(KEY_NOMCLI,dbNomCli);
                cv.put(KEY_TELCLI,dbTelCli);
                cv.put(KEY_EMAILCLI,dbEmailCli);
                cv.put(KEY_CODPROD,dbcodProd);
                cv.put(KEY_NOMPROD,dbnomProd);
                cv.put(KEY_LOTEPROD,dbloteProd);
                cv.put(KEY_FECHAEXPPROD,dbfechaExpProd);
                cv.put(KEY_CANTIDADDEV,dbcantdadDev);
                cv.put(KEY_OBSERVACIONDEV,dbobservacionDev);
                cv.put(KEY_FECHASYS,dbfecha_sys);
                cv.put(KEY_ESTADO,dbestado);

                //return mDb.insert(DATABASE_TABLE, null, cv);

            }catch (SQLException e){
                Log.i("SQLException",""+e);
            }
        long respuesta = mDb.insert(DATABASE_TABLE,null,cv);
        Log.i("mDb",""+mDb);
        return respuesta;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
