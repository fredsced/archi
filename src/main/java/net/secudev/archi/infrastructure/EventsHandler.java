package net.secudev.archi.infrastructure;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.secudev.archi.model.History;
import net.secudev.archi.repository.HistoryRepository;
import net.secudev.archi.service.event.DomainEvent;

@Component
@Slf4j
public class EventsHandler {
	
	@Autowired
	private HistoryRepository histories;

	
	@EventListener
	@Async
	public void onApplicationEvent(DomainEvent event) {
		log.info("Publishing TO External SYSTEMs -- "+ event.getMessage());
		//Event Storage
		History history = new History();
		history.setCreationTime(LocalDateTime.now());
		history.setEvent(event.getMessage());
		histories.save(history);
		
		
		//Send message to any external system like other web services,email, log, sms, Message Queue etc
	}
}
