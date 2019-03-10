package com.github.chen0040.rl.learning.sarsa;


import com.github.chen0040.rl.models.DefaultValues;
import com.github.chen0040.rl.models.EligibilityTraceUpdateMode;
import com.github.chen0040.rl.utils.Matrix;


/**
 * Created by xschen on 9/28/2015 0028.
 */
public class SarsaLambdaLearner extends SarsaLearner {
    private double lambda = DefaultValues.LAMBDA;
    private Matrix e;
    private EligibilityTraceUpdateMode traceUpdateMode = EligibilityTraceUpdateMode.ReplaceTrace;

    public SarsaLambdaLearner(final int stateCount, final int actionCount) {
        super(stateCount, actionCount);
        this.e = new Matrix(stateCount, actionCount);
    }

    public SarsaLambdaLearner(final int stateCount, final int actionCount, final double alpha, final double gamma, final double initialQ) {
        super(stateCount, actionCount, alpha, gamma, initialQ);
        this.e = new Matrix(stateCount, actionCount);
    }

    @SuppressWarnings("Used-by-user")
    public SarsaLambdaLearner(final SarsaLearner learner) {
        this.copy(learner);
        this.e = new Matrix(this.model.getStateCount(), this.model.getActionCount());
    }

    private SarsaLambdaLearner() {

    }

    @SuppressWarnings("Used-by-user")
    public EligibilityTraceUpdateMode getTraceUpdateMode() {
        return this.traceUpdateMode;
    }

    @SuppressWarnings("Used-by-user")
    public void setTraceUpdateMode(final EligibilityTraceUpdateMode traceUpdateMode) {
        this.traceUpdateMode = traceUpdateMode;
    }

    @SuppressWarnings("Used-by-user")
    public double getLambda() {
        return this.lambda;
    }

    public void setLambda(final double lambda) {
        this.lambda = lambda;
    }

    @Override
    public SarsaLambdaLearner clone() {
        final SarsaLambdaLearner clone = new SarsaLambdaLearner();
        clone.copy(this);
        return clone;
    }

    @Override
    public void copy(final SarsaLearner rhs) {
        super.copy(rhs);

        final SarsaLambdaLearner rhs2 = (SarsaLambdaLearner) rhs;
        this.lambda = rhs2.lambda;
        this.e = rhs2.e.makeCopy();
        this.traceUpdateMode = rhs2.traceUpdateMode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (obj instanceof SarsaLambdaLearner) {
            final SarsaLambdaLearner rhs = (SarsaLambdaLearner) obj;
            return rhs.lambda == this.lambda && this.e.equals(rhs.e) && this.traceUpdateMode == rhs.traceUpdateMode;
        }

        return false;
    }

    @SuppressWarnings("Used-by-user")
    public Matrix getEligibility() {
        return this.e;
    }

    @SuppressWarnings("Used-by-user")
    public void setEligibility(final Matrix e) {
        this.e = e;
    }

    @Override
    public void update(final int currentStateId, final int currentActionId, final int nextStateId, final int nextActionId, final double immediateReward) {
        // old_value is $Q_t(s_t, a_t)$
        double oldQ = this.model.getQ(currentStateId, currentActionId);

        // learning_rate;
        final double alpha = this.model.getAlpha(currentStateId, currentActionId);

        // discount_rate;
        final double gamma = this.model.getGamma();

        // estimate_of_optimal_future_value is $max_a Q_t(s_{t+1}, a)$
        final double nextQ = this.model.getQ(nextStateId, nextActionId);

        final double td_error = immediateReward + gamma * nextQ - oldQ;

        final int stateCount = this.model.getStateCount();
        final int actionCount = this.model.getActionCount();

        this.e.set(currentStateId, currentActionId, this.e.get(currentStateId, currentActionId) + 1);

        for (int stateId = 0; stateId < stateCount; ++stateId) {
            for (int actionId = 0; actionId < actionCount; ++actionId) {
                oldQ = this.model.getQ(stateId, actionId);

                final double newQ = oldQ + alpha * td_error * this.e.get(stateId, actionId);

                this.model.setQ(stateId, actionId, newQ);

                if (actionId != currentActionId) {
                    this.e.set(currentStateId, actionId, 0);
                } else {
                    this.e.set(stateId, actionId, this.e.get(stateId, actionId) * gamma * this.lambda);
                }
            }
        }
    }

}
