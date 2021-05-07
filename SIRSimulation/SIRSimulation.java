// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

// Code for COMP-102-112 - 2021T1, Assignment 6_and_7
//* Name: Nathan Bridge-Earney
//* Username: BridgeNath1
//* ID:200565456


import ecs100.*;

import java.util.*;
import java.awt.Color;


/**
 * SIRSimulation
 * An individual-based simulation program for the SIR (Susceptible-Infected-Recovered) model for spread of diseases.
 * The program creates a list of Individual each with some location within a defined region.
 * The Individual move around in some random way, and may come in contact with each other.
 * An individual may be susceptible to the disease, infected, or recovered.
 * The simulation should start with all the Individual in the Susceptible state.
 * If an infected individual comes in contact with a susceptible individual, then with some probability,
 * the susceptible individual may become infected.
 * After some period of time, an infected individual will recover.
 * The simulation should run as a loop with each cycle of the loop representing one time step,
 * in which each individual moves, and may change their state, and then the whole set of Individual
 * are redrawn in their new positions and new states.
 * The program should let the user control the speed of the simulation (by changing the delay between time steps).
 * There are several numeric parameters controlling the simulation which the user should be able to
 * modify (eg, using sliders). The parameters include at least the following:
 * - the population size
 * - the distance between Individual at which they are considered "in contact"
 * - the probability that a susceptible individual will be infected if they are in contact
 * with an infected individual.
 * - the time that an infected individual takes to recover
 * The program should allow the user to place new infected Individual into the simulation (using the mouse).
 * The program should also allow the user to place "Events" in the simulation (using the mouse)
 * An event will attract all the Individual in its "attraction radius" to move to the event.
 * The Individual will stay at the event until the event is stopped; then they will leave in random directions.
 * The program should also allow the user to place quarantine regions in the simulation.
 * A quarantine region should be a rectangular region that Individual are not allowed to exit.
 * (They may or may not be prevented from entering them).
 * The user should be able to add multiple quarantine regions, and should be able to get rid of them.
 * <p>
 * There are lots of different possible movement behaviours of the Individual.
 * The more realistic the behaviour,  the more complicated it is to implement.
 * Some kind of random motion, where Individual move in the same direction
 * for some number of steps, then change to a different random direction is OK (except for
 * events, where they should head straight for the event).
 * It would be nice to have Individual moving mostly between home, work, and the shops,
 * with random other trips, but this is much more complicated.
 */
public class SIRSimulation {


    // Constants
    public static final Region BOUNDARY = new Region(10, 10, 900, 600);
    private static ArrayList<Individual> citizens = new ArrayList<>();
    public static ArrayList<Region> quarantines = new ArrayList<>();
    public static ArrayList<Events> events = new ArrayList<>();

    public static ArrayList<Individual> getCitizens() {
        return citizens;
    }

    public static ArrayList<Region> getQuarantines() {
        return quarantines;
    }

    public static ArrayList<Events> getEvents() {
        return events;
    }


    public static double infectRad;
    public static double infectProb;
    public static int infectTimer = 100;
    public static int eventDuration;

    public static int getInfectionTime() {
        return infectTimer;
    }

    boolean eventRunning = false;
    int stepTimer = 250 / 5;
    private static Location startLoc;
    boolean running = false;
    public static int stepCount = 0;
    char mAction;
    /*# YOUR CODE HERE */


    // Main
    public static void main(String[] arguments) {
        new SIRSimulation().setupGUI();
        UI.clearGraphics();
        BOUNDARY.draw(Color.black);
        quarantines.add(new Region(10, 10, 900, 600));

    }

    public void popCount(double popNum) {
        running = true;
        UI.clearGraphics();
        citizens.clear();
        for (int i = 0; i < popNum; i++) {
            citizens.add(new Individual());
        }
        while (running = true) {
            stepCount++;
            if (stepCount == 50) {
                stepCount = 0;
            }
            UI.clearGraphics();
            BOUNDARY.draw(Color.black);
            for (Region r : quarantines) r.draw(Color.BLACK);
            for (int i = 0; i < citizens.size(); i++) {
                Individual iF = citizens.get(i);
                iF.move();
                iF.step();
            }
            for (int e = 0; e < events.size(); e++) {
                Events eF = events.get(e);
                if (eF.duration == 0) {
                    eventRunning = false;
                    events.remove(e);
                } else {
                    eventRunning = true;
                    eF.draw();
                }
            }

            UI.repaintGraphics();
            UI.sleep(stepTimer);
        }
    }

    public void reset() {
        quarantines.add(new Region(10, 10, 900, 600));
        running = false;
        UI.clearGraphics();
        citizens.clear();
        BOUNDARY.draw(Color.BLACK);
        UI.sleep(stepTimer);
    }

    public void doMouse(String action, double x, double y) {
        if (action.equals("pressed") && mAction == 'I') {
            citizens.add(new Individual(x, y));
        } else if (mAction == 'Q') {
            if (action.equals(("pressed"))) {
                startLoc = new Location(x, y);
            } else if (action.equals("released")) {
                quarantines.add(new Region(startLoc.getX(), startLoc.getY(), x, y));
                for (Region r : quarantines) r.draw(Color.BLACK);
            }
        } else if (mAction == 'E') {
            if (action.equals(("pressed"))) {
                startLoc = new Location(x, y);
                eventDur(100);
            } else if (action.equals("released")) {
                int rad = (int) (x - startLoc.getX());
                events.add(new Events(startLoc.getX(), startLoc.getY(), 100, rad, eventDuration));

            }
        }
    }


    private void removeQuarantine() {
        quarantines.clear();
        quarantines.add(new Region(10, 10, 900, 600));
    }

    public void eventDur(double v) {
        eventDuration = (int) v;
    }

    private void infectionProbability(double v) {
        infectProb = v * 0.01;
    }

    private void infectionDistance(double v) {
        infectRad = v;

    }

    private void infectionTime(double v) {
        infectTimer = (int) v;
    }


    public void setupGUI() {

        UI.addSlider("Population Size", 0, 1000, 1, this::popCount);
        UI.addButton("Reset", this::reset);
        UI.addButton("Add Infected", () -> mAction = 'I');
        UI.addButton("Event", () -> mAction = 'E');
        UI.addSlider("Event Duration", 0, 1000, 100, this::eventDur);
        UI.addButton("Quarantine", () -> mAction = 'Q');
        UI.addButton("Remove Quarantine", this::removeQuarantine);
        UI.addSlider("Infection Probability:", 0, 100, 0, this::infectionProbability);
        UI.addSlider("Infection Distance:", 1, 30, 15, this::infectionDistance);
        UI.addSlider("Infection Time:", 1, 500, 250, this::infectionTime);
        UI.addSlider("Simulation Speed", 1, 10, 5, (double v) -> stepTimer = (int) (250 / v));
        UI.addButton("quit", UI::quit);
        UI.setMouseListener(this::doMouse);


    }


}
