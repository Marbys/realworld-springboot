package io.github.marbys.myrealworldapp;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class IntegrationTestUtils {

    public static ResultMatcher[] validUserModel() {
        return new ResultMatcher[]{
                jsonPath("$.user").isMap(),
                jsonPath("$.user.username").isString(),
                jsonPath("$.user.email").isString(),
                jsonPath("$.user.bio").isString(),
                jsonPath("$.user.image").isString(),
                jsonPath("$.user.token").isString()};
    }

    public static ResultMatcher[] validProfile() {
        return new ResultMatcher[]{
                jsonPath("$.profile").isMap(),
                jsonPath("$.profile.username").isString(),
                jsonPath("$.profile.bio").isString(),
                jsonPath("$.profile.image").isString(),
                jsonPath("$.profile.following").isBoolean()};
    }
}
