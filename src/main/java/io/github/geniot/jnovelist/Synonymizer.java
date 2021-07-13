package io.github.geniot.jnovelist;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 19/07/16
 */
public class Synonymizer {
    public static Map<String, String> MAP_CONTENT = new HashMap();


    public static void init() {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResource("synonyms/syn_ru.txt").openStream();
            List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8.name());

            StringBuilder sb = new StringBuilder();
            for (String str : lines) {
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
                if (!StringUtils.isNumeric(str) && !StringUtils.isEmpty(str)) {
                    sb.append(str);
                    sb.append('\n');
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String search(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return "";
        }


        StringBuffer sb = new StringBuffer();
        if (MAP_CONTENT.containsKey(keyword)) {
            sb.append("<h1>" + keyword + "</h1>");
            sb.append(paragraph(MAP_CONTENT.get(keyword)));
//            sb.append("<hr>");
            sb.append("<br>");
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
//                sb.append("<hr>");
                sb.append("<br>");
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

