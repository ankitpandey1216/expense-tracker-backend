package com.ankit.expense_tracker.service.factory;

import com.ankit.expense_tracker.entities.SplitType;
import com.ankit.expense_tracker.service.strategy.CustomSplitStrategy;
import com.ankit.expense_tracker.service.strategy.EqualSplitStrategy;
import com.ankit.expense_tracker.service.strategy.SplitStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SplitStrategyFactory {
    @Autowired
    private EqualSplitStrategy equalSplitStrategy;
    @Autowired
    private CustomSplitStrategy customSplitStrategy;

    public SplitStrategy getSplitStrategy(SplitType splitType){
        if(splitType == SplitType.CUSTOM){
            return customSplitStrategy;
        }
        if(splitType == SplitType.EQUAL){
            return equalSplitStrategy;
        }
        throw new RuntimeException("Could not find a split strategy");
    }

}
