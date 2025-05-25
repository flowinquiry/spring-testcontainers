package io.flowinquiry.testcontainers.examples.postgresql;

import io.flowinquiry.testcontainers.jdbc.EnablePostgreSQL;
import org.junit.jupiter.api.Test;

@EnablePostgreSQL(version = "16.3")
public class DemoTest {

  @Test
  public void testDemo() {

    System.out.println("Demo test");
  }
}
