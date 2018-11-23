package v2;



import java.util.ArrayList;
import java.util.List;


public class Customer {
    private String name;
    private List<Rental> rentals = new ArrayList<Rental>();

    public Customer(String name) {
        this.name = name;
    }


    public  void addRental(Rental rental){
        rentals.add(rental);
    }

    public String getName() {
        return name;
    }

    public String statement() {
        String result = "Rental Record for " + getName() + "\n";
        for (Rental each :rentals) {
            result += "\t" + each.getMovie().getTitle() + "\t" + String.valueOf(each.getCharge()) + "\n";
        }
        // add footer lines
        result += "Amount owed is " + String.valueOf(getTotalAmount()) + "\n";
        result += "You earned " + String.valueOf(getTotalFrequentRenterPoints()) + " frequent renter points";
        return result;
    }

    private int getTotalFrequentRenterPoints(){
        int totalFrequentRenterPoints = 0;
        for (Rental each :rentals) {
            totalFrequentRenterPoints +=  each.getFrequentRenterPoints();
        }
        return totalFrequentRenterPoints;

    }


    private double  getTotalAmount()
    {
        double totalAmount = 0;
        for (Rental each :rentals) {
            totalAmount +=  each.getCharge();
        }
        return totalAmount;
    }

}


