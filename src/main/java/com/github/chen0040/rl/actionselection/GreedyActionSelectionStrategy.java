package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class GreedyActionSelectionStrategy extends AbstractActionSelectionStrategy {
    @Override
    public IndexValue selectAction(final int stateId, final QModel model, final Set<Integer> actionsAtState) {
        return model.actionWithMaxQAtState(stateId, actionsAtState);
    }

    @Override
    public Object clone() {
        return new GreedyActionSelectionStrategy();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof GreedyActionSelectionStrategy;
    }
}
