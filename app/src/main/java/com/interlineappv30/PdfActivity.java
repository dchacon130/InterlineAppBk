package com.interlineappv30;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Casa on 17/06/2017.
 */

public class PdfActivity extends PrecintoActivity {
    private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.interline.pdf";
    private static final String INVOICES = "Devoluciones";
    private static Font catFont;
    private static Font subFont ;
    private static Font smallBold ;
    private static Font smallFont ;
    private static Font italicFont ;
    private static Font italicFontBold ;
    //Declaramos nuestra fuente base que se encuentra en la carpeta "assets/fonts" folder
    //Usaremos arialuni.ttf que permite imprimir en nuestro PDF caracteres Unicode Cirílicos (Ruso, etc)
    private static BaseFont unicode;

    //!!!Importante: La carpeta "assets/fonts/arialuni.ttf" debe estar creada en nuestro projecto en
    //la subcarpeta "PdfCreator/build/exploded-bundles/ComAndroidSupportAppcompactV71900.aar"
    //En el caso de que Android Studio la eliminara la copiamos manualmente
    //PdfCreator/build/exploded-bundles/ComAndroidSupportAppcompactV71900.aarassets/fonts/arialuni.ttf
    private static File fontFile = new File("assets/fonts/arialuni.ttf");

    //Constructor set fonts and get context
    public PdfActivity(Context context) throws IOException, DocumentException {
        mContext = context;
        //Creamos los distintos estilos para nuestro tipo de fuente.
        unicode = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        catFont = new Font(unicode, 21,Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 10,Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 10,Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 8,Font.NORMAL, BaseColor.BLACK);
        italicFont = new Font(unicode, 10,Font.ITALIC, BaseColor.BLACK);
        italicFontBold = new Font(unicode, 10,Font.ITALIC|Font.BOLD, BaseColor.BLACK);

    }

    public void createPdfDocument(InvoiceObject invoiceObject) throws IOException, DocumentException{
        try {
            //Creamos las carpetas en nuestro dispositivo, si existen las eliminamos.
            String fullFileName = createDirectoryAndFileName(invoiceObject);

            if(fullFileName.length()>0){
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));

                document.open();

                //Creamos los metadatos del alchivo
                addMetaData(document, invoiceObject);
                //Adicionamos el logo de la empresa
                addImage(document);
                //Creamos información de empresa y cliente
                addTitlePage(document, invoiceObject);
                //Creamos el contenido en form de tabla del documento
                addInvoiceContent(document,invoiceObject.invoiceDetailsList);
                //Creamos el total de la factura del documento
                addInvoiceTotal(document, invoiceObject);

                document.close();

                Toast.makeText(mContext, "PDF file created successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createDirectoryAndFileName(InvoiceObject invoiceObject){
        String FILENAME = "Devolucion_"+invoiceObject.boletoDevolucion+".pdf";
        String fullFileName ="";
        //Obtenemos el directorio raiz "/sdcard"
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);

        //Creamos la carpeta "com.inteline.pdf" y la subcarpeta "Invoice"
        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + INVOICES);

            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }

            fullFileName = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME + File.separator + INVOICES + File.separator + FILENAME;

            File outputFile = new File(fullFileName);

            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fullFileName;
    }

    //PDF library add file metadata function
    private static void addMetaData(Document document, InvoiceObject invoiceObject) {
        document.addTitle("PDF Devolución_"+invoiceObject.boletoDevolucion);
        document.addSubject("Interline iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Interline.com");
        document.addCreator("www.interline.com");
    }

    //Creando el Título y los datos de la Empresa y el Cliente
    private static void addTitlePage(Document document, InvoiceObject invoiceObject)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        // Adicionamos dos línea en blanco
        addEmptyLine(preface, 1);
        // Adicionamos el títulos de la Factura y el número
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        preface.add(new Paragraph(mContext.getResources().getString(R.string.invoice_number) + invoiceObject.boletoDevolucion+
                "                                                       " +
                mContext.getResources().getString(R.string.invoice_date) + stringDate
                , subFont));
        // Adicionamos una línea en blanco
