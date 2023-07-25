package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import entities.Trip;

public class TripDAO {

	private final EntityManager em;

	public TripDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	public void save(Trip trip) {
		EntityTransaction et = em.getTransaction();

		try {
		et.begin();
		em.persist(trip);
		et.commit();
		System.out.println("Trip salvato correttamente");

		} catch (Exception ex) {
			et.rollback();
			System.err.println("Errore durante il salvataggio del viaggio: " + ex.getMessage());
		}
		
	}

}
