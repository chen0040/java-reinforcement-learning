package com.github.chen0040.rl.learning.actorcritic;

import com.github.chen0040.rl.utils.Vec;

import java.io.Serializable;
import java.util.Set;


/**
 * Created by chen0469 on 9/28/2015 0028.
 */
public class ActorCriticAgent implements Serializable {
    private ActorCriticLearner learner;
    private int currentState;
    private int prevState;
    private int prevAction;

    @SuppressWarnings("Used-by-user")
    public ActorCriticAgent(final int stateCount, final int actionCount) {
        this.learner = new ActorCriticLearner(stateCount, actionCount);
    }

    public ActorCriticAgent() {

    }

    public ActorCriticAgent(final ActorCriticLearner learner) {
        this.learner = learner;
    }

    @SuppressWarnings("Used-by-user")
    public void enableEligibilityTrace(final double lambda) {
        final ActorCriticLambdaLearner acll = new ActorCriticLambdaLearner(this.learner);
        acll.setLambda(lambda);
        this.learner = acll;
    }

    @SuppressWarnings("Used-by-user")
    public void start(final int stateId) {
        this.currentState = stateId;
        this.prevAction = -1;
        this.prevState = -1;
    }

    @SuppressWarnings("Used-by-user")
    public ActorCriticLearner getLearner() {
        return this.learner;
    }

    public void setLearner(final ActorCriticLearner learner) {
        this.learner = learner;
    }

    public ActorCriticAgent makeCopy() {
        final ActorCriticAgent clone = new ActorCriticAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(final ActorCriticAgent rhs) {
        this.learner = (ActorCriticLearner) rhs.learner.makeCopy();
        this.prevAction = rhs.prevAction;
        this.prevState = rhs.prevState;
        this.currentState = rhs.currentState;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ActorCriticAgent) {
            final ActorCriticAgent rhs = (ActorCriticAgent) obj;
            return this.learner.equals(rhs.learner) && this.prevAction == rhs.prevAction && this.prevState == rhs.prevState && this.currentState == rhs.currentState;

        }
        return false;
    }

    public int selectAction(final Set<Integer> actionsAtState) {
        return this.learner.selectAction(this.currentState, actionsAtState);
    }

    public int selectAction() {
        return this.learner.selectAction(this.currentState);
    }

    public void update(final int actionTaken, final int newState, final double immediateReward, final Vec V) {
        this.update(actionTaken, newState, null, immediateReward, V);
    }

    public void update(final int actionTaken, final int newState, final Set<Integer> actionsAtNewState, final double immediateReward, final Vec V) {

        this.learner.update(this.currentState, actionTaken, newState, actionsAtNewState, immediateReward, V::get);

        this.prevAction = actionTaken;
        this.prevState = this.currentState;

        this.currentState = newState;
    }

}
