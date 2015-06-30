package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.JNovelistFrame;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 30/06/15
 */
public class AbstractNovelistAction  {

    protected JNovelistFrame frame;

    public AbstractNovelistAction(JNovelistFrame f) {
        this.frame = f;
    }
}
