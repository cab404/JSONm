package com.cab404.jsonm.impl;

import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ParseTest {


    @Test
    public void indexOf() {
        Assert.assertEquals("IndexOf", SimpleJSONTemplate.indexOf("123453", '3', 0), 2);
        Assert.assertEquals("IndexOfFromIndex", SimpleJSONTemplate.indexOf("123453", '3', 3), 5);
    }

    @Test
    public void wrapper() {
        int how_much;
        StringBuilder builder = new StringBuilder("asd*fgh*jkl*");
        how_much = SimpleJSONTemplate.unwrap(builder);

        Assert.assertEquals(
                "SimpleIndexation",
                builder.toString(),
                "asd'|1'fgh'|2'jkl'|3'".replace('|', SimpleJSONTemplate.ITEM_REPLACER)
        );
        Assert.assertEquals("Num1", how_much, 3);


        builder = new StringBuilder("asd*fgh\\*jkl*");
        how_much = SimpleJSONTemplate.unwrap(builder);

        Assert.assertEquals(
                "EscapedIndexation",
                builder.toString(),
                "asd'|1'fgh*jkl'|2'".replace('|', SimpleJSONTemplate.ITEM_REPLACER)
        );
        Assert.assertEquals("Num2", how_much, 2);


        builder = new StringBuilder("asd*fgh\\\\*jkl*");
        how_much = SimpleJSONTemplate.unwrap(builder);
        Assert.assertEquals(
                "EscapedIndexation",
                builder.toString(),
                "asd'|1'fgh\\'|2'jkl'|3'".replace('|', SimpleJSONTemplate.ITEM_REPLACER)
        );
        Assert.assertEquals("Num3", how_much, 3);

        builder = new StringBuilder("\\\\asd*fgh\\\\*jkl*");
        how_much = SimpleJSONTemplate.unwrap(builder);

        Assert.assertEquals(
                "NearStartEscapedIndexation",
                builder.toString(),
                "\\asd'|1'fgh\\'|2'jkl'|3'".replace('|', SimpleJSONTemplate.ITEM_REPLACER)
        );
        Assert.assertEquals("Num4", how_much, 3);


        builder = new StringBuilder("asd*fgh\\\\*jkl*\\\\");
        how_much = SimpleJSONTemplate.unwrap(builder);
        Assert.assertEquals(
                "NearEndEscapedIndexation",
                builder.toString(),
                "asd'|1'fgh\\'|2'jkl'|3'\\".replace('|', SimpleJSONTemplate.ITEM_REPLACER)
        );
        Assert.assertEquals("Num5", how_much, 3);

    }

    @Test
    public void cloneTest() {
        JSONObject test = new JSONObject("{'a': [{'b':42}, {'a':12, 'b':11}, 'GERONIMO', {'a':12, 'b': 70, 'c': 11}]}");
        Assert.assertTrue(test.similar(JSONUtils.recursiveClone(test)));
    }
}