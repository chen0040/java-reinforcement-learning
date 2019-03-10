package com.github.chen0040.rl.actionselection;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public enum ActionSelectionStrategyFactory {
    ;

    public static ActionSelectionStrategy deserialize(final String conf) {
        final String[] comps = conf.split(";");

        final HashMap<String, String> attributes = new HashMap<>();
        for (final String comp : comps) {
            final String[] field = comp.split("=");
            if (field.length < 2) {
                continue;
            }
            final String fieldname = field[0].trim();
            final String fieldvalue = field[1].trim();

            attributes.put(fieldname, fieldvalue);
        }
        if (attributes.isEmpty()) {
            attributes.put("prototype", conf);
        }

        final String prototype = attributes.get("prototype");
        if (prototype.equals(GreedyActionSelectionStrategy.class.getCanonicalName())) {
            return new GreedyActionSelectionStrategy();
        } else if (prototype.equals(SoftMaxActionSelectionStrategy.class.getCanonicalName())) {
            return new SoftMaxActionSelectionStrategy();
        } else if (prototype.equals(EpsilonGreedyActionSelectionStrategy.class.getCanonicalName())) {
            return new EpsilonGreedyActionSelectionStrategy(attributes);
        } else if (prototype.equals(GibbsSoftMaxActionSelectionStrategy.class.getCanonicalName())) {
            return new GibbsSoftMaxActionSelectionStrategy();
        }

        return null;
    }

    public static String serialize(final ActionSelectionStrategy strategy) {
        final Map<String, String> attributes = strategy.getAttributes();
        attributes.put("prototype", strategy.getPrototype());

        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final Map.Entry<String, String> entry : attributes.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(";");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
}
