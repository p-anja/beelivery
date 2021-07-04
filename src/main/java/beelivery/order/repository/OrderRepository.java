package beelivery.order.repository;

import beelivery.misc.JSONRepository;
import beelivery.order.model.Order;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

public class OrderRepository extends JSONRepository<Order, String> {

    public OrderRepository(String filename) {
        super(filename, new TypeToken<List<Order>>(){}.getType());
    }

    public String getNextId() {
        int len = 10;
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(new Random().nextInt(AB.length())));
        return sb.toString();
    }
}
