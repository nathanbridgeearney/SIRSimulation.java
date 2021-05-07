import ecs100.UI;

import javax.swing.*;
import java.awt.*;

public class Events {
    public double x, y;
    public int size, radius, duration;
    Color eventColor = new Color(255, 255, 0, 159);
    Location eventLocation;


    public Events(double x, double y, int size, int radius, int duration) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.radius = radius;
        this.duration = duration;
        eventLocation = new Location(x, y);
    }

    public void draw() {
        UI.setColor(eventColor);
        UI.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        duration--;

    }

}