//        addEmptyLine(preface, 1);

        //Adicionamos los datos de la Empresa
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloEmpresa), smallBold));

        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfCodEmp) + " " + invoiceObject.codigoEmpresa
                + " - " +
                mContext.getResources().getString(R.string.pdfNombre) + " " + invoiceObject.nombreEmpresa
                ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfDirEmp) + " " + invoiceObject.direccionEmpresa
                + " - " +
                mContext.getResources().getString(R.string.pdfMunEmpr) + " " + invoiceObject.municipioEmpresa
                + " - " +
                mContext.getResources().getString(R.string.pdfDepEmp) + " " + invoiceObject.departamentoEmpresa
                ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfConEmp) + " " + invoiceObject.contactoEmpresa
                + " - " +
                mContext.getResources().getString(R.string.pdfEmailEmp) + " " + invoiceObject.emailEmpresa
                + " - " +
                mContext.getResources().getString(R.string.pdfTelEmp) + " " + invoiceObject.telefonoEmpresa
                ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfPrecEmp) + " " + invoiceObject.precintos,smallFont));
        /*Linea que separa*/
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfLinea),smallFont));
        //Adicionamos los datos del Cliente
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloCLiente), smallBold));

        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfNomCli) + " " + invoiceObject.nombreCliente
                + "                         " +
                mContext.getResources().getString(R.string.pdfDocCli) + " " + invoiceObject.documentoCliente
                ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfTelCli) + " " + invoiceObject.telefonoCliente
                + "                         " +
                mContext.getResources().getString(R.string.pdfEmaiCli) + " " + invoiceObject.emailCliente
                ,smallFont));


        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfCausal) + " " + invoiceObject.causal ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfObservacion) + " " + invoiceObject.observacion, smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfLinea),smallFont));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(mContext.getResources().getString(R.string.productos), smallBold));
        //Adicionamos el párrafo creado al documento
        document.add(preface);

        // Si queremos crear una nueva página
        //document.newPage();
    }
    //Creamos el contenido de la factura, las líneas con los artículos.
    private static void addInvoiceContent(Document document, java.util.List<InvoiceDetails> invoiceDetail) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable(paragraph, invoiceDetail);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }
    //Creamos el subtotal y el total de la factura.
    private static void addInvoiceTotal(Document document, InvoiceObject invoiceObject) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        document.add(new Phrase(mContext.getResources().getString(R.string.pdfNota),smallFont));
        // Adicionamos la tabla al párrafo
        createTotalInvoiceTable(paragraph, invoiceObject);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }
    //Procedimiento para crear los títulos de las columnas de la factura.
    private static void createInvoiceTable(Paragraph tableSection, java.util.List<InvoiceDetails> invoiceDetails)
            throws DocumentException {

        int TABLE_COLUMNS = 6;
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);// number of table columns

        //Definimos el ancho que corresponde a cada una de las 6 columnas
        float[] columnWidths = new float[]{60f, 200f, 60f, 120f, 60f, 210f};
        table.setWidths(columnWidths);

        //Definimos el ancho de nuestra tabla en %
        table.setWidthPercentage(100);

        // Aquí les dejos otras propiedades que pueden aplicar a la tabla
        //table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);

        //Definimos los títulos para cada una de las 5 columnas
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titCodigo),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titNombreProd),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titLote),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titFecha),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titCantidad),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.titObservaciones),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la sexta columna
        table.addCell(cell);

        //Creamos la fila de la tabla con las cabeceras
        table.setHeaderRows(1);

        //Creamos las lineas con los artículos de la factura;
        for (InvoiceDetails orderLine : invoiceDetails) {
            createInvoiceLine(orderLine, table);
        }

        tableSection.add(table);
    }
    //Procedimiento para crear una lines vacía
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    //Procedimiento para adicionar una imagen al documento PDF
    private static void addImage(Document document) throws IOException, DocumentException {

        Bitmap bitMap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.encabezado);
        bitMap = bitMap.createScaledBitmap(bitMap,300,90,false);  // cambio de resolucion de la imagen mapeada bitmap en pixeles
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        byte[] bitMapData = stream.toByteArray();
        Image image = Image.getInstance(bitMapData);
        //Posicionamos la imagen el el documento
        image.setAlignment(Element.ALIGN_CENTER);
        //image.setAbsolutePosition(400f, 650f);


        document.add(image);
    }
    //Procedimiento para crear las líneas de la factura en forma de tabla.
    private static void createInvoiceLine(InvoiceDetails invoiceLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();


        //Adicionamos celdas sin formato ni estilos, solo el valor
        table.addCell(invoiceLine.itemCode);
        table.addCell(invoiceLine.itemName);
        table.addCell(invoiceLine.itemLote);
        table.addCell(invoiceLine.itemFecha);
        table.addCell(invoiceLine.itemCantidad);
        table.addCell(invoiceLine.itemObservacion);

        /*Adicionamos celdas con formato y estilo: (font, align) para el correspondiente valor
        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.quantity),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos celdas con formato y estilo: (font, align)
        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.price), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos celdas con formato y estilo: (font, align)
        cell.setPhrase(new Phrase(String.valueOf(invoiceLine.total), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);*/

    }

    //Procedimiento para crear los totales y subtotales de la factura en forma de tabla.
    //Misma lógica utilizada para crear los títulos de las columnas de la factura
    private static void createTotalInvoiceTable(Paragraph tableSection, InvoiceObject orderHeaderModel)
            throws DocumentException {

        /*int TABLE_COLUMNS = 2;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{200f, 200f};
        table.setWidths(columnWidths);

        table.setWidthPercentage(100);

        //Adicionamos el título de la celda
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.invoice_subtotal),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        double subTotal = orderHeaderModel.total;
        //Adicionamos el contenido de la celda con el valor subtotal
        cell = new PdfPCell(new Phrase(String.valueOf(subTotal)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(cell);

        //Adicionamos el título de la celda
        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.invoice_tax),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos el contenido de la celda con el valor tax
        cell = new PdfPCell(new Phrase(String.valueOf(0)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos el título de la celda
        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.detail_total),smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //Adicionamos el contenido de la celda con el valor total
        cell = new PdfPCell(new Phrase(String.valueOf(orderHeaderModel.total)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        tableSection.add(table);*/

    }

    //Procedimiento para mostrar el documento PDF generado
    public void showPdfFile(String fileName, Context context){
        Toast.makeText(context, "Leyendo documento", Toast.LENGTH_LONG).show();

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String path = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;

        File file = new File(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
    }

    //Procedimiento para enviar por email el documento PDF generado
    public void sendPdfByEmail(String fileName, String emailTo, String emailCC, Context context, String Asunto, String mensaje){

        String user = "devoluciones@enlaceinternacional.com";
        String passwd = "Devoluciones2017*";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{emailCC});

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String fullFileName = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;

        Uri uri = Uri.fromFile(new File(fullFileName));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("application/pdf");

        context.startActivity(Intent.createChooser(emailIntent, "Send email using:"));


    }


}
