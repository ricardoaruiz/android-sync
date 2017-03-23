package br.com.rar.agenda.utils;

import com.google.gson.Gson;

/**
 * Created by ralmendro on 1/3/17.
 */

public class JsonUtil {

    public static Object fromJson(String json, Class clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

}
