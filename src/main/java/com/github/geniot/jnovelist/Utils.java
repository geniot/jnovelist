package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.LanguageElement;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.security.Key;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.geniot.jnovelist.Constants.DEFAULT_LAF;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static final String DEFAULT_ENCODING = "UTF-8";
    static BASE64Encoder enc = new BASE64Encoder();
    static BASE64Decoder dec = new BASE64Decoder();


    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public static String base64encode(String text) {
        try {
            return enc.encode(text.getBytes(DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }//base64encode

    public static String base64decode(String text) {
        try {
            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
    }//base64decode


    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        return encryptedValue;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public static byte[] encryptBytes(byte[] valueToEnc) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new BASE64Encoder().encode(encValue, baos);
            return baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return valueToEnc;
        }
    }

    public static byte[] decryptBytes(byte[] encryptedValue) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new BASE64Decoder().decodeBuffer(new ByteArrayInputStream(encryptedValue), baos);
            byte[] decValue = c.doFinal(baos.toByteArray());
            return decValue;
        } catch (Exception ex) {
            ex.printStackTrace();
            return encryptedValue;
        }
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

    public static JButton makeNavigationButton(String imageName,
                                               String actionCommand,
                                               String toolTipText,
                                               String altText) {
        String imgLocation = "images/" + imageName + ".png";
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.setFocusPainted(false);
        button.setIcon(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(imgLocation), altText));
        return button;
    }

    public static Object xml2entry(String content) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
            XMLDecoder decoder = new XMLDecoder(bais);
            Object eom = decoder.readObject();
            decoder.close();
            bais.close();
            return eom;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return new Object();
        }
    }

    public static String entry2xml(Object eom) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLEncoder encoder = new XMLEncoder(baos) {
                public PersistenceDelegate getPersistenceDelegate(Class<?> type) {
                    return super.getPersistenceDelegate(type);
                }
            };
            encoder.writeObject(eom);
            encoder.close();
            baos.close();
            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return "";
        }
    }

    public static void updateStatus(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container c = component.getParent();
                while (c != null && !(c instanceof JNovelistFrame)) {
                    c = c.getParent();
                }
                if (c != null) {
                    ((JNovelistFrame) c).updateStatus();
                }
            }
        });
    }

    public static void enableSave(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container c = component.getParent();
                while (c != null && !(c instanceof JNovelistFrame)) {
                    c = c.getParent();
                }
                if (c != null) {
                    ((JNovelistFrame) c).saveNovel.setEnabled(true);
                }
            }
        });
    }

    public static byte[] serialize(Object yourObject) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(yourObject);
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new byte[]{};
    }

    public static Object deserialize(byte[] yourBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    public static String html2text(String html) {
        if (html == null) {
            return html;
        }

        StringBuilder sb = new StringBuilder();
        Elements ps = Jsoup.parse(html).select("p");
        for (Element el : ps) {
            String s = el.toString();
            s = s.replaceAll("<[^>]*>", "");
            s = s.replaceAll("\\n", "");
            s = s.replaceAll("&nbsp;", " ");
            s = s.trim();
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String text2html(String text, String docStart, String docEnd) {
        StringBuilder sb = new StringBuilder();
        sb.append(docStart);
        String[] pps = text.split("\\n");
        for (String p : pps) {
            sb.append("<p>");
            sb.append(p);
            sb.append("</p>");
        }
        sb.append(docEnd);
        return sb.toString();
    }

    public static Comparator<File> FILE_NAME_NUMBER_COMPARATOR = new Comparator<File>() {

        @Override
        public int compare(File o1, File o2) {
            Integer i1 = getInteger(FilenameUtils.removeExtension(o1.getName()));
            Integer i2 = getInteger(FilenameUtils.removeExtension(o2.getName()));
            if (i1 == null && i2 == null) {
                return o1.getName().compareTo(o2.getName());
            }
            if (i1 == null) {
                return 1;
            }
            if (i2 == null) {
                return -1;
            }
            return i1.compareTo(i2);
        }

        @Override
        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    };


    public static Integer getInteger(String str) {
        try {
            return Integer.parseInt(str);

        } catch (NumberFormatException nfe) {
        }
        return null;
    }

    public static boolean textsEqual(String s1, String s2) {
        String tmp1 = s1.replaceAll("\n", "").replaceAll("\r", "");
        String tmp2 = s2.replaceAll("\n", "").replaceAll("\r", "");
        return tmp1.equals(tmp2);
    }


    // Parallel arrays used in the conversion process.
    public static String[] RCODE = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    public static int[] BVAL = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    public static String[] ALPHABET = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static String decimalIndexToRoman(int binary) {
        binary += 1;//binary is index that starts from 0
        if (binary <= 0 || binary >= 4000) {
            return "I";
        }
        String roman = "";         // Roman notation will be accumualated here.

        // Loop from biggest value to smallest, successively subtracting,
        // from the binary value while adding to the roman representation.
        for (int i = 0; i < RCODE.length; i++) {
            while (binary >= BVAL[i]) {
                binary -= BVAL[i];
                roman += RCODE[i];
            }
        }
        return roman;
    }


    public static String indexToDecimal(int binary) {
        return String.valueOf(binary + 1);
    }

    public static String indexToAlphabet(int binary) {
        if (binary < 0 || binary > ALPHABET.length - 1) {
            return ALPHABET[0];
        }
        return ALPHABET[binary];
    }

    public static File getGitRootDir(String projectFile) {
        File f = new File(projectFile);
        while (!isRepo(f) && f != null) {
            f = f.getParentFile();
        }
        return f;
    }

    private static boolean isRepo(File file) {
        File f = new File(file.getAbsolutePath() + File.separator + ".git");
        return f.exists() && f.isDirectory();
    }

    public static String runCommand(String command, File f) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command.toString(), null, f);

            InputStreamReader isr = new InputStreamReader(proc.getInputStream());
            BufferedReader rdr = new BufferedReader(isr);
            String line;
            while ((line = rdr.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }

            isr = new InputStreamReader(proc.getErrorStream());
            rdr = new BufferedReader(isr);
            while ((line = rdr.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            int rc = proc.waitFor();  // Wait for the process to complete
//            stringBuffer.append(rc);
//            stringBuffer.append("\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            stringBuffer.append(ex.getMessage());
        }
        return stringBuffer.toString();
    }

    /**
     * @param colorStr e.g. "#FFFFFF"
     * @return
     */
    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static String color2hex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase();
    }

    public static void setLAF(String lafName, Component component) {
        String lafClassName = Constants.LAF_PREFIX + lafName.toLowerCase() + "." + lafName + Constants.LAF_SUFFIX;
        try {
            if (lafClassName == null) {
                lafClassName = DEFAULT_LAF;
            }
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SortedSet<LanguageElement> getLanguages() {
        SortedSet<LanguageElement> s = new TreeSet<>();
        for (String propertyName : Constants.PROPS.stringPropertyNames()) {
            if (propertyName.startsWith("LANGUAGE_")) {
                String languageCode = Constants.PROPS.getProperty(propertyName);
                String displayText = Constants.RES.getString("language." + languageCode);
                LanguageElement languageElement = new LanguageElement(displayText, languageCode);
                s.add(languageElement);
            }
        }
        return s;
    }

    public static LanguageElement findElementByCode(ComboBoxModel model, String code) {
        for (int i = 0; i < model.getSize(); i++) {
            LanguageElement languageElement = (LanguageElement) model.getElementAt(i);
            if (languageElement.getCode().equalsIgnoreCase(code)) {
                return languageElement;
            }
        }
        return null;
    }
}
