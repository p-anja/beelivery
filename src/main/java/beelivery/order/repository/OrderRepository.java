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
        byte[] buffer = new byte[10];
        new Random().nextBytes(buffer);
        return new String(buffer, Charset.forName("UTF-8"));
    }
}
