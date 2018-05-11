package study.strategy.bean;

/**
 * 策略枚举(增加新策略时需要添加数据)
 * @author dell
 *
 */
public enum StatusEnum {

    StrateryA("study.strategy.StrategyA"),
    StrateryB("study.strategy.StrategyB");

    String value = "";

    private StatusEnum(String value){
        this.value = value;
    }

    public String getvalue(){
        return this.value;
    }
}
