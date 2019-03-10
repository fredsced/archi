package net.secudev.archi.service.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("serial")
@Slf4j
public class DomainEvent extends ApplicationEvent{
	
	@Getter
	private String message;
		
	public DomainEvent(Object source, String message,TargetEntity target, EventAction action) {
		super(source);		
		this.message =target.name()+"\\" +action.name()+" : "+message;
		log.trace(this.message);		
	}
}

