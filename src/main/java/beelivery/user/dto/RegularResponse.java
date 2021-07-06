package beelivery.user.dto;

import beelivery.user.model.Regular;
import beelivery.user.model.User;

public class RegularResponse extends UserResponse {
    private double points;
    private String memberType;
    private boolean suspicious;

    public RegularResponse(String username, String password, String firstName, String lastName, String sex, String birthDate, double points, String memberType, String profileImg, boolean suspicious) {
        super(username, password, firstName, lastName, sex, birthDate, profileImg);
        this.points = points;
        this.memberType = memberType;
        this.suspicious = suspicious;
    }

    public RegularResponse(Regular regular) {
        super(regular);
        this.points = regular.getPoints();
        this.memberType = regular.getMemberType().name();
        this.suspicious = regular.isSuspicious();
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
}
