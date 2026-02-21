package com.cloudtasker.cloud_tasker;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class TaskRepositoryTest {

  @Autowired private TaskRepository repository;

  @Autowired private EntityManager entityManager;

  @Test
  void save_ShouldPopulateAuditFields() {
    Task task = new Task();
    task.setName("Audit Task");

    Task saved = repository.save(task);
    entityManager.flush(); // Force PrePersist

    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
  }

  @Test
  void update_ShouldChangeUpdatedAt() throws InterruptedException {
    Task task = new Task();
    task.setName("Update Audit Task");
    Task saved = repository.save(task);
    entityManager.flush();

    var firstUpdate = saved.getUpdatedAt();

    Thread.sleep(10); // Ensure time difference

    saved.setName("New Name");
    Task updated = repository.save(saved);
    entityManager.flush();

    assertThat(updated.getUpdatedAt()).isAfter(firstUpdate);
  }
}
