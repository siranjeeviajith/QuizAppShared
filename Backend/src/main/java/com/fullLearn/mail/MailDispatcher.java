package com.fullLearn.mail;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Created by user on 6/14/2017.
 */
public class MailDispatcher {


    public static void sendEmail() {


        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", true); // added this line
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", "amandeep.pannu8233");
        props.put("mail.smtp.password", "Chandela8859@#");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", true);



        Session session = Session.getInstance(props,null);
        MimeMessage message = new MimeMessage(session);

        System.out.println("Port: "+session.getProperty("mail.smtp.port"));

        // Create the email addresses involved
        try {
            InternetAddress from = new InternetAddress("amandeep.pannu8233");
            message.setSubject("Yes we can");
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse("sangeethasridharan93@gmail.com"));

            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Create your text message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("some text to send");

            // Add the text part to the multipart
            multipart.addBodyPart(messageBodyPart);

            // Create the html part
            messageBodyPart = new MimeBodyPart();
            String htmlMessage = "Our html text";
            messageBodyPart.setContent(htmlMessage, "text/html");


            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            message.setContent(multipart);

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "amandeep.pannu8233", "Chandela8859@#");
            System.out.println("Transport: "+transport.toString());
            transport.sendMessage(message, message.getAllRecipients());


        } catch (AddressException e) {

                System.out.println("Address exceiption ");

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {

            e.printStackTrace();
        }
    }
    }




