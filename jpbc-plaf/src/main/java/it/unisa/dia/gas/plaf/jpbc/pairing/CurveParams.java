package it.unisa.dia.gas.plaf.jpbc.pairing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveParams extends LinkedHashMap<String, String> {


    public CurveParams() {
    }


    public String getType() {
        return get("type");
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

    public CurveParams load(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.isEmpty())
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

    public String toString(String separator) {
        StringBuffer buffer = new StringBuffer();

        for (String key : keySet()) {
            buffer.append(key).append(separator).append(get(key)).append("\n");
        }

        return buffer.toString();
    }

    
    public String toString() {
        return toString("=");
    }

}
