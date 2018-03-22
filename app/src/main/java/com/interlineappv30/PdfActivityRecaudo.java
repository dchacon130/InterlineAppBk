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
 * Created by INTEL on 9/11/2017.
 */

public class PdfActivityRecaudo {
    private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.interline.pdf";
    private static final String INVOICES = "Recaudos";
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
    public PdfActivityRecaudo(Context context) throws IOException, DocumentException {
        mContext = context;
        //Creamos los distintos estilos para nuestro tipo de fuente.
        unicode = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        catFont = new Font(unicode, 21,Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 14,Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 10,Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 9,Font.NORMAL, BaseColor.BLACK);
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
                //Creamos el contenido en form de tabla documentoa
                addInvoiceContent(document,invoiceObject.invoiceDocumentosList);
                //Creamos el contenido en form de tabla pagos
                addInvoiceContent2(document,invoiceObject.invoicePagosList);
                //Creamos el contenido en form de tabla pagos
                addInvoiceContent3(document,invoiceObject.invoiceDescuentoList);
                //Creamos el pie de pagina
                addInvoiceTotal(document, invoiceObject);

                document.close();

                Toast.makeText(mContext, "PDF file created successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createDirectoryAndFileName(InvoiceObject invoiceObject){
        String FILENAME = "Recaudo_"+invoiceObject.boletoDevolucion+".pdf";
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
        preface.add(new Paragraph(
                mContext.getResources().getString(R.string.consecutivo_recaudo) + invoiceObject.boletoDevolucion +
                        "                                                " +
                mContext.getResources().getString(R.string.date_recaudo) + " " +stringDate,subFont));
        // Adicionamos una línea en blanco
        addEmptyLine(preface, 1);

        //DATOS RECAUDO
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfTransferencia) + " " + invoiceObject.TotalRecaudo +
            "                                                                                                " +
                mContext.getResources().getString(R.string.pdfAscesor) + " " + invoiceObject.ascesor
                , smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfCredito) + " " + invoiceObject.notaCredito ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfTotal) + " " + invoiceObject.TotalRecaudo,smallFont));
        addEmptyLine(preface, 1);
        //Adicionamos los datos del Cliente
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloCLiente), smallBold));

        preface.add(new Paragraph(
                mContext.getResources().getString(R.string.pdfCodEmp) + " " + invoiceObject.codigoempresa + "                     " +
                        mContext.getResources().getString(R.string.pdfDirEmp) + " " + invoiceObject.direccionEmpresa
                ,smallFont));
        preface.add(new Paragraph(mContext.getResources().getString(R.string.pdfNifEmp) + " " + invoiceObject.nitempresa + "                       " +
                mContext.getResources().getString(R.string.pdfNombre) + " " + invoiceObject.nombreempresa
                ,smallFont));
        addEmptyLine(preface, 1);
        //Adicionamos el párrafo creado al documento
        document.add(preface);
        // Si queremos crear una nueva página
        //document.newPage();
    }
    //Creamos el contenido de la grilla, las líneas con los documentos.
    private static void addInvoiceContent(Document document, java.util.List<InvoiceDetails> invoiceDetail) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable(document, paragraph, invoiceDetail);
        addEmptyLine(paragraph, 1);
        // Adicionamos el párrafo al documento
        document.add(paragraph);
    }
    //Creamos el contenido de la grilla, las líneas con los pagos.
    private static void addInvoiceContent2(Document document, java.util.List<InvoiceDetails> invoiceDetail) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable2(document, paragraph, invoiceDetail);
        addEmptyLine(paragraph, 1);
        // Adicionamos el párrafo al documento
        document.add(paragraph);
    }

    //Creamos el contenido de la grilla, las líneas con los descuentos.
    private static void addInvoiceContent3(Document document, java.util.List<InvoiceDetails> invoiceDetail) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable3(document, paragraph, invoiceDetail);
        addEmptyLine(paragraph, 1);
        // Adicionamos el párrafo al documento
        document.add(paragraph);
    }

    //Creamos el subtotal y el total de la factura.
    private static void addInvoiceTotal(Document document, InvoiceObject invoiceObject) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        document.add(new Phrase(mContext.getResources().getString(R.string.pdfNotaRecaudo),smallFont));
        // Adicionamos la tabla al párrafo
        createTotalInvoiceTable(paragraph, invoiceObject);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }
    //Procedimiento para crear los títulos de las columnas de la factura.
    private static void createInvoiceTable(Document document, Paragraph tableSection, java.util.List<InvoiceDetails> invoiceDetails)
            throws DocumentException {
        int TABLE_COLUMNS = 5;
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloDocumentos), italicFont));
        document.add(preface);
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);// number of table columns
        //Definimos el ancho que corresponde a cada una de las 6 columnas
        float[] columnWidths = new float[]{150f, 70f, 90f, 90f, 60f};
        table.setWidths(columnWidths);
        //Definimos el ancho de nuestra tabla en %
        table.setWidthPercentage(100);

        // Aquí les dejos otras propiedades que pueden aplicar a la tabla
        //table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);

        //Definimos los títulos para cada una de las 5 columnas
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.numeroDocumento),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.tipoFactura),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.fechaDocu),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.fechaVenci),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.recSaldo),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        table.addCell(cell);

        //Creamos la fila de la tabla con las cabeceras
        table.setHeaderRows(1);

        //Creamos las lineas con los artículos de la factura;
        for (InvoiceDetails orderLine : invoiceDetails) {
            createInvoiceLine(orderLine, table);
        }

        tableSection.add(table);
    }
    //Procedimiento para crear los títulos de las columnas de la factura.
    private static void createInvoiceTable2(Document document, Paragraph tableSection, java.util.List<InvoiceDetails> invoiceDetails)
            throws DocumentException {
        int TABLE_COLUMNS = 6;
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloPagos), italicFont));
        document.add(preface);
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable t = new PdfPTable(TABLE_COLUMNS);// number of table columns
        //Definimos el ancho que corresponde a cada una de las 6 columnas
        float[] columnWidths = new float[]{80f, 100f, 90f, 90f, 70f, 60f};
        t.setWidths(columnWidths);
        //Definimos el ancho de nuestra tabla en %
        t.setWidthPercentage(100);

        // Aquí les dejos otras propiedades que pueden aplicar a la tabla
        //table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);

        //Definimos los títulos para cada una de las 6 columnas
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfTipoPago),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        t.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfNroCuenta),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        t.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfTipoBanco),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        t.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfNumCuenta),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        t.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfFechaPago),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        t.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfValorPago),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        t.addCell(cell);

        //Creamos la fila de la tabla con las cabeceras
        t.setHeaderRows(1);

        //Creamos las lineas con los artículos de la factura;
        for (InvoiceDetails orderLine : invoiceDetails) {
            createInvoiceLine2(orderLine, t);
        }

        tableSection.add(t);
    }
    //Procedimiento para crear los títulos de las columnas de la factura.
    private static void createInvoiceTable3(Document document, Paragraph tableSection, java.util.List<InvoiceDetails> invoiceDetails)
            throws DocumentException {
        int TABLE_COLUMNS = 4;
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(mContext.getResources().getString(R.string.tituloDescuentos), italicFont));
        document.add(preface);
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable tb = new PdfPTable(TABLE_COLUMNS);// number of table columns
        //Definimos el ancho que corresponde a cada una de las 6 columnas
        float[] columnWidths = new float[]{30f, 70f, 60f, 150f};
        tb.setWidths(columnWidths);
        //Definimos el ancho de nuestra tabla en %
        tb.setWidthPercentage(100);

        // Aquí les dejos otras propiedades que pueden aplicar a la tabla
        //table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);

        //Definimos los títulos para cada una de las 6 columnas
        PdfPCell cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfOdis),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        tb.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfTipoDescuento),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        tb.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfValorDescuento),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        tb.addCell(cell);

        cell = new PdfPCell(new Phrase(mContext.getResources().getString(R.string.pdfObservacionDesc),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        tb.addCell(cell);

        //Creamos la fila de la tabla con las cabeceras
        tb.setHeaderRows(1);

        //Creamos las lineas con los artículos de la factura;
        for (InvoiceDetails orderLine : invoiceDetails) {
            createInvoiceLine3(orderLine, tb);
        }
        tableSection.add(tb);
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
        bitMap = bitMap.createScaledBitmap(bitMap,450,96,false);  // cambio de resolucion de la imagen mapeada bitmap en pixeles
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
        table.addCell(invoiceLine.itemDocumento);
        table.addCell(invoiceLine.itemFactura);
        table.addCell(invoiceLine.itemFechaDoc);
        table.addCell(invoiceLine.itemFechaVenc);
        table.addCell(invoiceLine.itemSaldo);

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

    //Procedimiento para crear las líneas de la factura en forma de tabla.
    private static void createInvoiceLine2(InvoiceDetails invoiceLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        //Adicionamos celdas sin formato ni estilos, solo el valor
        table.addCell(invoiceLine.tipoPago);
        table.addCell(invoiceLine.numeroCuenta);
        table.addCell(invoiceLine.tipoBanco);
        table.addCell(invoiceLine.codigoCuenta);
        table.addCell(invoiceLine.fechaPago);
        table.addCell(invoiceLine.valorPago);

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
    //Procedimiento para crear las líneas de la factura en forma de tabla.
    private static void createInvoiceLine3(InvoiceDetails invoiceLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        //Adicionamos celdas sin formato ni estilos, solo el valor
        table.addCell(invoiceLine.odis);
        table.addCell(invoiceLine.tipoDescuento);
        table.addCell(invoiceLine.valorDescuento);
        table.addCell(invoiceLine.obsDescuento);

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
