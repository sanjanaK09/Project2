package com.RBU.Project2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RBU.Project2.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	public List<Task> findTaskByUserId(Long userId);
}
