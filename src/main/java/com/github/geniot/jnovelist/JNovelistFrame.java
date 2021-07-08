package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.actions.*;
import com.github.geniot.jnovelist.model.JNovel;
import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.SpellChecker;
import io.github.geniot.shtml.SHTMLPanelImpl;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.geniot.jnovelist.Utils.decimalIndexToRoman;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class JNovelistFrame extends JFrame {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SaveAction saveAction;

    public JButton loadNovel;
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
    public JNovel openNovel;
    public ResourceBundle resourceBundle;

    public JNovelistFrame() {
        super("JNovelist");

        resourceBundle = ResourceBundle.getBundle("ResourceBundle");
        setIconImage(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/favicon/favicon-32x32.png")).getImage());

        try {
            Constants.PROPS.load(new FileInputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME));
            if (Constants.PROPS.containsKey(Constants.PROP_STYLE)) {
                Constants.HTML_DOC_START = Constants.PROPS.getProperty(Constants.PROP_STYLE);
            }
            if (Constants.PROPS.containsKey(Constants.PROP_SYNOPSIS_STYLE)) {
                Constants.HTML_SYN_DOC_START = Constants.PROPS.getProperty(Constants.PROP_SYNOPSIS_STYLE);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new OnExitAction(this));

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);
        int borderSize = 3;
        toolBar.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));

        loadNovel = Utils.makeNavigationButton("Load", Constants.LOAD_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.open"), "Load");
        unloadNovel = Utils.makeNavigationButton("Unload", Constants.UNLOAD_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.close"), "Unload");
        exportNovel = Utils.makeNavigationButton("Export", Constants.EXPORT_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.export"), "Export");
        saveNovel = Utils.makeNavigationButton("Save", Constants.SAVE_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.save"), "Save");

        heroes = Utils.makeNavigationButton("Heros", Constants.HEROES_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.characters"), "Heroes");
        places = Utils.makeNavigationButton("Places", Constants.PLACES_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.places"), "Places");
        things = Utils.makeNavigationButton("Things", Constants.THINGS_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.artifacts"), "Things");
        notes = Utils.makeNavigationButton("Notes", Constants.NOTES_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.notes"), "Notes");
        images = Utils.makeNavigationButton("Images", Constants.IMAGES_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.images"), "Images");

        style = Utils.makeNavigationButton("Style", Constants.STYLE_NOVEL_ACTION_COMMAND, resourceBundle.getString("command.preferences"), "Style");

        dictionary = Utils.makeNavigationButton("Dictionary", Constants.DICTIONARY_ACTION_COMMAND, resourceBundle.getString("command.dictionary"), "Dictionary");
        info = Utils.makeNavigationButton("Info", Constants.INFO_ACTION_COMMAND, resourceBundle.getString("command.help"), "Info");

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

        saveAction = new SaveAction(this);
        saveNovel.addActionListener(saveAction);

        saveNovel.setEnabled(false);

        toolBar.add(loadNovel);
        toolBar.add(saveNovel);
        toolBar.add(exportNovel);
        toolBar.add(style);
        toolBar.add(dictionary);
        toolBar.add(info);
        toolBar.add(unloadNovel);
        toolBar.add(Box.createHorizontalGlue());
//        toolBar.addSeparator(new Dimension(30, 10));
        toolBar.add(heroes);
        toolBar.add(places);
        toolBar.add(things);
        toolBar.add(notes);
        toolBar.add(images);
//        toolBar.addSeparator(new Dimension(30, 10));


        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));
        statusPanel.setLayout(new BorderLayout());

        int border = 3;
        EmptyBorder emptyBorder = new EmptyBorder(border, border, border, border);
        statusLabel = new JLabel();
        statusLabel.setBorder(emptyBorder);
        statusPanel.add(statusLabel, BorderLayout.EAST);

//        partStatusLabel = new JLabel();
//        statusPanel.add(partStatusLabel, BorderLayout.CENTER);
//        partStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        allStatusLabel = new JLabel();
        allStatusLabel.setBorder(emptyBorder);
        statusPanel.add(allStatusLabel, BorderLayout.WEST);


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

        scheduler.scheduleAtFixedRate(new CommitterTask(this), 5, 5, TimeUnit.SECONDS);

    }

    public void load() {
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
        return openFileName == null ? "JNovelist" : openFileName;
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
            saveNovel.setEnabled(true);
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
//            partStatusLabel.setText("");
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
                sb.append(decimalIndexToRoman(i));
                sb.append('\n');
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;

                        sb.append(k + 1);
                        sb.append('\n');

                        try {
                            String newText = Utils.html2text(editor.getDocumentText());
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
