package io.flowinquiry.testcontainers.examples.postgresql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PostgresqlDemoApp.class)
@ActiveProfiles("test")
public class SimpleContextTest {

    @Test
    public void contextLoads() {
        System.out.println("[DEBUG_LOG] Spring context loaded successfully");
    }
}