package com.github.chen0040.rl.models;


import com.github.chen0040.rl.utils.IndexValue;
import com.github.chen0040.rl.utils.Matrix;
import com.github.chen0040.rl.utils.Vec;

import java.util.*;


/**
 * @author xschen 9/27/2015 0027. Q is known as the quality of state-action combination, note that it is different from
 *         utility of a state
 */
public class QModel {
    /**
     * Q value for (state_id, action_id) pair Q is known as the quality of state-action combination, note that it is
     * different from utility of a state
     */

    private Matrix Q;
    /**
     * $\alpha[s, a]$ value for learning rate: alpha(state_id, action_id)
     */

    private Matrix alphaMatrix;

    /**
     * discount factor
     */
    private double gamma = DefaultValues.GAMMA;

    private int stateCount;
    private int actionCount;

    public QModel(final int stateCount, final int actionCount, final double initialQ) {
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        this.Q = new Matrix(stateCount, actionCount);
        this.alphaMatrix = new Matrix(stateCount, actionCount);
        this.Q.setAll(initialQ);
        this.alphaMatrix.setAll(DefaultValues.ALPHA);
    }

    public QModel(final int stateCount, final int actionCount) {
        this(stateCount, actionCount, DefaultValues.INITIAL_Q);
    }

    public QModel() {

    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs instanceof QModel) {
            final QModel rhs2 = (QModel) rhs;
            return this.gamma == rhs2.gamma &&
                    this.stateCount == rhs2.stateCount &&
                    this.actionCount == rhs2.actionCount &&
                    (this.Q == null || rhs2.Q != null) &&
                    (this.Q != null || rhs2.Q == null) &&
                    (this.alphaMatrix == null || rhs2.alphaMatrix != null) &&
                    (this.alphaMatrix != null || rhs2.alphaMatrix == null) &&
                    !((this.Q != null && !this.Q.equals(rhs2.Q)) || (this.alphaMatrix != null && !this.alphaMatrix.equals(rhs2.alphaMatrix)));
        }
        return false;
    }

    public QModel makeCopy() {
        final QModel clone = new QModel();
        clone.copy(this);
        return clone;
    }

    public void copy(final QModel rhs) {
        this.gamma = rhs.gamma;
        this.stateCount = rhs.stateCount;
        this.actionCount = rhs.actionCount;
        this.Q = rhs.Q == null ? null : rhs.Q.makeCopy();
        this.alphaMatrix = rhs.alphaMatrix == null ? null : rhs.alphaMatrix.makeCopy();
    }


    public double getQ(final int stateId, final int actionId) {
        assert this.Q != null;
        return this.Q.get(stateId, actionId);
    }


    public void setQ(final int stateId, final int actionId, final double Qij) {
        assert this.Q != null;
        this.Q.set(stateId, actionId, Qij);
    }


    public double getAlpha(final int stateId, final int actionId) {
        assert this.alphaMatrix != null;
        return this.alphaMatrix.get(stateId, actionId);
    }


    public void setAlpha(final double defaultAlpha) {
        assert this.alphaMatrix != null;
        this.alphaMatrix.setAll(defaultAlpha);
    }


    public IndexValue actionWithMaxQAtState(final int stateId, final Set<Integer> actionsAtState) {
        assert this.Q != null;
        final Vec rowVector = this.Q.rowAt(stateId);
        return rowVector.indexWithMaxValue(actionsAtState);
    }

    private void reset(final double initialQ) {
        assert this.Q != null;
        this.Q.setAll(initialQ);
    }


    public IndexValue actionWithSoftMaxQAtState(final int stateId, final Set<Integer> actionsAtState, final Random random) {
        Set<Integer> atState = actionsAtState;
        assert this.Q != null;
        final Vec rowVector = this.Q.rowAt(stateId);
        double sum = 0;

        if (atState == null) {
            atState = new HashSet<>();
            for (int i = 0; i < this.actionCount; ++i) {
                atState.add(i);
            }
        }

        final List<Integer> actions = new ArrayList<>(atState);

        final double[] acc = new double[actions.size()];
        for (int i = 0; i < actions.size(); ++i) {
            sum += rowVector.get(actions.get(i));
            acc[i] = sum;
        }


        final double r = random.nextDouble() * sum;

        final IndexValue result = new IndexValue();
        for (int i = 0; i < actions.size(); ++i) {
            if (acc[i] >= r) {
                final int actionId = actions.get(i);
                result.setIndex(actionId);
                result.setValue(rowVector.get(actionId));
                break;
            }
        }

        return result;
    }


    public Matrix getQ() {
        return this.Q;
    }


    public Matrix getAlphaMatrix() {
        return this.alphaMatrix;
    }

    public double getGamma() {
        return this.gamma;
    }

    public void setGamma(final double gamma) {
        this.gamma = gamma;
    }

    public int getStateCount() {
        return this.stateCount;
    }

    public void setStateCount(final int stateCount) {
        this.stateCount = stateCount;
    }

    public int getActionCount() {
        return this.actionCount;
    }

    public void setActionCount(final int actionCount) {
        this.actionCount = actionCount;
    }
}
