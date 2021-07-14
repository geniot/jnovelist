package io.github.geniot.jnovelist;


import io.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;


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

    public DnDTabbedPane(String tnt, List<Chapter> chapterList) {
        super(JTabbedPane.TOP);
        this.titleNamingType = tnt;

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                //Utils.updateStatus(DnDTabbedPane.this);
            }
        });

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Utils.updateStatus(DnDTabbedPane.this);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
//                boolean enableRemoval = (e.isAltDown() && e.isControlDown());

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
                        ButtonTabComponent tabTitle = new ButtonTabComponent(DnDTabbedPane.this, comp);
                        insertTab(title, null, comp, null, tabNumber);
                        DnDTabbedPane.this.setTabComponentAt(tabNumber, tabTitle);
                        setSelectedComponent(comp);
                        updateLabels();
                    }
                }


                SwingUtilities.invokeLater(() -> {
                    Component c = getSelectedComponent();
                    if (c != null && c instanceof ChapterEditor) {
                        ((ChapterEditor) c).getDocumentPane().getEditor().requestFocus();
                    }
                });

                dragging = false;
                tabImage = null;
                repaint();
            }
        });


        plusPanel = new JPanel();
        addTab("+", plusPanel);

        for (Chapter chapter : chapterList) {
            addNewTab(chapter);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getSelectedComponent() != null
                        && getSelectedComponent().equals(plusPanel)) {
                    addNewTab(new Chapter());
                }
            }
        });
    }

    public void addNewTab(final Chapter chapter) {
        //adding new tab
        try {
            int count = getTabCount();

            final Component c;

            //defining component to add depending on the incoming parameter
            if (chapter == null) {
                //creating empty tab (mouse click)
                c = new ChapterEditor(new Chapter());
            } else {
                c = new ChapterEditor(chapter);
            }

            int extraTabsCount = 1;

            insertTab(getLabelByIndex(count - extraTabsCount), null, c, null, count - extraTabsCount);
            ButtonTabComponent tabTitle = new ButtonTabComponent(this, c);
            setTabComponentAt(count - extraTabsCount, tabTitle);
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
            return Utils.decimalIndexToRoman(i);
        } else if (titleNamingType.equals(INDEX_TO_DECIMAL)) {
            return Utils.indexToDecimal(i);
        } else if (titleNamingType.equals(INDEX_TO_ALPHABET)) {
            return Utils.indexToAlphabet(i);
        } else {
            return Utils.indexToDecimal(i);
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

}

