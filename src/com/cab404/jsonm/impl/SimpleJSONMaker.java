package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONMaker;
import com.cab404.jsonm.core.JSONTemplate;
import com.cab404.jsonm.core.TemplateInsertion;

import java.util.HashMap;
import java.util.Map;

/**
 * Sorry for no comments!
 * Created at 18:07 on 13.02.15
 *
 * @author cab404
 */
public class SimpleJSONMaker implements JSONMaker {

    private Map<String, JSONTemplate> library;

    public SimpleJSONMaker() {
        library = new HashMap<>();
        library.put(TemplateInsertion.ARRAY_KEY, new JSONArrayTemplate());
    }

    @Override
    public Object make(String templateName, Object... params) {
        return this.makeFromArray(templateName, params);
    }

    public Object makeFromArray(String templateName, Object[] params) {
        for (int i = 0; i < params.length; i++)
            if (params[i] instanceof TemplateInsertion) {
                TemplateInsertion params_ins = (TemplateInsertion) params[i];
                params[i] = makeFromArray(params_ins.templateName, params_ins.parameters);
            }

        return library.get(templateName).make(params);
    }


    @Override
    public JSONMaker add(String templateName, String expression) {
        library.put(templateName, new SimpleJSONTemplate(expression));
        return this;
    }

}
