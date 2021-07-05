package com.github.geniot.jnovelist;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 19/07/16
 */
public class Synonymizer {
    public static Map<String, String> MAP_CONTENT = new HashMap<String, String>();


    public static void init() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("synonyms" + File.separator + "syn_ru.txt"), "UTF8"));

            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = in.readLine()) != null) {
                if (StringUtils.isEmpty(str)) {
                    String entry = sb.toString();
                    if (!StringUtils.isEmpty(entry)) {
                        //dumping entry
                        int indexOf = entry.indexOf('\n');
                        String hw = entry.substring(0, indexOf);
                        String syn = entry.substring(indexOf + 1, entry.length() - 1);
                        MAP_CONTENT.put(hw, syn);
                    }
                    sb = new StringBuilder();
                }
                if (!StringUtils.isNumeric(str)) {
                    sb.append(str);
                    sb.append('\n');
                }
            }

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String search(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return "";
        }


        StringBuilder sb = new StringBuilder();
        if (MAP_CONTENT.containsKey(keyword)) {
            sb.append("<h1>" + keyword + "</h1>");
            sb.append(paragraph(MAP_CONTENT.get(keyword)));
            sb.append("<hr>");
        }

        for (String hw : MAP_CONTENT.keySet()) {
            if (hw.equals(keyword)) {
                continue;//avoid duplication in results
            }
            String content = paragraph(MAP_CONTENT.get(hw));
            String[] splits = content.split("\\b" + keyword + "\\b");
            if (splits.length > 1) {
                sb.append("<h1>" + hw + "</h1>");
                sb.append(content.replaceAll("\\b" + keyword + "\\b", "<b>" + keyword + "</b>"));
                sb.append("<hr>");
            }
        }

        return sb.toString();
    }

    private static String paragraph(String s) {
        String[] splits = s.split("\\n");
        StringBuilder sb = new StringBuilder();
        for (String split : splits) {
            sb.append("<p>");
            sb.append(split);
            sb.append("</p>");
        }
        return sb.toString();
    }

}
