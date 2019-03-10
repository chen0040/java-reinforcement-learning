package com.github.chen0040.rl.learning.sarsa;

import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027. Implement temporal-difference learning Sarsa, which is an on-policy TD control
 * algorithm
 */
public class SarsaAgent implements Serializable {
    private SarsaLearner learner;
    private int currentState;
    private int currentAction;
    private double currentValue;
    private int prevState;
    private int prevAction;

    public SarsaAgent(final int stateCount, final int actionCount, final double alpha, final double gamma, final double initialQ) {
        this.learner = new SarsaLearner(stateCount, actionCount, alpha, gamma, initialQ);
    }

    public SarsaAgent(final int stateCount, final int actionCount) {
        this.learner = new SarsaLearner(stateCount, actionCount);
    }

    public SarsaAgent(final SarsaLearner learner) {
        this.learner = learner;
    }

    public SarsaAgent() {

    }

    @SuppressWarnings("Used-by-user")
    public int getCurrentState() {
        return this.currentState;
    }

    @SuppressWarnings("Used-by-user")
    public int getCurrentAction() {
        return this.currentAction;
    }

    @SuppressWarnings("Used-by-user")
    public int getPrevState() {
        return this.prevState;
    }

    @SuppressWarnings("Used-by-user")
    public int getPrevAction() {
        return this.prevAction;
    }

    public void start(final int currentState) {
        this.currentState = currentState;
        this.prevState = -1;
        this.prevAction = -1;
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

    public void update(final int actionTaken, final int newState, final double immediateReward) {
        this.update(actionTaken, newState, null, immediateReward);
    }

    public void update(final int actionTaken, final int newState, final Set<Integer> actionsAtNewState, final double immediateReward) {

        final IndexValue iv = this.learner.selectAction(this.currentState, actionsAtNewState);
        final int futureAction = iv.getIndex();

        this.learner.update(this.currentState, actionTaken, newState, futureAction, immediateReward);

        this.prevState = this.currentState;
        this.prevAction = actionTaken;

        this.currentAction = futureAction;
        this.currentState = newState;
    }

    public SarsaLearner getLearner() {
        return this.learner;
    }

    public void setLearner(final SarsaLearner learner) {
        this.learner = learner;
    }

    @SuppressWarnings("Used-by-user")
    public void enableEligibilityTrace(final double lambda) {
        final SarsaLambdaLearner acll = new SarsaLambdaLearner(this.learner);
        acll.setLambda(lambda);
        this.learner = acll;
    }

    public SarsaAgent makeCopy() {
        final SarsaAgent clone = new SarsaAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(final SarsaAgent rhs) {
        this.learner.copy(rhs.learner);
        this.currentAction = rhs.currentAction;
        this.currentState = rhs.currentState;
        this.prevAction = rhs.prevAction;
        this.prevState = rhs.prevState;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SarsaAgent) {
            final SarsaAgent rhs = (SarsaAgent) obj;
            return this.prevAction == rhs.prevAction
                    && this.prevState == rhs.prevState
                    && this.currentAction == rhs.currentAction
                    && this.currentState == rhs.currentState
                    && this.learner.equals(rhs.learner);
        }
        return false;
    }
}
