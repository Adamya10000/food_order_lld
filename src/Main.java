import java.util.*;

public class Main {
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        String admin_id = "Admin";
        String admin_pass = "Admin";

        new Item("Chicken Biryani", 150.0, "Meals");
        new Item("Vegetable Biryani", 120.0, "Meals");
        new Item("Paneer Tikka", 100.0, "Snacks");
        new Item("Samosa", 20.0, "Snacks");
        new Item("Coke", 30.0, "Beverages");
        new Item("Sprite", 30.0, "Beverages");

        Customer c1 = new Customer("cust1", "pass1", "Aditya Malik");
        Customer c2 = new Customer("cust2", "pass2", "Tanish Goel");
        Customer c3 = new Customer("cust3", "pass3", "Dheeraj Arora");

        HashMap<Item, Integer> order1Items = new HashMap<>();
        order1Items.put(Item.getItemById(101), 2);
        order1Items.put(Item.getItemById(103), 1);
        Order order1 = new Order(order1Items, false, "No onions");

        HashMap<Item, Integer> order2Items = new HashMap<>();
        order2Items.put(Item.getItemById(102), 1);
        order2Items.put(Item.getItemById(104), 3);
        Order order2 = new Order(order2Items, true, "");

        c1.orderHistory.add(order1);
        c2.orderHistory.add(order2);
        Admin.pendingOrders.add(order1);
        Admin.pendingOrders.add(order2);

        while (true) {
            System.out.println("Welcome to Byte me!(please select your action 1/2/3):\n 1.Log in\n 2.Sign up\n 3.Exit");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    try {
                        login(admin_id, admin_pass);
                    } catch (InvalidLoginException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    System.out.println("Exiting the system.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void login(String admin_id, String admin_pass) throws InvalidLoginException{
        int c;
        String id, pass;

        System.out.println("""
                
                Please select user type:
                 1.Customer
                 2.Admin
                 3.Exit
                """);
        c = getIntInput();

        if (c == 3) {
            return;
        }

        System.out.println("Enter your id: ");
        id = in.nextLine();
        System.out.println("Enter your password: ");
        pass = in.nextLine();

        switch (c) {
            case 1:
                boolean Found = false;
                for (Customer cus : Customer.customers) {
                    if (cus.getId().equals(id) && cus.getPass().equals(pass)) {
                        System.out.println("You are logged in as a Student");
                        Found = true;
                        cus.cusfunc();
                        break;
                    }
                }
                if (!Found) {
                    throw new InvalidLoginException("Invalid student login");
                }
                break;
            case 2:
                if (admin_id.equals(id) && admin_pass.equals(pass)) {
                    System.out.println("You are logged in as Admin");
                    Admin admin = new Admin(id, pass);
                    admin.adminfunc();
                }
                else {
                    throw new InvalidLoginException("Invalid admin login");
                }
                break;
            default:
                System.out.println("Invalid user type");
        }
    }

    public static void signUp(){
        System.out.println("Welcome to Customer sign up portal.\nEnter your id: ");
        String id = in.nextLine();

        for(Customer c : Customer.customers) {
            if (c.getId().equals(id)) {
                System.out.println("You are already registered as a customer.");
                return;
            }
        }

        System.out.println("Enter your password: ");
        String pass = in.nextLine();
        System.out.println("Enter your name: ");
        String name = in.nextLine();

        Customer c = new Customer(id, pass, name);
        System.out.println("Customer registered successfully.");
    }

    public static int getIntInput() {
        int result = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                result = in.nextInt();
                in.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                in.nextLine();
            }
        }
        return result;
    }
}
