package io.flowinquiry.testcontainers.examples.postgresql.repository;

import io.flowinquiry.testcontainers.examples.postgresql.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for managing Store entities. */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  /**
   * Find stores by name.
   *
   * @param name the name to search for
   * @return a list of stores with the given name
   */
  List<Store> findByName(String name);

  /**
   * Find stores containing the given text in their description.
   *
   * @param text the text to search for in the description
   * @return a list of stores with descriptions containing the given text
   */
  List<Store> findByDescriptionContaining(String text);
}
