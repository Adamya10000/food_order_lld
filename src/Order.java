import java.util.*;

public class Order {
    private int orderId;
    private static int nextOrderId = 1001;
    private HashMap<Item, Integer> orderItems;
    private double totalPrice;
    private OrderStatus status;
    private boolean isVIP;
    private String specialRequest;

    public Order(HashMap<Item, Integer> cart, boolean isVIP, String specialRequest) {
        this.orderId = nextOrderId++;
        this.orderItems = new HashMap<>(cart);
        this.totalPrice = calculateTotalPrice();
        this.status = OrderStatus.Received;
        this.isVIP = isVIP;
        this.specialRequest = specialRequest;
        Admin.pendingOrders.add(this);
    }

    public Order(Order other){
        this.orderId = nextOrderId++;
        this.orderItems = new HashMap<>(other.orderItems);
        this.totalPrice = calculateTotalPrice();
        this.status = OrderStatus.Received;
        this.isVIP = other.isVIP;
        this.specialRequest = other.specialRequest;
        Admin.pendingOrders.add(this);
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public int getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void displayOrderDetails() {
        System.out.println("\nOrder ID: " + orderId);
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            Item item = entry.getKey();
            System.out.println(item.getName() + " x" + entry.getValue() + " = Rs." + (item.getPrice() * entry.getValue()));
        }
        System.out.println("Special Request: " + specialRequest+ "\nTotal Price: Rs." + totalPrice+"\nStatus: " + status);
    }
}
