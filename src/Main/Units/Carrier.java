//Simon Mathiasen
//smath17@student.aau.dk

package Main.Units;

import Main.Player;

public class Carrier extends Ship implements GetUnit {

    private int cost = 3;
    private int combat = 9;
    private int speed = 1;
    private int capacity = 6;
    private Player player;

    public Carrier(Player player) {
        this.player = player;
    }

    @Override
    public int getResource() {
        return cost;
    }

    @Override
    public int getCombat() {
        return combat;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Carrier";
    }
}
