package net.secudev.archi.service;

import java.util.List;

import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;

public interface IColumnService {
	
	Column createColumn(Column column) throws Exception;
	Column updateColumn(Column column) throws Exception;	
	Column findById(Long id);
	void removeColumn(Column column);
	void removeColumn(Long id);
	List<Column> getProjectColumns(Project project) throws Exception;
	List<Column> getProjectColumns(Long projectId) throws Exception;
}
