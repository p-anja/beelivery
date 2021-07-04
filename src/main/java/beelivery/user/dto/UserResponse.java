package beelivery.user.dto;

import beelivery.user.model.ESex;
import beelivery.user.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserResponse {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String sex;
    protected String birthDate;
    protected String profileImg;

    public UserResponse(String username, String password, String firstName, String lastName, String sex, String birthDate, String profileImg) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
        this.profileImg = profileImg;
    }

    public UserResponse(User user) {
        this.username = user.getId();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.sex = user.getSex().name();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.birthDate = format.format(user.getBirthDate());
        this.profileImg = user.getProfileImg();
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
