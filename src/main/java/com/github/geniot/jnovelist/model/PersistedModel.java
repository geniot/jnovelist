package com.github.geniot.jnovelist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class PersistedModel implements Serializable {
    private List<Chapter> novel = new ArrayList<Chapter>();
    private Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List<Chapter> getNovel() {
        return novel;
    }

    public void setNovel(List<Chapter> novel) {
        this.novel = novel;
    }
}
