package study.strategy.bean;

import study.strategy.Strategy;

/**
 * 生成具体策略工厂方法
 * @author dell
 *
 */
public class StrategyFactory {

    public static Strategy getStrategy(StatusEnum status){

        Strategy strategy = null;

        try {
            strategy = (Strategy) Class.forName(status.getvalue()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("生成策略出错");
        }

        return strategy;
    }
}
