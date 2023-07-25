package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import entities.Vehicle;
import entities.VehicleStatusUpdate;

public class VehicleStatusUpdateDAO {

	private final EntityManager em;

	public VehicleStatusUpdateDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	// - - - - - - - - - - - - - - - - - - - - save
	public void save(VehicleStatusUpdate update) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(update);
			et.commit();

			System.out.println("Status aggiornato correttamente");
		} catch (Exception ex) {
			et.rollback();
			System.err.println("Errore durante il salvataggio dell'aggiornamento dello stato: " + ex.getMessage());
		}
	}

	// - - - - - - - - - - - - - - - - - - - - findById
	public VehicleStatusUpdate findById(long _id) {
		
		try {
			VehicleStatusUpdate du = em.find(VehicleStatusUpdate.class, _id);
			return du;
		} catch (Exception ex) {
			System.err.println(
					"Errore durante la ricerca dello stato del veicolo con ID " + _id + " : " + ex.getMessage());
			return null;
		}
	}

	// - - - - - - - - - - - - - - - - - - - - findByIdAndDelete
	public void findByIdAndDelete(long _id) {
		VehicleStatusUpdate du = em.find(VehicleStatusUpdate.class, _id);
		if (du != null) {
			EntityTransaction et = em.getTransaction();
			try {
				et.begin();
				em.remove(du);
				et.commit();
			} catch (Exception ex) {
				et.rollback();
				System.err.println(
						"Errore durante la rimozione dello stato del veicolo con ID " + _id + ": " + ex.getMessage());
			}

		} else
			System.out.println("Status non trovato");
	}

	public List<VehicleStatusUpdate> maintenanceHistory(Vehicle vehicle) {
		try {
		TypedQuery<VehicleStatusUpdate> query = em.createQuery(
				"SELECT v FROM VehicleStatusUpdate v WHERE v.vehicle = :_vehicle", VehicleStatusUpdate.class);
		query.setParameter("_vehicle", vehicle);

		return query.getResultList();
		} catch (Exception ex) {
			System.err.println("Errore durante il caricamento della cronologia delle manutenzione del mezzo: " + ex.getMessage());
			return null;
		}
	}
}