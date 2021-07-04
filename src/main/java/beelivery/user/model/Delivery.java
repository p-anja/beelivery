package beelivery.user.model;

import java.util.Date;

public class Delivery extends User {
    public Delivery(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.DELIVERY, birthDate);
    }
}
