package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.utils.IndexValue;
import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.models.UtilityModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by xschen on 9/27/2015 0027.
 */
public interface ActionSelectionStrategy extends Serializable, Cloneable {
	IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState);

	IndexValue selectAction(int stateId, UtilityModel model, Set<Integer> actionsAtState);

	String getPrototype();

	Map<String, String> getAttributes();
}
