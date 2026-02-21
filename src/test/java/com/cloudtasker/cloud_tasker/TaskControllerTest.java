package com.cloudtasker.cloud_tasker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_ShouldReturnTask() throws Exception {
        Task task = new Task();
        task.setName("Test Task");

        when(service.create(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    void list_ShouldReturnTaskList() throws Exception {
        Task task = new Task();
        task.setName("Test Task");
        when(service.list()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    void find_ShouldReturnTask_WhenIdExists() throws Exception {
        Task task = new Task();
        task.setName("Test Task");
        when(service.find(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    void update_ShouldReturnUpdatedTask() throws Exception {
        Task task = new Task();
        task.setName("Updated Task");
        when(service.update(eq(1L), any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task"));
    }

    @Test
    void delete_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());
    }
}
