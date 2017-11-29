package com.github.chen0040.rl.learning.rlearn;


import com.github.chen0040.rl.utils.IndexValue;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;


/**
 * Created by xschen on 6/5/2017.
 */
public class RAgentUnitTest {

   @Test
   public void test_r_learn(){

         int stateCount = 100;
         int actionCount = 10;
         RAgent agent = new RAgent(stateCount, actionCount);

         Random random = new Random();
         agent.start(random.nextInt(stateCount));
         for(int time=0; time < 1000; ++time){

             IndexValue actionValue = agent.selectAction();
            int actionId = actionValue.getIndex();
            System.out.println("Agent does action-"+actionId);

            int newStateId = random.nextInt(actionCount);
            double reward = random.nextDouble();

            System.out.println("Now the new state is "+newStateId);
            System.out.println("Agent receives Reward = "+reward);

            agent.update(newStateId, reward);
         }

   }
}
