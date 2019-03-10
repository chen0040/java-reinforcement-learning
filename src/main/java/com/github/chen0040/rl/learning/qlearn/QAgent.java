package com.github.chen0040.rl.learning.qlearn;

import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class QAgent implements Serializable {
    private QLearner learner;
    private int currentState;
    private int prevState;

    /** action taken at prevState */
    private int prevAction;

    public QAgent(final int stateCount, final int actionCount, final double alpha, final double gamma, final double initialQ) {
        this.learner = new QLearner(stateCount, actionCount, alpha, gamma, initialQ);
    }

    public QAgent(final QLearner learner) {
        this.learner = learner;
    }

    public QAgent(final int stateCount, final int actionCount) {
        this.learner = new QLearner(stateCount, actionCount);
    }

    public QAgent() {

    }

    @SuppressWarnings("Used-by-user")
    public int getCurrentState() {
        return this.currentState;
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
        this.prevAction = -1;
        this.prevState = -1;
    }

    public IndexValue selectAction() {
        return this.learner.selectAction(this.currentState);
    }

    public IndexValue selectAction(final Set<Integer> actionsAtState) {
        return this.learner.selectAction(this.currentState, actionsAtState);
    }

    public void update(final int actionTaken, final int newState, final double immediateReward) {
        this.update(actionTaken, newState, null, immediateReward);
    }

    public void update(final int actionTaken, final int newState, final Set<Integer> actionsAtNewState, final double immediateReward) {

        this.learner.update(this.currentState, actionTaken, newState, actionsAtNewState, immediateReward);

        this.prevState = this.currentState;
        this.prevAction = actionTaken;

        this.currentState = newState;
    }

    public void enableEligibilityTrace(final double lambda) {
        final QLambdaLearner acll = new QLambdaLearner(this.learner);
        acll.setLambda(lambda);
        this.learner = acll;
    }

    public QLearner getLearner() {
        return this.learner;
    }

    public void setLearner(final QLearner learner) {
        this.learner = learner;
    }

    public QAgent makeCopy() {
        final QAgent clone = new QAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(final QAgent rhs) {
        this.learner.copy(rhs.learner);
        this.prevAction = rhs.prevAction;
        this.prevState = rhs.prevState;
        this.currentState = rhs.currentState;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof QAgent) {
            final QAgent rhs = (QAgent) obj;
            return this.prevAction == rhs.prevAction && this.prevState == rhs.prevState && this.currentState == rhs.currentState && this.learner.equals(rhs.learner);
        }
        return false;
    }
}
