package com.github.geniot.jnovelist.project;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 15/06/17
 */
@XmlRootElement
public class Scene implements Serializable {
    private String description = "";
    private String content = "";
    private int caretPos = 0;
    private int viewPos = 0;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    @XmlElement
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getViewPos() {
        return viewPos;
    }

    @XmlElement
    public void setViewPos(int viewPos) {
        this.viewPos = viewPos;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content;
    }

    public int getCaretPos() {
        return caretPos;
    }

    @XmlElement
    public void setCaretPos(int caretPos) {
        this.caretPos = caretPos;
    }
}
