# 简化条件表达式
* 通过重构使“分支逻辑”和“操作细节”分离
* 更突出每个分支的原因和每个分支的作用
* 代码描述从“做什么”的语句换成了“为什么这样做”

### Decompose Conditional(分解条件表达式)

* 问题
    * 条件逻辑使代码更难阅读
    * 代码意图和代码自身之间存在差距

* 做法
    * 将if段落提炼出来，构成一个独立函数
    * 将then段落和else段落都提炼出来，各自构成一个独立函数


* 把每个分支条件分解成新函数，根据新函数代码的用途，为分解的新函数命名
    * 突出每个分支的原因（条件表达式）
    * 更清楚每个分支的作用（操作细节）

* 例子
```java
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
```

### Consolidate Conditional Expression(合并条件表达式)
    
* 现象
  * 有一系列的条件测试，检查条件各不相同，但最终都得到相同结果

* 使用"逻辑或"和"逻辑与"将它们合并成一个条件表达式
  * 检查的用意更清晰："实际上只有一次条件检查，只不过有多个并列条件需要检查而以"。
  * 厘清代码意义：描述从"做什么"的语句换成了"为什么这样做"
> 如果检查项的确彼此独立，的确不应该被视为同一次检查，那么就不要使用本重构。

* 做法
  * 确定这些条件语句都没有副作用
    > 如果条件表达式有副作用，你就不能使用重构
  * 使用适当的逻辑操作符，将一系列相关条件表达式合并为一个
  * 编译，测试
  * 对合并后的条件表达式实施Extract Method (提炼函数)

### Consolidate Duplicate Conditional Fragments(合并重复的条件片段)

* 现象
  * 一组条件表达式的所有分支都执行了相同的某段代码

* 将相同的代码搬移到条件表达式外面
  * 代码得以更清晰地表明哪些东西随条件的变化而变化，哪些东西保持不变
* 做法
  * 鉴别出"执行方式不随条件变化而变化"的代码
  * 如果这些共同代码位于条件表达式起始处，将它移动到表达式之前
  * 如果这些共同代码位于条件表达式尾端，将它移动到表达式之后
  * 如果这些共同代码位于条件表达式中段，将共同代码向前或后移动，然后在如前处理
  * 如果这些共同代码不止一句，首先使用Extract Method (提炼函数)。再如前处理。
  > 同样的手法可以处理异常。如果try区域可能引发异常的语句之后，以及所有catch区域之内，都重复执行了同一段代码，就可以将这段重复代码移到final区域

### Remove Control Flag(移除控制标记)
* 现象
  * 在一系列布尔表达式中，某个变量带有"控制标记"（control flag）的作用
    * 所谓的"控制标记"就是跳出逻辑的控制标记值
* 以break语句或return语句取代控制标记

* 做法
  * 运用Extract Method (提炼函数)，将整段逻辑提炼到一个独立函数中。
  * 找出让你跳出这段逻辑的控制标记值
  * 找出对标记变量赋值的语句，代以恰当的return语句
  * 每次替换后，编译并测试
  > 如果函数带有副作用，那么需要先以Separate Query from Modifier(将查询函数和修改函数分离)
* 例子

```java
    /**
     * 注意 if("".equals(found))。控制了循环的结束，所以found就是一个控制标记值
     * 同时found还是运算结果
     * @param people
     */
    private void checkSecurity(String[] people){
        String found = "";
        for (int i = 0; i < people.length; i++) {
            if("".equals(found)){ // 控制标记：如果found有值，循环不会继续执行
                if(people[i].equals("Don")){
                    sendAlert();
                    found = people[i];
                }
                if(people[i].equals("John")){
                    sendAlert();
                    found = people[i];
                }
            }
        }
        someLaterCode(found);
    }

    /**
     * 重构之后控制标记被去掉了
     * 单独看foundMiscreant 方法，return很明确的告诉我们不再执行该函数中的其他代码
     * @param people
     */
    private void checkSecurity_new(String[] people){
        String found = foundMiscreant(people);
        someLaterCode(found);
    }
    private String foundMiscreant(String[] people) {
        for (int i = 0; i < people.length; i++) {
            if(people[i].equals("Don")){
                sendAlert();
                return people[i];
            }
            if(people[i].equals("John")){
                sendAlert();
                return  people[i];
            }
        }
        return "";
    }
```
### Replace Nested Conditional with Guard Clauses (以卫语句取代嵌套条件表达式)

* 现象和解决手段
  * 函数中的条件逻辑使人难以看清正常的执行路径，使用卫语句表现所有特殊情况
    * 如果某个条件极其罕见，就应该单独检查该条件，并在条件为真时立刻从函数中返回。这样的单独检查常常被称为"卫语句"
  * 使用"卫语句"好处：如果对函数剩余部分不再有兴趣，当然应该立刻退出，引导阅读者去看一个没有用的else区段，只会妨碍他们的理解
