/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pti.eesmail.main;

import com.pti.eesmail.crypto.AESEncrypt;
import com.pti.eesmail.mail.RetrieveMail;
import com.pti.eesmail.mail.SendMail;

/**
 *
 * @author Agustinus Agri
 */
public class EESMailMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        System.out.println("-------------");
        
        String username = "testing1.pti@gmail.com";
        String passwordEmail = "proyekteknologiinformasi";
        
        String ccEmail = "";
        String recipient = "testing2.pti@gmail.com";
        
        String subject = "EED-IS";
        
        String content = "Lorem ipsum dolor sit amet. halohalohalo";
        
        SendMail mail = new SendMail(username, passwordEmail);
        mail.setmRecipientEmail(recipient);
        mail.setmCCEmail(ccEmail);
        mail.setmSubjectMail(subject);
        mail.setmContentMail(content);
        
        mail.send();
        
        System.out.println("SUCESSFULLY SEND");
        
        System.out.println("---------------------");
        
        username = "testing2.pti@gmail.com";
        passwordEmail = "proyekteknologiinformasi";
                
        RetrieveMail retmail = new RetrieveMail(username, passwordEmail);
        
        retmail.check();
        
    }
    
}
