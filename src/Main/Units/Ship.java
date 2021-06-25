//Simon Mathiasen
//smath17@student.aau.dk

package Main.Units;

import Main.Player;

//Abstract class som dækker over alle slags units og de metoder de har fra GetUnit interfacet
//Bruges til at lave collection med flere slags units(skibe)
public abstract class Ship implements GetUnit {
    private int cost;
    private int combat;
    private int speed;
    private int capacity;
    private Player player;


}
