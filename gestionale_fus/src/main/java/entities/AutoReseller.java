package entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import enums.AutoResellerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AutoReseller extends Reseller {

	// - - - - - - - - - - - - - - - - - - - - attributes
	@Enumerated(EnumType.STRING)
	private AutoResellerStatus status;

	public AutoReseller(String _name, AutoResellerStatus _status) {
		super(_name);
		this.status = _status;
	}

	@Override
	public String toString() {
		return "AutoReseller [" + this.getName() + ", " + status + "]";
	}
}
