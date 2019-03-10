package net.secudev.archi;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.secudev.archi.dto.ColumnDTO;
import net.secudev.archi.dto.converter.ColumnDTOConverter;
import net.secudev.archi.model.Column;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DTOConvertersTests {

	@Autowired
	ColumnDTOConverter columnDTOConverter;

	@Test
	public void testColumnConverter() {
		// Entity to DTO
		Column col = new Column("to do");
		col.setId(1L);
		assertTrue(columnDTOConverter.columnToColumnDto(col).getName().equals("to do"));
		assertTrue(columnDTOConverter.columnToColumnDto(col).getId() == 1L);

		// DTO to entity
		ColumnDTO colDto = new ColumnDTO();
		colDto.setName("to do");
		colDto.setId(1L);
		assertTrue(columnDTOConverter.columnDtoToColumn(colDto).getName().equals("to do"));
		assertTrue(columnDTOConverter.columnDtoToColumn(colDto).getId() == 1L);
	}

}
