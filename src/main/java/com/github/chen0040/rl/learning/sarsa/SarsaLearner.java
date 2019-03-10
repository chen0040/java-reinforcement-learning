package com.github.chen0040.rl.learning.sarsa;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.rl.actionselection.AbstractActionSelectionStrategy;
import com.github.chen0040.rl.actionselection.ActionSelectionStrategy;
import com.github.chen0040.rl.actionselection.ActionSelectionStrategyFactory;
import com.github.chen0040.rl.actionselection.EpsilonGreedyActionSelectionStrategy;
import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027. Implement temporal-difference learning Q-Learning, which is an off-policy TD
 * control algorithm Q is known as the quality of state-action combination, note that it is different from utility of a
 * state
 */
public class SarsaLearner implements Serializable, Cloneable {
    protected QModel model;
    private ActionSelectionStrategy actionSelectionStrategy;

    public SarsaLearner() {

    }

    public SarsaLearner(final int stateCount, final int actionCount) {
        this(stateCount, actionCount, 0.1, 0.7, 0.1);
    }

    public SarsaLearner(final QModel model, final ActionSelectionStrategy actionSelectionStrategy) {
        this.model = model;
        this.actionSelectionStrategy = actionSelectionStrategy;
    }

    public SarsaLearner(final int stateCount, final int actionCount, final double alpha, final double gamma, final double initialQ) {
        this.model = new QModel(stateCount, actionCount, initialQ);
        this.model.setAlpha(alpha);
        this.model.setGamma(gamma);
        this.actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    @SuppressWarnings("Used-by-user")
    public static SarsaLearner fromJson(final String json) {
        return JSON.parseObject(json, SarsaLearner.class);
    }

    public static void main(final String[] args) {
        final int stateCount = 100;
        final int actionCount = 10;

        final SarsaLearner learner = new SarsaLearner(stateCount, actionCount);

        double reward; // reward gained by transiting from prevState to currentState
        final Random random = new Random();
        int currentStateId = random.nextInt(stateCount);
        int currentActionId = learner.selectAction(currentStateId).getIndex();

        for (int time = 0; time < 1000; ++time) {

            System.out.println("Controller does action-" + currentActionId);

            final int newStateId = random.nextInt(actionCount);
            reward = random.nextDouble();

            System.out.println("Now the new state is " + newStateId);
            System.out.println("Controller receives Reward = " + reward);

            final int futureActionId = learner.selectAction(newStateId).getIndex();

            System.out.println("Controller is expected to do action-" + futureActionId);

            learner.update(currentStateId, currentActionId, newStateId, futureActionId, reward);

            currentStateId = newStateId;
            currentActionId = futureActionId;
        }
    }

    @SuppressWarnings("Used-by-user")
    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public SarsaLearner makeCopy() {
        final SarsaLearner clone = new SarsaLearner();
        clone.copy(this);
        return clone;
    }

    public void copy(final SarsaLearner rhs) {
        this.model = rhs.model.makeCopy();
        this.actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SarsaLearner) {
            final SarsaLearner rhs = (SarsaLearner) obj;
            if (!this.model.equals(rhs.model)) {
                return false;
            }
            return this.actionSelectionStrategy.equals(rhs.actionSelectionStrategy);
        }
        return false;
    }

    public QModel getModel() {
        return this.model;
    }

    public void setModel(final QModel model) {
        this.model = model;
    }

    @SuppressWarnings("Used-by-user")
    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(this.actionSelectionStrategy);
    }

    @SuppressWarnings("Used-by-user")
    public void setActionSelection(final String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }

    public IndexValue selectAction(final int stateId, final Set<Integer> actionsAtState) {
        return this.actionSelectionStrategy.selectAction(stateId, this.model, actionsAtState);
    }

    public IndexValue selectAction(final int stateId) {
        return this.selectAction(stateId, null);
    }

    public void update(final int stateId, final int actionId, final int nextStateId, final int nextActionId, final double immediateReward) {
        // old_value is $Q_t(s_t, a_t)$
        final double oldQ = this.model.getQ(stateId, actionId);

        // learning_rate;
        final double alpha = this.model.getAlpha(stateId, actionId);

        // discount_rate;
        final double gamma = this.model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        final double nextQ = this.model.getQ(nextStateId, nextActionId);

        // learned_value = immediate_reward + gamma * estimate_of_optimal_future_value
        // old_value = oldQ
        // temporal_difference = learned_value - old_value
        // new_value = old_value + learning_rate * temporal_difference
        final double newQ = oldQ + alpha * (immediateReward + gamma * nextQ - oldQ);

        // new_value is $Q_{t+1}(s_t, a_t)$
        this.model.setQ(stateId, actionId, newQ);
    }


}
