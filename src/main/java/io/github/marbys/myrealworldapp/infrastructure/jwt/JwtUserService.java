package io.github.marbys.myrealworldapp.infrastructure.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.marbys.myrealworldapp.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class JwtUserService {

  private static final long TOKEN_LIFETIME = 3600;
  private static final String SECRET = "your-256-bit-secret";
  private static final String HEADER = "{\"alg\":\"HS256\",\"type\":\"JWT\"}";

  private final ObjectMapper mapper;
  private final Base64Util base64Util;

  public String tokenFromUserEntity(User entity) {
    JwtPayload jwtPayload =
        new JwtPayload(
            entity.getId(), entity.getEmail(), Instant.now().getEpochSecond() + TOKEN_LIFETIME);
    String messageToHash =
        base64Util.base64FromString(HEADER)
            + "."
            + base64Util.base64FromString(jwtPayload.generatePayload());
    return messageToHash.concat(
        "." + base64Util.base64FromBytes(HmacSha256Service.hash(messageToHash, SECRET)));
  }

  public JwtPayload payloadFromToken(String token) {
    String[] split = token.substring(6).split("\\.");
    JwtPayload jwtPayload;

    if (!split[0].equals(base64Util.base64FromString(HEADER)))
      throw new IllegalArgumentException("Invalid JWT Header.");

    if (!split[2].equals(
        base64Util.base64FromBytes(
            HmacSha256Service.hash(split[0].concat("." + split[1]), SECRET))))
      throw new IllegalArgumentException("Invalid JWT Signature.");

    try {
      jwtPayload = mapper.readValue(base64Util.stringFromBase64(split[1]), JwtPayload.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error while parsing token with payload: " + split[1]);
    }

    if (jwtPayload.isExpired()) throw new IllegalArgumentException("JWT Token has expired.");
    return jwtPayload;
  }
}
