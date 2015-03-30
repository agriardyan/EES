/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pti.eesmail.mail;

import com.pti.eesmail.crypto.AESEncrypt;
import com.sun.mail.smtp.SMTPTransport;
import java.security.Security;
import java.util.Properties;
import javax.mail.*;

/**
 *
 * @author Agustinus Agri
 */
public class RetrieveMail {

    private String mUsernameEmail;
    private String mPasswordEmail;
    private final String HOST = "imap.gmail.com";
    private final String STORE_TYPE = "imaps";

    public RetrieveMail(String mUsernameEmail, String mPasswordEmail) {
        this.mUsernameEmail = mUsernameEmail;
        this.mPasswordEmail = mPasswordEmail;
    }

    public String getmUsernameEmail() {
        return mUsernameEmail;
    }

    public String getmPasswordEmail() {
        return mPasswordEmail;
    }


    private Store connectToPOPIMAPServer(Session session) throws NoSuchProviderException, MessagingException {

        Store store = session.getStore(STORE_TYPE);
        store.connect(HOST, getmUsernameEmail(), getmPasswordEmail());

        return store;

    }

    private Session createSession() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.imap.host", HOST);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.connectiontimeout", "5000");
        props.setProperty("mail.imap.timeout", "5000");
        props.put("mail.imaps.starttls.enable", "true");

        Session session = Session.getInstance(props, null);

        return session;
    }

    public void check() {
        try {

            Session session = createSession();

            //create the POP3 store object and connect with the pop server
            Store store = connectToPOPIMAPServer(session);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();

            int messagesCount = messages.length;
            int msgIndex = messagesCount - 1;

            System.out.println("messages.length --- " + messagesCount);

            System.out.println("Email Number " + messagesCount);
                System.out.println("Subject: " + AESEncrypt.decrypt(messages[msgIndex].getSubject()));
                System.out.println("From: " + messages[msgIndex].getFrom()[0]);
                System.out.println("Text: " + AESEncrypt.decrypt(messages[msgIndex].getContent().toString()));

//            for (int i = 0, n = messages.length; i < n; i++) {
//                Message message = messages[i];
//                System.out.println("---------------------------------");
//                System.out.println("Email Number " + (i + 1));
//                System.out.println("Subject: " + AESEncrypt.decrypt(message.getSubject()));
//                System.out.println("From: " + message.getFrom()[0]);
//                System.out.println("Text: " + AESEncrypt.decrypt(message.getContent().toString()));
//
//            }

            //close the store and folder objects
            emailFolder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
