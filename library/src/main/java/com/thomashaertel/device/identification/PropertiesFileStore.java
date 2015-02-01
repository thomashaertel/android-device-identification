package com.thomashaertel.device.identification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class PropertiesFileStore implements KeyValueStore {

    private static final String FILE_ENCODING = "UTF8";

    private final String mFilename;

    private Properties mProperties;

    public PropertiesFileStore(String filename) {
        mFilename = filename;

        load();
    }

    public String getmFilename() {
        return mFilename;
    }

    @Override
    public void clear() {
        mProperties.clear();

        save();
    }

    @Override
    public boolean contains(String key) {
        return mProperties.containsKey(key);
    }

    @Override
    public String get(String key) {
        return mProperties.getProperty(key);
    }

    @Override
    public String put(String key, String value) {
        Object oldObj = mProperties.setProperty(key, value);

        save();

        return oldObj.toString();
    }

    @Override
    public boolean isEmpty() {
        return mProperties.isEmpty();
    }

    @Override
    public String remove(String key) {
        Object oldObj = mProperties.remove(key);

        save();

        return oldObj.toString();
    }

    @Override
    public int size() {
        return mProperties.size();
    }

    private void load() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(mFilename), FILE_ENCODING));
            mProperties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mFilename), FILE_ENCODING));
            mProperties.store(out, null);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
