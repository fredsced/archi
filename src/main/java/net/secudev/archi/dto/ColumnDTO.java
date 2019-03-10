package net.secudev.archi.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ColumnDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String name;
	@Getter @Setter
	private Long id;
	@Getter @Setter
	private Long projectId;
}
