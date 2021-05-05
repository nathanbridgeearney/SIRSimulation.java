import ecs100.UI;

import java.awt.*;
import java.util.ArrayList;

public class Individuals {
    private static final Region BOUNDARY = new Region(10, 10, 900, 600);
    private final Location loc;
    private int infectTimer;
    private int recovTimer;
    double dir;
    public String condition;


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
        if (BOUNDARY.onBoundary(loc)) {
            dir -= 150 + (60 * Math.random());
        }
        for (Region r: SIRSimulation.quarantines) {
            if(r.onBoundary(loc)){
                UI.println("d");
                dir -= 150 + (60 * Math.random());
            }
        }
        loc.moveInDirection(2.5, dir, BOUNDARY);
        if (condition.equals("Healthy")) {
            UI.setColor(Color.green);
        } else if (condition.equals("Infected")) {
            UI.setColor(Color.red);
            recovTimer++;
        } else if (recovTimer > 10 || condition.equals("Recovered") ) {
            condition = "Recovered";
            UI.setColor(Color.blue);
            infectTimer++;
            if (infectTimer > 100){
                condition = "Healthy";
            }
        }
        UI.fillOval(this.getLocation().getX() - 4, this.getLocation().getY() - 4, 8, 8);

    }


    public void step() {
        if (condition.equals("Healthy")){
            if (infection()) {
                condition = "Infected";
            }
        }
    }


    public Location getLocation() {
        return this.loc;
    }

    public String getCondition()
    {
        return this.condition;
    }
}