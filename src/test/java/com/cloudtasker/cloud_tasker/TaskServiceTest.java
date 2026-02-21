package com.cloudtasker.cloud_tasker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setName("Test Task");
        task.setDescription("Description");
    }

    @Test
    void create_ShouldReturnSavedTask() {
        when(repository.save(any(Task.class))).thenReturn(task);

        Task saved = service.create(task);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Task");
        verify(repository).save(task);
    }

    @Test
    void list_ShouldReturnTaskList() {
        when(repository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = service.list();

        assertThat(tasks).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void find_ShouldReturnTask_WhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        Task found = service.find(1L);

        assertThat(found).isNotNull();
        verify(repository).findById(1L);
    }

    @Test
    void find_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.find(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task with id 1 not found");
    }

    @Test
    void update_ShouldModifyOnlyProvidedFields() {
        Task existingTask = new Task();
        existingTask.setName("Old Name");
        existingTask.setDescription("Old Description");

        Task newData = new Task();
        newData.setName("New Name");

        when(repository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(repository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        Task updated = service.update(1L, newData);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getDescription()).isEqualTo("Old Description"); // Should NOT change
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        service.delete(1L);

        verify(repository, times(1)).delete(task);
    }
}
