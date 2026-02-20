package com.cloudtasker.cloud_tasker;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{
	private final TaskService service;

	public TaskController(TaskService service)
	{
		this.service = service;
	}

	@PostMapping
	public Task create(@RequestBody Task task)
	{
		return service.create(task);
	}

	@GetMapping
	public List<Task> list()
	{
		return service.list();
	}

	@GetMapping("/{id}")
	public Task find(@PathVariable Long id)
	{
		return service.find(id);
	}

	@PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return service.update(id, task);
    }

	@DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
