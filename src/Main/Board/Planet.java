//Simon Mathiasen
//smath17@student.aau.dk

package Main.Board;

public class Planet {
    private String name;
    private int resourceProduction;

    //ResourceProduction skal være mellem 0 - 6
    public Planet(String name, int resourceProduction) {
        if (resourceProduction < 0 || 6 < resourceProduction)
            throw new IllegalArgumentException("Resource production must be 0 - 6");
        this.name = name;
        this.resourceProduction = resourceProduction;
    }

    public String getName() {
        return name;
    }

    public int getResourceProduction() {
        return resourceProduction;
    }

    @Override
    public String toString() {
        return name + " with " + resourceProduction + " resource";
    }

}
