package it.unisa.dia.gas.crypto.circuit;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.StringTokenizer;

import static it.unisa.dia.gas.crypto.circuit.Gate.Type.*;
import static org.junit.Assert.assertEquals;

public class ArithmeticCircuitTest {


    @Test
    public void testMultiplication() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 2;
        int q = 1;
        int depth = 2;
        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuit.ArithmeticCircuitGate[]{
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(AND, 3, 2, new int[]{0, 1}, Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 1", 2)), Zq.newOneElement());
        assertEquals(circuit.evaluate(toElement(Zq, "2 2", 2)), Zq.newElement(4));
        assertEquals(circuit.evaluate(toElement(Zq, "1 0", 2)), Zq.newZeroElement());
    }

    @Test
    public void testAddition() throws Exception {
        Field Zq = new ZrField(new SecureRandom(), BigInteger.valueOf(17));

        int ell = 2;
        int q = 1;
        int depth = 2;
        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuit.ArithmeticCircuitGate[]{
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(OR, 3, 2, new int[]{0, 1}, Zq.newOneElement(), Zq.newOneElement()),
        });

        assertEquals(circuit.evaluate(toElement(Zq, "1 1", 2)), Zq.newElement(2));
        assertEquals(circuit.evaluate(toElement(Zq, "1 0", 2)), Zq.newOneElement());
        assertEquals(circuit.evaluate(toElement(Zq, "1 -1", 2)), Zq.newZeroElement());
    }


    protected Element[] toElement(Field Zq, String assignment, int ell) {
        Element[] elements = new Element[ell];
        StringTokenizer st = new StringTokenizer(assignment, " ");
        int i = 0;
        while (st.hasMoreTokens()) {
            elements[i++] = Zq.newElement(new BigInteger(st.nextToken()));
        }

        return elements;
    }

}