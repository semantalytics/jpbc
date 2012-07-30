package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.DFAAlphabet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DefaultAlphabet implements DFAAlphabet {
    private int size;
    private Map<Character, Integer> map;

    public DefaultAlphabet() {
        this.size = 0;
        this.map = new HashMap<Character, Integer>();
    }

    public void addLetter(Character... characters) {
        for (Character character : characters) {
            map.put(character, size++);
        }
    }

    public int getSize() {
        return size;
    }

    public int getIndex(Character character) {
        if (map.containsKey(character))
            return map.get(character);

        return -1;
    }
}
