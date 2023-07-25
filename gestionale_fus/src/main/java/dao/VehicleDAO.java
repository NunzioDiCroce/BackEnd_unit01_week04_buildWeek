package dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.Daily;
import entities.Route;
import entities.Ticket;
import entities.Vehicle;
import enums.VehicleType;

public class VehicleDAO {

	private final EntityManager em;

	public VehicleDAO(EntityManager em) {
		this.em = em;
	}

	// save vehicle
	public void saveVehicle(Vehicle vehicle) {

		EntityTransaction t = em.getTransaction();

		try {
			t.begin();

			em.persist(vehicle);

			t.commit();

			TypedQuery<Long> query = em.createQuery("SELECT MAX (id) FROM Vehicle", Long.class);
			long id = query.getSingleResult();
			System.out.printf(((vehicle.getType().equals(VehicleType.Bus)) ? "Bus salvato con successo" : "Tram salvato con successo") + " con id : %d\n", id);

		} catch (Exception ex) {
			t.rollback();
			System.err.println("Errore durante il salvataggio del veicolo: " + ex.getMessage());
		}
	}

	public Vehicle findById(long _id) {
		Vehicle v = em.find(Vehicle.class, _id);
		return v;
	}

	// validate daily ticket
	public void validateDaily(Daily daily, Vehicle vehicle, LocalDate obliterateDate) {

		if (daily.getObliterateDate() == null) {

			if (daily.getExpiryDate() != obliterateDate && daily.getExpiryDate().isAfter(obliterateDate)) {

				EntityTransaction t = em.getTransaction();
				try {
					t.begin();
					Query q = em.createQuery("UPDATE Daily d SET d.obliterateDate = :obliterateDate, d.vehicle = :vehicle WHERE d.id = :id");
					q.setParameter("obliterateDate", obliterateDate);
					q.setParameter("vehicle", vehicle);
					q.setParameter("id", daily.getId());

					q.executeUpdate();

					t.commit();
					System.out.println("Biglietto timbrato con successo");
				} catch (Exception ex) {
					t.rollback();
					System.err.println("Errore durante la validazione del biglietto giornaliero: " + ex.getMessage());
				}

			} else {
				System.out.println("Biglietto scaduto");

			}
		} else {
			System.out.println("Biglietto già timbrato");
		}

	}

	// total obliterated daily ticket
	public int obliteratedDaily(Vehicle _vehicle) {

		try {
			TypedQuery<Ticket> query = em.createQuery(
					"SELECT t FROM Ticket t WHERE vehicle_id = :_vehicle AND obliterateDate IS NOT NULL",
					Ticket.class);
			query.setParameter("_vehicle", _vehicle.getId());

			List<Ticket> tickets = query.getResultList();

			return tickets.size();
			
		} catch (Exception ex) {
			System.err.println("Errore nell'estrazione del numero biglietti: " + ex.getMessage());
			return 0;
		}
	}

	// daily ticket between dates
	public int dailyBetweenDates(Vehicle _vehicle, LocalDate _startDate, LocalDate _endDate) {

		TypedQuery<Ticket> query = em.createQuery(
				"SELECT t FROM Ticket t WHERE t.vehicle = :_vehicle AND t.dataTimbratura BETWEEN :_startDate AND :_endDate",
				Ticket.class).setParameter("_vehicle", _vehicle).setParameter("_startDate", _startDate)
				.setParameter("_endDate", _endDate);

		try {
			List<Ticket> tickets = query.getResultList();
			return tickets.size();
			
		} catch (Exception ex) {
			System.err.println("Errore durante il conteggio dei biglietti giornalieri tra le date " + _startDate + " e "
					+ _endDate + " : " + ex.getMessage());
			return -1;
		}

	}

	// define route
	public void defineRoute(Vehicle vehicle, Route route) {
		
		EntityTransaction t = em.getTransaction();

		try {

				t.begin();

				Query query = em.createQuery("UPDATE Vehicle v SET v.route = :route WHERE v.id = :vehicleId");
				query.setParameter("route", route);
				query.setParameter("vehicleId", vehicle.getId());

				int routeUpdate = query.executeUpdate();

				t.commit();

			System.out.println(
					(vehicle.getRoute() == null) ? "Rotta assegnata con successo" : "Rotta modificata con successo");
			
			
		} catch (Exception ex) {
			System.err.println("Errore durante la definizione della rotta: " + ex.getMessage());
		}

	}
}
