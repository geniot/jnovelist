package io.github.geniot.jnovelist;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Utils {
    // Parallel arrays used in the conversion process.
    public static String[] RCODE = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    public static int[] BVAL = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

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

    public static void stylizeButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setFocusable(false);
        Dimension dim = new Dimension(30, 30);
        button.setPreferredSize(dim);
        button.setMinimumSize(dim);
        button.setMaximumSize(dim);
        button.setMargin(new Insets(0, 0, 0, 0));
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
}
