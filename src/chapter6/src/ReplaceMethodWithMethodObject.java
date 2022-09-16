package chapter6.src;

public class ReplaceMethodWithMethodObject {

    class Account {
        /**
         *
         */
        int gamma(int inputVal, int quantity, int yearToDate) {
            int importantValue1 = (inputVal * quantity) + delta();
            int importantValue2 = (inputVal * yearToDate) + 100;
            if (yearToDate - importantValue1 > 100) {
                importantValue2 -= 20;
            }
            int importantValue3 = importantValue2 * 7;
            return importantValue3 - 2 * importantValue1;
        }

        /**
         * 重构方法
         * 新建一个类
         * 新类中新建一个字段保存源类的引用。然后针对原函数所有的参数和临时变量建立对应的属性
         * 在新类中创建一个构造函数，接收源对象，以及原函数的所有参数
         * 将原函数复制到新类中的computer中，去掉函数中临时变量的声明
         * 替换原函数代码为"创建新类，调用其中的方法"
         * @return
         */
        int gamma_new(int inputVal, int quantity, int yearToDate) {
            Gamma gamma = new Gamma(this,inputVal, quantity, yearToDate);
            return gamma.compute();
        }

        private int delta() {
            return 10;
        }
    }

    class Gamma {
        private final Account account;
        private int inputVal;
        private int quantity;
        private int yearToDate;
        private int importantValue1;
        private int importantValue2;
        private int importantValue3;

        Gamma(Account account, int inputVal, int quantity, int yearToDate) {
            this.account = account;
            this.inputVal = inputVal;
            this.quantity = quantity;
            this.yearToDate = yearToDate;
        }
        int compute(){
            importantValue1 = (inputVal * quantity) + account.delta();
            importantValue2 = (inputVal * yearToDate) + 100;
            if (yearToDate - importantValue1 > 100) {
                importantValue2 -= 20;
            }
            importantValue3 = importantValue2 * 7;
            return importantValue3 - 2 * importantValue1;
        }
    }
}
