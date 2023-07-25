package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.AutoReseller;

public class AutoResellerDAO {

	private final EntityManager em;

	public AutoResellerDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	public void save(AutoReseller _autoReseller) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(_autoReseller);
			et.commit();
			
			TypedQuery<Long> query = em.createQuery("SELECT MAX (id) FROM Reseller", Long.class);
			long id = query.getSingleResult();
			System.out.printf("Il distributore '%s' è stato creato con id: %d\n", _autoReseller.getName(), id);
		} catch (Exception ex) {
			et.rollback();
			System.err.println("Errore durante il salvataggio del distributore automatico:" + ex.getMessage());
		}
	}

	public AutoReseller findById(long _id) {
		AutoReseller dar = em.find(AutoReseller.class, _id);
		return dar;
	}

	public void findByIdAndDelete(long _id) {
		AutoReseller dar = em.find(AutoReseller.class, _id);
		if (dar != null) {
			EntityTransaction entityTransaction = em.getTransaction();
			try {
				entityTransaction.begin();
				em.remove(dar);
				entityTransaction.commit();
			} catch (Exception ex) {
				entityTransaction.rollback();
				System.err.println("Errore durante la rimozione del distributore automatico: " + ex.getMessage());
			}
		} else
			System.out.println("Distributore automatico non trovato");
	}

	public void changeStatus(long _id, String _status) {
		AutoReseller dar = em.find(AutoReseller.class, _id);
		if (dar != null) {
			EntityTransaction et = em.getTransaction();
			try {
				et.begin();

				Query query = em.createQuery("UPDATE AutoReseller a SET a.status = :_status WHERE a.id = :_id")
						.setParameter("_status", _status);

				query.executeUpdate();

				et.commit();
				System.out.println("Lo stato del distributore automatico è stato aggiornato con successo");

			} catch (Exception ex) {
				et.rollback();
				System.err.println(
						"Errore durante l'aggiornamento dello stato del distributore automatico: " + ex.getMessage());
			}
		} else
			System.out.println("Distributore automatico non trovato");
	}
}
