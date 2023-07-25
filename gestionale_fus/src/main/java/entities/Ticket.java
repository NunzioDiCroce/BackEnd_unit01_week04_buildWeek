package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor

public abstract class Ticket {

	@Id
	@GeneratedValue
	private long id;

	private LocalDate issueDate;

	@ManyToOne
	private Reseller reseller;

	public Ticket(LocalDate _issueDate, Reseller _reseller) {
		this.issueDate = _issueDate;
		this.reseller = _reseller;
	}

	@Override
	public String toString() {
		return "Ticket [ID Biglietto =" + id + ", data di emissione =" + issueDate + ", Rivenditore = " + reseller;
	}

}
