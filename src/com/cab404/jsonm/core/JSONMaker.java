package com.cab404.jsonm.core;

/**
 * Thing that makes a thing using a lot of thing-making things.
 * Created at 1:41 on 13.02.15
 *
 * @author cab404
 */
public interface JSONMaker {

    public Object make(String templateName, Object... params);

    public JSONMaker add(String templateName, String expression);

}
