//Simon Mathiasen
//smath17@student.aau.dk

package Main.Units;

import Main.Player;

public interface GetUnit {

    //Getters for unit values
    public int getResource();

    public int getCombat();

    public int getSpeed();

    public int getCapacity();

    public Player getPlayer();
}
