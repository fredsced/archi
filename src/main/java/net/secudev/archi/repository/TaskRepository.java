package net.secudev.archi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.secudev.archi.model.Column;
import net.secudev.archi.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByColumn(Column column);	
	List<Task> findByColumnAndPriority(Column column, int priority);
}
