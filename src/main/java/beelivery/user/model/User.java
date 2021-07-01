package beelivery.user.model;

import java.time.LocalDateTime;

public abstract class User {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected ESex sex;
    protected LocalDateTime birthDate;

    public User(String username, String password, String firstName, String lastName, ESex sex, LocalDateTime birthDate) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ESex getSex() {
        return sex;
    }

    public void setSex(ESex sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }
}
