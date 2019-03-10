package com.github.chen0040.rl.learning.rlearn;

import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class RAgent implements Serializable {
    private RLearner learner;
    private int currentState;
    private int currentAction;
    private double currentValue;

    public RAgent() {

    }

    public RAgent(final int stateCount, final int actionCount, final double alpha, final double beta, final double rho, final double initialQ) {
        this.learner = new RLearner(stateCount, actionCount, alpha, beta, rho, initialQ);
    }

    public RAgent(final int stateCount, final int actionCount) {
        this.learner = new RLearner(stateCount, actionCount);
    }

    @SuppressWarnings("Used-by-user")
    public int getCurrentState() {
        return this.currentState;
    }

    @SuppressWarnings("Used-by-user")
    public int getCurrentAction() {
        return this.currentAction;
    }

    public void start(final int currentState) {
        this.currentState = currentState;
    }

    public RAgent makeCopy() {
        final RAgent clone = new RAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(final RAgent rhs) {
        this.currentState = rhs.currentState;
        this.currentAction = rhs.currentAction;
        this.learner.copy(rhs.learner);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RAgent) {
            final RAgent rhs = (RAgent) obj;
            return this.learner.equals(rhs.learner) && this.currentAction == rhs.currentAction && this.currentState == rhs.currentState;
        }
        return false;
    }

    public IndexValue selectAction() {
        return this.selectAction(null);
    }

    public IndexValue selectAction(final Set<Integer> actionsAtState) {
        if (this.currentAction == -1) {
            final IndexValue iv = this.learner.selectAction(this.currentState, actionsAtState);
            this.currentAction = iv.getIndex();
            this.currentValue = iv.getValue();
        }
        return new IndexValue(this.currentAction, this.currentValue);
    }

    public void update(final int newState, final double immediateReward) {
        this.update(newState, null, immediateReward);
    }

    public void update(final int newState, final Set<Integer> actionsAtState, final double immediateReward) {
        if (this.currentAction != -1) {
            this.learner.update(this.currentState, this.currentAction, newState, actionsAtState, immediateReward);
            this.currentState = newState;
            this.currentAction = -1;
        }
    }

    public RLearner getLearner() {
        return this.learner;
    }

    public void setLearner(final RLearner learner) {
        this.learner = learner;
    }
}
