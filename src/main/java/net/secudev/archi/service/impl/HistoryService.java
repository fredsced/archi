package net.secudev.archi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.secudev.archi.model.History;
import net.secudev.archi.repository.HistoryRepository;
import net.secudev.archi.service.IHistoryService;

@Service
public class HistoryService implements IHistoryService{

	@Autowired
	private HistoryRepository history;

	@Override
	public List<History> getHistory() {
		return history.findAll();
	}
}
