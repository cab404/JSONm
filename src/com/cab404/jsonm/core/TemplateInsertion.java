package com.cab404.jsonm.core;

/**
 * Insertion data for templates, yeah.
 * Created at 2:08 on 13.02.15
 *
 * @author cab404
 */
public class TemplateInsertion {
    public static final String ARRAY_KEY = "$array";

    public final String templateName;
    public final Object[] parameters;

    public TemplateInsertion(String templateName, Object[] parameters) {
        this.templateName = templateName;
        this.parameters = parameters;
    }

    public static TemplateInsertion ins(String templateName, Object... parameters) {
        return new TemplateInsertion(templateName, parameters);
    }

    public static TemplateInsertion arr(Object... parameters) {
        return new TemplateInsertion(ARRAY_KEY, parameters);
    }

}
