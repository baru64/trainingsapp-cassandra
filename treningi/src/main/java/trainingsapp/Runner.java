package trainingsapp;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trainingsapp.Simulation.SimulationStats;
import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;
import trainingsapp.backend.Reservation;
import trainingsapp.backend.Training;
import trainingsapp.backend.User;

public class Runner extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private BackendSession session;
    private int id;
    private Training training;
    private User user;
    private Client client;
    private SimulationStats stats;
    private long interval;

    public Runner(BackendSession session, int id, Training training, SimulationStats stats, long interval) {
        this.session = session;        
        this.id = id;
        this.training = training;
        this.user = new User("tester_" + id, 100000000+id);
        this.client = new Client(this.session, this.user);
        this.stats = stats;
        this.interval = interval;
    }

    public void run() {
        Reservation reservation = null;
        long startTime = System.currentTimeMillis();
        while (reservation == null) {
            try {
                reservation = this.client.makeReservation(this.training);
            } catch (BackendException e) {
                logger.info(this.id + " | Reservation failed: " + e.getMessage());
            }
        }
        long timeElapsed = System.currentTimeMillis() - startTime;
        logger.info(this.id + " | Reservation time: " + timeElapsed + "ms");
        ReservationStatus rs = null;
        for (int i = 0; i < 5; ++i) {
            startTime = System.currentTimeMillis();
            try {
                rs = this.client.getReservationStatus();
                timeElapsed = System.currentTimeMillis() - startTime;
                logger.info(String.format("%d | %s | 0-%d | accepted: %b reserve_list: %b reserve_list_pos: %d | query time: %d",
                    this.id, this.training.name, i, rs.isAccepted, rs.isOnReserveList, rs.reserveListPosition, timeElapsed
                ));
                if (rs.isAccepted) this.stats.accepted[i].incrementAndGet(); 
                if (rs.isOnReserveList) this.stats.reserveList[i].incrementAndGet(); 
            } catch (BackendException e) {
                logger.info(this.id + " | Can't get reservation state: " + e.getMessage());
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        Random random = new Random(startTime);
        if (random.nextInt() % 4 == 3) {
            try {
                this.client.cancelReservation();
                stats.cancelled.incrementAndGet();
                return;
            } catch (BackendException e) {
                logger.error(this.id + " | Cancel reservation failed: " + e.getMessage());
            }
        }
        // if not accepted or on reserve list wait or cancel reservation
        // if accepted and not on reserve list cancel 10%
        for (int i = 0; i < 5; ++i) {
            startTime = System.currentTimeMillis();
            try {
                rs = this.client.getReservationStatus();
                timeElapsed = System.currentTimeMillis() - startTime;
                logger.info(String.format("%d | %s | 1-%d | accepted: %b reserve_list: %b reserve_list_pos: %d | query time: %d",
                    this.id, this.training.name, i, rs.isAccepted, rs.isOnReserveList, rs.reserveListPosition, timeElapsed
                ));
                if (rs.isAccepted) this.stats.accepted[5+i].incrementAndGet(); 
                if (rs.isOnReserveList) this.stats.reserveList[5+i].incrementAndGet(); 
            } catch (BackendException e) {
                logger.info(this.id + " | Can't get reservation state: " + e.getMessage());
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    
}
