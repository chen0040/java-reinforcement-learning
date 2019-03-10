package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.*;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class EpsilonGreedyActionSelectionStrategy extends AbstractActionSelectionStrategy {
    private static final String EPSILON = "epsilon";
    private Random random = new Random();

    @SuppressWarnings("Used-by-user")
    public EpsilonGreedyActionSelectionStrategy() {
        this.epsilon();
    }

    @SuppressWarnings("Used-by-user")
    public EpsilonGreedyActionSelectionStrategy(final HashMap<String, String> attributes) {
        super(attributes);
    }

    @SuppressWarnings("Used-by-user")
    public EpsilonGreedyActionSelectionStrategy(final Random random) {
        this.random = random;
        this.epsilon();
    }

    @Override
    public Object clone() {
        final EpsilonGreedyActionSelectionStrategy clone = new EpsilonGreedyActionSelectionStrategy();
        clone.copy(this);
        return clone;
    }

    public void copy(final EpsilonGreedyActionSelectionStrategy rhs) {
        this.random = rhs.random;
        for (final Map.Entry<String, String> entry : rhs.attributes.entrySet()) {
            this.attributes.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof EpsilonGreedyActionSelectionStrategy && this.epsilon() == ((EpsilonGreedyActionSelectionStrategy) obj).epsilon();
    }

    private double epsilon() {
        return Double.parseDouble(this.attributes.get(EpsilonGreedyActionSelectionStrategy.EPSILON));
    }

    @Override
    public IndexValue selectAction(final int stateId, final QModel model, final Set<Integer> actionsAtState) {
        if (this.random.nextDouble() < 1 - this.epsilon()) {
            return model.actionWithMaxQAtState(stateId, actionsAtState);
        } else {
            final int actionId;
            if (actionsAtState != null && !actionsAtState.isEmpty()) {
                final List<Integer> actions = new ArrayList<>(actionsAtState);
                actionId = actions.get(this.random.nextInt(actions.size()));
            } else {
                actionId = this.random.nextInt(model.getActionCount());
            }

            final double Q = model.getQ(stateId, actionId);
            return new IndexValue(actionId, Q);
        }
    }
}
