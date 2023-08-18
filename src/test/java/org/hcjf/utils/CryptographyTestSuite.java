package org.hcjf.utils;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author Andres Medina
 */
public class CryptographyTestSuite {

    private static final String message = "Las máquinas me sorprenden con mucha frecuencia";
    private static final String encryptedMessage = "e3742e1b4c746eb0b9f5585ed3087aa20c15d5b50c67d10ce080174ed2050ff5ebfae817ff3e9d52e4d6d8f3373a9c561d2f64a33b6d9af96ae77f822f876a9744fc53a102d5db498bfc5a7005b79f5951460ebdc9332fa7b5662898efbda4d46fa42b468b601a015ab71c2413ae533525a71d5be0418335ea8b14545aed84aed2b7db05ba9ece60cc8bc33a2fd42ef59db662a8d03dc9c5c58c512b5bf79e7d";
    byte[] test;

    @Test(timeout = 20000)
    public void encrypt() {
        Cryptography crypt = new Cryptography();//(key,"RandomIVTestService","HolandaCatalinaCrypt","AES","GCM","PKCS5Padding",128);
        for (int i = 0; i < 10; i++) {
            crypt.encrypt((message + i).getBytes());
        }
    }

    @Test
    public void decrypt() {
        Cryptography crypt = new Cryptography();//(key,"RandomIVTestService","HolandaCatalinaCrypt","AES","GCM","PKCS5Padding",128);
        String messageResult = new String(crypt.decrypt(Strings.hexToBytes(encryptedMessage)));
        Assert.assertEquals(message,messageResult);
    }
}
