package com.interlineappv30;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;

public class DbAdapter{
    long newRowId;
    /*DECLARO VARIABLES DEVOLUCIÓN*/
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
    public static final String KEY_DOCUMENTOCLI = "DocumentoCli";
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
    /*DECLARO VARIABLES RECAUDO*/
    public static final String KEY_ID_DB_SERVER = "id_server";
    public static final String KEY_DOCUMENTO_R = "documento_r";
    public static final String KEY_RFACTURA_R = "rfactura_r";
    public static final String KEY_FD_R = "fd_R";
    public static final String KEY_FV_R = "fv_R";
    public static final String KEY_SALDO_R = "saldo_r";
    public static final String KEY_TIPO_PAGO = "tipo_pago";
    public static final String KEY_NUMERO_CUENTA = "numero_cuenta";
    public static final String KEY_TIPO_BANCO = "tipo_banco";
    public static final String KEY_CODIGO_CUENTA = "codigo_cuenta";
    public static final String KEY_FECHA_PAGO = "fecha_pago";
    public static final String KEY_VALOR_PAGO = "valor_pago";
    public static final String KEY_TIPO_DESCUENTO = "tipo_descuento";
    public static final String KEY_VALOR_DESCUENTO = "valor_descuento";
    public static final String KEY_OBSERVACIONES_R = "observaciones";
    public static final String KEY_CONSECUTIVO_R = "consecutivo";

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /*DATOS DE LA TABLA*/
    private static final String DATABASE_NAME = "dbinterline001";
    private static final String SQLITE_TABLE = "detalle_producto_final";
    private static final String TABLE_RECAUDO_GENERAL = "recaudo_general";
    private static final String TABLE_PAGOS = "pagos";
    private static final String TABLE_DESCUENTOS = "descuentos";
    private static final int DATABASE_VERSION = 5;

    private final Context mCtx;
    /*TABLA RECAUDOS*/
    private static final String CREAR_TABLA_RECAUDO = "create table " + TABLE_RECAUDO_GENERAL + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CODEMPRESA + " text not null, "
            + KEY_NIFEMP + " text not null, "
            + KEY_ID_DB_SERVER + " text not null, "
            + KEY_DOCUMENTO_R + " text not null, "
            + KEY_RFACTURA_R + " text not null, "
            + KEY_FD_R + " text not null, "
            + KEY_FV_R + " text not null, "
            + KEY_SALDO_R + " text not null,"
            + KEY_CONSECUTIVO_R + " text not null,"
            + KEY_FECHASYS + " text not null,"
            + KEY_ESTADO + " text not null); ";

    /*TABLA PAGOS*/
    private static final String CREAR_TABLA_PAGOS = "create table " + TABLE_PAGOS + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CODEMPRESA + " text not null, "
            + KEY_NIFEMP + " text not null, "
            + KEY_TIPO_PAGO + " text not null, "
            + KEY_NUMERO_CUENTA + " text not null, "
            + KEY_TIPO_BANCO + " text not null, "
            + KEY_CODIGO_CUENTA + " text not null, "
            + KEY_FECHA_PAGO + " text not null, "
            + KEY_VALOR_PAGO + " text not null, "
            + KEY_CONSECUTIVO_R + " text not null,"
            + KEY_FECHASYS + " text not null, "
            + KEY_ESTADO + " text not null); ";

    /*TABLA DESCUENTOS*/
    private static final String CREAR_TABLA_DESCUENTOS = "create table " + TABLE_DESCUENTOS + "("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CODEMPRESA + " text not null, "
            + KEY_NIFEMP + " text not null, "
            + KEY_TIPO_DESCUENTO + " text not null,"
            + KEY_VALOR_DESCUENTO + " text not null,"
            + KEY_OBSERVACIONES_R + " text not null,"
            + KEY_CONSECUTIVO_R + " text not null,"
            + KEY_FECHASYS + " text not null,"
            + KEY_ESTADO + " text not null); ";


