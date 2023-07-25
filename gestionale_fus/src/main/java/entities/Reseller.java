package entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
public abstract class Reseller {

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany(mappedBy = "reseller")
	private Set<Ticket> tickets;
	
	private String name;

	public Reseller(String _name) {
		this.name = _name;
	}

	@Override
	public String toString() {
		return "Reseller [id=" + id + ", tickets=" + tickets + ", name=" + name + "]";
	}

	

}
