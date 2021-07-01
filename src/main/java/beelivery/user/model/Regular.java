package beelivery.user.model;

import java.time.LocalDateTime;

public class Regular extends User {

    // orders
    // cart
    private double points;
    private EType type;

    public Regular(String username, String password, String firstName, String lastName, ESex sex, LocalDateTime birthDate, double points, EType type) {
        super(username, password, firstName, lastName, sex, birthDate);
        this.points = points;
        this.type = type;
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
