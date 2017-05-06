package com.github.chen0040.rl.learning.qlearn;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class QAgent implements Serializable{
    private QLearner learner;
    private int currentState;
    private int prevState;

    /** action taken at prevState */
    private int prevAction;

    public int getCurrentState(){
        return currentState;
    }

    public int getPrevState(){
        return prevState;
    }

    public int getPrevAction(){
        return prevAction;
    }

    public void start(int currentState){
        this.currentState = currentState;
        this.prevAction = -1;
        this.prevState = -1;
    }

    public int selectAction(){
        return learner.selectAction(currentState);
    }

    public int selectAction(Set<Integer> actionsAtState){
        return learner.selectAction(currentState, actionsAtState);
    }

    public void update(int actionTaken, int newState, double immediateReward){
        update(actionTaken, newState, null, immediateReward);
    }

    public void update(int actionTaken, int newState, Set<Integer> actionsAtNewState, double immediateReward){

        learner.update(currentState, actionTaken, newState, actionsAtNewState, immediateReward);

        prevState = currentState;
        prevAction = actionTaken;

        currentState = newState;
    }

    public void enableEligibilityTrace(double lambda){
        QLambdaLearner acll = new QLambdaLearner(learner);
        acll.setLambda(lambda);
        learner = acll;
    }

    public QLearner getLearner(){
        return learner;
    }

    public void setLearner(QLearner learner){
        this.learner = learner;
    }

    public QAgent(int stateCount, int actionCount, double alpha, double gamma, double initialQ){
        learner = new QLearner(stateCount, actionCount, alpha, gamma, initialQ);
    }

    public QAgent(QLearner learner){
        this.learner = learner;
    }

    public QAgent(int stateCount, int actionCount){
        learner = new QLearner(stateCount, actionCount);
    }

    public QAgent(){

    }

    public QAgent makeCopy(){
        QAgent clone = new QAgent();
        clone.copy(this);
        return clone;
    }

    public void copy(QAgent rhs){
        learner.copy(rhs.learner);
        prevAction = rhs.prevAction;
        prevState = rhs.prevState;
        currentState = rhs.currentState;
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null && obj instanceof QAgent){
            QAgent rhs = (QAgent)obj;
            return prevAction == rhs.prevAction && prevState == rhs.prevState && currentState == rhs.currentState && learner.equals(rhs.learner);
        }
        return false;
    }
}
