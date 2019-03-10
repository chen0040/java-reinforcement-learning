package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;


/**
 * Created by xschen on 9/28/2015 0028.
 */
public class GibbsSoftMaxActionSelectionStrategy extends AbstractActionSelectionStrategy {

    private final Random random;

    @SuppressWarnings("Used-by-user")
    public GibbsSoftMaxActionSelectionStrategy() {
        this.random = new Random();
    }

    @SuppressWarnings("Used-by-user")
    public GibbsSoftMaxActionSelectionStrategy(final Random random) {
        this.random = random;
    }

    @Override
    public Object clone() {
        return new GibbsSoftMaxActionSelectionStrategy();
    }

    @Override
    public IndexValue selectAction(final int stateId, final QModel model, final Set<Integer> actionsAtState) {
        final List<Integer> actions = new ArrayList<Integer>();
        if (actionsAtState == null) {
            IntStream.range(0, model.getActionCount()).forEach(actions::add);
        } else {
            actions.addAll(actionsAtState);
        }

        double sum = 0;
        final List<Double> plist = new ArrayList<Double>();
        for (final int actionId : actions) {
            final double p = Math.exp(model.getQ(stateId, actionId));
            sum += p;
            plist.add(sum);
        }

        final IndexValue iv = new IndexValue();
        iv.setIndex(-1);
        iv.setValue(Double.NEGATIVE_INFINITY);

        final double r = sum * this.random.nextDouble();
        for (int i = 0; i < actions.size(); ++i) {

            if (plist.get(i) >= r) {
                final int actionId = actions.get(i);
                iv.setValue(model.getQ(stateId, actionId));
                iv.setIndex(actionId);
                break;
            }
        }

        return iv;
    }
}
