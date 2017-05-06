package com.github.chen0040.rl.models;

import com.github.chen0040.rl.utils.Vec;

import java.io.Serializable;


/**
 * @author xschen
 * 9/27/2015 0027.
 * Utility value of a state $U(s)$ is the expected long term reward of state $s$ given the sequence of reward and the optimal policy
 * Utility value $U(s)$ at state $s$ can be obtained by the Bellman equation
 * Bellman Equtation states that $U(s) = R(s) + \gamma * max_a \sum_{s'} T(s,a,s')U(s')$
 * where s' is the possible transitioned state given that action $a$ is applied at state $s$
 * where $T(s,a,s')$ is the transition probability of $s \rightarrow s'$ given that action $a$ is applied at state $s$
 * where $\sum_{s'} T(s,a,s')U(s')$ is the expected long term reward given that action $a$ is applied at state $s$
 * where $max_a \sum_{s'} T(s,a,s')U(s')$ is the maximum expected long term reward given that the chosen optimal action $a$ is applied at state $s$
 */
public class UtilityModel implements Serializable {
    private Vec U;
    private int stateCount;
    private int actionCount;

    public void setU(Vec U){
        this.U = U;
    }

    public Vec getU() {
        return U;
    }

    public double getU(int stateId){
        return U.get(stateId);
    }

    public int getStateCount() {
        return stateCount;
    }

    public int getActionCount() {
        return actionCount;
    }

    public UtilityModel(int stateCount, int actionCount, double initialU){
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        U = new Vec(stateCount);
        U.setAll(initialU);
    }

    public UtilityModel(int stateCount, int actionCount){
        this(stateCount, actionCount, 0.1);
    }

    public UtilityModel(){

    }

    public void copy(UtilityModel rhs){
        U = rhs.U==null ? null : (Vec)rhs.U.clone();
        actionCount = rhs.actionCount;
        stateCount = rhs.stateCount;
    }

    public UtilityModel makeCopy(){
        UtilityModel clone = new UtilityModel();
        clone.copy(this);
        return clone;
    }

    @Override
    public boolean equals(Object rhs){
        if(rhs != null && rhs instanceof  UtilityModel){
            UtilityModel rhs2 = (UtilityModel)rhs;
            if(actionCount != rhs2.actionCount || stateCount != rhs2.stateCount) return false;

            if((U==null && rhs2.U!=null) && (U!=null && rhs2.U ==null)) return false;
            return !(U != null && !U.equals(rhs2.U));

        }
        return false;
    }

    public void reset(double initialU){
        U.setAll(initialU);
    }
}
