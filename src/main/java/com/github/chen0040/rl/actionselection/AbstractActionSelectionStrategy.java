package com.github.chen0040.rl.actionselection;

import com.github.chen0040.rl.utils.IndexValue;
import com.github.chen0040.rl.models.QModel;
import com.github.chen0040.rl.models.UtilityModel;

import java.util.HashMap;
import java.util.Set;


/**
 * Created by xschen on 9/27/2015 0027.
 */
public abstract class AbstractActionSelectionStrategy implements ActionSelectionStrategy {

    private String prototype;
    protected HashMap<String, String> attributes = new HashMap<String, String>();

    public String getPrototype(){
        return prototype;
    }

    public IndexValue selectAction(int stateId, QModel model, Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    public IndexValue selectAction(int stateId, UtilityModel model, Set<Integer> actionsAtState) {
        return new IndexValue();
    }

    public AbstractActionSelectionStrategy(){
        prototype = this.getClass().getCanonicalName();
    }


    public AbstractActionSelectionStrategy(HashMap<String, String> attributes){
        this.attributes = attributes;
        if(attributes.containsKey("prototype")){
            this.prototype = attributes.get("prototype");
        }
    }

    public HashMap<String, String> getAttributes(){
        return attributes;
    }

    @Override
    public abstract Object clone();
}
