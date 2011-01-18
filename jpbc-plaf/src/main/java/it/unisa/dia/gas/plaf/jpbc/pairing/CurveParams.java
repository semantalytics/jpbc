package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * TODO: introduce immutable...
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveParams extends LinkedHashMap<String, String> implements CurveParameters {


    public CurveParams() {
    }


    public String getType() {
        return get("type");
    }

    public long getLong(String key) {
        String value = get(key);
        if (value == null)
            throw new IllegalArgumentException("Cannot find value for the following key : " + key);

        return Long.parseLong(value);
    }


    public String getString(String key) {
        String value = get(key);
        if (value == null)
            throw new IllegalArgumentException("Cannot find value for the following key : " + key);

        return value;
    }

    public int getInt(String key) {
        String value = get(key);
        if (value == null)
            throw new IllegalArgumentException("Cannot find value for the following key : " + key);

        return Integer.parseInt(value);
    }

    public BigInteger getBigInteger(String key) {
        String value = get(key);
        if (value == null)
            throw new IllegalArgumentException("Cannot find value for the following key : " + key);

        return new BigInteger(value);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;

        return new BigInteger(value);
    }

    public String getString(String key, String defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;

        return value;
    }

    public CurveParams load(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                line = line.trim();
                if (line.length() == 0)
                    continue;
                if (line.startsWith("#"))
                    continue;

                StringTokenizer tokenizer = new StringTokenizer(line, "= :", false);
                String key = tokenizer.nextToken();
                String value = tokenizer.nextToken();

                put(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public CurveParams load(String path) {
        InputStream inputStream = null;

        File file = new File(path);
        if (file.exists()) {
            try {
                inputStream = file.toURI().toURL().openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        }

        if (inputStream == null)
            throw new IllegalArgumentException("No valid resource found!");

        load(inputStream);

        try {
            inputStream.close();
        } catch (IOException e) {
        }

        return this;
    }

    public String toString(String separator) {
        StringBuffer buffer = new StringBuffer();

        for (Map.Entry<String, String> entry : entrySet()) {
            buffer.append(entry.getKey()).append(separator).append(entry.getValue()).append("\n");
        }

        return buffer.toString();
    }

    
    public String toString() {
        return toString(" ");
    }

}
