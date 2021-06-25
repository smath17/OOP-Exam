//Simon Mathiasen
//smath17@student.aau.dk

package Main.Board;

import Main.Player;
import Main.Units.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SystemPiece {
    private String name;
    private String location = "";
    private List<Planet> planets = new ArrayList<>();
    //Lister til at holde på units fra forskellige hold, Object type er ikke smart, men listen skal indeholder flere typer
    private List<Ship> team1Units = new ArrayList<>();
    private List<Ship> team2Units = new ArrayList<>();

    //Systemerne starter uden units
    //Systemet kan indeholde 0-3 planter
    public SystemPiece(String name) {
        this.name = name;
    }

    public SystemPiece(String name, Planet planet1) {
        this.name = name;
        this.planets.add(planet1);
    }

    public SystemPiece(String name, Planet planet1, Planet planet2) {
        this.name = name;
        this.planets.add(planet1);
        this.planets.add(planet2);
    }

    public SystemPiece(String name, Planet planet1, Planet planet2, Planet planet3) {
        this.name = name;
        this.planets.add(planet1);
        this.planets.add(planet2);
        this.planets.add(planet3);
    }

    public void setPlanet(Planet planet) {
        this.planets.add(planet);
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Ship> getTeam1Units() {
        if (team1Units.isEmpty() && !team2Units.isEmpty()) {
            team1Units.addAll(team2Units);
            team2Units.clear();
        }
        return team1Units;
    }

    public List<Ship> getTeam2Units() {
        return team2Units;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addUnit(Ship o) {
        if (team1Units.isEmpty()) {
            team1Units.add(o);
        } else if (o.getPlayer().equals(team1Units.get(0).getPlayer())) {
            team1Units.add(o);
        } else if (team2Units.isEmpty()) {
            team2Units.add(o);
        } else if (o.getPlayer().equals(team2Units.get(0).getPlayer())) {
            team2Units.add(o);
        } else {
            //Intet, der er kun plads til to spillere per system
        }

    }

    public void removeUnit(Ship o) {
        if (team1Units.contains(o)) {
            team1Units.remove(o);
        } else if (team2Units.contains(o)) {
            team2Units.remove(o);
        }
    }

    public List<Ship> retrieveUnitsAll() {
        List<Ship> unitsInSystem = new ArrayList<>();
        unitsInSystem.addAll(team1Units);
        unitsInSystem.addAll(team2Units);
        return unitsInSystem;
    }

    public void sortUnitListLowestCost() {
        // Lambda expression er brugt til at simplificere sorteringen, men vi har stadig parametre fra listen (o1, o2)
        // og kroppen -> () som returnerer int, hvilket bliver brugt i sort()
        team1Units.sort(((o1, o2) -> o1.getResource() - o2.getResource()));
        team2Units.sort(((o1, o2) -> o1.getResource() - o2.getResource()));
    }

    // Så længe der er units fra 2 spillere i systemet gentages funktionen
    public Player resovleCombat() {
        Random rand = new Random();

        while (getTeam1Units().size() >= 1 && getTeam2Units().size() >= 1) {
            int team1Hit = 0;
            int team2Hit = 0;
            //Tæl antallet af hits for hvert hold
            for (Ship unit : getTeam1Units()) {
                if (rand.nextInt(9 + 1) + 1 >= unit.getCombat()) { // 9 + 1, tæller op til og med 9, +1 starter fra 1 istedet for 0
                    team1Hit++;
                }
            }
            for (Ship unit : getTeam2Units()) {
                if (rand.nextInt(9 + 1) + 1 >= unit.getCombat()) {
                    team2Hit++;
                }
            }
            //sorter lister
            sortUnitListLowestCost();

            //Fjerner antal svarende til antal hits for begge hold
            //De fjernede units bliver automatisk fjernet fra galaksens liste når den opdateres (kaldes)
            //Units i listen bliver altid rykket ud til 0. position og derfor fjerner jeg fra index 0 flere gange
            for (int i = 0; i < team1Hit; i++) {
                if (!getTeam2Units().isEmpty()) {
                    getTeam2Units().remove(0);
                }
            }

            for (int i = 0; i < team2Hit; i++) {
                if (!getTeam1Units().isEmpty()) {
                    getTeam1Units().remove(0);
                }
            }

            //Vinderen bliver opdateret og spillerens units bliver lagt i Team1Units, hvor spilleren bliver returneret ud fra
            if (getTeam1Units().size() >= 1 && getTeam2Units().isEmpty()) {
                System.out.println("Winner is: " + getTeam1Units().get(0).getPlayer().toString());
                return getTeam1Units().get(0).getPlayer();
            } else if (getTeam1Units().isEmpty() && getTeam2Units().isEmpty()) {
                System.out.println("Draw");
                return null;
            }
        }
        throw new IllegalArgumentException("Team2 Won but wasn't updated");
    }
}
