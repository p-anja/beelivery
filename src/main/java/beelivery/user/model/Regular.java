package beelivery.user.model;

import java.util.Date;

public class Regular extends User {

    // orders
    private Cart cart;
    private double points;
    private EMemberType memberType;

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate, double points, EMemberType memberType) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = points;
        this.memberType = memberType;
        this.cart = new Cart(username);
    }

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = 0.0;
        this.memberType = EMemberType.BRONZE;
        this.cart = new Cart(username);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
