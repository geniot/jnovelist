package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.actions.*;
import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;
import com.lightdev.app.shtm.SHTMLPanelImpl;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class JNovelistFrame extends JFrame {

    protected JButton loadNovel;
    public JButton unloadNovel;
    public JButton saveNovel;
    public JButton exportNovel;

    public JButton heroes;
    public JButton places;
    public JButton things;
    public JButton notes;
    public JButton images;

    public JButton style;


    public JButton info;

    public JButton dictionary;

    public DnDTabbedPane dnDTabbedPane;

    public JLabel statusLabel;
    public JLabel partStatusLabel;
    public JLabel allStatusLabel;

    public String openFileName;

    public JNovelistFrame() {
        super("JNovelist");

        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/appicon.ico_32x32.png")).getImage());

        try {
            Constants.PROPS.load(new FileInputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME));
            if (Constants.PROPS.containsKey(Constants.PROP_STYLE)) {
                Constants.HTML_DOC_START = Constants.PROPS.getProperty(Constants.PROP_STYLE);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new OnExitAction(this));

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);

        loadNovel = Utils.makeNavigationButton("Load", Constants.LOAD_NOVEL_ACTION_COMMAND, "Открыть", "Load");
        unloadNovel = Utils.makeNavigationButton("Eject", Constants.UNLOAD_NOVEL_ACTION_COMMAND, "Закрыть", "Unload");
        exportNovel = Utils.makeNavigationButton("Export", Constants.EXPORT_NOVEL_ACTION_COMMAND, "Экспорт", "Export");

        heroes = Utils.makeNavigationButton("Heros", Constants.HEROES_NOVEL_ACTION_COMMAND, "Люди", "Heroes");
        places = Utils.makeNavigationButton("Places", Constants.PLACES_NOVEL_ACTION_COMMAND, "Места", "Places");
        things = Utils.makeNavigationButton("Things", Constants.THINGS_NOVEL_ACTION_COMMAND, "Вещи", "Things");
        notes = Utils.makeNavigationButton("Notes", Constants.NOTES_NOVEL_ACTION_COMMAND, "Записи", "Notes");
        images = Utils.makeNavigationButton("Images", Constants.IMAGES_NOVEL_ACTION_COMMAND, "Картинки", "Images");

        style = Utils.makeNavigationButton("Style", Constants.STYLE_NOVEL_ACTION_COMMAND, "Стиль", "Style");

        dictionary = Utils.makeNavigationButton("Dictionary", Constants.DICTIONARY_ACTION_COMMAND, "Синонимы", "Dictionary");
        info = Utils.makeNavigationButton("Info", Constants.INFO_ACTION_COMMAND, "Помощь", "Info");

        saveNovel = Utils.makeNavigationButton("Save", Constants.SAVE_NOVEL_ACTION_COMMAND, "Сохранить", "Save");

        loadNovel.addActionListener(new LoadNovelAction(this));
        unloadNovel.addActionListener(new UnloadAction(this));
        exportNovel.addActionListener(new ExportAction(this));

        heroes.addActionListener(new DialogAction(this));
        places.addActionListener(new DialogAction(this));
        things.addActionListener(new DialogAction(this));
        notes.addActionListener(new DialogAction(this));
        images.addActionListener(new DialogAction(this));
        style.addActionListener(new StyleAction(this));
        dictionary.addActionListener(new DictionaryAction(this));
        info.addActionListener(new InfoAction(this));

        saveNovel.addActionListener(new SaveAction(this));

        saveNovel.setEnabled(false);

        toolBar.add(loadNovel);
        toolBar.add(unloadNovel);
        toolBar.add(saveNovel);
        toolBar.add(exportNovel);
        toolBar.addSeparator(new Dimension(30, 10));
        toolBar.add(heroes);
        toolBar.add(places);
        toolBar.add(things);
        toolBar.add(notes);
        toolBar.add(images);
        toolBar.addSeparator(new Dimension(30, 10));
        toolBar.add(style);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(info);
        toolBar.add(dictionary);


        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));
        statusPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel();
        statusPanel.add(statusLabel, BorderLayout.WEST);

        partStatusLabel = new JLabel();
        statusPanel.add(partStatusLabel, BorderLayout.CENTER);
        partStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        allStatusLabel = new JLabel();
        statusPanel.add(allStatusLabel, BorderLayout.EAST);


        //Display the window.
        try {
            int width = Constants.PROPS.containsKey(Constants.PROP_WIDTH) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_WIDTH)) : 600;
            int height = Constants.PROPS.containsKey(Constants.PROP_HEIGHT) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_HEIGHT)) : 800;
            setPreferredSize(new Dimension(width, height));
        } catch (Exception ex) {
            setPreferredSize(new Dimension(600, 800));
        }

        try {
            int posX = Constants.PROPS.containsKey(Constants.PROP_POS_X) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_X)) : 0;
            int posY = Constants.PROPS.containsKey(Constants.PROP_POS_Y) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_Y)) : 0;
            setLocation(posX, posY);
        } catch (Exception ex) {
            setLocation(0, 0);
        }


        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyAction(this));
        updateState();

        load();


        // Create user dictionary in the current working directory of your application
        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());

        // Load the configuration from the file dictionaries.cnf and
        // use the current locale or the first language as default
        // You can download the dictionary files from http://sourceforge.net/projects/jortho/files/Dictionaries/
        SpellChecker.registerDictionaries(null, "ru");
        Synonymizer.init();

    }

    public void load(){
        if (Constants.PROPS.containsKey(Constants.PROP_LAST_OPEN_FILE)) {
            String lastOpenFile = Constants.PROPS.getProperty(Constants.PROP_LAST_OPEN_FILE);
            final File f = new File(lastOpenFile);
            if (f.exists()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        LoadNovelAction.loadNovel(JNovelistFrame.this, f);
                    }
                });
            }
        }
    }


    private String getDynTitle() {
//        return "JNovelist" + (openFileName == null ? "" : " - " + getNovelName());
        return openFileName == null ? "" : openFileName;
    }

    public String getNovelName() {
        return openFileName.substring(openFileName.lastIndexOf(File.separator) + 1, openFileName.length());
    }


    public void updateState() {
        if (openFileName == null) {
            if (dnDTabbedPane != null) {
                dnDTabbedPane.removeAll();
                getContentPane().remove(dnDTabbedPane);
                dnDTabbedPane = null;
                validate();
                repaint();
                SHTMLPanelImpl.pluginManager = null;
                System.gc();

                Window[] wws = getOwnedWindows();
                for (Window w : wws) {
                    if (w instanceof JDialog) {
                        JDialog jd = (JDialog) w;
                        jd.dispose();
                    }
                }

            }
            unloadNovel.setEnabled(false);
            heroes.setEnabled(false);
            places.setEnabled(false);
            things.setEnabled(false);
            notes.setEnabled(false);
            images.setEnabled(false);

            saveNovel.setEnabled(false);
            exportNovel.setEnabled(false);
        } else {
            exportNovel.setEnabled(true);
            unloadNovel.setEnabled(true);
            heroes.setEnabled(true);
            places.setEnabled(true);
            things.setEnabled(true);
            notes.setEnabled(true);
            images.setEnabled(true);
        }
        setTitle(getDynTitle());
        updateStatus();
    }


    public void updateStatus() {
        if (dnDTabbedPane == null) {
            partStatusLabel.setText("");
            statusLabel.setText("");
            allStatusLabel.setText("");
            return;
        }

        int chars = 0;
        int charsNoSpaces = 0;
        int words = 0;

        for (int i = 0; i < dnDTabbedPane.getTabCount(); i++) {
            Component c = dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        chars += editor.charsSpaces;
                        charsNoSpaces += editor.charsNoSpaces;
                        words += editor.words;
                    }
                }
            }
        }
        if (dnDTabbedPane.getSelectedComponent() != null && dnDTabbedPane.getSelectedComponent() instanceof DnDTabbedPane) {
            DnDTabbedPane selPart = (DnDTabbedPane) dnDTabbedPane.getSelectedComponent();

            if (selPart.getSelectedComponent() != null) {
                int partChars = 0;
                int partCharsNoSpaces = 0;
                int partWords = 0;
                for (int k = 0; k < selPart.getTabCount(); k++) {
                    Component o = selPart.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        partChars += editor.charsSpaces;
                        partCharsNoSpaces += editor.charsNoSpaces;
                        partWords += editor.words;
                    }
                }

                if (dnDTabbedPane.getTabCount() != 2) {//no need to show part stats if we have only one part
                    partStatusLabel.setText(" " + Constants.VARS.get(Constants.CHARS_COUNTER) + ": " + partChars + " / " + Constants.VARS.get(Constants.WORDS_COUNTER) + ": " + partWords);
                }

                if (selPart.getSelectedComponent() instanceof ChapterEditor) {
                    ChapterEditor chapterEditor = (ChapterEditor) selPart.getSelectedComponent();
                    statusLabel.setText(" " + Constants.VARS.get(Constants.CHARS_COUNTER) + ": " + chapterEditor.charsSpaces + " / " + Constants.VARS.get(Constants.WORDS_COUNTER) + ": " + chapterEditor.words);
                }
            }
        }
        allStatusLabel.setText(" " + Constants.VARS.get(Constants.CHARS_COUNTER) + ": " + chars + " / " + Constants.VARS.get(Constants.WORDS_COUNTER) + ": " + words);
    }

    public String getNovelText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dnDTabbedPane.getTabCount(); i++) {
            Component c = dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        try {
                            String newText = Utils.html2text(editor.getDocumentText());
                            sb.append(k + 1);
                            sb.append('\n');
                            sb.append(newText);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return sb.toString();
    }
}
