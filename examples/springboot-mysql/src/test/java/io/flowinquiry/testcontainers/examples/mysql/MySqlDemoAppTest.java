package io.flowinquiry.testcontainers.examples.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import io.flowinquiry.testcontainers.examples.mysql.entity.Store;
import io.flowinquiry.testcontainers.examples.mysql.repository.StoreRepository;
import io.flowinquiry.testcontainers.jdbc.mysql.EnableMySQL;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = MySqlDemoApp.class)
@EnableMySQL(version = "8.0")
@ActiveProfiles("test")
public class MySqlDemoAppTest {

  private static final Logger log = getLogger(MySqlDemoAppTest.class);

  @Autowired private StoreRepository storeRepository;

  @Test
  public void testStoreEntityAndRepository() {
    storeRepository.deleteAll();

    log.info("Creating a new store");
    Store store = new Store("Test Store", "A test store for demonstration");
    log.info("Saving the store");
    Store savedStore = storeRepository.save(store);
    log.info("Store saved with ID: {}", savedStore.getId());

    log.info("Verifying store attributes");
    assertNotNull(savedStore.getId());
    assertEquals("Test Store", savedStore.getName());
    assertEquals("A test store for demonstration", savedStore.getDescription());

    log.info("Testing findByName");
    List<Store> storesByName = storeRepository.findByName("Test Store");
    log.info("Found {} stores by name", storesByName.size());
    assertEquals(1, storesByName.size());
    assertEquals("Test Store", storesByName.get(0).getName());

    log.info("Testing findByDescriptionContaining");
    List<Store> storesByDescription = storeRepository.findByDescriptionContaining("demonstration");
    log.info("Found {} stores by description", storesByDescription.size());
    assertEquals(1, storesByDescription.size());
    assertEquals("A test store for demonstration", storesByDescription.get(0).getDescription());

    log.info("Creating another store");
    Store anotherStore =
        new Store("Another Store", "Another test store with a different description");
    log.info("Saving another store");
    storeRepository.save(anotherStore);

    log.info("Verifying total count");
    long count = storeRepository.count();
    log.info("Total count: {}", count);
    assertEquals(2, count);

    log.info("All tests passed successfully!");
  }
}
