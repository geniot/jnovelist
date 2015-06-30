package com.github.geniot.jnovelist.model;

import java.io.Serializable;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 24/06/15
 */
public class Chapter implements Serializable {
    private int number;
    private int part;
    private String text;
    private String synopsis;
    private int caretPosition;
    private int viewPosition;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public void setCaretPosition(int caretPosition) {
        this.caretPosition = caretPosition;
    }

    public int getViewPosition() {
        return viewPosition;
    }

    public void setViewPosition(int viewPosition) {
        this.viewPosition = viewPosition;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
