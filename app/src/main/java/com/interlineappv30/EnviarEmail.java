package com.interlineappv30;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by INTEL on 3/09/2017.
 */

public class EnviarEmail extends AsyncTask<EnviarEmail.Mail,Void,Void>{
    private final String user;
    private final String pass;

    public EnviarEmail(String user, String pass) {
        super();
        this.user=user;
        this.pass=pass;
    }

    @Override
    protected Void doInBackground(Mail... mails) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });
        for (Mail mail:mails) {
            try {
                // Se compone la parte del texto
                BodyPart texto = new MimeBodyPart();
                texto.setText(mail.mensaje);

                // Se compone el adjunto con la imagen
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(
                        new DataHandler(new FileDataSource(mail.filename)));
                adjunto.setFileName("devolución.pdf");

                // Una MultiParte para agrupar texto e imagen.
                MimeMultipart multiParte = new MimeMultipart();
                multiParte.addBodyPart(texto);
                multiParte.addBodyPart(adjunto);

                // Se compone el correo, dando to, from, subject y el
                // contenido.
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.setRecipients(Message.RecipientType.TO, mail.to);
                message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(mail.cc));
                message.setSubject(mail.asunto);
                message.setContent(multiParte);

                // Se envia el correo.
                Transport t = session.getTransport("smtp");
                t.connect(user, pass);
                t.sendMessage(message, message.getAllRecipients());
                t.close();
            } catch (MessagingException e) {
                Log.d("MailJob", e.getMessage());
            }
        }
        return null;
    }

    public static class Mail{
        private final String to;
        private final String cc;
        private final String asunto;
        private final String mensaje;
        private final String filename;

        public Mail(String to, String cc, String asunto, String mensaje, String filename) {
            this.to=to;
            this.cc=cc;
            this.asunto=asunto;
            this.mensaje=mensaje;
            this.filename=filename;
        }
    }
}

