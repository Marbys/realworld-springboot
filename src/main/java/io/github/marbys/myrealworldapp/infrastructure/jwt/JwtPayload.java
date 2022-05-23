package io.github.marbys.myrealworldapp.infrastructure.jwt;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class JwtPayload implements Serializable {
  private long sub;
  private String iss;
  private long exp;

  public JwtPayload() {}

  public JwtPayload(long sub, String iss, long exp) {
    this.sub = sub;
    this.iss = iss;
    this.exp = exp;
  }

  public long getSub() {
    return sub;
  }

  public boolean isExpired() {
    return exp < Instant.now().getEpochSecond();
  }

  public String generatePayload() {
    return "{\"sub\":" + sub + ",\"iss\":\"" + iss + "\",\"exp\":" + exp + "}";
  }
}
