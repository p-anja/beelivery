package beelivery.user.model;

import beelivery.misc.IIdentifiable;

import java.io.Serializable;
import java.util.Date;

public abstract class User implements IIdentifiable<String>, Serializable {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected ESex sex;
    protected ERole role;
    protected Date birthDate;
    protected boolean deleted;

    public User(String username, String password, String firstName, String lastName, ESex sex, ERole role, Date birthDate) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.role = role;
        this.birthDate = birthDate;
        this.deleted = false;
    }

    @Override
    public String getId() {
        return username;
    }

    @Override
    public void setId(String username) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
