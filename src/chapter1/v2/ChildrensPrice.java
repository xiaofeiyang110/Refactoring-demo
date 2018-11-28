package chapter1.v2;

public class ChildrensPrice extends  Price{
    @Override
    int getPriceCode() {
        return Movie.CHILDRENS;
    }

    @Override
    public double getCharge(int daysRented) {
        return  daysRented * 3;
    }
}
