package brain.pathfinding;

import grid.Location;

public class Square {

    private int value;
    private Location location;

    public Square(int val, Location loc){
        value = val;
        location = loc;
    }


    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
