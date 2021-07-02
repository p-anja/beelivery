package beelivery.user.model;

import java.util.Date;

public class Admin extends User {
    public Admin(String username, String password, String firstName, String lastName, ESex sex, ERole role, Date birthDate) {
        super(username, password, firstName, lastName, sex, role, birthDate);
    }
}
