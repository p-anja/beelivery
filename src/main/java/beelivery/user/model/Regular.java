package beelivery.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Regular extends User {

    private List<String> orders;
    private Cart cart;
    private double points;
    private EMemberType memberType;
    private boolean suspicious;
    private int cancelCount;

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate, double points, EMemberType memberType) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = points;
        this.memberType = memberType;
        this.cart = new Cart(username);
        this.orders = new ArrayList<String>();
        this.cancelCount = 0;
        this.blocked = false;
        this.suspicious = false;
    }

    public Regular(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.REGULAR, birthDate);
        this.points = 0.0;
        this.memberType = EMemberType.BRONZE;
        this.cart = new Cart(username);
        this.orders = new ArrayList<String>();
        this.cancelCount = 0;
        this.blocked = false;
        this.suspicious = false;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public void incCancelCount() {
        ++cancelCount;
        if(cancelCount > 5) {
            suspicious = true;
        }
    }

    public int getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void addPoints(double pts) {
        points += pts;
        if(points >= 3000 && points < 5000) {
            memberType = EMemberType.SILVER;
        }
        if(points >= 5000) {
            memberType = EMemberType.GOLD;
        }
    }

    public void subtractPoints(double pts) {
        points -= pts;
    }

    public void addOrder(String orderId) {
        if(!orders.contains(orderId)) {
            orders.add(orderId);
        }
    }

    public boolean removeOrder(String orderId) {
        return orders.remove(orderId);
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
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
