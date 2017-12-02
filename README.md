# java-reinforcement-learning
Package provides java implementation of reinforcement learning algorithms as described in the book "Reinforcement Learning: An Introduction" by Sutton

[![Build Status](https://travis-ci.org/chen0040/java-reinforcement-learning.svg?branch=master)](https://travis-ci.org/chen0040/java-reinforcement-learning) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-reinforcement-learning/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-reinforcement-learning?branch=master)


# Features

The following reinforcement learning are implemented:

* R-Learn
* Q-Learn
* Q-Learn with eligibility trace
* SARSA
* SARSA with eligibility trace
* Actor-Critic
* Actor-Critic with eligibility trace

The package also support a number of action-selection strategy:

* soft-max
* epsilon-greedy
* greedy
* Gibbs-soft-max


![Reinforcement Learning](images/rl.jpg)

# Install

Add the following dependency to your POM file:

```
<dependency>
  <groupId>com.github.chen0040</groupId>
  <artifactId>java-reinforcement-learning</artifactId>
  <version>1.0.5</version>
</dependency>
```

# Application Samples

The application sample of this library can be found in the following repositories:

* [java-reinforcement-learning-tic-tac-toe](https://github.com/chen0040/java-reinforcement-learning-tic-tac-toe) 

# Usage

### Create Agent

An reinforcement agent, say, Q-Learn agent, can be created by the following java code:

```java
import com.github.chen0040.rl.learning.qlearn.QAgent;

int stateCount = 100;
int actionCount = 10;
QAgent agent = new QAgent(stateCount, actionCount);
```

The agent created has a state map of 100 states, and 10 different actions for its selection.

For Q-Learn and SARSA, the eligibility trace lambda can be enabled by calling:

```java
agent.enableEligibilityTrace(lambda)
```

### Select Action

At each time step, a action can be selected by the agent, by calling:

```java
int actionId = agent.selectAction().getIndex();
```

If you want to limits the number of possible action at each states (say the problem restrict the actions avaliable at different state), then call:

```java
Set<Integer> actionsAvailableAtCurrentState = world.getActionsAvailable(agent);
int actionTaken = agent.selectAction(actionsAvailableAtCurrentState).getIndex();
```

The agent can also change to a different action-selection policy available in com.github.chen0040.rl.actionselection package, for example, the following code
switch the action selection policy to soft-max:

```java
agent.getLearner().setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());
```

### State-Action Update

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

 int actionId = agent.selectAction().getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

Alternatively, you can use RLearner if you want to learning after the episode:

```java

class Move {
    int oldState;
    int newState;
    int action;
    double reward;
    
    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.newState = newState;
        this.reward = reward;
        this.action = action;
    }
}

int stateCount = 100;
int actionCount = 10;
RLearner agent = new RLearner(stateCount, actionCount);

Random random = new Random();
int currentState = random.nextInt(stateCount));
List<TupleThree<Integer, Integer, Double>> moves = new ArrayList<>();
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction(currentState).getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);
 int oldStateId = currentState;
 moves.add(new Move(oldStateId, actionId, newStateId, reward));
  currentState = newStateId;
}

for(int i=moves.size()-1; i >= 0; --i){
    Move move = moves.get(i);
    agent.update(move.oldState, move.action, move.newState, world.getActionsAvailableAtState(nextStateId), move.reward);
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

 int actionId = agent.selectAction().getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

Alternatively, you can use QLearner if you want to learning after the episode:

```java

class Move {
    int oldState;
    int newState;
    int action;
    double reward;
    
    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.newState = newState;
        this.reward = reward;
        this.action = action;
    }
}

int stateCount = 100;
int actionCount = 10;
QLearner agent = new QLearner(stateCount, actionCount);

Random random = new Random();
int currentState = random.nextInt(stateCount));
List<TupleThree<Integer, Integer, Double>> moves = new ArrayList<>();
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction(currentState).getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);
 int oldStateId = currentState;
 moves.add(new Move(oldStateId, actionId, newStateId, reward));
  currentState = newStateId;
}

for(int i=moves.size()-1; i >= 0; --i){
    Move move = moves.get(i);
    agent.update(move.oldState, move.action, move.newState, move.reward);
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

 int actionId = agent.selectAction().getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 agent.update(actionId, newStateId, reward);
}
```

Alternatively, you can use SarsaLearner if you want to learning after the episode:

```java

class Move {
    int oldState;
    int newState;
    int action;
    double reward;
    
    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.newState = newState;
        this.reward = reward;
        this.action = action;
    }
}

int stateCount = 100;
int actionCount = 10;
SarsaLearner agent = new SarsaLearner(stateCount, actionCount);

Random random = new Random();
int currentState = random.nextInt(stateCount));
List<TupleThree<Integer, Integer, Double>> moves = new ArrayList<>();
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction(currentState).getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);
 int oldStateId = currentState;
 moves.add(new Move(oldStateId, actionId, newStateId, reward));
  currentState = newStateId;
}

for(int i=moves.size()-1; i >= 0; --i){
    Move next_move = moves.get(i);
    if(i != moves.size()-1) {
        next_move = moves.get(i+1);
    }
    Move current_move = moves.get(i);
    agent.update(current_move.oldState, current_move.action, current_move.newState, next_move.action, current_move.reward);
}
```

### Sample code for Actor Critic Model

```java
import com.github.chen0040.rl.learning.actorcritic.ActorCriticAgent;
import com.github.chen0040.rl.utils.Vec;

int stateCount = 100;
int actionCount = 10;
ActorCriticAgent agent = new ActorCriticAgent(stateCount, actionCount);
Vec stateValues = new Vec(stateCount);

Random random = new Random();
agent.start(random.nextInt(stateCount));
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction().getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);

 
 System.out.println("World state values changed ...");
 for(int stateId = 0; stateId < stateCount; ++stateId){
    stateValues.set(stateId, random.nextDouble());
 }
    
 agent.update(actionId, newStateId, reward, stateValues);
}
```

Alternatively, you can use ActorCriticLearner if you want to learning after the episode:

```java

class Move {
    int oldState;
    int newState;
    int action;
    double reward;
    
    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.newState = newState;
        this.reward = reward;
        this.action = action;
    }
}

int stateCount = 100;
int actionCount = 10;
SarsaLearner agent = new SarsaLearner(stateCount, actionCount);

Random random = new Random();
int currentState = random.nextInt(stateCount));
List<TupleThree<Integer, Integer, Double>> moves = new ArrayList<>();
for(int time=0; time < 1000; ++time){

 int actionId = agent.selectAction(currentState).getIndex();
 System.out.println("Agent does action-"+actionId);
 
 int newStateId = world.update(agent, actionId);
 double reward = world.reward(agent);

 System.out.println("Now the new state is " + newStateId);
 System.out.println("Agent receives Reward = "+reward);
 int oldStateId = currentState;
 moves.add(new Move(oldStateId, actionId, newStateId, reward));
  currentState = newStateId;
}

for(int i=moves.size()-1; i >= 0; --i){
    Move next_move = moves.get(i);
    if(i != moves.size()-1) {
        next_move = moves.get(i+1);
    }
    Move current_move = moves.get(i);
    agent.update(current_move.oldState, current_move.action, current_move.newState, next_move.action, current_move.reward);
}

```

### Save and Load RL models

To save the trained RL model (say QLeanrer):

```java
QLearner learner = new QLearner(stateCount, actionCount);
train(learner);
String json = learner.toJson();
```

To load the trained RL model from json:

```java
QLearner learner = QLearn.fromJson(json);
```

