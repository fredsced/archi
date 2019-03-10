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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
//Changement de nom de table  sinon les commandes sql s'embrouille car ele mot colmun est réservé....
@Table(name="pcolumn")
public class Column {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	@NotBlank(message = "Column Name cannot be empty or null")
	@Size(min = 2, max = 30, message = "Column Name length must be beetween 2 and 30 chars")
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Task> tasks = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)	
	@Getter
	@Setter
	private Project project;
	
	// pas encore géré coté service et controller, permet d'ordonner
	@Getter
	@Setter
	@Min(0)
	@javax.persistence.Column(name="rankOrder")//le mot clé order est réservé en sql
	private int order=0;

	public Column(String name) {
		this.setName(name);
	}
	
	public Column(String name, Project project) {
		this(name);
		this.setProject(project);
	}

	public void addTask(Task task) {
		this.tasks.add(task);
		task.setColumn(this);
	}

	public void removeTask(Task task) {
		this.tasks.remove(task);
		task.setColumn(null);
	}

	public List<Task> getTasks() {
		return new ArrayList<>(this.tasks);
	}
	
	public void removeAllTasks()
	{
		for(Task task : this.tasks)
		{
			removeTask(task);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Column))
			return false;

		return id != null && id.equals(((Column) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
