package com.thomashaertel.device.identification;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;


public class SharedPreferencesStore implements KeyValueStore {

    private static final String DEFAULT_PREFERENCE_FILE = "licenseprefs";

    private SharedPreferences preferences;

    public SharedPreferencesStore(Context ctx) {
        this(ctx, DEFAULT_PREFERENCE_FILE, false);
    }

    public SharedPreferencesStore(Context ctx, boolean secure) {
        this(ctx, DEFAULT_PREFERENCE_FILE, secure);
    }

    public SharedPreferencesStore(Context ctx, final String filename) {
        this(ctx, filename, false);
    }

    public SharedPreferencesStore(Context ctx, final String filename, boolean secure) {
        SharedPreferences sharedPref = init(ctx, filename, secure);
    }

    private static SharedPreferences init(Context ctx, String filename, boolean secure) {
        SharedPreferences prefs = null;

        if (secure) {
            prefs = new SecurePreferences(ctx); // TODO Exchange to configure filename
        } else {
            prefs = ctx.getSharedPreferences(filename, Context.MODE_PRIVATE);
        }

        return prefs;
    }

    @Override
    public void clear() {
        preferences.edit().clear().apply();
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public String get(String key) {
        return preferences.getString(key, null);
    }

    @Override
    public String put(String key, String value) {
        String old = preferences.getString(key, null);

        preferences.edit().putString(key, value).apply();

        return old;
    }

    @Override
    public boolean isEmpty() {
        return preferences.getAll().isEmpty();
    }

    @Override
    public String remove(String key) {
        String old = preferences.getString(key, null);

        preferences.edit().remove(key).apply();

        return old;
    }

    @Override
    public int size() {
        return preferences.getAll().size();
    }
}
