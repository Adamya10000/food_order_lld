import java.util.*;

public class Admin {
    private String id;
    private String pass;
    public static TreeSet<Order> pendingOrders = new TreeSet<>((o1, o2) -> {
        if (o1.isVIP() && !o2.isVIP()) return -1;
        if (!o1.isVIP() && o2.isVIP()) return 1;
        return Integer.compare(o1.getOrderId(), o2.getOrderId());
    });
    public static ArrayList<Order> completedOrders = new ArrayList<>();
    public static Scanner in = new Scanner(System.in);

    public Admin(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public void adminfunc() {
        while (true) {
            System.out.println("""
                    
                    Select action:
                     1.Menu Management
                     2.Order Management
                     3.Generate Report
                     4.Exit""");

            int choice = Main.getIntInput();
            switch (choice) {
                case 1:
                    menuManagement();
                    break;
                case 2:
                    orderManagement();
                    break;
                case 3:
                    generateReport();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void menuManagement() {
        while (true) {
            System.out.println("""
                    
                    Select action:
                     1.Add new item
                     2.Update existing item
                     3.Remove item
                     4.View current menu
                     5.Exit""");

            int choice = Main.getIntInput();
            switch (choice) {
                case 1:
                    System.out.println("Enter item name:");
                    String name = in.nextLine();
                    System.out.println("Enter price:");
                    double price = in.nextDouble();
                    in.nextLine();
                    System.out.println("Enter category: \n1.Snacks\n2.Beverages\n3.Meals");
                    int category = Main.getIntInput();
                    if(category == 1) {
                        new Item(name, price, "Snacks");
                    }
                    else if(category == 2) {
                        new Item(name, price, "Beverages");
                    }
                    else if(category == 3) {
                        new Item(name, price, "Meals");
                    }
                    else {
                        System.out.println("Invalid category. Please try again.");
                    }

                    System.out.println("Item added successfully.");
                    break;

                case 2:
                    System.out.println("Current menu:");
                    Item.view();
                    System.out.println("Enter item ID to update:");
                    int itemId = Main.getIntInput();
                    Item item = Item.getItemById(itemId);
                    if (item != null) {
                        while(true) {
                            System.out.println("""
                                    
                                    What would you like to update?
                                     1.Name
                                     2.Price
                                     3.Category
                                     4.Availability
                                     5.Exit""");
                            int updateChoice = Main.getIntInput();
                            switch(updateChoice) {
                                case 1:
                                    System.out.println("Enter item name:");
                                    String nam = in.nextLine();
                                    item.setName(nam);
                                    System.out.println("name updated successfully.");
                                    break;

                                case 2:
                                    System.out.println("Enter new price:");
                                    double newPrice = in.nextDouble();
                                    in.nextLine();
                                    Item.prices.remove(item);
                                    item.setPrice(newPrice);
                                    Item.prices.add(item);
                                    System.out.println("Price updated successfully.");
                                    break;

                                case 3:
                                    System.out.println("Enter category: \n1.Snacks\n2.Beverages\n3.Meals):");
                                    int catego = Main.getIntInput();
                                    if(catego == 1) {
                                        item.setCategory("Snacks");
                                        System.out.println("Category updated successfully.");
                                    }
                                    else if(catego == 2) {
                                        item.setCategory("Beverages");
                                        System.out.println("Category updated successfully.");
                                    }
                                    else if(catego == 3) {
                                        item.setCategory("Meals");
                                        System.out.println("Category updated successfully.");
                                    }
                                    else {
                                        System.out.println("Invalid category. Please try again.");
                                    }
                                    break;

                                case 4:
                                    System.out.println("Enter availability (true/false):");
                                    boolean newAvailability = in.nextBoolean();
                                    in.nextLine();
                                    item.setAvailability(newAvailability);
                                    System.out.println("Availability updated successfully.");
                                    break;

                                case 5:
                                    return;

                                default:
                                    System.out.println("Invalid choice.");
                            }
                        }
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case 3:
                    System.out.println("Current menu:");
                    Item.view();
                    System.out.println("Enter item ID to remove:");
                    int removeId = Main.getIntInput();
                    Item removeItem = Item.getItemById(removeId);
                    if (removeItem != null) {
                        Item.items.remove(removeItem);
                        Item.prices.remove(removeItem);
                        System.out.println("Item removed successfully.");
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case 4:
                    Item.view();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void orderManagement() {
        while (true) {
            System.out.println("""
                    
                    Select action:
                     1.View pending orders
                     2.Update order status
                     3.Process refunds
                     4.Exit""");

            int choice = Main.getIntInput();
            switch (choice) {
                case 1:
                    if (pendingOrders.isEmpty()) {
                        System.out.println("No pending orders.");
                    } else {
                        System.out.println("Pending Orders (VIP orders first):");
                        for (Order order : pendingOrders) {
                            order.displayOrderDetails();
                        }
                    }
                    break;

                case 2:
                    if (pendingOrders.isEmpty()) {
                        System.out.println("No pending orders.");
                        break;
                    }
                    System.out.println("Enter order ID to update:");
                    int orderId = Main.getIntInput();
                    Order orderToUpdate = null;
                    for (Order order : pendingOrders) {
                        if (order.getOrderId() == orderId) {
                            orderToUpdate = order;
                            break;
                        }
                    }
                    if (orderToUpdate != null) {
                        System.out.println("""
                                Select new status:
                                1. Preparing
                                2. Out for Delivery
                                3. Delivered""");
                        int statusChoice = Main.getIntInput();
                        switch (statusChoice) {
                            case 1:
                                orderToUpdate.setStatus(OrderStatus.Preparing);
                                break;
                            case 2:
                                orderToUpdate.setStatus(OrderStatus.Out_for_Delivery);
                                break;
                            case 3:
                                orderToUpdate.setStatus(OrderStatus.Delivered);
                                pendingOrders.remove(orderToUpdate);
                                completedOrders.add(orderToUpdate);
                                break;
                            default:
                                System.out.println("Invalid status choice.");
                                break;
                        }
                        System.out.println("Order status updated successfully.");
                    } else {
                        System.out.println("Order not found.");
                    }
                    break;

                case 3:
                    System.out.println("Enter order ID for refund:");
                    int refundOrderId = Main.getIntInput();
                    boolean found = false;
                    for (Order order : completedOrders) {
                        if (order.getOrderId() == refundOrderId && order.getStatus() == OrderStatus.Cancelled){
                            System.out.println("Refund processed for Order ID: " + refundOrderId);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("No refundable order found with this ID.");
                    }
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void generateReport() {
        System.out.println("\nDaily Sales Report");

        double totalSales = 0;

        for (Order order : completedOrders) {
            if (order.getStatus() == OrderStatus.Delivered) {
                totalSales += order.getTotalPrice();
            }
        }

        System.out.println("Total Sales: Rs." + totalSales);
        System.out.println("Total Orders Processed: " + completedOrders.size());
        System.out.println("Pending Orders: " + pendingOrders.size());
    }

}