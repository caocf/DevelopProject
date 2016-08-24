package com.xhl.world.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonSerializeUtil {

    /**
     * 序列化方法
     *
     * @param bean
     * @return
     */
    public static String bean2Json(Object bean) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(bean);
    }

    /**
     * 带泛型的集合的序列化方法
     *
     * @return
     */
    public static <T> String list2Json(List<T> list) {
        Type type = new com.google.gson.reflect.TypeToken<T>() {
        }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(list, type);
    }

    public static <T> List<T> json2List(String json) {
        Type type = new com.google.gson.reflect.TypeToken<List<T>>() {
        }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json, type);
    }

    /**
     * 带泛型的hash的序列化方法
     *
     * @return
     */
    public static <S, T> String map2Json(Map<S, T> map) {
        Type type = new com.google.gson.reflect.TypeToken<T>() {
        }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(map, type);
    }


    public static <S, T> Map<S, T> json2Map(String json) {
        Type type = new com.google.gson.reflect.TypeToken<Map<S, T>>() {
        }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json, type);
    }

    /**
     * 反序列化方法
     *
     * @return T 泛型表示任意类型
     */
    public static <T> T json2Bean(String json, Class<T> _class) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json, _class);
    }


    public static <T> T json2Bean(String json,Type type) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json, type);
    }



    public static class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public JsonElement serialize(Timestamp src, Type arg1, JsonSerializationContext arg2) {
            String dateFormatAsString = format.format(new Date(src.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }

        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }

            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }
}
