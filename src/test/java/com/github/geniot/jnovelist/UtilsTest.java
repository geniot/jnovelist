package com.github.geniot.jnovelist;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 06/07/16
 */
public class UtilsTest {
    @Test
    public void testEncrypt() {
        String str = "some string плюс русский текст";
        String code = Utils.base64encode(str);
        String decode = Utils.base64decode(code);
        assertEquals(decode, str);
    }

    @Test
    public void testByteEncrypt() {
        try {
            String str = "some string плюс русский текст";
            byte[] code = Utils.encryptBytes(str.getBytes("UTF-8"));
            byte[] decode = Utils.decryptBytes(code);
            assertEquals(new String(decode, "UTF-8"), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
