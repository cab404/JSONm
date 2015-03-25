package com.cab404.jsonm.core;

/**
 * Thing that makes json thing from several other things.
 * Created at 18:09 on 13.02.15
 *
 * @author cab404
 */
public interface JSONTemplate {

    public Object make(JSONMaker parent, Object... parameters);

}
