package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.utils.IndexValue;
import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.models.UtilityModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xschen on 9/27/2015 0027.
 */
public abstract class AbstractActionSelectionStrategy implements ActionSelectionStrategy {

	private String prototype;
	protected Map<String, String> attributes = new HashMap<>();

	public String getPrototype() {
		return prototype;
	}

	public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
		return new IndexValue();
	}

	public IndexValue selectAction(int stateId, UtilityModel model, Set<Integer> actionsAtState) {
		return new IndexValue();
	}

	public AbstractActionSelectionStrategy() {
		prototype = this.getClass().getCanonicalName();
	}

	public AbstractActionSelectionStrategy(HashMap<String, String> attributes) {
		this.attributes = attributes;
		if (attributes.containsKey("prototype")) {
			this.prototype = attributes.get("prototype");
		}
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public boolean equals(Object obj) {
		ActionSelectionStrategy rhs = (ActionSelectionStrategy) obj;
		if (!prototype.equalsIgnoreCase(rhs.getPrototype())) return false;
		for (Map.Entry<String, String> entry : rhs.getAttributes().entrySet()) {
			if (!attributes.containsKey(entry.getKey())) {
				return false;
			}
			if (!attributes.get(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			if (!rhs.getAttributes().containsKey(entry.getKey())) {
				return false;
			}
			if (!rhs.getAttributes().get(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public abstract Object clone();
}
