package com.cab404.jsonm.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.cab404.jsonm.impl.SimpleJSONTemplate.ITEM_REPLACER;
import static com.cab404.jsonm.impl.SimpleJSONTemplate.NUM_FIELD_START;

/**
 * Sorry for no comments!
 * Created at 1:33 on 15.02.15
 *
 * @author cab404
 */

public class JSONUtils {

    protected static void recurseThroughArray(List<List<JSONAddressNode>> targets, List<JSONAddressNode> cur_path, JSONArray object) throws JSONException {
        for (int key = 0; key < object.length(); key++) {
            Object o = object.get(key);

            if (o instanceof String) {
                String str = (String) o;
                if (!str.isEmpty() && str.charAt(0) == ITEM_REPLACER) {
                    int index = str.charAt(1) - NUM_FIELD_START;
                    List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                    copied_path.add(new JSONArrayAddressNode(key));
                    targets.set(index, copied_path);
                } else
                    continue;
            }

            if (o instanceof JSONArray) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONArrayAddressNode(key));
                recurseThroughArray(targets, copied_path, (JSONArray) o);
            }

            if (o instanceof JSONObject) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONArrayAddressNode(key));
                recurseThroughObject(targets, copied_path, (JSONObject) o);
            }

        }
    }

    protected static void recurseThroughObject(List<List<JSONAddressNode>> targets, List<JSONAddressNode> cur_path, JSONObject object) throws JSONException {
        JSONArray names = object.names();
        if (names == null) return;

        ArrayList<String> names_array = new ArrayList<>();

        for (int index = 0; index < names.length(); index++) {
            names_array.add((String) names.get(index));
        }

        for (String key : names_array) {
            Object o = object.get(key);

            if (o instanceof String) {
                String str = (String) o;
                if (!str.isEmpty() && str.charAt(0) == ITEM_REPLACER) {
                    int index = str.charAt(1) - NUM_FIELD_START;
                    List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                    copied_path.add(new JSONObjectAddressNode(key));
                    targets.set(index, copied_path);
                } else
                    continue;
            }

            if (o instanceof JSONArray) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONObjectAddressNode(key));
                recurseThroughArray(targets, copied_path, (JSONArray) o);
            }

            if (o instanceof JSONObject) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONObjectAddressNode(key));
                recurseThroughObject(targets, copied_path, (JSONObject) o);
            }

        }
    }


    /**
     * Creates deep copy of JSONArray
     */
    protected static JSONArray recursiveCloneJsonArray(JSONArray array) throws JSONException {
        JSONArray copy = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            Object obj = array.opt(i);
            if (obj instanceof JSONArray)
                copy.put(i, recursiveCloneJsonArray(((JSONArray) obj)));
            else if (obj instanceof JSONObject)
                copy.put(i, recursiveCloneJsonObject(((JSONObject) obj)));
            else
                copy.put(i, obj);

        }
        return copy;
    }

    /**
     * Creates deep copy of JSONObject
     */
    protected static JSONObject recursiveCloneJsonObject(JSONObject object) throws JSONException {
        JSONObject copy = new JSONObject();

        for (String i : getIterable(object.keys())) {
            Object obj = object.opt(i);
            if (obj instanceof JSONArray)
                copy.put(i, recursiveCloneJsonArray(((JSONArray) obj)));
            else if (obj instanceof JSONObject)
                copy.put(i, recursiveCloneJsonObject(((JSONObject) obj)));
            else
                copy.put(i, obj);
        }

        return copy;
    }

    /**
     * Wraps iterator into iterable
     */
    protected static <T> Iterable<T> getIterable(final Iterator<T> i) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

}
