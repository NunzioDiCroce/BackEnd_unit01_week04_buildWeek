package entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import enums.VehicleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

	@Id
	@GeneratedValue
	private long id;

	@Enumerated(EnumType.STRING)
	private VehicleType type;

	@OneToMany
	private Set<Daily> dailies;

	@OneToMany
	private Set<VehicleStatusUpdate> history;

	private int capacity;

	@ManyToOne
	private Route route;

	public Vehicle(VehicleType type) {
		this.capacity = (type.equals(VehicleType.Bus)) ? 30 : 50;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", type=" + type + ", dailies=" + dailies + ", history=" + history + ", capacity="
				+ capacity + ", route=" + route + "]";
	}

	
}
