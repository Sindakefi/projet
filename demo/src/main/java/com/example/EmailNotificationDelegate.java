package com.example;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotificationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Retrieve process variables
        String patientEmail = (String) execution.getVariable("patientEmail"); // Patient's email address
        String assignedDoctor = (String) execution.getVariable("assignedDoctor"); // Doctor's name
        
        if (patientEmail == null || assignedDoctor == null) {
            throw new RuntimeException("Missing required variables: patientEmail or assignedDoctor");
        }

        // Set up email properties
        final String fromEmail = "sinda.696969@gmail.com"; // Replace with your email address
        final String fromPassword = "Love.2469"; // Replace with your email password
        String subject = "Appointment Confirmation";
        String messageBody = "Dear Patient,\n\nYour appointment has been confirmed with Dr. " 
                              + assignedDoctor + ".\n\nThank you.";

        // Configure email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your email SMTP server
        props.put("mail.smtp.port", "587");

        // Create a session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully to " + patientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
