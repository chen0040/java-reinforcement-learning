package com.github.chen0040.rl.learning.sarsa;


import com.github.chen0040.rl.models.EligibilityTraceUpdateMode;
import com.github.chen0040.rl.utils.Matrix;


/**
 * Created by xschen on 9/28/2015 0028.
 */
public class SarsaLambdaLearner extends SarsaLearner {
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

    @Override
    public  Object clone(){
        SarsaLambdaLearner clone = new SarsaLambdaLearner();
        clone.copy(this);
        return clone;
    }

    @Override
    public void copy(SarsaLearner rhs){
        super.copy(rhs);

        SarsaLambdaLearner rhs2 = (SarsaLambdaLearner)rhs;
        lambda = rhs2.lambda;
        e = (Matrix) rhs2.e.clone();
        traceUpdateMode = rhs2.traceUpdateMode;
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj)){
            return false;
        }

        if(obj instanceof SarsaLambdaLearner){
            SarsaLambdaLearner rhs = (SarsaLambdaLearner)obj;
            return rhs.lambda == lambda && e.equals(rhs.e) && traceUpdateMode == rhs.traceUpdateMode;
        }

        return false;
    }

    public SarsaLambdaLearner(){
        super();
    }

    public SarsaLambdaLearner(int stateCount, int actionCount){
        super(stateCount, actionCount);
        e = new Matrix(stateCount, actionCount);
    }

    public SarsaLambdaLearner(int stateCount, int actionCount, double alpha, double gamma, double initialQ){
        super(stateCount, actionCount, alpha, gamma, initialQ);
        e = new Matrix(stateCount, actionCount);
    }

    public SarsaLambdaLearner(SarsaLearner learner){
        copy(learner);
        e = new Matrix(model.getStateCount(), model.getActionCount());
    }

    public Matrix getEligibility()
    {
        return e;
    }

    public void setEligibility(Matrix e){
        this.e = e;
    }

    @Override
    public void update(int currentStateId, int currentActionId, int nextStateId, int nextActionId, double immediateReward)
    {
        // old_value is $Q_t(s_t, a_t)$
        double oldQ = model.getQ(currentStateId, currentActionId);

        // learning_rate;
        double alpha = model.getAlpha(currentStateId, currentActionId);

        // discount_rate;
        double gamma = model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        double nextQ = model.getQ(nextStateId, nextActionId);

        double td_error = immediateReward + gamma * nextQ - oldQ;

        int stateCount = model.getStateCount();
        int actionCount = model.getActionCount();

        e.set(currentStateId, currentActionId, e.get(currentStateId, currentActionId) + 1);

        for(int stateId = 0; stateId < stateCount; ++stateId){
            for(int actionId = 0; actionId < actionCount; ++actionId){
                oldQ = model.getQ(stateId, actionId);

                double newQ = oldQ + alpha * td_error * e.get(stateId, actionId);

                model.setQ(stateId, actionId, newQ);

                if (actionId != currentActionId) {
                    e.set(currentStateId, actionId, 0);
                } else {
                    e.set(stateId, actionId, e.get(stateId, actionId) * gamma * lambda);
                }
            }
        }
    }

}
