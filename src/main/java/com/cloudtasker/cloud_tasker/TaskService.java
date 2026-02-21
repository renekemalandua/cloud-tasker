package com.cloudtasker.cloud_tasker;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private final TaskRepository repository;

  private static final String TASK_NOT_FOUND_MSG = "Task with id %d not found";

  public TaskService(TaskRepository repository) {
    this.repository = repository;
  }

  public Task create(Task task) {
    return repository.save(task);
  }

  public List<Task> list() {
    return repository.findAll();
  }

  public Task find(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException(String.format(TASK_NOT_FOUND_MSG, id)));
  }

  public Task update(Long id, Task data) {
    Task task =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format(TASK_NOT_FOUND_MSG, id)));

    if (data.getName() != null) {
      task.setName(data.getName());
    }

    if (data.getDescription() != null) {
      task.setDescription(data.getDescription());
    }

    return repository.save(task);
  }

  public void delete(Long id) {
    Task task =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format(TASK_NOT_FOUND_MSG, id)));

    repository.delete(task);
  }
}
