package com.github.chen0040.rl.models;

import com.github.chen0040.rl.utils.Vec;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    private Vec U;
    private int stateCount;
    private int actionCount;

    @SuppressWarnings("Used-by-user")
    public UtilityModel(final int stateCount, final int actionCount, final double initialU) {
        this.stateCount = stateCount;
        this.actionCount = actionCount;
        this.U = new Vec(stateCount);
        this.U.setAll(initialU);
    }

    private UtilityModel() {
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
        if (rhs instanceof UtilityModel) {
            final UtilityModel rhs2 = (UtilityModel) rhs;
            return this.actionCount == rhs2.actionCount &&
                    this.stateCount == rhs2.stateCount &&
                    !(this.U != null && !this.U.equals(rhs2.U));

        }
        return false;
    }

    public void reset(final double initialU) {
        assert this.U != null;
        this.U.setAll(initialU);
    }
}
