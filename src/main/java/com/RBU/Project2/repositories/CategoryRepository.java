package com.RBU.Project2.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RBU.Project2.entities.Category;
import com.RBU.Project2.entities.Task;
import com.RBU.Project2.entities.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	public List<Category> findCategoryByUserId(Long userId);
	
	public Category findByName(String name);
}
