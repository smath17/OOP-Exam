//Simon Mathiasen
//smath17@student.aau.dk

package Main.Board;

import Main.IllegalGalaxyException;
import Main.Player;
import Main.Units.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Galaxy {
    private SystemPiece[][] galaxySystem = new SystemPiece[3][3];

    //Systembrikker placeres i to-dimensionelt array og bliver tildelt deres placering
    public Galaxy(SystemPiece north_west, SystemPiece north, SystemPiece north_east, SystemPiece south_west, SystemPiece south, SystemPiece south_east, SystemPiece center) {
        this.galaxySystem[0][0] = north_west;
        this.galaxySystem[0][1] = north;
        this.galaxySystem[0][2] = north_east;
        this.galaxySystem[1][0] = null;
        this.galaxySystem[1][1] = center;
        this.galaxySystem[1][2] = null;
        this.galaxySystem[2][0] = south_west;
        this.galaxySystem[2][1] = south;
        this.galaxySystem[2][2] = south_east;

        north_west.setLocation("north-west");
        north.setLocation("north");
        north_east.setLocation("north-east");
        south_west.setLocation("south-west");
        south.setLocation("south");
        south_east.setLocation("south-east");
        center.setLocation("center");
    }

    //Funktion til at gå igennem og returnere alle systemer i galaksen
    public List<SystemPiece> allSystems() {
        List<SystemPiece> systemsInGalaxy = new ArrayList<>();

        //Gennemgår hver kolonne i hver række (for 1 = række, for 2 = kolonne)
        for (SystemPiece[] rowGalaxySystem : galaxySystem) {
            for (int c = 0; c < galaxySystem.length; c++) {
                if (rowGalaxySystem[c] != null)
                    systemsInGalaxy.add(rowGalaxySystem[c]);
            }
        }
        return systemsInGalaxy;
    }

    public List<Planet> getGalaxyPlanets() {
        List<Planet> planetsInGalaxy = new ArrayList<>();
        for (SystemPiece system : allSystems()) {
            planetsInGalaxy.addAll(system.getPlanets());
        }
        return planetsInGalaxy;
    }

    //Opdateres når den kaldes
    public List<Ship> getGalaxyUnits() {
        List<Ship> unitsInGalaxy = new ArrayList<>();
        for (SystemPiece system : allSystems()) {
            unitsInGalaxy.addAll(system.retrieveUnitsAll());
        }
        return unitsInGalaxy;
    }


    //kunne være private men det gør det vanskeligt at teste
    public String printSpecificUnitAndAmount(Class unit, List<Ship> whichTeam) {
        int unitAmount = 0;
        String unitAmountAndName;
        for (Ship specificUnit : whichTeam) {
            if ((specificUnit.getClass()).equals(unit)) {
                unitAmount++;
            }
        }
        if (unitAmount == 1) {
            unitAmountAndName = "is a " + unit.getSimpleName();
        } else if (unitAmount > 1) {
            unitAmountAndName = "are " + String.valueOf(unitAmount) + " " + unit.getSimpleName() + "s"; //Cast unitAmount til string for at undgå matematisk forståelse
        } else {
            unitAmountAndName = ""; //Burde være null, men er tom for udskriften skyld
        }
        return unitAmountAndName;
    }

    //hjælpefunktion fjerner overflødige is/are
    private StringBuilder delIsAre(StringBuilder text) {
        if (!text.toString().equals("")) {
            if (text.charAt(0) == 'i') {
                for (int i = 0; i <= 2; i++)
                    text.deleteCharAt(0);
            } else if (text.charAt(0) == 'a') {
                for (int i = 0; i <= 3; i++)
                    text.deleteCharAt(0);
            }
        }
        return text;
    }

    //Hjælpefunktion til printSystemUnits som returnerer units i det rigtge format (antal og tilhørende spiller)
    public StringBuilder printUnitFormat(List<Ship> whichTeam) {
        StringBuilder carrierText = new StringBuilder(printSpecificUnitAndAmount(Carrier.class, whichTeam));
        StringBuilder destroyerText = new StringBuilder(printSpecificUnitAndAmount(Destroyer.class, whichTeam));
        StringBuilder dreadnoughtText = new StringBuilder(printSpecificUnitAndAmount(Dreadnought.class, whichTeam));
        StringBuilder cruiserText = new StringBuilder(printSpecificUnitAndAmount(Cruiser.class, whichTeam));

        //Flere IF's behøves ikke da rækkefølgen de printes i kendes
        if (!carrierText.toString().equals("") && (!destroyerText.toString().equals("") || !cruiserText.toString().equals("") || !dreadnoughtText.toString().equals(""))) {
            carrierText.append(" and ");
            destroyerText = delIsAre(destroyerText);
            cruiserText = delIsAre(cruiserText);
            dreadnoughtText = delIsAre(dreadnoughtText);

        }
        if (!destroyerText.toString().equals("") && (!dreadnoughtText.toString().equals("") || !cruiserText.toString().equals(""))) {
            destroyerText.append(" and ");
            dreadnoughtText = delIsAre(dreadnoughtText);
            cruiserText = delIsAre(cruiserText);
        }
        if (!dreadnoughtText.toString().equals("") && !cruiserText.toString().equals("")) {
            dreadnoughtText.append(" and ");
            cruiserText = delIsAre(cruiserText);
        }

        //Sæt strings sammen
        return carrierText.append(destroyerText).append(dreadnoughtText).append(cruiserText);
    }

    //Hjælpefunktion til printGalaxy som returnerer systemet og units i det
    public String printSystemUnits(SystemPiece system) {
        String systemUnitFormat;
        if (system.retrieveUnitsAll().isEmpty()) { //Ingen units i systemet
            systemUnitFormat = "";
            //\u2202 = unicode for punktliste
        } else if (system.getTeam1Units().size() >= 1 && system.getTeam2Units().size() >= 1) { //Begge hold har units i systemet
            systemUnitFormat = "\u2202 In the " + system.getName() + " system there " + printUnitFormat(system.getTeam1Units()) + " owned by the " + system.getTeam1Units().get(0).getPlayer().getColor() + " player,"
                    + " and there " + printUnitFormat(system.getTeam2Units()) + " owned by the " + system.getTeam2Units().get(0).getPlayer().getColor() + " player.\n";
        } else if (system.getTeam1Units().size() >= 1 && system.getTeam2Units().isEmpty()) { //Kun et hold med units i systemet (der behøves ikke at tjekke for hold to på samme måde, da Team1Units bliver brugt først og derefter team2)
            systemUnitFormat = "\u2202 In the " + system.getName() + " system there " + printUnitFormat(system.getTeam1Units()) + " owned by the " + system.getTeam1Units().get(0).getPlayer().getColor() + " player.\n";
        } else {
            throw new IllegalArgumentException("If-statements doesn't cover all possibilities");
        }
        return systemUnitFormat;
    }

    //Hjælpefunktion til at printe galaksen, funktionen formaterer en tekst baseret på systemet
    public String printSystemLocationAndPlanets(SystemPiece system) {
        String systemFormat;
        if (system.getPlanets().isEmpty()) {
            systemFormat = "\u2202 To the " + system.getLocation() + " is an empty system.\n";
        } else if (system.getLocation().equals("center")) {
            systemFormat = "\u2202 At the center is a system that contains the planet " + system.getPlanets() + ".\n";
        } else if (system.getPlanets().size() > 1) {
            systemFormat = "\u2202 To the " + system.getLocation() + " is a system that contains the planets " + system.getPlanets() + ".\n";
        } else
            systemFormat = "\u2202 To the " + system.getLocation() + " is a system that contains the planet " + system.getPlanets() + ".\n";
        return systemFormat;
    }


    //Hjælpefunktion til playerinfo
    private String playerNameRaceString(Player player) {
        return player.getName() + " playing " + player.getRace() + ", ";
    }

    //Hjælpefunktion til playerinfo
    private String playerNameColor(Player player) {
        return player.getName() + " is " + player.getColor() + ", ";
    }

    //Hjælpefunktion som lægger spiller info sammen i én string med Stringbuilder
    public String printPlayersInfo(Player... players) {
        String playerIntro;
        StringBuilder tempBuilder = new StringBuilder();
        for (Player player : players) {
            tempBuilder.append(playerNameRaceString(player));
        }
        tempBuilder.setCharAt(tempBuilder.length() - 2, '.');
        for (Player player : players) {
            tempBuilder.append(playerNameColor(player));
        }
        //fjerner overskydende separatorer (", ") og erstatter det med '.'
        tempBuilder.setCharAt(tempBuilder.length() - 2, '.');
        tempBuilder.deleteCharAt(tempBuilder.length() - 1);

        playerIntro = tempBuilder.toString();
        return playerIntro;
    }

    //Player... players = varargs som gør det muligt at have varierende antal spillere
    //En string med alle oplysninger om galaksen
    public String printGalaxy(int numberOfPlayers, Player... players) {
        return "The galaxy has " + numberOfPlayers + " players: " + printPlayersInfo(players) + " The galaxy has the following systems:\n"
                + printSystemLocationAndPlanets(galaxySystem[0][0]) + printSystemLocationAndPlanets(galaxySystem[0][1])
                + printSystemLocationAndPlanets(galaxySystem[0][2]) + printSystemLocationAndPlanets(galaxySystem[1][1])
                + printSystemLocationAndPlanets(galaxySystem[2][0]) + printSystemLocationAndPlanets(galaxySystem[2][1])
                + printSystemLocationAndPlanets(galaxySystem[2][2]) +
                "The galaxy contains the following ships:\n" + printSystemUnits(galaxySystem[0][0])
                + printSystemUnits(galaxySystem[0][1]) + printSystemUnits(galaxySystem[0][2])
                + printSystemUnits(galaxySystem[1][1]) + printSystemUnits(galaxySystem[2][0])
                + printSystemUnits(galaxySystem[2][1]) + printSystemUnits(galaxySystem[2][2]);
    }

    //hjælpefunktion tjekker om den samme planet er i mere end ét system
    public boolean samePlanetTwice() {
        List<Planet> planetchecklist = new ArrayList<>();
        for (Planet planetCurrent : getGalaxyPlanets()) {
            for (Planet planetCheckAgainst : planetchecklist) {
                if (planetCurrent.equals(planetCheckAgainst)) {
                    return true;
                } else
                    planetchecklist.add(planetCurrent);
            }
        }
        return false;
    }

    /* Funktionen tjekker om de to systemer ligger rigtigt i forhold til hinanden (rowAdd = 1 og colAdd = 1, for at få øst-vest og vest-øst relation)
    public boolean systemDirection(SystemPiece system1, SystemPiece system2, int rowAdd, int colAdd) {
        boolean system1Direction = false;
        boolean system2Direction = false;

        for (int r = 0; r < galaxySystem.length; r += (rowAdd)) {
            for (int c = 0; c < galaxySystem.length; c += (colAdd)) {
                if (galaxySystem[r][c].equals(system1)) {
                    system1Direction = true;
                }
                if (galaxySystem[r][c].equals(system2)) {
                    system2Direction = true;
                }
            }
        }
        if (system1Direction && system2Direction) {
            return true;
        }
        return false;
    }
    */

    //verificerer om en galakse er gyldig for at centersystemet kun har Mecatol planeten og at andre systemer maks har 3 planeter
    public void validateGalaxy() {
        if (galaxySystem[1][1].getPlanets().size() > 1) {
            if (galaxySystem[1][1].getPlanets().get(0).getName().equals("Mecatol Rex")) {
                throw new IllegalGalaxyException("Center system should contain Mecatol");
            }
            throw new IllegalGalaxyException("Too many planets in center system");
        }
        for (SystemPiece system : allSystems()) {
            if (system.getPlanets().size() > 3) {
                throw new IllegalGalaxyException("System contains more than 3 planets");
            }
        }
        // Den sidste del skulle tjekke at hvis [0][0] var nord for [1][0] så skulle den regne [1]-1[0] for at se hvad
        // der er nord for systemet, altså ved at trække en række fra, [0][0] og [1]-1[0] skal sammenlignes og være det samme ellers skal Exception kastes
        // det samme for andre retninger hvor øst west bliver udregninger i kolonnen og syd nord er rækken
        /* TODO: Match directions
        if (systemDirection(galaxySystem[0][0], )) {
            throw new IllegalGalaxyException();
        }
        */
    }

    //Finder alle units i galaksen tilhørende bestemt person og sorterer dem
    public List<Ship> playerAllUnitsSorted(Player player) {
        List<Ship> specificPlayerUnits = new ArrayList<>();
        for (Ship unit : getGalaxyUnits()) {
            if (unit.getPlayer().equals(player)) {
                specificPlayerUnits.add(unit);
            }
        }
        // Sorter efter laveste combatvalue, hvis de er lige sorter efter højeste resource
        // Lambda simplification for læsevenlighed og fordi denne sorteringfunktion kun bruges 1 gang, se mere under SystemPiece linje 107
        specificPlayerUnits.sort((o1, o2) -> {
            if (o2.getCombat() == o1.getCombat()) {
                return o2.getResource() - o1.getResource();
            } else
                return o1.getCombat() - o2.getCombat();
        });
        return specificPlayerUnits;
    }


    public StringBuilder playerControlsPlanets(Player... players) {
        StringBuilder playerAndPlanets = new StringBuilder();
        for (Player player : players) {
            playerAndPlanets.append("\n").append(player.toString()).append("\n");
            for (SystemPiece system : allSystems()) {
                if (!system.getTeam1Units().isEmpty() && system.getTeam2Units().isEmpty()) {
                    if (player.equals(system.getTeam1Units().get(0).getPlayer())) {
                        for (Planet planet : system.getPlanets()) {
                            playerAndPlanets.append("\t").append(planet).append("\n");
                        }
                    }
                }
            }
        }
        return playerAndPlanets;
    }

    public void systemPlanetControl(Player... players) {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Planet_control.txt"), "utf-8"))) {
            //Deler stringbuilderen op i mindre strings delt ved \n og laver derefter en newline ved hver ny string
            String[] lines = playerControlsPlanets(players).toString().split("\n");
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Galaxy constructRandom(Player... players) {
        Random rand = new Random();

        Planet centerPlanet = new Planet("Mecatol", 5);


        SystemPiece system1 = new SystemPiece("Center", centerPlanet);
        SystemPiece system2 = new SystemPiece("North");
        SystemPiece system3 = new SystemPiece("North-west");
        SystemPiece system4 = new SystemPiece("North-east");
        SystemPiece system5 = new SystemPiece("South");
        SystemPiece system6 = new SystemPiece("South-west");
        SystemPiece system7 = new SystemPiece("South-east");

        List<SystemPiece> systemList = new ArrayList<>();
        //ikke center systemet!
        systemList.add(system2);
        systemList.add(system3);
        systemList.add(system4);
        systemList.add(system5);
        systemList.add(system6);
        systemList.add(system7);


        //Tilføjer 0-3 planeter til hvert system
        for (SystemPiece system : systemList) {
            //Danner for hvert system (undtagen center) 0-3 planeter
            for (int i = 0; i < rand.nextInt(4); i++) {
                Planet planet = new Planet("Planet" + i, rand.nextInt(4 + 1) + 2);
                system.setPlanet(planet);
            }
            for (Player player : players) {
                //tilføjer 2 til 5 units til hvert system for hver spiller, så skulle der ihvertfald være 2 systemer med 2 units i :^)
                for (int i = 0; i <= rand.nextInt(4) + 2; i++) {
                    //Switch tager et tal mellem 1-4 som bestemmer typen af unit
                    switch (rand.nextInt(4)) {
                        case 0:
                            //Flere objekter hedder det samme, men har forskellig HashCode
                            Dreadnought dreadnought = new Dreadnought(player);
                            system.addUnit(dreadnought);
                            break;
                        case 1:
                            Destroyer destroyer = new Destroyer(player);
                            system.addUnit(destroyer);
                            break;
                        case 2:
                            Carrier carrier = new Carrier(player);
                            system.addUnit(carrier);
                            break;
                        case 3:
                            Cruiser cruiser = new Cruiser(player);
                            system.addUnit(cruiser);
                            break;
                    }
                }
            }
        }
        return new Galaxy(system2, system3, system4, system5, system6, system7, system1);
    }
}
