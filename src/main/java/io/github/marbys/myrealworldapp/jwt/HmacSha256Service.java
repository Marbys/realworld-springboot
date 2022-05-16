package io.github.marbys.myrealworldapp.jwt;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class HmacSha256Service {

    private static String ALG = "HmacSHA256";

    public static byte[] hash(String message, String secret) {
        final Mac hmacSHA256;
        try {
            hmacSHA256 = Mac.getInstance(ALG);
            hmacSHA256.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALG));
            byte[] signed = hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return signed;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Unable to hash message with: " + ALG);
    }

}
