package java.com.king.netty;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

public class CrawlerTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String viStr = "d321e9e01d57f2389ba1eb3cc36bb929";
//        byte[] encoded = secretKey.getEncoded();
//        d321e9e01d57f2389ba1eb3cc36bb929
//        System.out.println(new BigInteger(1, encoded).toString(16));

        String skStr = "d7f0f00999ce23d3333f2215456ba50b";

        byte[] vi = DatatypeConverter.parseHexBinary(viStr);
        byte[] sk = DatatypeConverter.parseHexBinary(skStr);
        SecretKey key = new SecretKeySpec(sk, "AES");

//        byte[] encrypt = SecureUtil.aes().setIv(vi).init(SymmetricAlgorithm.AES.getValue(), key).encrypt("wide wide web!wide wide web!wide wide web!wide wide web!wide wide web!wide wide web!wide wide web!wide wide web!wide wide web!".getBytes(StandardCharsets.UTF_8));
//        System.out.println(new BigInteger(1, encrypt).toString(16));

        String data = "75c4fb0903baf22bea399089d0942abe7abc42a63d536ccc84f4e39d5e3b6942238c55de9b2131cec945efabebf96c67718062859b51cc7585ed853a590aa6ca41e1a7b2fe6ecb3f097db6e511b31702464db90bd57b7e895c611621f930dcacfe70763668880bec1f220428e706a62311db05b65a4bbed7f3a5dada2d3d1d2b";

//        Calendar calendar = DatatypeConverter.parseDateTime("2024-12-16");
//        System.out.println(calendar.getTime());



//        byte[] decrypt = SecureUtil.aes().setIv(vi).init(SymmetricAlgorithm.AES.getValue(), key).decrypt(data);
//        System.out.println(new String(decrypt));

    }
}
