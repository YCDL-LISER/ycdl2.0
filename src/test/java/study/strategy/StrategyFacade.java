package study.strategy;

import study.strategy.bean.StatusEnum;
import study.strategy.bean.StrategyFactory;

/**
 * 封装执行的操作
 * @author dell
 *
 */
public class StrategyFacade {

    /**
     * 实际执行的策略
     * @param number
     * @return
     */
    public static boolean doOperate(int number){

        Strategy strategy = StrategyFactory.getStrategy(getStrategyType(number));
        strategy.operate();

        return true;
    }
    /**
     * 根据烈性条件生成不同的策略(使用策略场景变化是需要修改)
     * @param number
     * @return
     */
    private static StatusEnum getStrategyType(int number){

        if(number > 10){
            return StatusEnum.StrateryB;
        }else{
            return StatusEnum.StrateryA;
        }

    }

}