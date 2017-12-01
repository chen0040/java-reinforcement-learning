package com.github.chen0040.rl.learning.actorcritic;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chen0040.rl.models.EligibilityTraceUpdateMode;
import com.github.chen0040.rl.utils.Matrix;

import java.util.Set;
import java.util.function.Function;


/**
 * Created by chen0469 on 9/28/2015 0028.
 */
public class ActorCriticLambdaLearner extends ActorCriticLearner {
    private Matrix e;
    private double lambda = 0.9;
    private EligibilityTraceUpdateMode traceUpdateMode = EligibilityTraceUpdateMode.ReplaceTrace;

    public ActorCriticLambdaLearner(){
        super();
    }

    public ActorCriticLambdaLearner(int stateCount, int actionCount){
        super(stateCount, actionCount);
        e = new Matrix(stateCount, actionCount);
    }



    public ActorCriticLambdaLearner(ActorCriticLearner learner){
        copy(learner);
        e = new Matrix(P.getStateCount(), P.getActionCount());
    }

    public ActorCriticLambdaLearner(int stateCount, int actionCount, double alpha, double gamma, double lambda, double initialP){
        super(stateCount, actionCount, alpha, gamma, initialP);
        this.lambda = lambda;
        e = new Matrix(stateCount, actionCount);
    }

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

    public ActorCriticLambdaLearner makeCopy(){
        ActorCriticLambdaLearner clone = new ActorCriticLambdaLearner();
        clone.copy(this);
        return clone;
    }

    @Override
    public void copy(ActorCriticLearner rhs){
        super.copy(rhs);

        ActorCriticLambdaLearner rhs2 = (ActorCriticLambdaLearner)rhs;
        e = rhs2.e.makeCopy();
        lambda = rhs2.lambda;
        traceUpdateMode = rhs2.traceUpdateMode;
    }

    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj)){
            return false;
        }

        if(obj instanceof ActorCriticLambdaLearner){
            ActorCriticLambdaLearner rhs = (ActorCriticLambdaLearner)obj;
            return e.equals(rhs.e) && lambda == rhs.lambda && traceUpdateMode == rhs.traceUpdateMode;
        }

        return false;
    }

    public Matrix getEligibility(){
        return e;
    }

    public void setEligibility(Matrix e){
        this.e = e;
    }

    @Override
    public void update(int currentStateId, int currentActionId, int newStateId, Set<Integer> actionsAtNewState, double immediateReward,  Function<Integer, Double> V){

        double td_error =  immediateReward + V.apply(newStateId) - V.apply(currentStateId);

        int stateCount = P.getStateCount();
        int actionCount = P.getActionCount();

        double gamma = P.getGamma();

        e.set(currentStateId, currentActionId, e.get(currentStateId, currentActionId) + 1);


        for(int stateId = 0; stateId < stateCount; ++stateId){
            for(int actionId = 0; actionId < actionCount; ++actionId){

                double oldP = P.getQ(stateId, actionId);
                double alpha = P.getAlpha(currentStateId, currentActionId);
                double newP = oldP +  alpha * td_error * e.get(stateId, actionId);

                P.setQ(stateId, actionId, newP);

                if (actionId != currentActionId) {
                    e.set(currentStateId, actionId, 0);
                } else {
                    e.set(stateId, actionId, e.get(stateId, actionId) * gamma * lambda);
                }
            }
        }
    }


}
