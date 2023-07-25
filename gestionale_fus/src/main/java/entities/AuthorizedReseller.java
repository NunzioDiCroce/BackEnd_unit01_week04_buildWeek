package entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthorizedReseller extends Reseller {

	public AuthorizedReseller(String _name) {super(_name);}

	@Override
	public String toString() {return "AuthorizedReseller [" + this.getName() + "]";}
}
