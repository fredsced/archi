package net.secudev.archi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;

@Repository
public interface ColumnRepository extends JpaRepository<Column,Long> {

	List<Column> findByProject(Project project);
}
