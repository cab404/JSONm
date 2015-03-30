package com.cab404.jsonm.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Some usefulness for putting JSON back and forth!
 * Created at 13:37 on 30.03.15
 *
 * @author cab404
 */
public class JSONSerializer<T> {

    protected final Class<T> clazz;

    protected final Map<String, Record> description;

    protected static final Class[] JSON_COMPATIBLE = {
            Long.TYPE,
            Long.class,
            Double.TYPE,
            Double.class,
            Integer.TYPE,
            Integer.class,
            Boolean.TYPE,
            Boolean.class,
            String.class,
            JSONObject.class,
            JSONArray.class,
    };

    protected class Record {
        Putter putter;
        Class clazz;
        Field field;
    }

    protected interface Putter {
        public void put(Object obj, Field where, Object val) throws IllegalAccessException;
    }

    protected static class DefaultPutter implements Putter {
        public static final DefaultPutter INSTANCE = new DefaultPutter();

        @Override
        public void put(Object obj, Field where, Object val) throws IllegalAccessException {
            where.set(obj, val);
        }

    }

    protected static class IntPutter implements Putter {
        public static final IntPutter INSTANCE = new IntPutter();

        @Override
        public void put(Object obj, Field where, Object val) throws IllegalAccessException {
            int the_thing;

            if (val instanceof Long)
                the_thing = ((Long) val).intValue();
            else if (val instanceof Integer)
                the_thing = (int) val;
            else throw new RuntimeException("Incompatible types: Integer and " + val.getClass().getName());

            where.set(obj, the_thing);
        }
    }


    public JSONSerializer(Class<T> clazz) {
        this.clazz = clazz;
        description = new HashMap<>();

        for (Field f : clazz.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) != 0) continue;
            for (Class d : JSON_COMPATIBLE) {
                if (f.getType().isAssignableFrom(d)) {
                    Record record = new Record();
                    record.clazz = d;
                    record.field = f;
                    if (f.getType().equals(Integer.class)) {
                        record.putter = IntPutter.INSTANCE;
                    } else if (f.getType().equals(Integer.class)) {
                        record.putter = IntPutter.INSTANCE;
                    } else
                        record.putter = DefaultPutter.INSTANCE;

                    description.put(f.getName(), record);
                    break;
                }
            }
        }

    }

    public JSONObject serialize(T what, JSONObject to) throws IllegalAccessException, JSONException {
        for (Record rec : description.values()) {
            to.put(rec.field.getName(), rec.field.get(what));
        }
        return to;
    }

    protected <X> Iterable<X> iter(final Iterator<X> what) {
        return new Iterable<X>() {
            @Override
            public Iterator<X> iterator() {
                return what;
            }
        };
    }

    public T deserialize(JSONObject what, T to) throws JSONException, IllegalAccessException {
        for (String s : iter(what.keys())) {
            if (description.containsKey(s)) {
                Record f = description.get(s);
                f.putter.put(to, f.field, what.get(s));
            }
        }
        return to;
    }


}
