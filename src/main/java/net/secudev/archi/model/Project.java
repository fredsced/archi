package net.secudev.archi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;
	
	@Getter
	@Setter
	@NotBlank(message = "Project Name cannot be empty or null")
	@Size(min = 2, max = 30, message = "Project Name length must be beetween 2 and 30 chars")	
	@javax.persistence.Column(unique = true)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<@NotNull Column> columns = new HashSet<>();

	public Project(String name) {
		this.setName(name);	
	}

	public void addColumn(Column column) {
		this.columns.add(column);
		column.setProject(this);
	}

	public void removeColumn(Column column) {	
		this.columns.remove(column);		
		column.setProject(null);
	}

	public List<Column> getColumns() {
		return new ArrayList<>(this.columns);
	}

	@Override
	public String toString() {
		return "[ID:" + this.id + "],[Name:" + this.name + "]";
	}
}
