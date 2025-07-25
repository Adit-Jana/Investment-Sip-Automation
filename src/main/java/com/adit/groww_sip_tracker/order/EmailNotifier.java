package com.adit.groww_sip_tracker.order;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotifier {

    public static void sendEmail(String subject, String body, String toEmail, String fromEmail, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // or your SMTP host
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("📧 Email alert sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
