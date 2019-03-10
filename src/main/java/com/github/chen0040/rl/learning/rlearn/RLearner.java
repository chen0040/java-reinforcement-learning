package com.github.chen0040.rl.learning.rlearn;


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


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class RLearner implements Serializable, Cloneable {

    private QModel model;
    private ActionSelectionStrategy actionSelectionStrategy;
    private double rho;
    private double beta;

    public RLearner() {

    }

    public RLearner(final int stateCount, final int actionCount) {
        this(stateCount, actionCount, 0.1, 0.1, 0.7, 0.1);
    }

    public RLearner(final int state_count, final int action_count, final double alpha, final double beta, final double rho, final double initial_Q) {
        this.model = new QModel(state_count, action_count, initial_Q);
        this.model.setAlpha(alpha);

        this.rho = rho;
        this.beta = beta;

        this.actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    public static RLearner fromJson(final String json) {
        return JSON.parseObject(json, RLearner.class);
    }

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }

    public RLearner makeCopy() {
        final RLearner clone = new RLearner();
        clone.copy(this);
        return clone;
    }

    public void copy(final RLearner rhs) {
        this.model = rhs.model.makeCopy();
        this.actionSelectionStrategy = (ActionSelectionStrategy) ((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
        this.rho = rhs.rho;
        this.beta = rhs.beta;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RLearner) {
            final RLearner rhs = (RLearner) obj;
            if (!this.model.equals(rhs.model)) {
                return false;
            }
            if (!this.actionSelectionStrategy.equals(rhs.actionSelectionStrategy)) {
                return false;
            }
            if (this.rho != rhs.rho) {
                return false;
            }
            return this.beta == rhs.beta;
        }
        return false;
    }

    public double getRho() {
        return this.rho;
    }

    public void setRho(final double rho) {
        this.rho = rho;
    }

    public double getBeta() {
        return this.beta;
    }

    public void setBeta(final double beta) {
        this.beta = beta;
    }

    public QModel getModel() {
        return this.model;

    }

    public void setModel(final QModel model) {
        this.model = model;
    }

    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(this.actionSelectionStrategy);
    }

    public void setActionSelection(final String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }

    private double maxQAtState(final int stateId, final Set<Integer> actionsAtState) {
        return this.model.actionWithMaxQAtState(stateId, actionsAtState).getValue();
    }

    public void update(final int currentState, final int actionTaken, final int newState, final Set<Integer> actionsAtNextStateId, final double immediate_reward) {
        final double oldQ = this.model.getQ(currentState, actionTaken);
        final double alpha = this.model.getAlpha(currentState, actionTaken); // learning rate;
        final double maxQ = this.maxQAtState(newState, actionsAtNextStateId);
        final double newQ = oldQ + alpha * (immediate_reward - this.rho + maxQ - oldQ);
        final double maxQAtCurrentState = this.maxQAtState(currentState, null);
        if (newQ == maxQAtCurrentState) {
            this.rho += this.beta * (immediate_reward - this.rho + maxQ - maxQAtCurrentState);
        }
        this.model.setQ(currentState, actionTaken, newQ);
    }

    public IndexValue selectAction(final int stateId, final Set<Integer> actionsAtState) {
        return this.actionSelectionStrategy.selectAction(stateId, this.model, actionsAtState);
    }
}
