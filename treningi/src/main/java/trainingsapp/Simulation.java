package trainingsapp;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;
import trainingsapp.backend.Training;

class Simulation{

    class SimulationStats {
        public AtomicInteger[] accepted;
        public AtomicInteger[] reserveList;
        public AtomicInteger cancelled;

        public SimulationStats() {
            this.accepted = new AtomicInteger[10];
            this.reserveList = new AtomicInteger[10];
            this.cancelled = new AtomicInteger();
        }

        public void print() {
            String acceptedStr = new String("Accepted");
            String reserveStr = new String("Reserve");
            for (int i = 0; i < 10; ++i) {
                acceptedStr += " > " + this.accepted[i].get();
                reserveStr += " > " + this.reserveList[i].get();
            }
            System.out.printf("%s\n%s\nCancelled: %d\n", acceptedStr, reserveStr, this.cancelled.get());
        }
    }

    public BackendSession session;

    public Simulation(BackendSession session) {
        this.session = session;
    }

	public void start(String[] args){
        Training t1 = null;
        Training t2 = null;
        try {
		    t1 = session.trainingController.createTraining("yoga", 1);
		    t2 = session.trainingController.createTraining("crossfit", 1);
		    session.roomController.createRoom(15);
		    session.roomController.createRoom(10);
        } catch (BackendException e) {
            System.out.println("Could not perform a query. " + e.getMessage() + ".");
            return;
        }
        int numberOfThreads = 35;
        if (args.length >= 2) {
		    numberOfThreads = Integer.parseInt(args[1]);
        }
        LinkedList<Runner> runners = new LinkedList<>();
        SimulationStats s1 = new SimulationStats();
        SimulationStats s2 = new SimulationStats();
		for(int i=0; i < numberOfThreads; i++){
            Runner runner = null;
            if (i%2 == 0) runner = new Runner(session, i, t1, s1, 10);
            else runner = new Runner(session, i, t2, s2, 10);
            runner.start();
            runners.add(runner);
        }
        try {
		    for(Runner r : runners){
                r.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
