package com.github.chen0040.rl.models;

import com.github.chen0040.rl.utils.Vec;

import java.io.Serializable;


/**
 * @author xschen 9/27/2015 0027. Utility value of a state $U(s)$ is the expected long term reward of state $s$ given
 *         the sequence of reward and the optimal policy Utility value $U(s)$ at state $s$ can be obtained by the
 *         Bellman equation Bellman Equtation states that $U(s) = R(s) + \gamma * max_a \sum_{s'} T(s,a,s')U(s')$ where
 *         s' is the possible transitioned state given that action $a$ is applied at state $s$ where $T(s,a,s')$ is the
 *         transition probability of $s \rightarrow s'$ given that action $a$ is applied at state $s$ where $\sum_{s'}
 *         T(s,a,s')U(s')$ is the expected long term reward given that action $a$ is applied at state $s$ where $max_a
 *         \sum_{s'} T(s,a,s')U(s')$ is the maximum expected long term reward given that the chosen optimal action $a$
 *         is applied at state $s$
 */
public class UtilityModel implements Serializable {
    private Vec U;
    private int stateCount;
    private int actionCount;

    public UtilityModel(final int stateCount, final int actionCount, final double initialU) {
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        this.U = new Vec(stateCount);
        this.U.setAll(initialU);
    }

    public UtilityModel(final int stateCount, final int actionCount) {
        this(stateCount, actionCount, 0.1);
    }

    public UtilityModel() {

    }

    public Vec getU() {
        return this.U;
    }

    public void setU(final Vec U) {
        this.U = U;
    }

    public double getU(final int stateId) {
        return this.U.get(stateId);
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

    public void copy(final UtilityModel rhs) {
        this.U = rhs.U == null ? null : rhs.U.makeCopy();
        this.actionCount = rhs.actionCount;
        this.stateCount = rhs.stateCount;
    }

    public UtilityModel makeCopy() {
        final UtilityModel clone = new UtilityModel();
        clone.copy(this);
        return clone;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs != null && rhs instanceof UtilityModel) {
            final UtilityModel rhs2 = (UtilityModel) rhs;
            if (this.actionCount != rhs2.actionCount || this.stateCount != rhs2.stateCount) {
                return false;
            }

            if ((this.U == null && rhs2.U != null) && (this.U != null && rhs2.U == null)) {
                return false;
            }
            return !(this.U != null && !this.U.equals(rhs2.U));

        }
        return false;
    }

    public void reset(final double initialU) {
        this.U.setAll(initialU);
    }
}
