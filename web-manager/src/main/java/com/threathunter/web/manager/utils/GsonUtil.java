package com.threathunter.web.manager.utils;

import java.util.*;
import com.google.gson.*;
import java.io.*;
import com.google.gson.stream.*;

public class GsonUtil
{
    private static Gson gson;
    
    public static Gson getGsonInstance() {
        return GsonUtil.gson;
    }
    
    public static <T> T fromJson(final String json, final Class<T> classOfT) {
        T t = null;
        if (json != null && json.length() > 0 && !json.startsWith("[") && classOfT == List.class) {
            t = (T)new ArrayList();
            final List<String> o = (List<String>)t;
            o.add(json);
            return t;
        }
        t = (T)GsonUtil.gson.fromJson(json, (Class)classOfT);
        if (t == null) {
            try {
                if (classOfT == List.class) {
                    t = (T)new ArrayList();
                }
                if (classOfT == Map.class) {
                    t = (T)new HashMap();
                }
            }
            catch (Exception e) {
                throw new IllegalStateException(String.format("GsonUtil:fromJson,json=%s,class=%s", json, classOfT.getCanonicalName()));
            }
        }
        return t;
    }
    
    public static String toJson(final Object obj) {
        return GsonUtil.gson.toJson(obj);
    }
    
    static {
        GsonUtil.gson = new GsonBuilder().create();
    }
    
    public static class TestLongTypeAdapter extends TypeAdapter<Long>
    {
        public void write(final JsonWriter jsonWriter, final Long aLong) throws IOException {
        }
        
        public Long read(final JsonReader jsonReader) throws IOException {
            if (!jsonReader.hasNext()) {
                return null;
            }
            final Boolean b = jsonReader.nextBoolean();
            if (b) {
                return 1L;
            }
            if (!b) {
                return 0L;
            }
            return null;
        }
    }
}
