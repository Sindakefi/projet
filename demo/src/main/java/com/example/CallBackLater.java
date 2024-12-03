package com.example;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class CallBackLater implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // Retrieve patient email and message content from process variables
        String patientEmail = (String) delegateExecution.getVariable("patientEmail");
        String subject = "Please Call Back Later";
        String body = "Dear patient,\n\nDue to unavailability, please call back later to schedule your appointment.\n\nBest regards,\nYour Clinic";

        // Send email notification
        sendEmail(patientEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        // Set up the mail server properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.example.com"); // Use your SMTP server
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Authenticate using your email credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sinda.696969@gmail.com", "Love.2469");
            }
        });

        // Compose the message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sinda.696969@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        // Send the message
        Transport.send(message);
        System.out.println("Email sent to: " + to);
    }
}
