package com.sh.homestaymanagement.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.sh.homestaymanagement.domain.RoomDomain;

import java.lang.reflect.Type;
import java.util.List;

public class MySharedPreferences {
    private static final String MY_SHARE_PREFERENCES = "MySharedPreferencesHomestay";
    private final Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void clearAllData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void clearDataByKey(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    public void putListRoomCart(String key, List<RoomDomain> listItems) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new GsonBuilder().create();
        JsonArray array = gson.toJsonTree(listItems).getAsJsonArray();
        editor.putString(key, array.toString());
        editor.apply();
    }

    public List<RoomDomain> getListRoomCart(String key) {
        Gson gson = new Gson();
        List<RoomDomain> listItems;
        SharedPreferences sharedPref = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(key, "");

        Type type = new TypeToken<List<RoomDomain>>() {
        }.getType();
        listItems = gson.fromJson(jsonPreferences, type);

        return listItems;
    }
}
