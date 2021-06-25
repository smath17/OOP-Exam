//Simon Mathiasen
//smath17@student.aau.dk

package Main;

import Main.Board.Galaxy;
import Main.Board.Planet;
import Main.Board.SystemPiece;
import Main.Units.Carrier;
import Main.Units.Destroyer;
import Main.Units.Dreadnought;

public class Main {

    public static void main(String[] args) {
        Player simon = new Player("Simon", "Human", "Red");
        Player coral = new Player("Carl", "Elk", "Blue");

        Destroyer destroyer1 = new Destroyer(simon);
        Destroyer destroyer2 = new Destroyer(coral);
        Carrier carrier1 = new Carrier(simon);
        Dreadnought dreadnought1 = new Dreadnought(simon);
        Dreadnought dreadnought2 = new Dreadnought(coral);
        Dreadnought dreadnought3 = new Dreadnought(coral);

        Planet moon = new Planet("Moon", 2);
        Planet jupiter = new Planet("Jupiter", 3);
        Planet mecatol = new Planet("Mecatol Rex", 5);

        SystemPiece system1 = new SystemPiece("Carl", moon);
        SystemPiece system2 = new SystemPiece("Carl-johan", jupiter);
        SystemPiece system3 = new SystemPiece("Moonface");
        SystemPiece system4 = new SystemPiece("Play");
        SystemPiece system5 = new SystemPiece("Dough");
        SystemPiece system6 = new SystemPiece("Heya");
        SystemPiece system7 = new SystemPiece("Center", mecatol);

        system1.addUnit(destroyer1);
        system1.addUnit(dreadnought1);
        system1.addUnit(destroyer2);
        system2.addUnit(carrier1);
        system1.addUnit(dreadnought2);
        system1.addUnit(dreadnought3);


        System.out.println("Print spiller: " + simon);
        system1.retrieveUnitsAll();
        Galaxy galax = new Galaxy(system1, system2, system3, system4, system5, system6, system7);
        system1.resovleCombat();
        galax.allSystems();
        galax.playerAllUnitsSorted(simon);
        galax.playerAllUnitsSorted(coral);
        System.out.println(galax.printGalaxy(2, simon, coral));
        galax.validateGalaxy();

        Galaxy galaxRand = Galaxy.constructRandom(simon, coral);
        System.out.println(galaxRand.printGalaxy(2, simon, coral));
        galaxRand.playerAllUnitsSorted(simon);
        galaxRand.validateGalaxy();
        galaxRand.systemPlanetControl(simon, coral); //Ejer ingen planeter fordi der er units fra flere hold i alle systemer
    }
}
