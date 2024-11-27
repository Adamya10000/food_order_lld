import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JunitTest {
    private Customer customer;
    private Item outOfStockItem;

    @Before
    public void setUp() {
        customer = new Customer("testUser", "testPass", "Test Customer");

        outOfStockItem = new Item("Out of Stock Burger", 120.0, "Meals");
        outOfStockItem.setAvailability(false);
    }

    @Test
    public void testOrderingOutOfStockItem() {
        try {
            customer.addItemToCart(outOfStockItem, 1);
            fail("Expected an exception when adding out-of-stock item");
        } catch (IllegalStateException e) {
            assertEquals("That item is not available.", e.getMessage());
        }
    }

    @Test
    public void testInvalidCustomerLogin() {
        try {
            Main.performLogin(1, "invalidUser", "invalidPassword", "Admin", "Admin");
            fail("Expected InvalidLoginException for invalid customer login");
        } catch (InvalidLoginException e) {
            assertEquals("Invalid login", e.getMessage());
        }
    }

    @Test
    public void testInvalidAdminLogin() {
        try {
            Main.performLogin(2, "invalidAdmin", "invalidAdminPass", "Admin", "Admin");
            fail("Expected InvalidLoginException for invalid admin login");
        } catch (InvalidLoginException e) {
            assertEquals("Invalid admin login", e.getMessage());
        }
    }

}