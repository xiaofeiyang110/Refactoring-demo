package chapter9.src;

public class RemoveControlFlag {
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
     * 单独foundMiscreant 方法，return很明确的告诉我们不再执行该函数中的其他代码
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

    private void someLaterCode(String found) {
    }

    private void sendAlert() {
    }
}
