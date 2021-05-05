import ecs100.UI;

import java.awt.*;

public class Events {
    public double x, y;
    public int size, radius, duration;

    public Events(double x, double y, int size, int radius, int duration){
        this.x = x;
        this.y = y;
        this.size = size;
        this.radius = radius;
        this.duration = duration;
    }

    public void draw(){
        UI.setColor(Color.blue);
        UI.fillOval(x-radius,y-radius,radius,radius);
    }


}

