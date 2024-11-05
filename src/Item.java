import java.util.*;

public class Item {
    private String Name;
    private int id;
    private static int nextid = 101;
    private double Price;
    private String Category;
    private boolean available;
    private HashMap<Customer,String> reviews = new HashMap<>();
    public static ArrayList<Item> items = new ArrayList<>();
    public static TreeSet<Item> prices = new TreeSet<>(Comparator.comparingDouble(Item::getPrice).thenComparing(Item::getId));

    public Item(String Name, double Price, String Category) {
        this.Name = Name;
        this.id = nextid++;
        this.Price = Price;
        this.Category = Category;
        this.available = true;
        items.add(this);
        prices.add(this);
    }

    public static void view(){
        for(Item item : items){
            System.out.println(item);
        }
    }

    public static void filter(String categor){
        for(Item item : items){
            if(item.Category.equals(categor)){
                System.out.println(item);
            }
        }
    }

    public static void search(String key){
        for(Item item : items){
            String name = item.Name.toLowerCase();
            if(name.contains(key.toLowerCase())){
                System.out.println(item);
            }
        }
    }

    public static void sortPrice(boolean ascending){
        if(ascending){
            for(Item item : prices){
                System.out.println(item);
            }
        }
        else{
            Iterator<Item> iterator = prices.descendingIterator();
            while(iterator.hasNext()){
                Item item = iterator.next();
                System.out.println(item);
            }
        }
    }

    public static Item getItemById(int id){
        for(Item item : items){
            if(item.id == id){
                return item;
            }
        }
        return null;
    }

    public void addReview(Customer customer,String review){
        reviews.put(customer, review);
    }

    public void viewReviews(){
        if(reviews.isEmpty()){
            System.out.println("No reviews found");
            return;
        }
        for(Map.Entry<Customer,String> entry : reviews.entrySet()){
            System.out.println(entry.getKey().getName()+": "+entry.getValue()+"\n");
        }
    }

    public String getCategory(){
        return Category;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return Name;
    }

    public double getPrice(){
        return Price;
    }

    public boolean getAvailable(){
        return available;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public void setCategory(String category){
        Category = category;
    }

    public void setPrice(double price){
        Price = price;
    }

    public void setAvailability(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Item id: "+ this.id + "\nName: "+ this.Name + "\nPrice: "+ this.Price +
                "\nCategory: "+ this.Category+ "\nAvailable: "+ this.available +"\n";
    }
}
