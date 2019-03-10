package com.github.chen0040.rl.learning.qlearn;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.rl.actionselection.AbstractActionSelectionStrategy;
import com.github.chen0040.rl.actionselection.ActionSelectionStrategy;
import com.github.chen0040.rl.actionselection.ActionSelectionStrategyFactory;
import com.github.chen0040.rl.actionselection.EpsilonGreedyActionSelectionStrategy;
import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Set;

import static com.github.chen0040.rl.models.DefaultValues.*;


/**
 * Created by xschen on 9/27/2015 0027. Implement temporal-difference learning Q-Learning, which is an off-policy TD
 * control algorithm Q is known as the quality of state-action combination, note that it is different from utility of a
 * state
 */
public class QLearner implements Serializable, Cloneable {
    protected QModel model;

    private ActionSelectionStrategy actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();

    public QLearner() {

    }

    public QLearner(final int stateCount, final int actionCount) {
        this(stateCount, actionCount, ALPHA, GAMMA, INITIAL_Q);
    }

    public QLearner(final QModel model, final ActionSelectionStrategy actionSelectionStrategy) {
        this.model = model;
        this.actionSelectionStrategy = actionSelectionStrategy;
    }

    public QLearner(final int stateCount, final int actionCount, final double alpha, final double gamma, final double initialQ) {
        this.model = new QModel(stateCount, actionCount, initialQ);
        this.model.setAlpha(alpha);
        this.model.setGamma(gamma);
        this.actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    @SuppressWarnings("Used-by-user")
    public static QLearner fromJson(final String json) {
        return JSON.parseObject(json, QLearner.class);
    }

    public QLearner makeCopy() {
        final QLearner clone = new QLearner();
        clone.copy(this);
        return clone;
    }

    @SuppressWarnings("Used-by-user")
    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public void copy(final QLearner rhs) {
        this.model = rhs.model.makeCopy();
        this.actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof QLearner) {
            final QLearner rhs = (QLearner) obj;
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

    double maxQAtState(final int stateId, final Set<Integer> actionsAtState) {
        return this.model.actionWithMaxQAtState(stateId, actionsAtState).getValue();
    }

    @SuppressWarnings("Used-by-user")
    public IndexValue selectAction(final int stateId, final Set<Integer> actionsAtState) {
        return this.actionSelectionStrategy.selectAction(stateId, this.model, actionsAtState);
    }

    @SuppressWarnings("Used-by-user")
    public IndexValue selectAction(final int stateId) {
        return this.selectAction(stateId, null);
    }


    public void update(final int stateId, final int actionId, final int nextStateId, final double immediateReward) {
        this.update(stateId, actionId, nextStateId, null, immediateReward);
    }

    public void update(final int stateId, final int actionId, final int nextStateId, final Set<Integer> actionsAtNextStateId, final double immediateReward) {
        // old_value is $Q_t(s_t, a_t)$
        final double oldQ = this.model.getQ(stateId, actionId);

        // learning_rate;
        final double alpha = this.model.getAlpha(stateId, actionId);

        // discount_rate;
        final double gamma = this.model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        final double maxQ = this.maxQAtState(nextStateId, actionsAtNextStateId);

        // learned_value = immediate_reward + gamma * estimate_of_optimal_future_value
        // old_value = oldQ
        // temporal_difference = learned_value - old_value
        // new_value = old_value + learning_rate * temporal_difference
        final double newQ = oldQ + alpha * (immediateReward + gamma * maxQ - oldQ);

        // new_value is $Q_{t+1}(s_t, a_t)$
        this.model.setQ(stateId, actionId, newQ);
    }


}
