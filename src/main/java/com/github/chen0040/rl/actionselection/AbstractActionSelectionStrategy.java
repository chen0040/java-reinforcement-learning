package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.models.UtilityModel;
import com.github.chen0040.rl.utils.IndexValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public abstract class AbstractActionSelectionStrategy implements ActionSelectionStrategy {

    Map<String, String> attributes = new HashMap<>();
    private String prototype;

    @SuppressWarnings("Used-by-user")
    public AbstractActionSelectionStrategy() {
        this.prototype = this.getClass().getCanonicalName();
    }

    @SuppressWarnings("Used-by-user")
    public AbstractActionSelectionStrategy(final HashMap<String, String> attributes) {
        this.attributes = attributes;
        if (attributes.containsKey("prototype")) {
            this.prototype = attributes.get("prototype");
        }
    }

    @Override
    public String getPrototype() {
        return this.prototype;
    }

    @Override
    public IndexValue selectAction(final int stateId, final QModel model, final Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    @Override
    public IndexValue selectAction(final int stateId, final UtilityModel model, final Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public boolean equals(final Object obj) {
        final ActionSelectionStrategy rhs = (ActionSelectionStrategy) obj;
        return this.prototype.equalsIgnoreCase(rhs.getPrototype()) &&
                rhs.getAttributes().entrySet().stream().noneMatch(entry -> !this.attributes.containsKey(entry.getKey()) ||
                        !this.attributes.get(entry.getKey()).equals(entry.getValue())) &&
                this.attributes.entrySet().stream().noneMatch(entry -> !rhs.getAttributes().containsKey(entry.getKey()) ||
                        !rhs.getAttributes().get(entry.getKey()).equals(entry.getValue()));
    }

    @Override
    public abstract Object clone();
}
