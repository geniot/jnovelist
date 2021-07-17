package io.github.geniot.jnovelist.actions;

import io.github.geniot.jnovelist.view.ExtrasDialog;
import io.github.geniot.jnovelist.view.JNovelistApplication;

import java.awt.event.ActionEvent;

public class NovelAction extends AbstractNovelistAction {
    private Type type;

    public NovelAction(JNovelistApplication f, Type t) {
        super(f);
        this.type = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (type.equals(Type.HEROES)) {
            new ExtrasDialog(frame.novel.getHeroes(), frame, "Characters").setVisible(true);
        } else if (type.equals(Type.PLACES)) {
            new ExtrasDialog(frame.novel.getPlaces(), frame, "Places").setVisible(true);
        } else if (type.equals(Type.THINGS)) {
            new ExtrasDialog(frame.novel.getThings(), frame, "Artefacts").setVisible(true);
        } else if (type.equals(Type.NOTES)) {
            new ExtrasDialog(frame.novel.getNotes(), frame, "Notes").setVisible(true);
        } else if (type.equals(Type.IMAGES)) {
            new ExtrasDialog(frame.novel.getImages(), frame, "Images").setVisible(true);
        }
    }

    public enum Type {
        HEROES, PLACES, THINGS, NOTES, IMAGES
    }
}
