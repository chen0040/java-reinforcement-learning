package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.utils.IndexValue;
import com.github.chen0040.rl.models.QModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public class EpsilonGreedyActionSelectionStrategy extends AbstractActionSelectionStrategy {
    public static final String EPSILON = "epsilon";
    private Random random = new Random();

    @Override
    public Object clone(){
        EpsilonGreedyActionSelectionStrategy clone = new EpsilonGreedyActionSelectionStrategy();
        clone.copy(this);
        return clone;
    }

    public void copy(EpsilonGreedyActionSelectionStrategy rhs){
        random = rhs.random;
        for(Map.Entry<String, String> entry : rhs.attributes.entrySet()){
            attributes.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null && obj instanceof EpsilonGreedyActionSelectionStrategy){
            EpsilonGreedyActionSelectionStrategy rhs = (EpsilonGreedyActionSelectionStrategy)obj;
            if(epsilon() != rhs.epsilon()) return false;
           // if(!random.equals(rhs.random)) return false;
            return true;
        }
        return false;
    }

    private double epsilon(){
        return Double.parseDouble(attributes.get(EPSILON));
    }

    public EpsilonGreedyActionSelectionStrategy(){
        epsilon(0.1);
    }

    public EpsilonGreedyActionSelectionStrategy(HashMap<String, String> attributes){
        super(attributes);
    }

    private void epsilon(double value){
        attributes.put(EPSILON, "" + value);
    }

    public EpsilonGreedyActionSelectionStrategy(Random random){
        this.random = random;
        epsilon(0.1);
    }

    @Override
    public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
        if(random.nextDouble() < 1- epsilon()){
            return model.actionWithMaxQAtState(stateId, actionsAtState);
        }else{
            int actionId = random.nextInt(model.getActionCount());
            double Q = model.getQ(stateId, actionId);
            return new IndexValue(actionId, Q);
        }
    }
}
