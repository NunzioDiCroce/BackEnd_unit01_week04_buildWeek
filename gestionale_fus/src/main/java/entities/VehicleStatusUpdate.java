package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import enums.VehicleStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VehicleStatusUpdate {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private Vehicle vehicle;
	
	private LocalDate startDate;
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	private VehicleStatus maintenanceWork;

	public VehicleStatusUpdate(Vehicle vehicle, LocalDate startDate, LocalDate endDate, VehicleStatus maintenanceWork) {
		this.vehicle = vehicle;
		this.startDate = startDate;
		this.endDate = endDate;
		this.maintenanceWork = maintenanceWork;
	}

	@Override
	public String toString() {
		return "VehicleStatusUpdate [id=" + id + ", vehicleId=" + vehicle + ", start=" + startDate + ", end=" + endDate
				+ ", maintenanceWork=" + maintenanceWork + "]";
	}

}
