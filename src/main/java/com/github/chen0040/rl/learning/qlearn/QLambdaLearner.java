package com.github.chen0040.rl.learning.qlearn;


import com.github.chen0040.rl.models.EligibilityTraceUpdateMode;
import com.github.chen0040.rl.utils.Matrix;

import java.util.Set;


/**
 * Created by xschen on 9/28/2015 0028.
 */
public class QLambdaLearner extends QLearner {
    private double lambda = 0.9;
    private Matrix e;
    private EligibilityTraceUpdateMode traceUpdateMode = EligibilityTraceUpdateMode.ReplaceTrace;

    public EligibilityTraceUpdateMode getTraceUpdateMode() {
        return traceUpdateMode;
    }

    public void setTraceUpdateMode(EligibilityTraceUpdateMode traceUpdateMode) {
        this.traceUpdateMode = traceUpdateMode;
    }

    public double getLambda(){
        return lambda;
    }

    public void setLambda(double lambda){
        this.lambda = lambda;
    }

    public  QLambdaLearner makeCopy(){
        QLambdaLearner clone = new QLambdaLearner();
        clone.copy(this);
        return clone;
    }

    @Override
    public void copy(QLearner rhs){
        super.copy(rhs);

        QLambdaLearner rhs2 = (QLambdaLearner)rhs;
        lambda = rhs2.lambda;
        e = (Matrix) rhs2.e.clone();
        traceUpdateMode = rhs2.traceUpdateMode;
    }

    public QLambdaLearner(QLearner learner){
        copy(learner);
        e = new Matrix(model.getStateCount(), model.getActionCount());
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj)){
            return false;
        }

        if(obj instanceof QLambdaLearner){
            QLambdaLearner rhs = (QLambdaLearner)obj;
            return rhs.lambda == lambda && e.equals(rhs.e) && traceUpdateMode == rhs.traceUpdateMode;
        }

        return false;
    }

    public QLambdaLearner(){
        super();
    }

    public QLambdaLearner(int stateCount, int actionCount){
        super(stateCount, actionCount);
        e = new Matrix(stateCount, actionCount);
    }

    public QLambdaLearner(int stateCount, int actionCount, double alpha, double gamma, double initialQ){
        super(stateCount, actionCount, alpha, gamma, initialQ);
        e = new Matrix(stateCount, actionCount);
    }

    public Matrix getEligibility()
    {
        return e;
    }

    public void setEligibility(Matrix e){
        this.e = e;
    }

    @Override
    public void update(int currentStateId, int currentActionId, int nextStateId, Set<Integer> actionsAtNextStateId, double immediateReward)
    {
        // old_value is $Q_t(s_t, a_t)$
        double oldQ = model.getQ(currentStateId, currentActionId);

        // learning_rate;
        double alpha = model.getAlpha(currentStateId, currentActionId);

        // discount_rate;
        double gamma = model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        double maxQ = maxQAtState(nextStateId, actionsAtNextStateId);

        double td_error = immediateReward + gamma * maxQ - oldQ;

        int stateCount = model.getStateCount();
        int actionCount = model.getActionCount();

        e.set(currentStateId, currentActionId, e.get(currentStateId, currentActionId) + 1);


        for(int stateId = 0; stateId < stateCount; ++stateId){
            for(int actionId = 0; actionId < actionCount; ++actionId){
                oldQ = model.getQ(stateId, actionId);
                double newQ = oldQ + alpha * td_error * e.get(stateId, actionId);

                // new_value is $Q_{t+1}(s_t, a_t)$
                model.setQ(currentStateId, currentActionId, newQ);

                if (actionId != currentActionId) {
                    e.set(currentStateId, actionId, 0);
                } else {
                    e.set(stateId, actionId, e.get(stateId, actionId) * gamma * lambda);
                }
            }
        }



    }

}
