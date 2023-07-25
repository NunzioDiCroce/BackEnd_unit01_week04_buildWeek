package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import entities.Reseller;
import entities.Ticket;

public class ResellerDAO {

	private final EntityManager em;

	public ResellerDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	// - - - - - - - - - - - - - - - - - - - - getResellerTicketsByTime
	public List<Ticket> getResellerTicketsByTime(Reseller _reseller, LocalDate _startDate, LocalDate _endDate) {

		TypedQuery<Ticket> query = em.createQuery(
				"SELECT t FROM Ticket t WHERE t.reseller = :_reseller AND t.issueDate >= :_startDate AND t.issueDate <= :_endDate",
				Ticket.class)
				.setParameter("_reseller", _reseller)
				.setParameter("_startDate", _startDate)
				.setParameter("_endDate", _endDate);
		try {
			return query.getResultList();
		} catch (Exception ex) {
			System.err.println("Errore durante la ricerca dei biglietti del rivenditore: " + ex.getMessage());
			return null;
		}
	}

	public Reseller findById(long _id) {
		Reseller ar = em.find(Reseller.class, _id);
		return ar;
	}

}
