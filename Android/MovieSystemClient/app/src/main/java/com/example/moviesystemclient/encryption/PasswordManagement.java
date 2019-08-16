package com.example.moviesystemclient.encryption;

/**
 * @Title: PasswordManagement.java
 * @Package: com.example.moviesystemclient.encryption
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class PasswordManagement {

    private static String RSAPublic;
    private static int RSAKey;
    private static String DES;
    private static String Signature;
    private static int SignatureKey;

    public static void getSaved(String RSAPublic, int RSAKey, String DES,  String Signature, int SignatureKey){
        if(RSAPublic!=null){
            PasswordManagement.RSAPublic = RSAPublic;
        }
        if(RSAKey!=0){
            PasswordManagement.RSAKey = RSAKey;
        }
        if(DES!=null){
            PasswordManagement.DES = DES;
        }
        if(Signature!=null){
            PasswordManagement.Signature = Signature;
        }
        if(SignatureKey!=0){
            PasswordManagement.SignatureKey = SignatureKey;
        }
    }

    public static void clearPassword(){
        RSAPublic = "";
        RSAKey = 0;
        DES = "";
    }

    public static void clearPasswordAndSignature(){
        clearPassword();
        Signature = "";
        SignatureKey = 0;
    }

    public static String getRSAPublic() {
        return RSAPublic;
    }

    public static int getRSAKey() {
        return RSAKey;
    }

    public static String getDES() {
        return DES;
    }

    public static String getSignature() {
        return Signature;
    }

    public static int getSignatureKey() {
        return SignatureKey;
    }

    public static void setRSAPublic(String RSAPublic) {
        PasswordManagement.RSAPublic = RSAPublic;
    }

    public static void setRSAKey(int RSAKey) {
        PasswordManagement.RSAKey = RSAKey;
    }

    public static void setDES(String DES) {
        PasswordManagement.DES = DES;
    }

    public static void setSignature(String signature) {
        Signature = signature;
    }

    public static void setSignatureKey(int signatureKey) {
        SignatureKey = signatureKey;
    }
}
