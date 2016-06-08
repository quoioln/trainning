package com.ntt.smartpbx.test.util.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Util {
    /**
     * Use for mock final static field
     * @param field
     * @param newValue
     * @throws Exception
     */
    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
