package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.Route;
import entities.Trip;

public class RouteDAO {

	private final EntityManager em;

	public RouteDAO(EntityManager _entityManager) {
		this.em = _entityManager;
	}

	public void saveRoute(Route route) {
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.persist(route);
			et.commit();
			
			System.out.printf("La tratta %s è stata creata.\n", route.getRouteName());
		} catch (Exception ex) {
			et.rollback();
		}
	}

	public Route findByName(String routeName) {
		try {
			Route r = em.find(Route.class, routeName);
			return r;
		} catch (Exception ex) {
			System.err.println("Errore durante la ricerca del percorso: " + ex.getMessage());
			return null;
		}
	}
	
	public void avgUpdate(Route route) {
		
		EntityTransaction t = em.getTransaction();
		t.begin();
		
		TypedQuery<Trip> queryTrip = em.createQuery("SELECT t FROM Trip t WHERE routeName = :routeName", Trip.class);
		queryTrip.setParameter("routeName", route.getRouteName());
		
		List<Trip> trips = queryTrip.getResultList();
		
		int c = 0;
		int sum = 0;
		
		for(Trip tr: trips) {
			c++;
			sum += tr.getTripTime();
		}
		
		double avg = sum / c;
		
		Query q = em.createQuery("UPDATE Route r SET avgTime = :avgTime WHERE routeName = :routeName");
		q.setParameter("avgTime", avg);
		q.setParameter("routeName", route.getRouteName());

		q.executeUpdate();

		t.commit();
	}
}
