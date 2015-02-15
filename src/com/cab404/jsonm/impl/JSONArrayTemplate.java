package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONTemplate;
import org.json.JSONArray;

/**
 * Sorry for no comments!
 * Created at 3:12 on 15.02.15
 *
 * @author cab404
 */
public class JSONArrayTemplate implements JSONTemplate {
    @Override
    public Object make(Object... parameters) {
        return new JSONArray(parameters);
    }
}
