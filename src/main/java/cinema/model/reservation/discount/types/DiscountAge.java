package cinema.model.reservation.discount.types;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.InvalidAgeException;
import cinema.model.reservation.discount.types.util.InvalidPercentageValueException;
import lombok.Getter;

/** BREVE DESCRIZIONE CLASSE DiscountAge  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sull'età
 *  degli spettatori che visioneranno il film
 */
@Getter
public class DiscountAge implements ReservationDiscountStrategy{

	
	/** ATTRIBUTI
	 * @param MIN_AGE	  Età al di sotto della quale lo sconto è valido
	 * @param MAX_AGE	  Età al di sopra della quale lo sconto è valido
	 * @param PERCENTAGE  Percentuale di sconto effettuata
	 */
	private int min_age = 5;
	private int max_age = 80;
	private double percentage = 0.15;
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		double totalPrice = 0;
		if (r.getNumberPeopleUntilMinAge() == 0) {
			if (r.getNumberPeopleOverMaxAge() == 0) {
				totalPrice += r.getProjection().getPrice().getAmount()*r.getNSeats();
				return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
			}
			else {
				totalPrice += r.getProjection().getPrice().getAmount()*r.getNumberPeopleOverMaxAge()*(1 - percentage);
			}
		}
		else {
			totalPrice += r.getProjection().getPrice().getAmount()*r.getNumberPeopleUntilMinAge()*(1 - percentage);
		}
		if (r.getNumberPeopleUntilMinAge() != 0) {
			if (r.getNumberPeopleOverMaxAge() != 0) {
				totalPrice += r.getProjection().getPrice().getAmount()*r.getNumberPeopleOverMaxAge()*(1 - percentage);
			}
		}
		totalPrice += (r.getNSeats() - r.getNumberPeopleOverMaxAge() - r.getNumberPeopleUntilMinAge()) * r.getProjection().getPrice().getAmount();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}
	
	
	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * @param min_age
	 * @throws InvalidMinAgeException 
	 */
	public void setMin_Age(int min_age) throws InvalidAgeException {
		if (min_age >= 0) {
			this.min_age = min_age;
		}
		else throw new InvalidAgeException();
	}
	
	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * @param max_age
	 * @throws InvalidMaxAgeException 
	 */
	public void setMax_Age(int max_age) throws InvalidAgeException {
		if (max_age >= 0) {
			this.max_age = max_age;
		}
		else throw new InvalidAgeException();
	}
	
	
	/**
	 * METODO per settare il nuovo sconto 
	 * @param f		      Percentuale di sconto da applicare
	 * @throws InvalidPercentageValueException 
	 */
	public void setPercentage(double d) throws InvalidPercentageValueException {
		if ((d <= 0) || (d >= 1)){
			throw new InvalidPercentageValueException();
		}
		else percentage = d;
	}
}