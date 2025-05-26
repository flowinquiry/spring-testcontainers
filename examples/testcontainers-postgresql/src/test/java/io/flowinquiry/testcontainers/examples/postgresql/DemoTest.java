package io.flowinquiry.testcontainers.examples.postgresql;

import io.flowinquiry.testcontainers.examples.postgresql.entity.Store;
import io.flowinquiry.testcontainers.examples.postgresql.repository.StoreRepository;
import io.flowinquiry.testcontainers.jdbc.EnablePostgreSQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = PostgresqlDemoApp.class)
@EnablePostgreSQL(version = "16.3")
@ActiveProfiles("test")
public class DemoTest {

  @Autowired
  private StoreRepository storeRepository;

  @Test
  public void testStoreEntityAndRepository() {
    System.out.println("[DEBUG_LOG] Starting test");

    // Clear any existing data
    System.out.println("[DEBUG_LOG] Attempting to delete all stores");
    storeRepository.deleteAll();

    // Create and save a new store
    System.out.println("[DEBUG_LOG] Creating a new store");
    Store store = new Store("Test Store", "A test store for demonstration");
    System.out.println("[DEBUG_LOG] Saving the store");
    Store savedStore = storeRepository.save(store);
    System.out.println("[DEBUG_LOG] Store saved with ID: " + savedStore.getId());

    // Verify the store was saved with an ID
    System.out.println("[DEBUG_LOG] Verifying store attributes");
    assertNotNull(savedStore.getId());
    assertEquals("Test Store", savedStore.getName());
    assertEquals("A test store for demonstration", savedStore.getDescription());

    // Test findByName
    System.out.println("[DEBUG_LOG] Testing findByName");
    List<Store> storesByName = storeRepository.findByName("Test Store");
    System.out.println("[DEBUG_LOG] Found " + storesByName.size() + " stores by name");
    assertEquals(1, storesByName.size());
    assertEquals("Test Store", storesByName.get(0).getName());

    // Test findByDescriptionContaining
    System.out.println("[DEBUG_LOG] Testing findByDescriptionContaining");
    List<Store> storesByDescription = storeRepository.findByDescriptionContaining("demonstration");
    System.out.println("[DEBUG_LOG] Found " + storesByDescription.size() + " stores by description");
    assertEquals(1, storesByDescription.size());
    assertEquals("A test store for demonstration", storesByDescription.get(0).getDescription());

    // Create and save another store
    System.out.println("[DEBUG_LOG] Creating another store");
    Store anotherStore = new Store("Another Store", "Another test store with a different description");
    System.out.println("[DEBUG_LOG] Saving another store");
    storeRepository.save(anotherStore);

    // Verify we now have 2 stores
    System.out.println("[DEBUG_LOG] Verifying total count");
    long count = storeRepository.count();
    System.out.println("[DEBUG_LOG] Total count: " + count);
    assertEquals(2, count);

    System.out.println("[DEBUG_LOG] All tests passed successfully!");
  }
}
