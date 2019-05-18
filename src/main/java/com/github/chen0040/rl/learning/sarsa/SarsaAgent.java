package com.github.chen0040.rl.learning.sarsa;

import com.github.chen0040.rl.utils.IndexValue;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

/**
 * Created by xschen on 9/27/2015 0027. Implement temporal-difference learning Sarsa, which is an
 * on-policy TD control algorithm
 */
public class SarsaAgent implements Serializable {
	private SarsaLearner learner;
	private int currentState;
	private int currentAction;
	private double currentValue;
	private int prevState;
	private int prevAction;

	public int getCurrentState() {
		return currentState;
	}

	public int getCurrentAction() {
		return currentAction;
	}

	public int getPrevState() { return prevState; }

	public int getPrevAction() { return prevAction; }

	public void start(int currentState) {
		this.currentState = currentState;
		this.prevState = -1;
		this.prevAction = -1;
	}

	public IndexValue selectAction() {
		return selectAction(null);
	}

	public IndexValue selectAction(Set<Integer> actionsAtState) {
		if (currentAction == -1) {
			IndexValue iv = learner.selectAction(currentState, actionsAtState);
			currentAction = iv.getIndex();
			currentValue = iv.getValue();
		}

		return new IndexValue(currentAction, currentValue);
	}

	public void update(int actionTaken, int newState, double immediateReward) {
		update(actionTaken, newState, null, immediateReward);
	}

	public void update(int actionTaken, int newState, Set<Integer> actionsAtNewState, double immediateReward) {

		IndexValue iv = learner.selectAction(currentState, actionsAtNewState);
		int futureAction = iv.getIndex();

		learner.update(currentState, actionTaken, newState, futureAction, immediateReward);

		prevState = this.currentState;
		this.prevAction = actionTaken;

		currentAction = futureAction;
		currentState = newState;
	}

	public SarsaLearner getLearner() {
		return learner;
	}

	public void setLearner(SarsaLearner learner) {
		this.learner = learner;
	}

	public SarsaAgent(int stateCount, int actionCount, double alpha, double gamma, double initialQ) {
		learner = new SarsaLearner(stateCount, actionCount, alpha, gamma, initialQ);
	}

	public SarsaAgent(int stateCount, int actionCount) {
		learner = new SarsaLearner(stateCount, actionCount);
	}

	public SarsaAgent(SarsaLearner learner) {
		this.learner = learner;
	}

	public SarsaAgent() {

	}

	public void enableEligibilityTrace(double lambda) {
		SarsaLambdaLearner acll = new SarsaLambdaLearner(learner);
		acll.setLambda(lambda);
		learner = acll;
	}

	public SarsaAgent makeCopy() {
		SarsaAgent clone = new SarsaAgent();
		clone.copy(this);
		return clone;
	}

	public void copy(SarsaAgent rhs) {
		learner.copy(rhs.learner);
		currentAction = rhs.currentAction;
		currentState = rhs.currentState;
		prevAction = rhs.prevAction;
		prevState = rhs.prevState;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SarsaAgent) {
			SarsaAgent rhs = (SarsaAgent) obj;
			return prevAction == rhs.prevAction
					&& prevState == rhs.prevState
					&& currentAction == rhs.currentAction
					&& currentState == rhs.currentState
					&& learner.equals(rhs.learner);
		}
		return false;
	}
}
