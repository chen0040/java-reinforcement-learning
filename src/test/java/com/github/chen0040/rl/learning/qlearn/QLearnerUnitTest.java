package com.github.chen0040.rl.learning.qlearn;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;

import org.testng.annotations.Test;

import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testng.Assert.*;

/**
 * Created by xschen on 6/5/2017.
 */
public class QLearnerUnitTest {

	private static final int stateCount = 100;
	private static final int actionCount = 10;

	@Test
	public void testJsonSerialization() {

		QLearner learner = new QLearner(stateCount, actionCount);

		run(learner);

		String json = learner.toJson();

		QLearner learner2 = QLearner.fromJson(json);

		assertThat(learner.getModel()).isEqualTo(learner2.getModel());

		assertThat(learner.getActionSelection()).isEqualTo(learner2.getActionSelection());

	}

	@Test
	public void test_q_learn() {

		QLearner learner = new QLearner(stateCount, actionCount);

		run(learner);

	}

	private void run(QLearner learner) {
		Random random = new Random();
		int currentStateId = random.nextInt(stateCount);
		for (int time = 0; time < 1000; ++time) {

			int actionId = learner.selectAction(currentStateId).getIndex();
			System.out.println("Controller does action-" + actionId);

			int newStateId = random.nextInt(actionCount);
			double reward = random.nextDouble();

			System.out.println("Now the new state is " + newStateId);
			System.out.println("Controller receives Reward = " + reward);

			learner.update(currentStateId, actionId, newStateId, reward);
			currentStateId = newStateId;
		}
	}
}
