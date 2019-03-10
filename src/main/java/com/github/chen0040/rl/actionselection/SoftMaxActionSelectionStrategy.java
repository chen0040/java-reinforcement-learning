package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.Random;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class SoftMaxActionSelectionStrategy extends AbstractActionSelectionStrategy {
    private Random random = new Random();

    public SoftMaxActionSelectionStrategy() {

    }

    public SoftMaxActionSelectionStrategy(final Random random) {
        this.random = random;
    }

    @Override
    public Object clone() {
        return new SoftMaxActionSelectionStrategy(this.random);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof SoftMaxActionSelectionStrategy;
    }

    @Override
    public IndexValue selectAction(final int stateId, final QModel model, final Set<Integer> actionsAtState) {
        return model.actionWithSoftMaxQAtState(stateId, actionsAtState, this.random);
    }
}