* 做法
  * 对于每个检查，放进一个卫语句
    * 卫语句要不就从函数中返回，要不就抛出一个异常
  * 每次将条件检查替换成卫语句后，编译并测试
    * 如果所有卫语句都导致相同结果，使用Consolidate Conditional Expression(合并条件表达式)

### Introduce Null Object (引入Null对象)
* 现象和解决手段
  * 你需要再三检查某对象是否为null。将null值替换为null对象
  > Null Object 模式，我感觉用处好像没有那么大
  > 核心的思想是如果你要在程序里写(if value == null){nullMethod();}else{value.method();}那么你可以创建是一个nullValue对象，他的method方法为nullMethod()。
  > 这样上面的代码就可以去掉if/else。直接写成value.method()。
  > <br/>需要做的另一件事是在返回value对象的地方不再返回null，使用nullValue对象代替。
  > <br/>还有就是nullValue里面多一个isNull方法，返回true。如果有需要null对象做的事情不一样，不再使用value == null判断，替换成value.isNull()。 
* 从本质上看，比Null Object模式更大的模式：Special Case模式
  > 这个本质上和Null Object是一样的。或者说Null Object是一种Special Case模式。他的Special类型为Null

### Introduce Assertion(引入断言)
* 某一段代码需要对程序状态做出某种假设，以断言明确表现这种假设
 > 断言用来检查"一定必须为真"的条件。

### Replace Conditional with Polymorphism(以多态取代条件表达式) 

* 有个条件表达式，它根据对象类型的不同而选择不同的行为。可以将这个条件表达式的每个分支放进一个子类。利用"多态"去掉条件表达式
* 问题： 如果一组条件表达式在程序许多地点出现，如果想要添加新类型，就必须查找并更新所有条件表达式。违反开闭原则
* 使用多态，添加新类型，只需建立一个新的子类，并在其中提供适当的函数就行了。类的用户不需要了解这个子类。降低系统各部分之间的依赖。同时也符合开闭原则

* 做法：
  * 首先建立继承结构
    > 如果你需要在对象创建好之后修改类型码，就不能使用继承手法，只能使用state/strategy模式 Replace Type code with State/Strategy(以State/Strategy取代类型码)。如果不是可以尽可能使用比较简单的Replace Type Code with Subclasses（以类取代类型码）
    > <br/>如果若干switch语句针对的是同一个类型码，你只需针对这个类型码建立一个继承结构就行了。
    > <br/>如果要重构的类已经有了子类，那么你也得使用state/strategy模式
  * 如果要处理的条件表达式是一个更大函数中的一部分，使用Extract Method (提炼函数)将它提炼到一个独立函数中去。
  * 如果有必要，使用Move Method (搬移函数) 将条件表达式放置到继承结构的顶端
  * 任选一个子类，在其中建立一个函数，使之覆写超类中容纳条件表达式的那个函数。将与该子类相关的条件表达式分支复制到新建函数中，并对它进行适当调整。
    * 为了顺利进行这一步骤，你可能需要将超类中的某些private字段声明为protected。
    > 说的直接点，在子类新建一个函数，和父类中函数的签名相同，并把和这个分支相关的条件代码要做的代码复制过来
  * 编译，测试
  * 在超类中删除条件表达式内被复制的分支
  * 编译，测试
  * 针对条件表达式的每个分支，重复上述过程，直到所有分支都被移动到子类内的函数为止
  * 将超类之中容纳条件表达式的函数声明为抽象函数

## 总结
* 对于条件表达式如果可以，我们可以选择把它子类化。
  * Replace Conditional with Polymorphism(以多态取代条件表达式)
* 如果不能子类化，我们争取做到把代码描述从"做什么”的语句换成了“为什么这样做”
  * Decompose Conditional(分解条件表达式)：把条件抽取成独立的方法用"为什么"来命名
  * Consolidate Conditional Expression(合并条件表达式)：把相关的条件检查一起放到"为什么"里
* 我们还需要注意区分哪些东西会变化，哪些东西不会变化
  * Consolidate Duplicate Conditional Fragments(合并重复的条件片段)
* 对于特殊的条件表达式，如果触发时对函数剩余部分不再有兴趣，或者必须为真的假设，应该通过代码明确表示出来
  * Remove Control Flag(移除控制标记)
  * Replace Nested Conditional with Guard Clauses (以卫语句取代嵌套条件表达式)
  * Introduce Assertion(引入断言)
* 如果想去掉null的再三检查，我们可以
  * Introduce Null Object (引入Null对象)或Special Case模式