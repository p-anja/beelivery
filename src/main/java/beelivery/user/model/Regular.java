package beelivery.user.model;

import java.util.Date;

public class Regular extends User {

    // orders
    // cart
    private double points;
    private EMemberType memberType;

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate, double points, EMemberType memberType) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = points;
        this.memberType = memberType;
    }

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = 0.0;
        this.memberType = EMemberType.BRONZE;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public EMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(EMemberType memberType) {
        this.memberType = memberType;
    }
}
