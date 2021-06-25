//Simon Mathiasen
//smath17@student.aau.dk

package Tests;

import Main.Board.Galaxy;
import Main.Board.Planet;
import Main.Board.SystemPiece;
import Main.Player;
import Main.Units.Carrier;
import Main.Units.Destroyer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Gennemgående for flere tests kommer jeg ikke ind i alle If-sætninger, men tager dem som dækker over mest
public class GalaxyTest {
    Player player1, player2;
    Destroyer destroyer1, destroyer2;
    Carrier carrier1, carrier2;
    Planet mecatol, moon, earth;
    SystemPiece system1, system2, system3, system4, system5, system6, system7;
    Galaxy galaxy;

    //Opsætning af ny galakse til hver test, for at sikre at de forskellige tests ikke påvirker hinanden
    @BeforeEach
    public void setupGalaxy() {
        player1 = new Player("Crassus", "The Emirates of Hacan", "blue");
        player2 = new Player("Pompey", "The Federation of Sol", "red");

        destroyer1 = new Destroyer(player1);
        destroyer2 = new Destroyer(player1);
        carrier1 = new Carrier(player1);
        carrier2 = new Carrier(player2);

        mecatol = new Planet("Mecatol Rex", 2);
        moon = new Planet("Moon", 0);
        earth = new Planet("Earth", 1);

        system1 = new SystemPiece("Carl", mecatol);
        system2 = new SystemPiece("Carl-johan", moon, earth);
        system3 = new SystemPiece("Moonface");
        system4 = new SystemPiece("Play");
        system5 = new SystemPiece("Dough");
        system6 = new SystemPiece("Heya");
        system7 = new SystemPiece("Deja");

        galaxy = new Galaxy(system2, system3, system4, system5, system6, system7, system1);
    }

    @Test
    public void printSpecificUnitAndAmountTest() {
        system2.addUnit(destroyer1);
        assertEquals("is a Destroyer", galaxy.printSpecificUnitAndAmount(Destroyer.class, system2.getTeam1Units()));
        system2.addUnit(destroyer2);
        assertEquals("are 2 Destroyers", galaxy.printSpecificUnitAndAmount(Destroyer.class, system2.getTeam1Units()));
    }

    @Test
    public void printUnitFormatTest() {
        system2.addUnit(destroyer1);
        system2.addUnit(carrier1);
        assertEquals("is a Carrier and a Destroyer", galaxy.printUnitFormat(system2.getTeam1Units()).toString());
    }

    @Test
    public void printSystemUnits() {
        system2.addUnit(destroyer1);
        system2.addUnit(destroyer2);
        system2.addUnit(carrier2);
        assertEquals("\u2202 In the Carl-johan system there are 2 Destroyers owned by the blue player, and there is a Carrier owned by the red player.\n", galaxy.printSystemUnits(system2));
    }

    @Test
    public void printSystemLocationAndPlanets() {
        assertEquals("\u2202 To the north-west is a system that contains the planets " + system2.getPlanets() + ".\n", galaxy.printSystemLocationAndPlanets(system2));
        assertEquals("\u2202 To the south is an empty system.\n", galaxy.printSystemLocationAndPlanets(system6));
    }

    @Test
    public void printPlayersInfoTest() {
        assertEquals("Crassus playing The Emirates of Hacan, Pompey playing The Federation of Sol. Crassus is blue, Pompey is red.", galaxy.printPlayersInfo(player1, player2));
    }


}
