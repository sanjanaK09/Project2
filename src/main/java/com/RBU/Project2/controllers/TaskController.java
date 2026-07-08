package com.RBU.Project2.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RBU.Project2.entities.Category;
import com.RBU.Project2.entities.Task;
import com.RBU.Project2.repositories.CategoryRepository;
import com.RBU.Project2.repositories.TaskRepository;
import com.RBU.Project2.security.CustomUserDetails;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	private final TaskRepository taskRepository;
	private final CategoryRepository categoryRepository;

	public TaskController (TaskRepository taskRepository, CategoryRepository categoryRepository) {
		this.taskRepository = taskRepository;
		this.categoryRepository = categoryRepository;
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> createTask(@RequestBody Task task, 
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		
		if (task.getCategory() != null && task.getCategory().getId() != null) {
			Optional<Category> categoryOpt = categoryRepository.findById(task.getCategory().getId());
			if (categoryOpt.isEmpty() || !categoryOpt.get().getUserId().equals(userDetails.getUserId())) {
				return ResponseEntity.badRequest().body("Error: Invalid category or access denied.");
			}
			task.setCategory(categoryOpt.get());
		}
		
		task.setUserId(userDetails.getUserId());
		if (task.getStatus() == null) {
			task.setStatus("TODO");
		}
		Task savedTask = taskRepository.save(task);
		return ResponseEntity.status(201).body(savedTask);
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		return ResponseEntity.ok(taskRepository.findTaskByUserId(userDetails.getUserId()));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		Optional<Task> taskOpt = taskRepository.findById(id);
		if (taskOpt.isPresent() && taskOpt.get().getUserId().equals(userDetails.getUserId())) {
			return ResponseEntity.ok(taskOpt.get());
		}
		return ResponseEntity.status(404).body("Error: Task not found or access denied.");
	}
}
