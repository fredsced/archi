package net.secudev.archi.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.secudev.archi.repository.ProjectRepository;

@Component
@Slf4j
public class Scheduler {
	@Autowired
	ProjectRepository projects;
	
	//Tous les jours à minuit
	@Scheduled(cron="0 0 0 * * *")
    public void reportCurrentTime() {
		projects.deleteAll();
        log.info("all data cleared...");
    }
}
