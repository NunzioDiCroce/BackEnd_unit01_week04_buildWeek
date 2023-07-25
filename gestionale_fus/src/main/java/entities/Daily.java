package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Daily extends Ticket {
	
	@ManyToOne
	private Vehicle vehicle;

	private LocalDate obliterateDate = null;
	private LocalDate expiryDate;

	public Daily(LocalDate _issueDate, Reseller _reseller) {
		super(_issueDate, _reseller);
		this.expiryDate = _issueDate.plusDays(2);
	}

	@Override
	public String toString() {
		return "Biglietto giornaliero [ID Biglietto =" + this.getId() + ", Data di emissione =" + this.getIssueDate()
				+ ", Rivenditore = " + this.getReseller() + ", Data obliterazione =" + obliterateDate + "Data di scadenza ="
				+ expiryDate;
	}

}
