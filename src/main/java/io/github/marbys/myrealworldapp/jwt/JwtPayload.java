package io.github.marbys.myrealworldapp.jwt;

import java.time.Instant;

public class JwtPayload {
  private long sub;
  private String iss;
  private long exp;

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
