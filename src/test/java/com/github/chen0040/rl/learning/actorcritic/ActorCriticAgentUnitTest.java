package com.github.chen0040.rl.learning.actorcritic;


import com.github.chen0040.rl.utils.Vec;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;


/**
 * Created by xschen on 6/5/2017.
 */
public class ActorCriticAgentUnitTest {

   @Test
   public void test_learn(){
      int stateCount = 100;
      int actionCount = 10;

      ActorCriticAgent agent = new ActorCriticAgent(stateCount, actionCount);
      Vec stateValues = new Vec(stateCount);

      Random random = new Random();
      agent.start(random.nextInt(stateCount));
      for(int time=0; time < 1000; ++time){

         int actionId = agent.selectAction();
         System.out.println("Agent does action-"+actionId);

         int newStateId = random.nextInt(actionCount);
         double reward = random.nextDouble();

         System.out.println("Now the new state is "+newStateId);
         System.out.println("Agent receives Reward = "+reward);

         System.out.println("World state values changed ...");
         for(int stateId = 0; stateId < stateCount; ++stateId){
            stateValues.set(stateId, random.nextDouble());
         }

         agent.update(actionId, newStateId, reward, stateValues);
      }
   }
}
