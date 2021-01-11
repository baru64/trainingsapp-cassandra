package trainingsapp;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;
import trainingsapp.backend.Training;
import trainingsapp.backend.User;

class Cli {
	public BackendSession session;
	private final Scanner input = new Scanner(System.in);
	
	public Cli(BackendSession session) {
		this.session = session;
    }

	public void cmdLoop(){
        LinkedList<Training> trainings = null;
        try {
            trainings = session.trainingController.selectAllTrainings();
        } catch (BackendException e) {
            System.out.println("Could not perform a query. " + e.getMessage() + ".");
            return;
        }
		for (int i = 0; i < trainings.size(); ++i) {
			System.out.println(
				i + ") ID: "+ trainings.get(i).trainingId + " | nazwa: " + trainings.get(i).name +
				" | czas: " + trainings.get(i).timeslot
			);
		}
		System.out.println("Wprowadz swoje imie i nazwisko");
		String name = input.nextLine();
		User user = new User(name, 123456789);
        Client client = new Client(session, user);
		while (true) {
			System.out.println(
				"Wybierz co chcesz zrobić:\n1 - zrób rezerwacje\n2 - anuluj rezerwacje\n3 - sprawdź rezerwacje\n4 - wyjdź\n"
			);
			int op = input.nextInt();
			switch(op) {
				case 1:
					System.out.println("Wprowadz numer zajęć na które chcesz zrobić rezerwację");
					int trainingNum = input.nextInt();
					try {
						client.makeReservation(trainings.get(trainingNum));
        			} catch (BackendException e) {
						System.out.println("Could not perform a query. " + e.getMessage() + ".");
						break;
        			}
					break;
				case 2:
					try {
						client.cancelReservation();
        			} catch (BackendException e) {
						System.out.println("Could not perform a query. " + e.getMessage() + ".");
						break;
        			}
					break;
				case 3:
					ReservationStatus reservationStatus = null;
					try {
						reservationStatus = client.getReservationStatus();
        			} catch (BackendException e) {
						System.out.println("Could not perform a query. " + e.getMessage() + ".");
						break;
        			}
					System.out.printf(
						"Akceptacja: %b\nLista rezerwowa: %b\nPozycja na liście: %d\n",
						reservationStatus.isAccepted, reservationStatus.isOnReserveList, reservationStatus.reserveListPosition
					);
					break;
				case 4:
					return;
				default:
					break;
			}
		}
	}
}
