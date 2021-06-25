//Simon Mathiasen
//smath17@student.aau.dk

package Main;

import java.util.Objects;

public class Player {
    private String name;
    private String race;
    private String color;

    public Player(String name, String race, String color) {
        this.name = name;
        this.race = race;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getName(), player.getName()) &&
                Objects.equals(getRace(), player.getRace()) &&
                Objects.equals(getColor(), player.getColor());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getRace(), getColor());
    }

    @Override
    public String toString() {
        return name + ", " + race + ", " + color;
    }
}
