package app;

import java.time.LocalDate;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dao.AuthorizedResellerDAO;
import dao.AutoResellerDAO;
import dao.DailyDAO;
import dao.PassDAO;
import dao.ResellerDAO;
import dao.RouteDAO;
import dao.TripDAO;
import dao.UserDAO;
import dao.VehicleDAO;
import dao.VehicleStatusUpdateDAO;
import entities.AuthorizedReseller;
import entities.AutoReseller;
import entities.Daily;
import entities.Pass;
import entities.Reseller;
import entities.Route;
import entities.Trip;
import entities.User;
import entities.Vehicle;
import entities.VehicleStatusUpdate;
import enums.AutoResellerStatus;
import enums.VehicleStatus;
import enums.VehicleType;
import utils.JpaUtil;

public class GestionaleFus {

	private static EntityManagerFactory entityManagerFactory = JpaUtil.getEntityManagerFactory();

	public static void main(String[] args) {

		// entity manager 
		EntityManager em = entityManagerFactory.createEntityManager();
		
		// scanner
		Scanner scanner = new Scanner(System.in);
		
		// while flags variables
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		int c4 = 0;
		int c5 = 0;
		
		// MANAGED ENTITIES-----------------------------------------------------------------
		
		ResellerDAO resellerDAO = new ResellerDAO(em);
		AutoResellerDAO autoResellerDAO = new AutoResellerDAO(em);
		AuthorizedResellerDAO authorizedResellerDAO = new AuthorizedResellerDAO(em);
		DailyDAO dailyDAO = new DailyDAO(em);
		UserDAO userDAO = new UserDAO(em);
		PassDAO passDAO = new PassDAO(em);
		VehicleDAO vehicleDAO = new VehicleDAO(em);
		VehicleStatusUpdateDAO vehicleStatusUpdateDAO = new VehicleStatusUpdateDAO(em);
		RouteDAO routeDAO = new RouteDAO(em);
		TripDAO tripDAO = new TripDAO(em);
		
		// MAIN MENU------------------------------------------------------------------------
		
		do {
			System.out.println("-------------------------GESTIONALE F.U.S. TRASPORTI-------------------------\n");
			System.out.println("Selezionare tipo di profilo");
			System.out.println("1 - Utenti");
			System.out.println("2 - Admin\n");
			System.out.println("0 - Chiudi Programma");

			c1 = Integer.parseInt(scanner.nextLine());
			
			System.out.println();
			
			if(c1 != 0) {
				switch(c1) {
					case 1:
						
						System.out.println("Quale operazione vuoi effettuare?");
						System.out.println("1 - Crea nuovo utente");
						System.out.println("2 - Accedi");
						System.out.println("3 - Crea biglietto giornaliero");
						System.out.println("4 - Oblitera biglietto giornaliero\n");
						System.out.println("0 - Torna al menù");
						
						c2 = Integer.parseInt(scanner.nextLine());
						
						System.out.println();
						
						if(c2 != 0) {
							switch(c2) {
								case 1:
									// ---------------------------------------------------------------NEW USER
									System.out.print("Inserire nome nuovo utente: ");
									String _name = scanner.nextLine();
									System.out.print("Inserire cognome nuovo utente: ");
									String _surname = scanner.nextLine();
									System.out.print("Inserire nuova password: ");
									String _password = scanner.nextLine();
									System.out.print("Inserire data nascita (yyyy-mm-dd): ");
									String _birth = scanner.nextLine();
									System.out.print("Inserire luogo di nascita: ");
									String _birthPlace = scanner.nextLine();
									System.out.println();
									userDAO.save(new User(_name, _surname, _password, LocalDate.parse(_birth), _birthPlace));
									System.out.println();
									System.out.print("Premi invio per effettuare altre operazioni");
									scanner.nextLine();
									System.out.println("\n");
									break;
									
								case 2:
									// ---------------------------------------------------------------LOG IN USER
									System.out.print("Inserisci il tuo ID: ");
									long _idLog = Long.parseLong(scanner.nextLine());
									System.out.print("Inserisci la tua password: ");
									String _passwordLog = scanner.nextLine();
									
									System.out.println();
																		
									if(userDAO.findById(_idLog) != null && userDAO.findById(_idLog).getPassword().equals(_passwordLog)) {
										
										do {
											System.out.println("Quale operazione vuoi effettuare?");
											System.out.println("1 - Visualizza i tuoi dati");
											System.out.println("2 - Sottoscrivi tessera");
											System.out.println("3 - Verifica validità tessera");
											System.out.println("4 - Rinnova tessera");
											System.out.println("5 - Rinnova abbonamento");
											System.out.println("6 - Elimina profilo\n");
											System.out.println("0 - Log Out e torna al menù");
											
											c3 = Integer.parseInt(scanner.nextLine());
											
											System.out.println();
											
											if(c3 != 0) {
												switch(c3) {
													case 1:
														System.out.println(userDAO.findById(_idLog).toString());
														break;
													case 2:
														if(userDAO.findById(_idLog).getPass() == null) {
															System.out.print("Dove stai creando la tessera (id reseller): ");
															long _idReseller = Long.parseLong(scanner.nextLine());
															System.out.println();
															// pass creation
															Pass localPass = new Pass(LocalDate.now(), resellerDAO.findById(_idReseller), userDAO.findById(_idLog));
															passDAO.savePass(localPass);
															System.out.println();
															// assign to user
															userDAO.assignPass(userDAO.findById(_idLog).getId(), passDAO.findById(localPass.getId()));
															System.out.println();
														}else {
															if(userDAO.findById(_idLog).getPass().getExpiryDatePass().getYear() == LocalDate.now().getYear())
																System.err.println("Tessera già presente e in corso di validità.");
															else
																System.err.println("Tessera già presente, ma scaduta. Effettuare rinnovo.");
														}
							
														break;
													case 3:
														if(userDAO.findById(_idLog).getPass() != null)
															System.out.println((userDAO.findById(_idLog).getPass().getExpiryDatePass().getYear() == LocalDate.now().getYear()) ? "Tessera Valida" : "Tessera scaduta");
														else
															System.err.println("Nessuna tessera registrata presso questo utente.");
														break;
													case 4:
														System.out.println();
														if(userDAO.findById(_idLog).getPass() != null) {
															if(userDAO.findById(_idLog).getPass().getExpiryDatePass().getYear() == LocalDate.now().getYear()){
																passDAO.renewalPass(passDAO.findPassByUserId(_idLog));
															}else
																System.err.println("Tessera ancora valida, non è necessario rinnovare.");
														}else
															System.err.println("Nessuna tessera registrata presso questo utente.");
														
														break;
													case 5:
														if(userDAO.findById(_idLog).getPass() != null) {
															if(userDAO.findById(_idLog).getPass().getSubType() == null) {
																System.out.print("Scegliere il tipo di abbonamento da sottoscrivere (Weekly/Monthly): ");
																String sub = scanner.nextLine();
																passDAO.editSubscription(userDAO.findById(_idLog).getPass().getId(), sub, LocalDate.now());
																System.out.println();
															}else {
																System.err.printf("La tessera presenta un abbonamento %s in corso.\n", userDAO.findById(_idLog).getPass().getSubType().toString());
																System.err.println("Attendere la fine dell'abbonamento corrente per rinnovare.");
															}
														}else
															System.err.println("Nessuna tessera registrata presso questo utente.");
														
														
														break;
													case 6:
														userDAO.findByIdAndDelete(userDAO.findById(_idLog).getId());
														c3 = 0;
														break;
													default:
														System.out.println("Comando non valido.");
															
												}
											
											}
											
											
											System.out.println();
											System.out.print("Premi invio per effettuare altre operazioni");
											scanner.nextLine();
											System.out.println("\n");											
											
										}while(c3 != 0);
										
									}else {
										System.err.println("ID o password errati.\n");
										System.out.print("Premi invio per tornare al menù");
										scanner.nextLine();
										System.out.println("\n");
									}
									
								break;
								
								case 3:
									// ---------------------------------------------------------------CREATE DAILY TICKET
									System.out.print("Dove stai creando il biglietto: ");
									long _idReseller = Long.parseLong(scanner.nextLine());
									System.out.println();
									dailyDAO.createDailyTicket(new Daily(LocalDate.now(), resellerDAO.findById(_idReseller)));
									System.out.println();
									System.out.println();
									System.out.print("Premi invio per effettuare altre operazioni");
									scanner.nextLine();
									System.out.println("\n");
									break;
								
								
								case 4:
									System.out.print("Inserire id biglietto da validare: ");
									long _idDaily = Long.parseLong(scanner.nextLine());
									System.out.println();
									System.out.print("Inserire id veicolo avvenuta timbratura: ");
									long _idV = Long.parseLong(scanner.nextLine());
									vehicleDAO.validateDaily(dailyDAO.findById(_idDaily), vehicleDAO.findById(_idV), LocalDate.now());
									System.out.println();
									break;
								default:
									System.err.println("Comando non valido.");
									break;
							}
							
						}
						
						break;
				
					case 2:
						System.out.println("Scegli sezione");
						System.out.println("1 - Creazione");
						System.out.println("2 - Gestione\n");
						System.out.println("0 - Torna al menù");
						
						c4 = Integer.parseInt(scanner.nextLine());
						
						System.out.println();
						switch(c4) {
							case 1:
								System.out.println("Quale operazione vuoi effettuare?");
								System.out.println("1 - Crea reseller");
								System.out.println("2 - Crea mezzo");
								System.out.println("3 - Creare tratta");
								
								c5 = Integer.parseInt(scanner.nextLine());
								
								System.out.println();
								switch(c5) {
									case 1:
										int i1 = 0;

										System.out.println("Scegliere tipologia reseller");
										System.out.println("1 - Automatico");
										System.out.println("2 - Autorizzato");
										
										i1 = Integer.parseInt(scanner.nextLine());
										
										System.out.println();
										
										switch(i1) {
											case 1:
												System.out.print("Definire il nome del reseller: ");
												String _autoResellerName = scanner.nextLine();
												System.out.println();
												autoResellerDAO.save(new AutoReseller(_autoResellerName, AutoResellerStatus.ACTIVE));
												System.out.println();
												break;
											case 2:
												System.out.print("Definire il nome del reseller: ");
												String _resellerName = scanner.nextLine();
												System.out.println();
												authorizedResellerDAO.save(new AuthorizedReseller(_resellerName));
												System.out.println();
												break;
										}
										System.out.println();
										System.out.print("Premi invio per effettuare altre operazioni");
										scanner.nextLine();
										System.out.println("\n");	
										break;
									case 2:
										int i2 = 0;

										System.out.println("Scegliere tipologia di mezzo");
										System.out.println("1 - Bus");
										System.out.println("2 - Tram");
										
										i2 = Integer.parseInt(scanner.nextLine());
										
										System.out.println();
										
										switch(i2) {
											case 1:
												System.out.println();
												vehicleDAO.saveVehicle(new Vehicle(VehicleType.Bus));
												System.out.println();
												break;
											case 2:
												System.out.println();
												vehicleDAO.saveVehicle(new Vehicle(VehicleType.Tram));
												System.out.println();
												break;
										}
										System.out.println();
										System.out.print("Premi invio per effettuare altre operazioni");
										scanner.nextLine();
										System.out.println("\n");
										break;
									case 3:
										System.out.print("Inserire nome rotta: ");
										String routeName = scanner.nextLine();
										System.out.print("Inserire inizio tratta: ");
										String startName = scanner.nextLine();
										System.out.print("Inserire fine tratta: ");
										String terminalName = scanner.nextLine();
										System.out.println();
										routeDAO.saveRoute(new Route(routeName, startName, terminalName));
										System.out.println();
										break;
									default:
										System.err.println("Comando non valido.");
										break;
								}
								
								break;
							case 2:
								int dadegi = 0;
								do {
									System.out.println("Quale operazione vuoi effettuare?");
									System.out.println("1 - Stampa numero totale biglietti obliterati da un veicolo");
									System.out.println("2 - Stampa numero totale biglietti obliterati in un periodo");
									System.out.println("3 - Stampa numero biglietti e abbonamenti di un reseller in un periodo");
									System.out.println("4 - Aggiungi periodo di servizio / manutenzione");
									System.out.println("5 - Assegna tratta ad un veicolo");
									System.out.println("6 - Percorri tratta con un veicolo");
									System.out.println("0 - Torna al menù");
									
									dadegi = Integer.parseInt(scanner.nextLine());
									
									System.out.println();
									
									switch(dadegi) {
										case 1:
											System.out.print("Inserisci id veicolo: ");
											long _idVehicle = Long.parseLong(scanner.nextLine());
											System.out.println();
											System.out.printf("Biglietti obliterati dal veicolo selezionato: %d\n", vehicleDAO.obliteratedDaily(vehicleDAO.findById(_idVehicle)));
											System.out.println();
											break;
										case 2:
											System.out.print("Inserire data di inizio (yyyy-mm-dd): ");
											String _s = scanner.nextLine();
											System.out.print("Inserire data di fine (yyyy-mm-dd): ");
											String _e = scanner.nextLine();
											System.out.println();
											System.out.printf("Biglietti obliterati nel periodo selezionato: %d\n", dailyDAO.getTicketObliterated(LocalDate.parse(_s), LocalDate.parse(_e)));
											System.out.println();
											break;
										case 3:
											System.out.print("Seleziona reseller: ");
											long _idReseller = Long.parseLong(scanner.nextLine());
											System.out.println();
											System.out.print("Inserire data di inizio (yyyy-mm-dd): ");
											String _start = scanner.nextLine();
											System.out.print("Inserire data di fine (yyyy-mm-dd): ");
											String _end = scanner.nextLine();
											System.out.println();
											System.out.printf("Quantità biglietti e abbonamenti emessi da %s nel periodo selezionato: %d",
													resellerDAO.findById(_idReseller).getName(),resellerDAO.getResellerTicketsByTime(resellerDAO.findById(_idReseller),
															LocalDate.parse(_start), LocalDate.parse(_end)).size() );
											System.out.println("\n");
											System.out.print("Premi invio per effettuare altre operazioni");
											scanner.nextLine();
											System.out.println("\n");	
											break;
										case 4:
											System.out.print("Seleziona veicolo da id: ");
											long _idPippo = Long.parseLong(scanner.nextLine());
											System.out.print("Specifica tipologia (Service / Maintenance): ");
											String _t = scanner.nextLine();
											System.out.print("Inizio periodo: ");
											String _ss = scanner.nextLine();
											System.out.print("Fine periodo: ");
											String _se = scanner.nextLine();
											System.out.println();
											vehicleStatusUpdateDAO.save(new VehicleStatusUpdate(vehicleDAO.findById(_idPippo), LocalDate.parse(_ss), LocalDate.parse(_se), VehicleStatus.valueOf(_t)));											
											System.out.println();
											break;
										case 5:
											System.out.print("Inserisci id veicolo: ");
											long _idPaperino = Long.parseLong(scanner.nextLine());
											System.out.print("Inserisci nome tratta: ");
											String _nRoute = scanner.nextLine();
											System.out.println();
											vehicleDAO.defineRoute(vehicleDAO.findById(_idPaperino), routeDAO.findByName(_nRoute));
											System.out.println();
											break;
										case 6:
											System.out.print("Inserisci id veicolo: ");
											long _idPluto = Long.parseLong(scanner.nextLine());
											System.out.print("Tempo di percorrenza: ");
											int _tp = Integer.parseInt(scanner.nextLine());
											System.out.println();
											tripDAO.save(new Trip(vehicleDAO.findById(_idPluto), _tp));
											System.out.println();
											routeDAO.avgUpdate(vehicleDAO.findById(_idPluto).getRoute()); 
											break;
										default:
											System.err.println("Comando non valido.");
											break;
										
									}
									
									
								}while(dadegi != 0);
								
								
								break;
							default:
								System.err.println("Comando non valido.");
								break;
						}
						break;
					
					default:
						System.err.println("Comando non valido.");
						System.out.println();
						System.out.print("Premi invio per tornare al menù");
						scanner.nextLine();
						System.out.println("\n");
						break;	
				}
			}
		}while(c1 != 0);
		
		em.close();
		entityManagerFactory.close();
		
		// scanner close
		scanner.close();
		
		System.out.println("Pogramma chiuso con successo.");
		
		
	}

}
