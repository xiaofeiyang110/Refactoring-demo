package chapter6.src;

public class ReplaceTempWithQuery {
    int quantity;
    int itemPrice;

    /**
     * 重构之前，有两个临时变量basePrice，discountFactor
     * @return
     */
    double getPrice(){
        int basePrice = quantity*itemPrice;
        double discountFactor;
        if(basePrice > 1000) {
            discountFactor = 0.95;
        } else {
            discountFactor = 0.98;
        }
        return basePrice * discountFactor;
    }

    /**
     * 重构之后
     * @return
     */
    double getPrice_new(){
        return getBasePrice() * getDiscountFactor();
    }

    private double getDiscountFactor() {
        if(getBasePrice() > 1000) {
           return  0.95;
        } else {
            return 0.98;
        }
    }

    private int getBasePrice() {
        return quantity * itemPrice;
    }
}
