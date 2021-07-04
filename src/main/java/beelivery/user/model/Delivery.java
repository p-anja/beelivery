package beelivery.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Delivery extends User {

    private List<String> orders;

    public Delivery(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.DELIVERY, birthDate);
        orders = new ArrayList<String>();
        blocked = false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
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
}
