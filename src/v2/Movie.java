package v2;

public class Movie {
    public  static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String title;
    private int priceCode;

    Price price;

    public Movie(String title, int priceCode) {
        this.title = title;
//        this.priceCode = priceCode;
        setPriceCode(priceCode);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriceCode() {
        return price.getPriceCode();
    }

    public void setPriceCode(int priceCode){
        switch (priceCode){
            case REGULAR:
                price = new RegularPrice();
                break;
            case CHILDRENS:
                price = new ChildrensPrice();
                break;
            case NEW_RELEASE:
                price = new NewReleasPrice();
                break;
            default:
                throw new IllegalArgumentException("Incorrect Price code");
        }
        this.priceCode = priceCode;
    }


    public double getCharge(int daysRented)
    {
      return price.getCharge(daysRented);
    }

    public int getFrequentRenterPoints(int daysRented)
    {
        return price.getFrequentRenterPoints(daysRented);

    }


}
