package net.secudev.archi.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.secudev.archi.dto.ColumnDTO;
import net.secudev.archi.model.Column;

@Component
public class ColumnDTOConverter{

	@Autowired
	ModelMapper modelMapper;
	
	public Column columnDtoToColumn(ColumnDTO dto) {		
		return modelMapper.map(dto, Column.class);
	}

	public ColumnDTO columnToColumnDto(Column entity) {	
		return modelMapper.map(entity, ColumnDTO.class);
	}

	public List<ColumnDTO> columnListToColumnDtoList(List<Column> columns) {
		return columns.stream().map(column -> columnToColumnDto(column)).collect(Collectors.toList());
	}
}
