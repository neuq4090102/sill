package com.elv.traning.designMode.mobileParser;

import java.util.Iterator;
import java.util.List;

import com.elv.core.util.RandomUtil;
import com.elv.traning.designMode.mobileParser.strategy.IStrategy;

/**
 * @author lxh
 * @since 2020-06-29
 */
public abstract class AbstractParser {

    protected AbstractParser nextParser; // 其他解析方式
    protected List<IStrategy> strategies; // 策略列表

    /**
     * 抓取策略
     *
     * @param mobile 手机号
     * @return com.elv.traning.designMode.mobileParser.MobileResult
     */
    protected abstract MobileResult parse(String mobile);

    /**
     * 默认抓取策略
     *
     * @param mobile 手机号
     * @return com.elv.traning.designMode.mobileParser.MobileResult
     */
    protected MobileResult defaultParse(String mobile) {
        List<IStrategy> strategies = this.getStrategies();
        if (strategies == null || strategies.size() == 0) {
            return null;
        }

        // 先随机取一个策略（防止因频繁调用某个接口而被拉黑），若可以获取，则返回结果
        IStrategy randomStrategy = strategies.get(RandomUtil.randomInt(strategies.size()));
        MobileResult mobileResult = randomStrategy.defaultFetch(mobile);
        if (mobileResult != null) {
            return mobileResult;
        }

        // 否则，逐个策略挨个获取，直到取到期望结果
        Iterator<IStrategy> iterator = strategies.iterator();
        while (iterator.hasNext()) {
            IStrategy strategy = iterator.next();
            if (randomStrategy == strategy) {
                iterator.remove();
            } else {
                MobileResult result = strategy.defaultFetch(mobile);
                if (result != null) {
                    return result;
                } else {
                    iterator.remove();
                }
            }
        }

        return null;
    }

    public MobileResult deal(String mobile) {
        MobileResult result = this.parse(mobile);
        if (result != null) {
            // 找到目标数据
            return result;
        } else if (this.getNextParser() != null) {
            return this.getNextParser().deal(mobile);
        }

        return null;
    }

    public AbstractParser getNextParser() {
        return nextParser;
    }

    public void setNextParser(AbstractParser nextParser) {
        this.nextParser = nextParser;
    }

    public List<IStrategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<IStrategy> strategies) {
        this.strategies = strategies;
    }

    public AbstractParser strategies(List<IStrategy> strategies) {
        this.strategies = strategies;
        return this;
    }
}
