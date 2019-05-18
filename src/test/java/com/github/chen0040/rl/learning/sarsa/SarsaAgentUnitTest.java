package com.github.chen0040.rl.learning.sarsa;

import org.testng.annotations.Test;

import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testng.Assert.*;

/**
 * Created by xschen on 6/5/2017.
 */
public class SarsaAgentUnitTest {

	@Test
	public void test_sarsa() {
		int stateCount = 100;
		int actionCount = 10;
		SarsaAgent agent = new SarsaAgent(stateCount, actionCount);

		double reward = 0; //immediate reward by transiting from prevState to currentState
		Random random = new Random();
		agent.start(random.nextInt(stateCount));
		int actionTaken = agent.selectAction().getIndex();
		for (int time = 0; time < 1000; ++time) {

			System.out.println("Agent does action-" + actionTaken);

			int newStateId = random.nextInt(actionCount);
			reward = random.nextDouble();

			System.out.println("Now the new state is " + newStateId);
			System.out.println("Agent receives Reward = " + reward);

			agent.update(actionTaken, newStateId, reward);
		}

		SarsaLearner learner = agent.getLearner();

		SarsaLearner learner2 = SarsaLearner.fromJson(learner.toJson());

		assertThat(learner2).isEqualTo(learner);
	}
}
