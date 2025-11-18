package com.example.user_microservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "JWT_SECRET=test_secret_key_value_for_builds_123456789")
class UserMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }

}
