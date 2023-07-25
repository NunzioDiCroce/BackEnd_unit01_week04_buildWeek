package dao;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.Pass;
import entities.User;
import enums.SubscriptionType;

public class PassDAO {
	private final EntityManager em;

	public PassDAO(EntityManager em) {
		this.em = em;
	}

	public void savePass(Pass e) {
		EntityTransaction t = em.getTransaction();

		try {
			t.begin();

			em.persist(e);

			t.commit();
			TypedQuery<Long> query = em.createQuery("SELECT MAX (id) FROM Ticket", Long.class);
			long id = query.getSingleResult();
			System.out.printf("Tessera creata con id: %d\n",  id);
		} catch (Exception ex) {
			t.rollback();
			System.err.println("Errore durante la creazione della tessera: " + ex.getMessage());
		}
	}
	
	public Pass findById(long _id) {

		try {
			Pass pass = em.find(Pass.class, _id);
			return pass;
		} catch (Exception ex) {
			System.err.println("Errore durante il recupero dell'utente con ID: " + _id + ": " + ex.getMessage());
			return null;
		}
	}

	public Pass findPassByUserId(long id) {
		try {
			TypedQuery<Pass> query = em.createQuery("SELECT p FROM Pass p WHERE p.user.id = :id", Pass.class);
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			System.err.println("Errore durante la ricerca della tessera per l'utente: " + e.getMessage());
			return null;
		}
	}

	public void renewalPass(Pass pass) {
		if (pass != null) {
			if (pass.getExpiryDatePass().getYear() != LocalDate.now().getYear()) {

				try {
					long id = pass.getId();
					LocalDate data = LocalDate.of(LocalDate.now().getYear(), 12, 31);

					EntityTransaction t = em.getTransaction();
					t.begin();
					Query q = em.createQuery("UPDATE Pass p SET expiryDatePass = :expiryDatePass WHERE id = :id");
					q.setParameter("expiryDatePass", data);
					q.setParameter("id", id);

					q.executeUpdate();

					t.commit();
					System.out.println("Tessera rinnovata con successo!");

				} catch (Exception ex) {
					System.err.println("Errore durante il rinnovo della tessera: " + ex.getMessage());
				}
			} else {
				System.out.println("La tessera è ancora valida.");
			}
		} else {
			System.out.println("Tessera non trovata!");
		}
	}

	public void editSubscription(long passId, String type, LocalDate data) {
		
		EntityTransaction t = em.getTransaction();

			try {
		        t.begin();

		        Query query = em.createQuery("UPDATE Pass p SET p.subType = :subType, p.expireDateSub = :expireDateSub WHERE p.id = :passId");
		        query.setParameter("expireDateSub", (type.equals(SubscriptionType.Weekly.toString()))? LocalDate.now().plusWeeks(1) : LocalDate.now().plusMonths(1));
		        query.setParameter("subType", SubscriptionType.valueOf(type));
		        query.setParameter("passId", passId);

		        query.executeUpdate();

		        t.commit();

		        System.out.printf("Abbonamento aggiornato con successo a %s.\n", type);

			} catch (Exception ex) {
				t.rollback();
				System.err.println("Errore durante l'aggiornamento del tipo di abbonamento: " + ex.getMessage());
			}

	}

	public void checkSubType(Pass pass) {
		long id = pass.getId();
		SubscriptionType subType = pass.getSubType();

		try {
			TypedQuery<Long> query = em
					.createQuery("SELECT COUNT(p) FROM Pass p WHERE p.id = :id AND p.subType = :subType", Long.class);
			query.setParameter("id", id);
			query.setParameter("subType", subType);

			long result = query.getSingleResult();

			if (result > 0) {
				System.out.println("L'abbonamento " + subType + "é valido.");
			} else {
				System.out.println("L'abbonamento " + subType + ". Non è valido.");
			}
		} catch (Exception ex) {
			System.err.println("Errore durante il controllo dell'abbonamento: " + ex.getMessage());
		}
	}
}