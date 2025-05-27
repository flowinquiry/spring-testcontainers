package io.flowinquiry.testcontainers.examples.postgresql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.slf4j.LoggerFactory.getLogger;

import io.flowinquiry.testcontainers.examples.postgresql.config.PostgresqlConfig;
import io.flowinquiry.testcontainers.examples.postgresql.entity.Store;
import io.flowinquiry.testcontainers.examples.postgresql.repository.StoreRepository;
import io.flowinquiry.testcontainers.jdbc.EnablePostgreSQL;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostgresqlConfig.class)
@EnablePostgreSQL
@ActiveProfiles("test")
public class PostgresqlTest {

  private static final Logger log = getLogger(PostgresqlTest.class);

  @Autowired private StoreRepository storeRepository;

  @Test
  public void testStoreEntityAndRepository() {
    storeRepository.deleteAll();

    // Create and save a new store
    log.info("Creating a new store");
    Store store = new Store("Test Store", "A test store for demonstration");
    log.info("Saving the store");
    Store savedStore = storeRepository.save(store);
    log.info("Store saved with ID: {}", savedStore.getId());

    // Verify the store was saved with an ID
    log.info("Verifying store attributes");
    assertNotNull(savedStore.getId());
    assertEquals("Test Store", savedStore.getName());
    assertEquals("A test store for demonstration", savedStore.getDescription());

    // Test findByName
    log.info("Testing findByName");
    List<Store> storesByName = storeRepository.findByName("Test Store");
    log.info("Found {} stores by name", storesByName.size());
    assertEquals(1, storesByName.size());
    assertEquals("Test Store", storesByName.get(0).getName());

    // Test findByDescriptionContaining
    log.info("Testing findByDescriptionContaining");
    List<Store> storesByDescription = storeRepository.findByDescriptionContaining("demonstration");
    log.info("Found {} stores by description", storesByDescription.size());
    assertEquals(1, storesByDescription.size());
    assertEquals("A test store for demonstration", storesByDescription.get(0).getDescription());

    // Create and save another store
    log.info("Creating another store");
    Store anotherStore =
        new Store("Another Store", "Another test store with a different description");
    log.info("Saving another store");
    storeRepository.save(anotherStore);

    // Verify we now have 2 stores
    log.info("Verifying total count");
    long count = storeRepository.count();
    log.info("Total count: {}", count);
    assertEquals(2, count);

    log.info("All tests passed successfully!");
  }
}
