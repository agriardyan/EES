/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pti.eesmail.mail;

import com.pti.eesmail.crypto.AESEncrypt;
import com.sun.mail.smtp.SMTPTransport;
import java.io.ByteArrayOutputStream;
import java.security.Security;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Agustinus Agri
 */
public class SendMail {
    
    private String mUsernameEmail;
    private String mPasswordEmail;
    private String mPasswordCrypto;
    private String mRecipientEmail;
    private String mCCEmail;
    private String mSubjectMail;
    private String mContentMail;

    public SendMail(String mUsernameEmail, String mPasswordEmail) {
        this.mUsernameEmail = mUsernameEmail;
        this.mPasswordEmail = mPasswordEmail;
    }
    public String getmUsernameEmail() {
        return mUsernameEmail;
    }

    public String getmPasswordEmail() {
        return mPasswordEmail;
    }

    public String getmPasswordCrypto() {
        return mPasswordCrypto;
    }

    public void setmPasswordCrypto(String mPasswordCrypto) {
        this.mPasswordCrypto = mPasswordCrypto;
    }

    public String getmRecipientEmail() {
        return mRecipientEmail;
    }

    public void setmRecipientEmail(String mRecipientEmail) {
        this.mRecipientEmail = mRecipientEmail;
    }

    public String getmCCEmail() {
        return mCCEmail;
    }

    public void setmCCEmail(String mCCEmail) {
        this.mCCEmail = mCCEmail;
    }

    public String getmSubjectMail() {
        return mSubjectMail;
    }

    public void setmSubjectMail(String mSubjectMail) {
        this.mSubjectMail = mSubjectMail;
    }

    public String getmContentMail() {
        return mContentMail;
    }

    public void setmContentMail(String mContentMail) {
        this.mContentMail = mContentMail;
    }
    
    private SMTPTransport connectToSMTPServer(Session session) throws NoSuchProviderException, MessagingException {
        SMTPTransport smtp = (SMTPTransport) session.getTransport("smtps");
        smtp.connect("smtp.gmail.com", getmUsernameEmail(), getmPasswordEmail());
        return smtp;
    }
    
    private Session createSession() {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        /*
         If set to false, the QUIT command is sent and the connection is immediately closed. If set 
         to true (the default), causes the transport to wait for the response to the QUIT command.

         ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
         http://forum.java.sun.com/thread.jspa?threadID=5205249
         smtpsend.java - demo program from javamail
         */
        props.setProperty("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);
        
        return session;
    }

    /**
     * Send email using GMail SMTP server.
     * 
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the
     * @throws Exception wtf?
     * connected state or if the message is not a MimeMessage
     */
    public void send() 
            throws AddressException, MessagingException, Exception {
        
        Session session = createSession();
        
        String encryptedSubject = AESEncrypt.encrypt(getmSubjectMail());
        String encryptedContent = AESEncrypt.encrypt(getmContentMail());
        
        // koneksi ke mail server
        SMTPTransport SMTPTransport = connectToSMTPServer(session);

        MimeMessage msg = new MimeMessage(session);

        // Pengirim & Penerima
        msg.setFrom(new InternetAddress(getmUsernameEmail()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getmRecipientEmail(), false));

        // CC Email. Format multiple CC / multiple recipient belum diketahui
        if (getmCCEmail().length() > 0) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(getmCCEmail(), false));
        }

        // Judul dan konten pesan
        msg.setSubject(encryptedSubject);
        msg.setText(encryptedContent);
        msg.setSentDate(new Date());
        
        SMTPTransport.sendMessage(msg, msg.getAllRecipients());
        SMTPTransport.close();
        
    }
    
    
    
}
