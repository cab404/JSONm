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

    public static class TestClass0 {
        public enum TE {
            o1, o2, o3
        }

        public TE tst = TE.o1;
        public int intVal = 42;
        public long longVal = 90000000000000001l;
    }

    @Test
    public void serialize() throws Exception {
        JSONSerializer<TestClass0> serializer = new JSONSerializer<>(TestClass0.class);

        TestClass0 obj = new TestClass0();
        JSONObject serialized = serializer.serialize(obj, new JSONObject());

        Assert.assertTrue(
                new JSONObject("{'intVal':42,'longVal':90000000000000001, 'tst':'o1'}")
                        .similar(serialized)
        );

        serialized.put("intVal", 71);
        serialized.put("longVal", 88);
        serialized.put("tst", "o2");

        serializer.deserialize(serialized, obj);

        Assert.assertEquals(obj.tst, TestClass0.TE.o2);
        Assert.assertEquals(obj.intVal, 71);
        Assert.assertEquals(obj.longVal, 88);

//        Assert.assertTrue(serialized);

    }


}
