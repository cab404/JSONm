package com.cab404.jsonm.impl;

import com.cab404.jsonm.core.JSONGenerationException;
import com.cab404.jsonm.core.JSONMaker;
import junit.framework.Assert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.cab404.jsonm.impl.JSONArrayTemplate.arr;
import static com.cab404.jsonm.impl.MapChainTemplateInsertion.map;
import static com.cab404.jsonm.impl.MapChainTemplateInsertion.mod;
import static com.cab404.jsonm.impl.SimpleJSONMaker.ins;

/**
 * Sorry for no comments!
 * Created at 5:37 on 25.03.15
 *
 * @author cab404
 */
@RunWith(JUnit4.class)
public class IntegrationTest {
    JSONMaker maker;

    @Before
    public void init() {
        maker = new SimpleJSONMaker();
    }

    @Test
    public void arrays() {
        Object array = maker.make(JSONArrayTemplate.KEY, 1, 2, 3, 4, 5);
        Assert.assertTrue(new JSONArray("[1,2,3,4,5]").similar(array));
    }

    @Test
    public void object_template() {
        maker.add("tt0", "{'1': 42, '2': *}");
        Object object = maker.make("tt0", 13);
        Assert.assertTrue(new JSONObject("{'1': 42, '2': 13}").similar(object));
    }

    @Test
    public void template_insertions_and_mods() {
        maker.add("tt1", "{'a': [*,*,*,*]}");
        maker.add("tt2", "{'b': *}");

        Object made = maker.make("tt1",
                ins("tt2", 42),
                map()
                        .put("a", 12)
                        .put("b", 11)
                ,
                "GERONIMO",
                mod(ins("tt2", 70))
                        .put("a", 12)
                        .put("c", 11)

        );

        Assert.assertTrue(
                new JSONObject(
                        "{'a': [{'b':42}, {'a':12, 'b':11}, 'GERONIMO', {'a':12, 'b': 70, 'c': 11}]}"
                ).similar(made));
    }

    @Test
    public void errors() {
        try {
            maker.eval(mod(arr(1, 2, 3, 4, 5)).put("a", 23));
            Assert.fail("Has not failed on array modification.");
        } catch (JSONGenerationException ignored) {
        }

    }

}