    /*BASE DE DATOS DEVOLUCIONES*/
    private static final String DATABASE_CREATE = "create table " + SQLITE_TABLE +" ("
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
            + KEY_DOCUMENTOCLI + " text not null, "
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


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase mDb) {
            try{
                mDb.execSQL(DATABASE_CREATE);
                mDb.execSQL(CREAR_TABLA_RECAUDO);
                mDb.execSQL(CREAR_TABLA_PAGOS);
                mDb.execSQL(CREAR_TABLA_DESCUENTOS);
                Log.w(TAG, DATABASE_CREATE);
                Log.w(TAG, CREAR_TABLA_RECAUDO);
                Log.w(TAG, CREAR_TABLA_PAGOS);
                Log.w(TAG, CREAR_TABLA_DESCUENTOS);
            }catch (SQLException e){
                Log.i("SQLException",""+ e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase mDb, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            mDb.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_RECAUDO_GENERAL);
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGOS);
            mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCUENTOS);
            onCreate(mDb);
        }
    }

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public int agregarPrecito(String trama, String id){
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRECINTO, trama);
        try{
            newRowId  = mDb.update(SQLITE_TABLE, cv, "codEmpresa= ?",  new String[]{id});
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }
    /*AGREGA CONSECUTIVO DE BOLETO DEVOLUCIÓN*/
    public int agregarBoleto(String boleto, String id){
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NUMBOLETO, boleto);
        try{
            newRowId  = mDb.update(SQLITE_TABLE, cv, "codEmpresa= ?",  new String[]{id});
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }
    /*AGREGAR CONSECUTIVO A DOCUMENTOS*/
    public int agregarConsecutivoDocumentos(String consecutivo, String id){
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CONSECUTIVO_R, consecutivo);
        try{
            newRowId  = mDb.update(TABLE_RECAUDO_GENERAL, cv, "codEmpresa= ?",  new String[]{id});
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }
    /*AGREGAR CONSECUTIVO A PAGOS*/
    public int agregarConsecutivoPagos(String consecutivo, String id){
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CONSECUTIVO_R, consecutivo);
        try{
            newRowId  = mDb.update(TABLE_PAGOS, cv, "codEmpresa= ?",  new String[]{id});
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }
    /*AGREGAR CONSECUTIVO A DESCUENTOS*/
    public int agregarConsecutivoDescuentos(String consecutivo, String id){
        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CONSECUTIVO_R, consecutivo);
        try{
            newRowId  = mDb.update(TABLE_DESCUENTOS, cv, "codEmpresa= ?",  new String[]{id});
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }

    public int agregarRegistro(String dbcodEje, String dbnombreEje, String dbcodEmpresa, String dbnifEmp, String dbnombreEmpresa,
                               String dbdireccionEmp, String dbidCausal, String dbnomCausal, String dbObservacionCli, String dbDocumentoCli,
                               String dbNomCli, String dbTelCli, String dbEmailCli, String dbcodProd, String dbnomProd,
                               String dbloteProd, String dbfechaExpProd, String dbcantdadDev, String dbobservacionDev,
                               String dbfecha_sys, String dbestado) {

            open();
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
            cv.put(KEY_DOCUMENTOCLI,dbDocumentoCli);
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

        try{
            newRowId  = mDb.insert(SQLITE_TABLE, null, cv);
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }

    public int agregarRecaudo(String iddb,
                              String codEmp,
                              String nifEmp,
                              String documento,
                              String referencia_factura,
                              String fecha_documento,
                              String fecha_vc_mto,
                              String saldo) {
        try{
        String valorCero = "0";

        open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID_DB_SERVER,iddb);
        cv.put(KEY_CODEMPRESA, codEmp);
        cv.put(KEY_NIFEMP, nifEmp);
        cv.put(KEY_DOCUMENTO_R,documento);
        cv.put(KEY_RFACTURA_R,referencia_factura);
        cv.put(KEY_FD_R,fecha_documento);
        cv.put(KEY_FV_R,fecha_vc_mto);
        cv.put(KEY_SALDO_R,saldo);
        cv.put(KEY_CONSECUTIVO_R,valorCero);
        cv.put(KEY_FECHASYS, valorCero);
        cv.put(KEY_ESTADO,"1");

            newRowId  = mDb.insert(TABLE_RECAUDO_GENERAL, null, cv);
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }

    /*PAGOS*/
    public int agregarPago(String codEmp,
                           String nifEmp,
                              String tipoPago,
                              String numeroCuenta,
                              String tipoBanco,
                              String codigoCuenta,
                              String fechaPago,
                              String valorPago) {
        try{
            String valorCero = "0";

            open();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CODEMPRESA, codEmp);
            cv.put(KEY_NIFEMP, nifEmp);
            cv.put(KEY_TIPO_PAGO,tipoPago);
            cv.put(KEY_NUMERO_CUENTA,numeroCuenta);
            cv.put(KEY_TIPO_BANCO,tipoBanco);
            cv.put(KEY_CODIGO_CUENTA,codigoCuenta);
            cv.put(KEY_FECHA_PAGO,fechaPago);
            cv.put(KEY_VALOR_PAGO,valorPago);
            cv.put(KEY_CONSECUTIVO_R,valorCero);
            cv.put(KEY_FECHASYS, valorCero);
            cv.put(KEY_ESTADO,"1");

            newRowId  = mDb.insert(TABLE_PAGOS, null, cv);
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }

    public String countPagos(String empresa) {
        Cursor cursor;
        String cantidad = "";
        String[] args = new String[] {empresa};
        String query = "select SUM("+KEY_VALOR_PAGO+") as saldo from "+TABLE_PAGOS+
                " where "+KEY_CODEMPRESA+"='"+empresa+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no existan más registros
            do {
                cantidad = cursor.getString(0);
                Log.i("count",""+cantidad);
            } while(cursor.moveToNext());
        }
        return cantidad;
    }

    /*DESCUENTOS*/
    public int agregarDescuento(String codEmp, String nifEmp,
                           String tipoDescuento,
                           String valorDescuento,
                           String observacionesDesc) {
        try{
            String valorCero = "0";

            open();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CODEMPRESA, codEmp);
            cv.put(KEY_NIFEMP, nifEmp);
            cv.put(KEY_TIPO_DESCUENTO,tipoDescuento);
            cv.put(KEY_VALOR_DESCUENTO,valorDescuento);
            cv.put(KEY_OBSERVACIONES_R,observacionesDesc);
            cv.put(KEY_CONSECUTIVO_R,valorCero);
            cv.put(KEY_FECHASYS, valorCero);
            cv.put(KEY_ESTADO,"1");

            newRowId  = mDb.insert(TABLE_DESCUENTOS, null, cv);
            Log.i("newRowId",""+newRowId);
            mDb.close();
        }catch (SQLException e){
            Log.i("SQLException",""+e);
        }
        return (int) newRowId;
    }

    public String countDescuentos(String empresa) {
        Cursor cursor;
        String cantidad = "";
        String[] args = new String[] {empresa};
        String query = "select SUM("+KEY_VALOR_DESCUENTO+") as saldo from "+TABLE_DESCUENTOS+
                " where "+KEY_CODEMPRESA+"='"+empresa+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no existan más registros
            do {
                cantidad = cursor.getString(0);
                Log.i("count",""+cantidad);
            } while(cursor.moveToNext());
        }
        return cantidad;
    }



    public boolean eliminarRegistro(String id) {
        int doneDelete = 0;
        try {
            doneDelete = mDb.delete(SQLITE_TABLE, " _id = ?", new String[]{String.valueOf(id)});
            Log.w(TAG, Integer.toString(doneDelete));
            return doneDelete > 0;
        }catch (Exception e){
            Log.i("Exception",""+e);
            return false;
        }
    }

    public String countLoteProducto(String lote, String producto) {
        Cursor cursor;
        String cantidad = "";
        String[] args = new String[] {lote,producto};
        String query = "select count(*) as cantidad from "+SQLITE_TABLE+
                " where "+KEY_LOTEPROD+"='"+lote+
                "' and "+KEY_CODPROD+"='"+producto+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                cantidad = cursor.getString(0);
                Log.i("count",""+cantidad);
            } while(cursor.moveToNext());
        }
        return cantidad;
    }

    public String countDocumentos(String empresa) {
        Cursor cursor;
        String cantidad = "";
        String[] args = new String[] {empresa};
        String query = "select SUM("+KEY_SALDO_R+") as saldo from "+TABLE_RECAUDO_GENERAL+
                " where "+KEY_CODEMPRESA+"='"+empresa+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no existan más registros
            do {
                cantidad = cursor.getString(0);
                Log.i("count",""+cantidad);
            } while(cursor.moveToNext());
        }
        return cantidad;
    }


    public String getEmail(String codeje, String codemp) {
        Cursor cursor;
        String email = "";
        String[] args = new String[] {codeje,codemp};
        String query = "select DISTINCT "+KEY_EMAILCLI+" as email from "+SQLITE_TABLE+
                " where "+KEY_CODEJE+"='"+codeje+
                "' and "+KEY_CODEMPRESA+"='"+codemp+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                email = cursor.getString(0);
                Log.i("count",""+email);
            } while(cursor.moveToNext());
        }
        return email;
    }

    public Cursor consultarDocumentosByEmpresa(String codEmpresa) throws SQLException {
        Log.w(TAG, codEmpresa);
            Cursor mCursor = mDb.query(TABLE_RECAUDO_GENERAL, new String[] {
                            KEY_ID,
                            KEY_DOCUMENTO_R,
                            KEY_RFACTURA_R,
                            KEY_FD_R,
                            KEY_FV_R,
                            KEY_SALDO_R
                    },
                    KEY_CODEMPRESA + " = '" + codEmpresa + "';", null,
                    null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
    }

    public Cursor consultarRegistrosByEmpresa(String codEje, String codEmpresa) throws SQLException {
        Log.w(TAG, codEmpresa);

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID,
                        KEY_CODEJE, KEY_NOMBREEJE, KEY_CODEMPRESA, KEY_NIFEMP, KEY_NOMBREEMPRESA, KEY_DIRECCIONEMP,
                        KEY_ID_CAUSAL, KEY_NOMCAUSAL, KEY_OBCLIENTE, KEY_DOCUMENTOCLI, KEY_DOCUMENTOCLI,KEY_NOMCLI, KEY_TELCLI,
                        KEY_EMAILCLI, KEY_CODPROD, KEY_NOMPROD, KEY_LOTEPROD, KEY_FECHAEXPPROD, KEY_CANTIDADDEV,
                        KEY_OBSERVACIONDEV, KEY_FECHASYS, KEY_NUMBOLETO,KEY_PRECINTO,KEY_ESTADO},
                        KEY_CODEMPRESA + " = '" + codEmpresa + "';", null,
                        null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String countCantidadLotes(String codEmpresa) {
        Cursor cursor;
        String cantidad = "";
        String[] args = new String[] {codEmpresa};
        String query = "select count(*) as cantidad from "+SQLITE_TABLE+
                " where "+KEY_CODEMPRESA+"='"+codEmpresa+"'";
        Log.i("query",""+query);
        open();
        cursor = mDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                cantidad = cursor.getString(0);
                Log.i("count",""+cantidad);
            } while(cursor.moveToNext());
        }
        return cantidad;
    }


    public Cursor getAllContacts(String codEmpresa) throws JSONException {
        Cursor cursor = null;
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_OBCLIENTE + ", " +
                KEY_DOCUMENTOCLI + ", " +
                KEY_NOMCLI + ", " +
                KEY_TELCLI + ", " +
                KEY_EMAILCLI + ", " +
                KEY_CANTIDADDEV + ", " +
                KEY_OBSERVACIONDEV + ", " +
                KEY_NUMBOLETO + ", " +
                KEY_PRECINTO + ", " +
                KEY_ESTADO + ", " +
                KEY_CODEJE + ", " +
                KEY_ID_CAUSAL + ", " +
                KEY_LOTEPROD + ", " +
                KEY_CODPROD + ", " +
                KEY_CODEMPRESA + ", " +
                KEY_NIFEMP + ", " +
                KEY_ID + ", " +
                KEY_NOMPROD + ", " +
                KEY_FECHAEXPPROD + " " +
                "FROM " + SQLITE_TABLE + " WHERE " + KEY_CODEMPRESA + "='" + codEmpresa + "';";
                Log.i("selectQuery", "" + selectQuery);
        try {
            open();
            /*Se ejecuta la consulta */
            cursor = mDb.rawQuery(selectQuery, null);
            Log.i("cursor", "" + cursor);
            /*Recorrido de la consulta*/
        }catch (SQLException e){
            Log.i("SQLException", "" + e);
        }
        return cursor;
    }

    //TOMA LOS DOCUMENTOS DE LA DB DEL DISPOSITIVO
    public Cursor getDocumentsData(String codEmpresa) throws JSONException {
        Cursor cursor = null;
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_CODEMPRESA + ", " +
                KEY_NIFEMP + ", " +
                KEY_ID_DB_SERVER + ", " +
                KEY_DOCUMENTO_R + ", " +
                KEY_RFACTURA_R + ", " +
                KEY_FD_R + ", " +
                KEY_FV_R + ", " +
                KEY_SALDO_R + ", " +
                KEY_CONSECUTIVO_R + ", " +
                KEY_ID + " " +
                "FROM " + TABLE_RECAUDO_GENERAL + " WHERE " + KEY_CODEMPRESA + "='" + codEmpresa + "';";
        Log.i("selectQuery", "" + selectQuery);
        try {
            open();
            /*Se ejecuta la consulta */
            cursor = mDb.rawQuery(selectQuery, null);
            Log.i("cursor", "" + cursor);
            /*Recorrido de la consulta*/
        }catch (SQLException e){
            Log.i("SQLException", "" + e);
        }
        return cursor;
    }
