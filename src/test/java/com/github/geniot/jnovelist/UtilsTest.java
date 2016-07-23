package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.Utils;
import junit.framework.TestCase;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 06/07/16
 */
public class UtilsTest extends TestCase {
    public void testEncrypt(){
        String str = "some string плюс русский текст";
        String code = Utils.base64encode(str);
        String decode = Utils.base64decode(code);
        assertEquals(decode,str);
    }

    public void testByteEncrypt(){
        try {
            String str = "some string плюс русский текст";
            byte[] code = Utils.encryptBytes(str.getBytes("UTF-8"));
            byte[] decode = Utils.decryptBytes(code);
            assertEquals(new String(decode,"UTF-8"),str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
