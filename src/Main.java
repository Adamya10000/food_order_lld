import javax.swing.*;
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

        while (true) {
            System.out.println("Welcome to Byte me!(please select your action 1/2/3/4):\n" +
                    " 1.Log in\n 2.Sign up\n 3.Open GUI\n 4.Exit");
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
                    SwingUtilities.invokeLater(() -> {
                        GUI gui = new GUI();
                        gui.setVisible(true);
                    });
                    break;
                case 4:
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


        performLogin(c, id, pass, admin_id, admin_pass);
    }

    public static void performLogin(int userType, String id, String pass,
                                    String admin_id, String admin_pass) throws InvalidLoginException {
        switch (userType) {
            case 1:
                boolean Found = false;
                for (Customer cus : Customer.customers) {
                    if (cus.getId().equals(id) && cus.getPass().equals(pass)) {
                        System.out.println("You are logged in as a Customer");
                        Found = true;
                        cus.cusfunc();
                        break;
                    }
                }
                if (!Found) {
                    throw new InvalidLoginException("Invalid login");
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
                throw new InvalidLoginException("Invalid user type");
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
