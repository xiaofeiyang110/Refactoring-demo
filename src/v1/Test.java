package v1;

import v1.Movie;
import v1.Rental;
import  v1.Customer;

public class Test {

    public static void  main(String[] args)
    {
        Movie m1 = new Movie("儿童1",Movie.CHILDRENS);
        Movie m2 = new Movie("新片1",Movie.NEW_RELEASE);

        Rental r1 = new Rental(m1,1);
        Rental r2 = new Rental(m2,2);

        Customer c = new Customer("张三");
        c.addRental(r1);
        c.addRental(r2);
        String res = c.statement();

        System.out.print(res);
    }
}
