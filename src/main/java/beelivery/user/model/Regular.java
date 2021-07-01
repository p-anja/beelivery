package beelivery.user.model;

import java.util.Date;

public class Regular extends User {

    // orders
    // cart
    private double points;
    private EType type;

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate, double points, EType type) {
        super(username, password, firstName, lastName, sex, birthDate);
        this.points = points;
        this.type = type;
    }

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, birthDate);
        this.points = 0.0;
        this.type = EType.BRONZE;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        this.type = type;
    }
}
