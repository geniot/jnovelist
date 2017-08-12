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
public class Chapter extends Scene implements Serializable {
    private Scene[] scenes = new Scene[]{};

    public Scene[] getScenes() {
        return scenes;
    }

    @XmlElement
    public void setScenes(Scene[] scenes) {
        this.scenes = scenes;
    }
}
