import java.util.*;

public class Customer {
    private String id;
    private String pass;
    private String name;
    public static ArrayList<Customer> customers = new ArrayList<>();
    private HashMap<Item,Integer> cart = new HashMap<>();
    public ArrayList<Order> orderHistory = new ArrayList<>();
    private boolean isVIP;
    public static Scanner in = new Scanner(System.in);

    public Customer(String id, String pass, String name) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.isVIP = false;
        customers.add(this);
    }

    public void cusfunc(){
        while(true) {
            int c;
            System.out.println("""
                    
                    Select action:
                     1.Browse Menu
                     2.View Cart
                     3.View Orders
                     4.Give/view Reviews
                     5.Become VIP
                     6.Exit""");

            c = Main.getIntInput();
            switch(c) {
                case 1:
                    browse();
                    break;
                case 2:
                    cartfunc();
                    break;

                case 3:
                    track();
                    break;

                case 4:
                    giveReview();
                    break;

                case 5:
                    becomeVIP();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void browse(){
        while(true) {
            int c;
            System.out.println("""
                    
                    Select action:
                     1.View Items
                     2.Search Items
                     3.Filter by category
                     4.Sort items by price
                     5.Exit""");

            c = Main.getIntInput();
            switch(c) {
                case 1:
                    Item.view();
                    break;
                case 2:
                    System.out.println("Please enter keyword to search by: ");
                    String key = in.nextLine();
                    Item.search(key);
                    break;

                case 3:
                    System.out.println("Select category to view:\n1.Snacks\n2.Beverages\n3.Meals");
                    int c1 = Main.getIntInput();

                    if(c1 == 1) {
                        Item.filter("Snacks");
                    }
                    else if(c1 == 2) {
                        Item.filter("Beverages");
                    }
                    else if(c1 == 3) {
                        Item.filter("Meals");
                    }
                    else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 4:
                    System.out.println("Select sort type(1/2):\n1.asc\n2.desc");
                    int choice = Main.getIntInput();
                    if(choice == 1) {
                        Item.sortPrice(true);
                    }
                    else if(choice == 2) {
                        Item.sortPrice(false);
                    }
                    else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void cartfunc() {
        while (true) {
            System.out.println("""
                    
                    Select action:
                     1.Add Item
                     2.Modify Quantity
                     3.Remove Item from Cart
                     4.View total
                     5.Checkout
                     6.Exit""");

            int choice = Main.getIntInput();
            switch (choice) {
                case 1:
                    addItemToCart();
                    break;
                case 2:
                    modifyItemQuantity();
                    break;
                case 3:
                    removeItemFromCart();
                    break;
                case 4:
                    viewTotal();
                    break;
                case 5:
                    checkout();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addItemToCart() {
        Item.view();
        System.out.print("Enter Item ID to add to cart: ");
        int itemId = Main.getIntInput();
        Item item = Item.getItemById(itemId);
        if (item != null) {
            if(item.getAvailable()) {
                System.out.print("Enter quantity: ");
                int quantity = Main.getIntInput();
                cart.put(item, cart.getOrDefault(item, 0) + quantity);
                System.out.println("Added to cart successfully.");
            }
            else{
                System.out.println("That item is not available.");
            }
        } else {
            System.out.println("Item not found.");
        }
    }

    private void modifyItemQuantity() {
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            System.out.println("Item id: "+item.getId() + " Name: "+item.getName() +" Quantity: "+ entry.getValue());
        }
        System.out.print("Enter Item ID to modify quantity: ");
        int itemId = Main.getIntInput();
        Item item = Item.getItemById(itemId);
        if (cart.containsKey(item)) {
            System.out.print("Enter new quantity: ");
            int newQuantity = Main.getIntInput();
            cart.put(item, newQuantity);
            System.out.println("Quantity updated successfully.");
        } else {
            System.out.println("Item not in cart.");
        }
    }

    private void removeItemFromCart() {
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            System.out.println("Item id: "+item.getId() + " Name: "+item.getName() +" Quantity: "+ entry.getValue());
        }
        System.out.print("Enter Item ID to remove from cart: ");
        int itemId = Main.getIntInput();
        Item item = Item.getItemById(itemId);
        if (cart.containsKey(item)) {
            cart.remove(item);
            System.out.println("Item removed from cart successfully.");
        } else {
            System.out.println("Item not in cart.");
        }
    }

    private void viewTotal() {
        System.out.println("Items in your cart:");
        double totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(item.getName() + " x" + quantity + " = Rs." + (item.getPrice() * quantity));
            totalPrice += item.getPrice() * quantity;
        }
        System.out.println("Total: Rs." + totalPrice);
    }

    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        double totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            totalPrice += item.getPrice() * entry.getValue();
        }
        System.out.println("Please pay Rs."+totalPrice);
        int amount = Main.getIntInput();
        if (amount >= totalPrice) {
            System.out.println("Do you have any special requests?(press enter for none)");
            String special = in.nextLine();
            Order o = new Order(new HashMap<>(cart), isVIP,special);
            orderHistory.add(o);
            cart.clear();
            System.out.println("Order placed successfully! Order ID: " + o.getOrderId());
        }
        else {
            System.out.println("Insufficient amount payed.");
        }
    }

    private void track() {
        while (true) {
            System.out.println("""
                    
                    Select action:
                     1.View pending order status
                     2.Cancel pending orders
                     3.View order history
                     4.Exit""");

            int choice = Main.getIntInput();
            switch (choice) {
                case 1:
                    if(orderHistory.isEmpty()){
                        System.out.println("No pending orders.");
                    }
                    for(Order o : orderHistory) {
                        if(o.getStatus() !=OrderStatus.Cancelled && o.getStatus()!=OrderStatus.Delivered){
                            o.displayOrderDetails();
                        }
                    }
                    break;
                case 2:
                    boolean found=false;
                    for(Order o: orderHistory){
                        if(o.getStatus() ==OrderStatus.Received){
                            found=true;
                            o.displayOrderDetails();
                            System.out.println("Do you wish to cancel this order?(y/n)");
                            String answer = in.nextLine();
                            if(answer.equalsIgnoreCase("y")){
                                o.setStatus(OrderStatus.Cancelled);
                                Admin.pendingOrders.remove(o);
                                Admin.completedOrders.remove(o);
                                System.out.println("Order cancelled.");
                            }
                        }
                    }
                    if(!found){
                        System.out.println("No cancellable orders.");

                    }
                    break;
                case 3:
                    for(Order o: orderHistory){
                        o.displayOrderDetails();
                    }
                    System.out.println("Do you wish to repeat an order?(y/n)");
                    String answer = in.nextLine();
                    if(answer.equalsIgnoreCase("y")){
                        System.out.println("Enter order id to repeat:");
                        int orderId = Main.getIntInput();
                        boolean f=false;
                        Order repeat=null;
                        for (Order o : orderHistory) {
                            if (o.getOrderId() == orderId) {
                                f = true;
                                repeat = new Order(o);
                            }
                        }
                        if(!f){
                            System.out.println("Order not found.");
                        }
                        else {
                            orderHistory.add(repeat);
                            System.out.println("Order repeated successfully.");
                        }
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void giveReview() {
        System.out.println("Select action:\n1.View reviews\n2.Give reviews");

        int choice = Main.getIntInput();
        System.out.print("Enter Item ID: ");
        int Id = Main.getIntInput();
        Item item = Item.getItemById(Id);
        switch (choice) {
            case 1:
                if (item != null) {
                    System.out.println("Reviews for "+item.getName()+": ");
                    item.viewReviews();
                } else {
                    System.out.println("Item not found.");
                }
                break;

            case 2:
                if (item != null) {
                    System.out.print("Enter your review: ");
                    String review = in.nextLine();
                    item.addReview(this, review);
                    System.out.println("Review submitted");
                } else {
                    System.out.println("Item not found.");
                }
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }

    }

    private void becomeVIP() {
        if(isVIP){
            System.out.println("You are already a VIP.");
            return;
        }
        System.out.println("Upgrade to VIP for order priority");
        System.out.println("Please pay Rs.500 for becoming a VIP ");
        int amount = Main.getIntInput();
        if (amount >= 500) {
            this.isVIP = true;
            System.out.println("Congratulations! You are now a VIP customer.");
        } else {
            System.out.println("Insufficient amount to become VIP.");
        }
    }

    public String getId() {
        return id;
    }
    public String getPass() {
        return pass;
    }
    public String getName() {
        return name;
    }
}