//    ELIMINA LOS DOCUMENTOS ENVIADOS A LA DB LOCAL
    public boolean eliminarRegistroDocumentos(String id) {
        int doneDelete = 0;
        try {
            doneDelete = mDb.delete(TABLE_RECAUDO_GENERAL, " _id = ?", new String[]{String.valueOf(id)});
            Log.w(TAG, Integer.toString(doneDelete));
            return doneDelete > 0;
        }catch (Exception e){
            Log.i("Exception",""+e);
            return false;
        }
    }


    //TOMA LOS PAGOS DE LA DB DEL DISPOSITIVO
    public Cursor getPagosData(String codEmpresa) throws JSONException {
        Cursor cursor = null;
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_CODEMPRESA + ", " +
                KEY_NIFEMP + ", " +
                KEY_TIPO_PAGO + ", " +
                KEY_NUMERO_CUENTA + ", " +
                KEY_TIPO_BANCO + ", " +
                KEY_CODIGO_CUENTA + ", " +
                KEY_FECHA_PAGO + ", " +
                KEY_VALOR_PAGO + ", " +
                KEY_CONSECUTIVO_R + ", " +
                KEY_FECHASYS + ", " +
                KEY_ESTADO + ", " +
                KEY_ID + " " +
                "FROM " + TABLE_PAGOS + " WHERE " + KEY_CODEMPRESA + "='" + codEmpresa + "';";
        Log.i("selectQuery", "" + selectQuery);
        try {
            open();
            /*Se ejecuta la consulta */
            cursor = mDb.rawQuery(selectQuery, null);
            Log.i("cursor", "" + cursor);
            /*Recorrido de la consulta*/
        }catch (SQLException e){
            Log.i("SQLException", "" + e);
        }
        return cursor;
    }
    //    ELIMINA LOS PAGOS ENVIADOS A LA DB LOCAL
    public boolean eliminarRegistroPagos(String id) {
        int doneDelete = 0;
        try {
            doneDelete = mDb.delete(TABLE_PAGOS, " _id = ?", new String[]{String.valueOf(id)});
            Log.w(TAG, Integer.toString(doneDelete));
            return doneDelete > 0;
        }catch (Exception e){
            Log.i("Exception",""+e);
            return false;
        }
    }



    //TOMA LOS DESCUENTOS DE LA DB DEL DISPOSITIVO
    public Cursor getDescuentosData(String codEmpresa) throws JSONException {
        Cursor cursor = null;
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_CODEMPRESA + ", " +
                KEY_NIFEMP + ", " +
                KEY_TIPO_DESCUENTO + ", " +
                KEY_VALOR_DESCUENTO + ", " +
                KEY_OBSERVACIONES_R + ", " +
                KEY_CONSECUTIVO_R + ", " +
                KEY_FECHASYS + ", " +
                KEY_ESTADO + ", " +
                KEY_ID + " " +
                "FROM " + TABLE_DESCUENTOS + " WHERE " + KEY_CODEMPRESA + "='" + codEmpresa + "';";
        Log.i("selectQuery", "" + selectQuery);
        try {
            open();
            /*Se ejecuta la consulta */
            cursor = mDb.rawQuery(selectQuery, null);
            Log.i("cursor", "" + cursor);
            /*Recorrido de la consulta*/
        }catch (SQLException e){
            Log.i("SQLException", "" + e);
        }
        return cursor;
    }
    //    ELIMINA LOS PAGOS ENVIADOS A LA DB LOCAL
    public boolean eliminarRegistroDescuentos(String id) {
        int doneDelete = 0;
        try {
            doneDelete = mDb.delete(TABLE_DESCUENTOS, " _id = ?", new String[]{String.valueOf(id)});
            Log.w(TAG, Integer.toString(doneDelete));
            return doneDelete > 0;
        }catch (Exception e){
            Log.i("Exception",""+e);
            return false;
        }
    }


    public int deleteById(String id){
        int doneDelete = 0;
        open();
        doneDelete = mDb.delete(SQLITE_TABLE, KEY_ID+"=?" , new String[]{id});
        mDb.close();
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete;
    }

    public int eliminarDocumento(String id){
        int doneDelete = 0;
        open();
        doneDelete = mDb.delete(TABLE_RECAUDO_GENERAL, KEY_ID+"=?" , new String[]{id});
        mDb.close();
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete;
    }

    public Cursor consultaAll() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ID,
                        KEY_CODEJE, KEY_NOMBREEJE, KEY_CODEMPRESA, KEY_NIFEMP, KEY_NOMBREEMPRESA, KEY_DIRECCIONEMP,
                        KEY_ID_CAUSAL, KEY_NOMCAUSAL, KEY_OBCLIENTE, KEY_DOCUMENTOCLI, KEY_NOMCLI, KEY_TELCLI, KEY_EMAILCLI, KEY_CODPROD,
                        KEY_NOMPROD, KEY_LOTEPROD, KEY_FECHAEXPPROD, KEY_CANTIDADDEV, KEY_OBSERVACIONDEV, KEY_FECHASYS,
                        KEY_ESTADO},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


}