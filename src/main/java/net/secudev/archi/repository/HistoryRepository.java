package net.secudev.archi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.secudev.archi.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{

}
