package io.github.marbys.myrealworldapp.infrastructure.jwt;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Base64Util {

  public String stringFromBase64(String token) {
    return new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
  }

  public String base64FromString(String s) {
    return base64FromBytes(s.getBytes(StandardCharsets.UTF_8));
  }

  public String base64FromBytes(byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
