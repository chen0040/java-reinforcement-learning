# java-reinforcement-learning
Package provides java implementation of reinforcement learning algorithms

[![Build Status](https://travis-ci.org/chen0040/java-reinforcement-learning.svg?branch=master)](https://travis-ci.org/chen0040/java-reinforcement-learning) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-reinforcement-learning/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-reinforcement-learning?branch=master)

#Features

The following reinforcement learning are implemented:

* Q-Learn
* Q(lambda)-Learn
* R-Learn
* SARSA
* Actor-Critic

# Usage

An reinforcement agent, say, Q-Learn agent, can be created by the following java code:

```java
import com.github.chen0040.rl.learning.qlearn.QAgent;

int stateCount = 100;
int actionCount = 10;
QAgent agent = new QAgent(stateCount, actionCount);
```

The agent created has a state map of 100 states, and 10 different actions for its selection.

At each time step, a action can be selected by the agent, by calling:

```java
int actionId = agent.selectAction();
```

If you want to limits the number of possible action at each states (say the problem restrict the actions avaliable at different state), then call:

```java
Set<Integer> actionsAvailableAtCurrentState = world.getActionsAvailable(agent);
int actionTaken = agent.selectAction(actionsAvailableAtCurrentState);
```

Once the world state has been updated due to the agent's selected action, its internal state-action Q matrix will be updated by calling:

```java
int newStateId = world.update(agent, actionTaken);
double reward = world.reward(agent);

agent.update(actionTaken, newStateId, reward);
```

# Sample code

### Sample code for R-Learn

```java
import com.github.chen0040.rl.learning.rlearn.RAgent;

int stateCount = 100;
int actionCount = 10;
RAgent agent = new RAgent(stateCount, actionCount);

Random random = new Random();
agent.start(random.nextInt(stateCount));
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

### Sample code for Q-Learn

```java
import com.github.chen0040.rl.learning.qlearn.QAgent;

int stateCount = 100;
int actionCount = 10;
QAgent agent = new QAgent(stateCount, actionCount);

Random random = new Random();
agent.start(random.nextInt(stateCount));
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

### Sample code for SARSA

```java
import com.github.chen0040.rl.learning.sarsa.SarsaAgent;

int stateCount = 100;
int actionCount = 10;
SarsaAgent agent = new SarsaAgent(stateCount, actionCount);

Random random = new Random();
agent.start(random.nextInt(stateCount));
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

