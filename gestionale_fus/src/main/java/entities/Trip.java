package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trip {

	@Id
	@GeneratedValue
	private long id;

	private long idVehicle;

	private String routeName;

	private int tripTime;

	public Trip(Vehicle vehicle, int tripTime) {
		this.idVehicle = vehicle.getId();
		this.routeName = vehicle.getRoute().getRouteName();
		this.tripTime = tripTime;
	}

	@Override
	public String toString() {
		return "Trip [id=" + id + ", idVehicle=" + idVehicle + ", routeName=" + routeName + ", tripTime=" + tripTime
				+ "]";
	}

}
