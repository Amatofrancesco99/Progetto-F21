package cinema.model.projection;

import java.time.LocalDateTime;
import java.util.ArrayList;

import cinema.model.money.Money;
import cinema.model.Movie;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;
import lombok.*;


/** BREVE DESCRIZIONE CLASSE Projection
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe comprende tutte le informazioni e metodi
 * che servono per rappresentare una proiezione che viene
 * effettuata dal cinema.
 */
@Data
@AllArgsConstructor
public class Projection implements Comparable<Projection> {
	
	
	/** ATTRIBUTI
	 * @param id			Id
	 * @param movie			Film associato
	 * @param room			Sala in cui il film verrà proiettato
	 * @param dateTime		Data e ora
	 * @param price			Prezzo
	 * @param seats			Posti della sala in cui il film è proiettato
	 */
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime dateTime;
	private Money price;
	private ArrayList<ArrayList<ProjectionSeat>> seats;
	
	
	/**
	 * COSTRUTTORE 
	 * @param id
	 * @param movie
	 * @param dateTime
	 * @param price
	 * @param room
	 */
	public Projection(int id, Movie movie, LocalDateTime dateTime, Money price, Room room) {
		this.id = id;
		this.movie = movie;
		this.dateTime = dateTime;
		this.price = price;
		this.room = room;
		this.seats = new ArrayList<ArrayList<ProjectionSeat>>();
		for(int i = 0; i < room.getNumberRows(); i++) {
			ArrayList<ProjectionSeat> row = new ArrayList<ProjectionSeat>();
			for(int j = 0; j < room.getNumberCols(); j++) {
				row.add(new ProjectionSeat(room.getSeat(i, j), true));
			}
			seats.add(row);
		}
	}
	
	
	/**
	 * METODO che serve per verificare se un posto specifico
	 * è libero.
	 * @param row, col		Coordinate 
	 * @return				True: libero, False: occupato
	 */
	public boolean verifyIfSeatAvailable(int row, int col) {
		return seats.get(row).get(col).isAvailable();
	}
	
	
	/**
	 * METODO occupa posto della sala in cui è fatta la proiezione
	 * @param row, col		Coordinate 
	 * @return esito 		Esito occupazione del posto
	 */
	public boolean takeSeat(int row, int col) {
			if(verifyIfSeatAvailable(row, col)) {
				seats.get(row).get(col).setAvailable(false);
				return true;
			}
			return false;		
	}
	
	
	/**
	 * METODO per liberare il posto di una sala
	 * @param row, col		Coordinate 
	 * @return esito 		Esito rilascio del posto
	 */
	public boolean freeSeat(int row, int col) {
		if(!verifyIfSeatAvailable(row, col)) {
			seats.get(row).get(col).setAvailable(true);
			return true;
		}
		return false;
	}
	
	
	/**
	 * METODO per restituire un posto, date le coordinate
	 * @param row, col		Coordinate 
	 * @return 		 		Posto fisico
	 */
	public PhysicalSeat getPhysicalSeat(int row, int col) {
		return this.getSeats().get(row).get(col).getPhysicalSeat();
	}
	
	
	/**
	 * METODO per farsi dare le coordinate di un posto
	 * @param s			Posto fisico
	 * @return			Coordinate
	 */
	public String getSeatCoordinates(PhysicalSeat s) {
		for(int i=0; i < room.getNumberRows(); i++) {
			for(int j=0; j < room.getNumberCols(); j++) {
				if(getPhysicalSeat(i,j) == s)
					return "Fila: " + getCharFromNumber(i) + "\t\t\tPosto: " + j;		
			}
		}
		return null;		
	}
	
	/**
	 * METODO per convertire un numero in un carattere
	 * 
	 */
	private String getCharFromNumber(int i) {
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	    if ((i > 25)||(i < 0)) {
	        return null;
	    }
	    return Character.toString(alphabet[i]);
	}
	
	
	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
}