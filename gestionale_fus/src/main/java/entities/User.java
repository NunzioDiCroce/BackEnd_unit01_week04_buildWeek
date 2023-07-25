package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

	// - - - - - - - - - - - - - - - - - - - - attributes
	@Id
	@GeneratedValue
	private long id;

	protected String name;
	protected String surname;

	@OneToOne
	protected Pass pass;

	protected LocalDate birthDate;
	protected String birthPlace;
	protected String password;

	public User(String _name, String _surname, String _password, LocalDate _birthDate, String _birthPlace) {
		this.name = _name;
		this.surname = _surname;
		this.password = _password;
		this.birthDate = _birthDate;
		this.birthPlace = _birthPlace;
	}
	
	@Override
	public String toString() {
		String passId = "none";
		if(pass != null) passId = Long.toString(pass.getId());
		return "Dati utente " + Long.toString(this.getId()) + " [" + name + ", " + surname + ", " + birthDate + ", " + birthPlace + ", Pass id: " + passId + "]";
	}

}
