package io.github.geniot.jnovelist.model;

public class LanguageElement implements Comparable<LanguageElement> {
    private String displayText;
    private String code;

    public LanguageElement(String dt, String c) {
        this.displayText = dt;
        this.code = c;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return displayText;
    }

    @Override
    public int compareTo(LanguageElement o) {
        return displayText.compareTo(o.getDisplayText());
    }
}
