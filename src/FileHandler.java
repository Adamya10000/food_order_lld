import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String ORDER_HISTORY_DIR = "order_histories/";
    private static final String CART_FILE = "temp_cart.txt";

    static {
        new File(ORDER_HISTORY_DIR).mkdirs();
    }

    public static void saveOrderHistory(Customer customer) {
        String fileName = ORDER_HISTORY_DIR + customer.getId() + "_orders.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Order order : customer.orderHistory) {
                writer.println("ORDER_START|" + order.getOrderId() + "|" + order.isVIP() + "|" +
                        order.getStatus() + "|" + order.getTotalPrice() + "|" + order.getSpecialRequest());

                for (Map.Entry<Item, Integer> entry : order.orderItems.entrySet()) {
                    Item item = entry.getKey();
                    writer.println("ITEM|" + item.getId() + "|" + entry.getValue());
                }
                writer.println("ORDER_END");
            }
        } catch (IOException e) {
            System.err.println("Error saving order history: " + e.getMessage());
        }
    }

    public static void loadOrderHistory(Customer customer) {
        String fileName = ORDER_HISTORY_DIR + customer.getId() + "_orders.txt";
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Order currentOrder = null;
            HashMap<Item, Integer> orderItems = new HashMap<>();
            ArrayList<String> oparts = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                switch (parts[0]) {
                    case "ORDER_START":
                        orderItems = new HashMap<>();
                        oparts.clear();
                        Collections.addAll(oparts, parts);
                        break;
                    case "ITEM":
                        Item item = Item.getItemById(Integer.parseInt(parts[1]));
                        if (item != null) {
                            orderItems.put(item, Integer.parseInt(parts[2]));
                        }
                        break;
                    case "ORDER_END":
                        if (oparts.size() == 5) {
                            oparts.add("");
                        }
                        currentOrder = reconstructOrder(orderItems, oparts,customer);
                        if (currentOrder != null && !customer.orderHistory.contains(currentOrder)) {
                            customer.orderHistory.add(currentOrder);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading order history: " + e.getMessage());
        }
    }

    public static void saveCart(String customerId, HashMap<Item, Integer> cart) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CART_FILE))) {
            writer.println("CUSTOMER_ID|" + customerId);
            for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
                writer.println(entry.getKey().getId() + "|" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    public static HashMap<Item, Integer> loadCart(String customerId) {
        HashMap<Item, Integer> cart = new HashMap<>();
        File file = new File(CART_FILE);
        if (!file.exists()) return cart;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) return cart;

            String[] customerParts = line.split("\\|");
            if (!customerParts[1].equals(customerId)) return cart;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Item item = Item.getItemById(Integer.parseInt(parts[0]));
                if (item != null) {
                    cart.put(item, Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cart: " + e.getMessage());
        }
        return cart;
    }

    private static Order reconstructOrder(HashMap<Item, Integer> orderItems, ArrayList<String> orderParts, Customer cus) {
        try {
            Order order = new Order(orderItems, Boolean.parseBoolean(orderParts.get(2)), orderParts.get(5),cus);
            order.setOrderId(Integer.parseInt(orderParts.get(1)));
            order.setStatus(OrderStatus.valueOf(orderParts.get(3)));
            if(order.getStatus() == OrderStatus.Cancelled){
                Admin.pendingOrders.remove(order);
                Admin.completedOrders.add(order);
            }
            return order;
        } catch (Exception e) {
            System.err.println("Error reconstructing order: " + e.getMessage());
            return null;
        }
    }
}