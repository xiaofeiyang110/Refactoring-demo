package chapter9.src;

import java.util.Date;

public class DecomposeConditional {
    private  Date SUMMER_START;
    private  Date SUMMER_END;
    private double WINTER_RATE;
    private double WINTER_SERVICE_CHARGE;
    private double SUMMER_RATE;

    /**
     * 重构前，代码的描述是做什么：
     * 时间在summer_start之前或summer_end之后做什么
     * 其他时间做什么
     */

    private double getCharge(int quantity) {
        Date date = new Date();
        double charge;
        if (date.before(SUMMER_START) || date.after(SUMMER_END)){
            charge = quantity * WINTER_RATE + WINTER_SERVICE_CHARGE;
        }else{
            charge= quantity * SUMMER_RATE;
        }
        return charge;
    }

    /**
     * 重构之后，代码描述的是为什么做：
     * 不是夏天，winterCharge
     * summerCharge
     * 分支的原因和分支的作用更加清晰了
     *
     */
    private double getCharge_new(int quantity) {
        Date date = new Date();
        double charge;
        if (notSummer(date)){
            charge = winterCharge(quantity);
        }else{
            charge= summerCharge(quantity);
        }
        return charge;
    }

    private double summerCharge(int quantity) {
        return quantity * SUMMER_RATE;
    }

    private double winterCharge(int quantity) {
        return quantity * WINTER_RATE + WINTER_SERVICE_CHARGE;
    }

    private boolean notSummer(Date date) {
        return date.before(SUMMER_START) || date.after(SUMMER_END);
    }
}
