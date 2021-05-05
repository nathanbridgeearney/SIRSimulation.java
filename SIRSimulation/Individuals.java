import ecs100.UI;

import java.awt.*;
import java.util.ArrayList;

public class Individuals {
    public static Region BOUNDARY = new Region(10, 10, 900, 600);
    private final Location loc;
    double dir;
    public String condition;
    private int infectTimer = 0;

    public Individuals() {
        loc = new Location(SIRSimulation.BOUNDARY);
        dir = Math.random() * 360;
        condition = "Healthy";
    }

    public Individuals(double Ix, double Iy) {
        loc = new Location(Ix, Iy);
        dir = Math.random() * 360;
        condition = "Infected";
    }

    public boolean infection() {
        ArrayList<Individuals> individuals = SIRSimulation.getCitizens();
        for (Individuals i : individuals)
            if (i.getCondition().equals("Infected") && Math.random() < SIRSimulation.infectProb)
                if (this.loc.distanceTo((i.getLocation())) < SIRSimulation.infectRad)
                    return true;
        return false;
    }

    public void move() {
        if (SIRSimulation.stepCount == 40) {
            dir += (int) (Math.random() * 360);
        }
        for (Region i : SIRSimulation.getQuarantines()) {
            if (i.contains(loc)) {
                BOUNDARY = new Region(i.west, i.north, i.east, i.south);
                if (BOUNDARY.onBoundary(loc)) {
                    dir = dir + 180;
                }
            }
        }
        loc.moveInDirection(2.5, dir, BOUNDARY);
        if (infectTimer == SIRSimulation.getInfectionTime() && condition.equals("Infected")) {
            condition = "Recovered";
            UI.setColor(Color.blue);
            infectTimer = 0;
        } else if (infectTimer == SIRSimulation.getInfectionTime() && condition.equals("Recovered")) {
            condition = "Healthy";
            infectTimer = 0;
        } else if (condition.equals("Recovered")) {
            UI.setColor(Color.blue);
            infectTimer++;
        } else if (condition.equals("Healthy")) {
            UI.setColor(Color.green);
        } else if (condition.equals("Infected")) {
            UI.setColor(Color.red);
            infectTimer++;
        }
        UI.fillOval(this.getLocation().getX() - 4, this.getLocation().getY() - 4, 8, 8);
    }

    public void step() {
        if (condition.equals("Healthy")) {
            if (infection()) {
                condition = "Infected";
            }
        }
    }

    public Location getLocation() {
        return this.loc;
    }

    public String getCondition() {
        return this.condition;
    }
}