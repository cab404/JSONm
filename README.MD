[![Build Status](https://travis-ci.org/cab404/JSONm.svg)](https://travis-ci.org/cab404/JSONm)
JSONmake
========
Pretty simple json template builder for json.org library (integrated into Android).

### Simple example
```java
JSONMaker maker = new SimpleJSONMaker();
maker.add("request", "{'auth': {'session_id': *}, 'request-type': *}");

maker.make("request", "SOME_MD_HASH", "login");
// Returns JSONObject with
// {'auth': {'session_id': 'SOME_MD_HASH'}, 'request': 'login'}
```

### A little but more complicated examle
```java
// all stuff from previous example
maker.add("request-data", "{'method':*, 'uri':*, 'body':*}");

maker.make(
  "request-data",
  "POST", "/comments",
  // 'mod' modifies supplied map
  mod(// 'ins' inserts template with given parameters
      ins("request", "ASDF_ASFF", "comment")
      )
      .put(
        "request-data",
        // Map inserts new empty map
        map().put("text", "Nobody likes you, anon.")
      )
);
// Returns JSONObject with
/*
{
    "method": "POST",
    "uri": "/comments",
    "body": {
        "request-type": "comment",
        "auth": {"session_id": "ASDF_ASFF"},
        "request-data": {"text": "Nobody likes you, anon."}
    }
}
*/

```
### Also features simple JSON serializer!
```java
  public static class Teeest {
      public static final JSONSerializer<Teeest.class> SERIALIZER
       = new JSONSerializer<>(Teeest.class);

      public int intVal = 42;
      public long longVal = 413;
      public String waaa = "hnnnnnnnn~"

  }

  // ...

  Teeest tea = new Teeest();
  JSONObject sarapls = Teeest.SERIALIZER.serialize(tea, new JSONObject());
  // {'intVal': 42, 'longVal': 412, 'waaa': 'hnnnnnnnn~'}

  sarapls.put("waaa", "~nnnnnng!");
  Teeest.SERIALIZER.deserialize(sarapls, tea);
  System.out.println(tea.waaa);
  // ~nnnnnng!


```
