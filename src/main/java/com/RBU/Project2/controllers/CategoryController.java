package com.RBU.Project2.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RBU.Project2.entities.Category;
import com.RBU.Project2.repositories.CategoryRepository;
import com.RBU.Project2.security.CustomUserDetails;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryRepository categoryRepository;
	public CategoryController (CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> createCategory(@RequestBody Category category,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		if (userDetails == null) {
	        return ResponseEntity.status(401).body("Error: User is not authenticated.");
	    }
		
		category.setUserId(userDetails.getUserId());
		Category savedCategory = categoryRepository.save(category);
		return ResponseEntity.status(201).body("Category Saved");
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getCategories(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		return ResponseEntity.ok(categoryRepository.findCategoryByUserId(userDetails.getUserId()));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getCategory(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		Optional<Category> categoryOpt = categoryRepository.findById(id);
		if (categoryOpt.isPresent() && categoryOpt.get().getUserId().equals(userDetails.getUserId())) {
			return ResponseEntity.ok(categoryOpt.get());
		}
		return ResponseEntity.status(404).body("Category not found or access denied.");
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(401).body("Error: User is not authenticated.");
		}
		Optional<Category> categoryOpt = categoryRepository.findById(id);
		if (categoryOpt.isPresent()) {
			if (categoryOpt.get().getUserId().equals(userDetails.getUserId())) {
				categoryRepository.delete(categoryOpt.get());
				return ResponseEntity.ok("Category Deleted");
			} else {
				return ResponseEntity.status(403).body("Error: Access denied.");
			}
		}
		return ResponseEntity.status(404).body("Error: Category not found.");
	}
	
}

