package io.github.marbys.myrealworldapp.jwt;

import lombok.Value;

@Value
public class JwtUserPayload {
    private String sub;
    private int exp;
    private int iat;
}
