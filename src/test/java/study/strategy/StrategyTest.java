package study.strategy;

/**
 * 策略模式+工厂模式
 * @author dell
 * @version 1.0   使用策略发生变化时，需要修改1.封装策略类的具体声测策略方法 2.策略枚举类 增加新的策略 3.创建新的策略类并实现其具体方法
 */
public class StrategyTest {

    public static void main(String[] args) {

        int number = 15;
        //业务模块就一行代码搞定了，即使策略变化，或者使用策略的具体方案变了，我们只要修改策略代码就好了，业务代码不需要变化

        StrategyFacade.doOperate(number);
    }

}
