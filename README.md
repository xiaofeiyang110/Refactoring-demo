#### 重构的一个例子

学习《重构-改善既有代码的设计》，同时做成文档，做一个分享，同时也作为自己的一个沉淀。

#### 业务说明

系统根据课程租的电影，租期，计算每个顾客的消费金额和打印详单。

  * 入参：顾客，  租的影片，租期
  
  * 系统根据租凭时间和影片类型计算费用，影片分为3类，普通片，儿童片，新片
  除了计算费用，还要为常客计算积分，积分根据租片是否为新片而不同。
  
##### 系统类图

![类图](doc/class.png)

##### 基础版本代码 v0

* 代码可以正常运行，看起来一切都ok。可是开发中唯一不变的就是变化。终于变化来了。

##### 第一个变化v1
* 用户希望用html格式输出详单。很简单把statement拷贝，粘贴一下，修改一下就完工了
（CV大法发挥强大用处的时候到了）。

* 恭喜第一个变化OK了，很快第二个变化来了，用户希望改变影片分类规则，
这些改变会影响客户消费和常客积分点的计算。为了应对改变，程序必须修改statement和htmlStatement。

* 随着这些小的改动越来越多，终于这个系统没有人可以维护了。这时候重构技术就该粉墨登场了。

> 如果你发现自己需要为程序添加一个特性，而代码结构使你无法很方便地达成目的，那就先重构那个程序，
使特性的添加比较容易进行，然后再添加特性。

###### 重构第一步
* 找出系统的逻辑泥团，本例就是swith语句，把他提炼到独立函数中似乎比较好。

```java
...
switch (each.getMovie().getPriceCode()) {
    case Movie.REGULAR:
        thisAmount += 2;
        if (each.getDaysRented() > 2) {
            thisAmount += (each.getDaysRented() - 2) * 1.5;
        }
        break;
    case Movie.CHILDRENS:
        thisAmount += each.getDaysRented() * 3;
        break;
    case Movie.NEW_RELEASE:
        thisAmount += 1.5;
        if (each.getDaysRented() > 3) {
            thisAmount += (each.getDaysRented() - 3) * 1.5;
        }
        break;
    default:
        break;
}
... 

```

* 找到函数内的局部变量和参数，有两个 each，thisAmount。
each没有被修改，可以直接作为参数传入新的函数。thisAmount是个临时变量，
每次循环开始之前设置为0，switch之前不会变，所以可以作为新函数的返回值。
so 我们先把这个计算thisAmount的代码抽取成一个独立的函数。

```java
private double amountFor(Rental each) {
    double thisAmount = 0;
    switch (each.getMovie().getPriceCode()) {
        case Movie.REGULAR:
            thisAmount += 2;
            if (each.getDaysRented() > 2) {
                thisAmount += (each.getDaysRented() - 2) * 1.5;
            }
            break;
        case Movie.CHILDRENS:
            thisAmount += each.getDaysRented() * 3;
            break;
        case Movie.NEW_RELEASE:
            thisAmount += 1.5;
            if (each.getDaysRented() > 3) {
                thisAmount += (each.getDaysRented() - 3) * 1.5;
            }
            break;
        default:
            break;
    }
    return thisAmount;
}
```

* 再看修改好的代码，使用了来自Rental的信息，但是没有使用来自Customer的信息。这是一个信号，
"它是否放错了位置"。

> 绝大多数情况下，函数应该在它使用的数据的所属对象内。

* 回到Customer的statement方法，我们下一步要对"积分计算"部分做同样的处理。

   首先需要运用Extract Method重构手法，同样我们看一下局部变量。这里再一次
   用到了each，而它可以被当作参数传入新函数中。另一个变量frequentRenterPoints，
   在使用前已经先有了初始值，但提炼出来的函数并没有读取该值，所以不需要把
   它作为参数传递进去。
   
* 再次回到Customer的statement方法，这次我们要开始处理临时变量。运用Replace Temp with Query 。
利于查询函数来取代临时变量。
   
   1，thisAmount： 使用each.getCharge(); 直接替换。
   
   2，totalAmount：使用getTotalAmount()代替totalAmount，由于totalAmount在循环内复制，需要把整个循环复制到查询函数中。
   
   3,frequentRenterPoints: 和totalAmount一样的处理。
   
 * 目前

* 修改之后我们的类图发生了变化
![类图V1](doc/classV1.png)
