package com.github.geniot.jnovelist;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class DnDTabbedPane extends JTabbedPane {
    static public String DECIMAL_TO_ROMAN = "DECIMAL_TO_ROMAN";
    static public String INDEX_TO_DECIMAL = "INDEX_TO_DECIMAL";
    static public String INDEX_TO_ALPHABET = "INDEX_TO_ALPHABET";


    private boolean dragging = false;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private int draggedTabIndex = 0;
    private String titleNamingType = DECIMAL_TO_ROMAN;
    public JPanel plusPanel;
    private String tabContentClassName;
    private String boundModelClassName;

    public String getBoundModelClassName() {
        return boundModelClassName;
    }

    public void setBoundModelClassName(String boundModelClassName) {
        this.boundModelClassName = boundModelClassName;
    }

    public String getTabContentClassName() {
        return tabContentClassName;
    }

    public void setTabContentClassName(String tabContentClassName) {
        this.tabContentClassName = tabContentClassName;
    }

    public String getTitleNamingType() {
        return titleNamingType;
    }

    public void setTitleNamingType(String titleNamingType) {
        this.titleNamingType = titleNamingType;
    }

    public DnDTabbedPane(String tnt) {
        super();
        this.titleNamingType = tnt;
        final DnDTabbedPane thisRef = this;

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                Utils.updateStatus(DnDTabbedPane.this);
            }
        });

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utils.updateStatus(DnDTabbedPane.this);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {

                if (!dragging) {
                    // Gets the tab index based on the mouse position
                    int tabNumber = getUI().tabForCoordinate(DnDTabbedPane.this, e.getX(), e.getY());

                    if (tabNumber >= 0 && tabNumber < getTabCount() - 1) {
                        draggedTabIndex = tabNumber;
                        Rectangle bounds = getUI().getTabBounds(DnDTabbedPane.this, tabNumber);


                        // Paint the tabbed pane to a buffer
                        Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics totalGraphics = totalImage.getGraphics();
                        totalGraphics.setClip(bounds);
                        // Don't be double buffered when painting to a static image.
                        setDoubleBuffered(false);
                        paintComponent(totalGraphics);

                        // Paint just the dragged tab to the buffer
                        tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
                        Graphics graphics = tabImage.getGraphics();
                        graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, DnDTabbedPane.this);

                        dragging = true;
                        repaint();
                    }
                } else {
                    currentMouseLocation = e.getPoint();

                    // Need to repaint
                    repaint();
                }

                super.mouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {

                if (dragging) {
                    int tabNumber = getUI().tabForCoordinate(DnDTabbedPane.this, e.getX(), 10);

                    if (tabNumber >= 0 && tabNumber < getTabCount() - 1) {
                        Component comp = getComponentAt(draggedTabIndex);
                        String title = getTitleAt(draggedTabIndex);
                        removeTabAt(draggedTabIndex);
                        ButtonTabComponent tabTitle = new ButtonTabComponent(thisRef);
                        insertTab(title, null, comp, null, tabNumber);
                        thisRef.setTabComponentAt(tabNumber, tabTitle);
                        setSelectedComponent(comp);
                        updateLabels();
                        Utils.enableSave(comp);
                    }
                }


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Component c = getSelectedComponent();
                        if (c != null && c instanceof ChapterEditor) {
                            ((ChapterEditor) c).getDocumentPane().getEditor().requestFocus();
                        }
                    }
                });

                dragging = false;
                tabImage = null;
                repaint();
            }
        });

        plusPanel = new JPanel();
        addTab("+", plusPanel);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getSelectedComponent() != null
                        && getSelectedComponent().equals(plusPanel)) {
                    if (titleNamingType.equals(DECIMAL_TO_ROMAN)) {
                        newProject();
                    } else {
                        addNewTab(null);
                    }
                }
            }
        });
    }

    public void addNewTab(final File file) {
        //adding new tab
        try {
            int count = getTabCount();
            ButtonTabComponent tabTitle = new ButtonTabComponent(this);

            final Component c;

            //defining component to add depending on the incoming parameter
            if (file == null) {
                //creating empty tab (mouse click)
                c = new ChapterEditor(file);
                Utils.enableSave(this);
            } else {
                if (file.isDirectory()) {
                    c = new DnDTabbedPane(DnDTabbedPane.INDEX_TO_DECIMAL);
                    final File[] ffs = file.listFiles();
                    Arrays.sort(ffs, Utils.FILE_NAME_NUMBER_COMPARATOR);
                    for (File f : ffs) {
                        if (StringUtils.isNumeric(FilenameUtils.getBaseName(f.getName()))){
                            ((DnDTabbedPane) c).addNewTab(f);
                        }
                    }
                } else {
                    c = new ChapterEditor(file);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ((ChapterEditor) c).getDocumentPane().getEditor().requestFocus();
                            int viewPos = 0;
                            if (Constants.PROPS.getProperty("verticalScrollBar:" + file.getAbsolutePath()) != null) {
                                viewPos = Integer.parseInt(Constants.PROPS.getProperty("verticalScrollBar:" + file.getAbsolutePath()));
                            }
                            if (file != null) {
                                ((ChapterEditor) c).getDocumentPane().getVerticalScrollBar().setValue(viewPos);
                            }
                        }
                    });

                }
            }


            insertTab(getLabelByIndex(count - 1), null, c, null, count - 1);
            setTabComponentAt(count - 1, tabTitle);
            setSelectedComponent(c);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void newProject() {
        try {
            int count = getTabCount();
            ButtonTabComponent tabTitle = new ButtonTabComponent(this);

            final Component c = new DnDTabbedPane(DnDTabbedPane.INDEX_TO_DECIMAL);
            ((DnDTabbedPane) c).addNewTab(null);

            insertTab(getLabelByIndex(count - 1), null, c, null, count - 1);
            setTabComponentAt(count - 1, tabTitle);
            setSelectedComponent(c);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateLabels() {
        for (int i = 0; i < getTabCount() - 1; i++) {
            setTitleAt(i, getLabelByIndex(i));
            getTabComponentAt(i).validate();
            getTabComponentAt(i).repaint();
        }
    }

    public String getLabelByIndex(int i) {
        if (titleNamingType.equals(DECIMAL_TO_ROMAN)) {
            return decimalToRoman(i);
        } else if (titleNamingType.equals(INDEX_TO_DECIMAL)) {
            return indexToDecimal(i);
        } else if (titleNamingType.equals(INDEX_TO_ALPHABET)) {
            return indexToAlphabet(i);
        } else {
            return indexToDecimal(i);
        }
    }


    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Are we dragging?
        if (dragging && currentMouseLocation != null && tabImage != null) {
            // Draw the dragged tab
            g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
        }
    }


    // Parallel arrays used in the conversion process.
    private static String[] RCODE = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static int[] BVAL = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static String[] ALPHABET = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private String decimalToRoman(int binary) {
        binary += 1;//binary is index that starts from 0
        if (binary <= 0 || binary >= 4000) {
            return "I";
        }
        String roman = "";         // Roman notation will be accumualated here.

        // Loop from biggest value to smallest, successively subtracting,
        // from the binary value while adding to the roman representation.
        for (int i = 0; i < RCODE.length; i++) {
            while (binary >= BVAL[i]) {
                binary -= BVAL[i];
                roman += RCODE[i];
            }
        }
        return roman;
    }


    private String indexToDecimal(int binary) {
        return String.valueOf(binary + 1);
    }

    private String indexToAlphabet(int binary) {
        if (binary < 0 || binary > ALPHABET.length - 1) {
            return ALPHABET[0];
        }
        return ALPHABET[binary];
    }


}
