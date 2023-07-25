package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import entities.AuthorizedReseller;

public class AuthorizedResellerDAO {

	private final EntityManager em;

	public AuthorizedResellerDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	public void save(AuthorizedReseller _authorizedReseller) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(_authorizedReseller);
			et.commit();
			
			TypedQuery<Long> query = em.createQuery("SELECT MAX (id) FROM Reseller", Long.class);
			long id = query.getSingleResult();
			System.out.printf("Il rivenditore '%s' è stato autorizzato con id: %d\n", _authorizedReseller.getName(), id);
		} catch (Exception ex) {
			et.rollback();
		}

	}

	public AuthorizedReseller findById(long _id) {
		
		try {
			AuthorizedReseller dar = em.find(AuthorizedReseller.class, _id);
			return dar;
		} catch (Exception ex) {
			System.err.println("Errore durante la ricerca del rivenditore autorizzato per ID: " + ex.getMessage());
			return null;
		}
	}

	public void findByIdAndDelete(long _id) {
		EntityTransaction et = em.getTransaction();

		try {
			AuthorizedReseller dar = em.find(AuthorizedReseller.class, _id);
			if (dar != null) {
				et.begin();
				em.remove(dar);
				et.commit();

			} else {
				System.err.println("Rivenditore autorizzato non trovato");
			}
		} catch (Exception ex) {
			et.rollback();
			System.err.println("Errore durante la rimozione del rivenditore autorizzato:" + ex.getMessage());
		}
	}
}
