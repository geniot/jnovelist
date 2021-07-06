package com.github.geniot.jnovelist;


import com.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class ButtonTabComponent extends JPanel {
    private final DnDTabbedPane pane;
    private final JButton removeButton;
    private Component component;
    private Chapter file;
//    private JTextArea textArea;

    public ButtonTabComponent(final DnDTabbedPane pane, Component c, String actionCommand, Chapter f) {
        //unset default FlowLayout' gaps
        super(new BorderLayout());
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.file = f;
        this.component = c;
        this.pane = pane;
        setOpaque(false);

//        if (actionCommand.equals(Constants.LOAD_NOVEL_ACTION_COMMAND)) {
//            textArea = new JTextArea();
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//            textArea.addFocusListener(new FocusAdapter() {
//                @Override
//                public void focusGained(FocusEvent e) {
//                    pane.setSelectedComponent(ButtonTabComponent.this.component);
//                }
//            });
//            textArea.getDocument().addDocumentListener(new DocumentListener() {
//                @Override
//                public void insertUpdate(DocumentEvent e) {
//                    Utils.enableSave(ButtonTabComponent.this);
//                }
//
//                @Override
//                public void removeUpdate(DocumentEvent e) {
//                    Utils.enableSave(ButtonTabComponent.this);
//                }
//
//                @Override
//                public void changedUpdate(DocumentEvent e) {
//                    Utils.enableSave(ButtonTabComponent.this);
//                }
//            });
//            if (file != null) {
//                textArea.setText(file.getDescription());
////                if (Constants.SYNOPSIS_PROPS.containsKey(file.getAbsolutePath())) {
////                    textArea.setText(Constants.SYNOPSIS_PROPS.getProperty(file.getAbsolutePath()));
////                }
//            }
//            //add more space between the label and the button
//            textArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
//
//            JScrollPane sp = new JScrollPane(textArea);
//            sp.setPreferredSize(new Dimension(300, pane.getTitleNamingType().equals(DnDTabbedPane.DECIMAL_TO_ROMAN) ? 55 : 55));
//
//            add(sp, BorderLayout.CENTER);
//        } else {
            //make JLabel read titles from JTabbedPane
            JLabel label = new JLabel() {
                public String getText() {
                    int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                    if (i != -1) {
                        return pane.getTitleAt(i);
                    }
                    return null;
                }
            };
            label.setSize(new Dimension(label.getPreferredSize().width * 2, label.getPreferredSize().height * 2));
            add(label, BorderLayout.CENTER);
//        }

        //tab button
        removeButton = new TabButton();
        add(removeButton, BorderLayout.EAST);

        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        enableRemoval(false);

    }

//    public String getText() {
//        return textArea.getText();
//    }

    public void enableRemoval(boolean enable) {
        removeButton.setEnabled(enable);
        removeButton.setVisible(enable);
    }

    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);

            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    Component component = e.getComponent();
                    if (component instanceof AbstractButton && isEnabled()) {
                        AbstractButton button = (AbstractButton) component;
                        button.setBorderPainted(true);
                    }
                }

                public void mouseExited(MouseEvent e) {
                    Component component = e.getComponent();
                    if (component instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) component;
                        button.setBorderPainted(false);
                    }
                }

            });

            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                //removing tab
                pane.remove(i);
                if (i == pane.getTabCount() - 1) {
                    pane.setSelectedIndex(pane.getTabCount() - 2);
                }
                pane.updateLabels();
                Utils.enableSave(pane);
            }
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(isEnabled() ? Color.BLACK : Color.GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.BLUE);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

}
