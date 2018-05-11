package study.strategy;

/**
 * 策略接口
 * @author dell
 *
 */
public interface Strategy {
    //执行的方法，由于java中方法的执行，对象会根据具体类型执行对应子类的方法，
    // 所以实际实现中会执行对应子类的方法(自认为这个原理是策略模式的根本)
    public void operate();
}
