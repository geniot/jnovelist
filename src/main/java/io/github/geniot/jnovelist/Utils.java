package io.github.geniot.jnovelist;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static final Map<String, String> LAFS;

    static {
        Map<String, String> aMap = new LinkedHashMap<>();
        aMap.put("FlatLight", "com.formdev.flatlaf.FlatLightLaf");
        aMap.put("FlatDark", "com.formdev.flatlaf.FlatDarkLaf");
        aMap.put("FlatDarcula", "com.formdev.flatlaf.FlatDarculaLaf");
        aMap.put("FlatIntelliJ", "com.formdev.flatlaf.FlatIntelliJLaf");

        aMap.put("Arc", "com.formdev.flatlaf.intellijthemes.FlatArcIJTheme");

        aMap.put("Acryl", "com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        aMap.put("Aero", "com.jtattoo.plaf.aero.AeroLookAndFeel");
        aMap.put("Aluminium", "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        aMap.put("Bernstein", "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
        aMap.put("Fast", "com.jtattoo.plaf.fast.FastLookAndFeel");
        aMap.put("Graphite", "com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        aMap.put("HiFi", "com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        aMap.put("Luna", "com.jtattoo.plaf.luna.LunaLookAndFeel");
        aMap.put("McWin", "com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        aMap.put("Mint", "com.jtattoo.plaf.mint.MintLookAndFeel");
        aMap.put("Noire", "com.jtattoo.plaf.noire.NoireLookAndFeel");
        aMap.put("Smart", "com.jtattoo.plaf.smart.SmartLookAndFeel");
        aMap.put("Texture", "com.jtattoo.plaf.texture.TextureLookAndFeel");
        LAFS = Collections.unmodifiableMap(aMap);
    }

    public static final String DEFAULT_LAF = "Smart";
    // Parallel arrays used in the conversion process.
    public static String[] RCODE = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    public static int[] BVAL = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    public static String[] ALPHABET = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static String LAF_SUFFIX = "LookAndFeel";


    public static String decimalIndexToRoman(int binary) {
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

    public static void stylizeButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setFocusable(false);
        Dimension dim = new Dimension(40, 40);
        button.setPreferredSize(dim);
        button.setMinimumSize(dim);
        button.setMaximumSize(dim);
        button.setFont(new Font("Arial", Font.BOLD, 10));
//        button.setBorder(null);
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    public static String text2html(String text, String docStart, String docEnd) {
        StringBuffer sb = new StringBuffer();
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

    public static String html2text(String html) {
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
        String lafClassName = LAFS.get(lafName);
        try {
            if (lafClassName == null) {
                lafClassName = LAFS.get(DEFAULT_LAF);
            }
            initFlat(lafClassName);
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initFlat(String lafClassName) {
        if (lafClassName.equals("FlatLight")) {
            FlatLightLaf.setup();
        }
        if (lafClassName.equals("FlatDark")) {
            FlatDarkLaf.setup();
        }
        if (lafClassName.equals("FlatDarcula")) {
            FlatDarculaLaf.setup();
        }
        if (lafClassName.equals("FlatIntelliJ")) {
            FlatIntelliJLaf.setup();
        }
        if (lafClassName.equals("Arc")) {
            FlatArcIJTheme.setup();
        }
    }

}
