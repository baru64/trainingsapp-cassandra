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
				i + ") ID: "+ trainings.get(i).trainingId + " | name: " + trainings.get(i).name +
				" | time: " + trainings.get(i).timeslot
			);
		}
		System.out.println("Enter username:");
		String name = input.nextLine();
		User user = new User(name, 123456789);
        Client client = new Client(session, user);
		while (true) {
			System.out.println(
				"Choose an action:\n1 - make reservation\n2 - cancel reservation\n3 - check reservation\n4 - exit\n"
			);
			int op = input.nextInt();
			switch(op) {
				case 1:
					System.out.println("Enter training number ");
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
						"Status:\nAccepted: %b\nOn reserve list: %b\nReserve list position: %d\n",
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
