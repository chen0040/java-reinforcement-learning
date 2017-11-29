package com.github.chen0040.rl.learning.sarsa;


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
 * Created by xschen on 9/27/2015 0027.
 * Implement temporal-difference learning Q-Learning, which is an off-policy TD control algorithm
 * Q is known as the quality of state-action combination, note that it is different from utility of a state
 */
public class SarsaLearner implements Serializable,Cloneable {
    protected QModel model;
    private ActionSelectionStrategy actionSelectionStrategy;

    public SarsaLearner makeCopy(){
        SarsaLearner clone = new SarsaLearner();
        clone.copy(this);
        return clone;
    }

    public void copy(SarsaLearner rhs){
        model = (QModel)rhs.model.clone();
        actionSelectionStrategy = (ActionSelectionStrategy)((AbstractActionSelectionStrategy) rhs.actionSelectionStrategy).clone();
    }

    @Override
    public boolean equals(Object obj){
        if(obj !=null && obj instanceof SarsaLearner){
            SarsaLearner rhs = (SarsaLearner)obj;
            if(!model.equals(rhs.model)) return false;
            return actionSelectionStrategy.equals(rhs.actionSelectionStrategy);
        }
        return false;
    }

    public QModel getModel() {
        return model;
    }

    public void setModel(QModel model) {
        this.model = model;
    }

    public String getActionSelection() {
        return ActionSelectionStrategyFactory.serialize(actionSelectionStrategy);
    }

    public void setActionSelection(String conf) {
        this.actionSelectionStrategy = ActionSelectionStrategyFactory.deserialize(conf);
    }

    public SarsaLearner(){

    }

    public SarsaLearner(int stateCount, int actionCount){
        this(stateCount, actionCount, 0.1, 0.7, 0.1);
    }

    public SarsaLearner(QModel model, ActionSelectionStrategy actionSelectionStrategy){
        this.model = model;
        this.actionSelectionStrategy = actionSelectionStrategy;
    }

    public SarsaLearner(int stateCount, int actionCount, double alpha, double gamma, double initialQ)
    {
        model = new QModel(stateCount, actionCount, initialQ);
        model.setAlpha(alpha);
        model.setGamma(gamma);
        actionSelectionStrategy = new EpsilonGreedyActionSelectionStrategy();
    }

    public static void main(String[] args){
        int stateCount = 100;
        int actionCount = 10;

        SarsaLearner learner = new SarsaLearner(stateCount, actionCount);

        double reward = 0; // reward gained by transiting from prevState to currentState
        Random random = new Random();
        int currentStateId = random.nextInt(stateCount);
        int currentActionId = learner.selectAction(currentStateId).getIndex();

        for(int time=0; time < 1000; ++time){

            System.out.println("Controller does action-"+currentActionId);

            int newStateId = random.nextInt(actionCount);
            reward = random.nextDouble();

            System.out.println("Now the new state is " + newStateId);
            System.out.println("Controller receives Reward = " + reward);

            int futureActionId = learner.selectAction(newStateId).getIndex();

            System.out.println("Controller is expected to do action-"+futureActionId);

            learner.update(currentStateId, currentActionId, newStateId, futureActionId, reward);

            currentStateId = newStateId;
            currentActionId = futureActionId;
        }
    }


    public IndexValue selectAction(int stateId, Set<Integer> actionsAtState){
        return actionSelectionStrategy.selectAction(stateId, model, actionsAtState);
    }

    public IndexValue selectAction(int stateId){
        return selectAction(stateId, null);
    }

    public void update(int stateId, int actionId, int nextStateId, int nextActionId, double immediateReward)
    {
        // old_value is $Q_t(s_t, a_t)$
        double oldQ = model.getQ(stateId, actionId);

        // learning_rate;
        double alpha = model.getAlpha(stateId, actionId);

        // discount_rate;
        double gamma = model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        double nextQ = model.getQ(nextStateId, nextActionId);

        // learned_value = immediate_reward + gamma * estimate_of_optimal_future_value
        // old_value = oldQ
        // temporal_difference = learned_value - old_value
        // new_value = old_value + learning_rate * temporal_difference
        double newQ = oldQ + alpha * (immediateReward + gamma * nextQ - oldQ);

        // new_value is $Q_{t+1}(s_t, a_t)$
        model.setQ(stateId, actionId, newQ);
    }



}
