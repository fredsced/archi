package net.secudev.archi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.secudev.archi.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Project findByName(String name);
	List<Project> findByNameContainingIgnoreCase(String keyword);
}
