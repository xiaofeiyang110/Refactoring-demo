package chapter1.v1;

import chapter1.v1.ChildrenPrice;
import chapter1.v1.NewRealse;

public class Movie {
    public  static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String title;
    private int priceCode;

    private Price price;

    public Movie(String title, int priceCode) {
        this.title = title;
        setPriceCode(priceCode);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(int priceCode) {
        switch (priceCode) {
            case REGULAR:
                price = new RegularPrice();
                break;
            case NEW_RELEASE:
                price = new NewRealse();
                break;
            case CHILDRENS:
                price = new ChildrenPrice();
                break;
        }

    }

    public double getCharge(int daysRented) {
        return price.getCharge(daysRented);
    }
}
