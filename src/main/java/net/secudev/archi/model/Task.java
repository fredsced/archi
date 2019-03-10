package net.secudev.archi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	@NotBlank(message = "Task Name cannot be empty or null")
	@Size(min = 2, max = 100, message = "Task Name length must be beetween 2 and 100 chars")
	private String name;

	// pas encore géré coté service et controller, permet d'ordonner
	@Getter
	@Setter
	@Min(0)
	@javax.persistence.Column(name="rankOrder")//le mot clé order est réservé en sql
	private int order;

	@Getter
	@Setter
	@Min(1)
	@Max(10)
	private int priority = 1;

	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	@Setter	
	private Column column;

	public Task(String name) {
		this.setName(name);
	}

	public Task(String name, Column column) {
		this(name);
		this.setColumn(column);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Task))
			return false;

		return id != null && id.equals(((Task) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
