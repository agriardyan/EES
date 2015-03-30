/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pti.eesmail.main;

import com.pti.eesmail.crypto.AESEncrypt;

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
        String originalMessage = "HaloBro";
        
        String encrypt = AESEncrypt.encrypt(originalMessage);
        System.out.println(encrypt);
        
        String resultMessage = AESEncrypt.decrypt(encrypt);
        System.out.println(resultMessage);
        
        System.out.println("-------------");
        
    }
    
}
