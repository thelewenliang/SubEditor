/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subeditor;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 *
 * @author Staff
 */
public class SubEditor {

    JFrame frame;
    String wholeText = "";
    File file;
    ArrayList<Time> timeList = new ArrayList<>();
    Pattern timePattern = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)");
    boolean isPickedSaveFile = false;
    JTextArea mainTextArea;

    public SubEditor() throws Exception {
        setupGUI();
    }

    public void setupGUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        String lcOSName = System.getProperty("os.name").toLowerCase();
        System.setProperty("apple.awt.fileDialogForDirectories", "true");
        boolean IS_MAC = lcOSName.startsWith("mac os x");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.fileDialogForDirectories", "false");

        frame = new JFrame("SubEditor");
        mainTextArea = new JTextArea();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        loadMenuItem.addActionListener(a -> {
            file = getLoadFile();
            if (file == null) {
                return;
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while (br.ready()) {
                    wholeText += br.readLine() + "\n";
                }
                Matcher timeMatcher = timePattern.matcher(wholeText);
                while (timeMatcher.find()) {
                    timeList.add(new Time(Integer.parseInt(timeMatcher.group(1)), Integer.parseInt(timeMatcher.group(2)), Integer.parseInt(timeMatcher.group(3)), Integer.parseInt(timeMatcher.group(4)), timeMatcher.start()));
                }
                mainTextArea.setText(wholeText);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            isPickedSaveFile = false;
        });
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(a -> {
            if(isPickedSaveFile == false) {
                saveAs();
            }
            else {
                try {
                    save();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK));
        saveAsMenuItem.addActionListener(a -> {
            saveAs();
        });

        JMenu editMenu = new JMenu("Edit");
        JMenuItem refactorMenuItem = new JMenuItem("Refactor");
        refactorMenuItem.addActionListener(a -> {
            int position = mainTextArea.getCaretPosition();
            int offset = 0;
            int index = 0;

            String newWholeText = mainTextArea.getText();
            for (Time x : timeList) {
                String newText = newWholeText.substring(x.index, x.end).trim();
                if (!newText.equals(x.srtFormat)) {
                    Matcher timeMatcher = timePattern.matcher(newText);
                    timeMatcher.find();
                    Time newTime = new Time(Integer.parseInt(timeMatcher.group(1)), Integer.parseInt(timeMatcher.group(2)), Integer.parseInt(timeMatcher.group(3)), Integer.parseInt(timeMatcher.group(4)), timeMatcher.start());
                    offset = newTime.getMilli() - x.getMilli();
                    StringBuilder builder = new StringBuilder(newWholeText);
                    for (int y = index; y < timeList.size(); y++) {
                        timeList.get(y).addMilli(offset);
                        builder.replace(timeList.get(y).index, timeList.get(y).end, timeList.get(y).toSRTFormat());
                    }
                    mainTextArea.setText(builder.toString());
                    break;
                }
                index++;
            }
            mainTextArea.setCaretPosition(position);

        });
        refactorMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        editMenu.add(refactorMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        frame.setJMenuBar(menuBar);

        JScrollPane mainPanel = new JScrollPane(mainTextArea);
        mainPanel.setPreferredSize(new Dimension(1000, 800));
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void saveAs() {
        File saveFile = getSaveFile();
        if (saveFile == null) {
            return;
        }
        file = saveFile; //sensitive
        isPickedSaveFile = true;
        try {
            save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getSaveFile() {
        FileDialog chooser = new FileDialog(frame, "Save As", FileDialog.SAVE);
        chooser.setVisible(true);
        String selected = chooser.getDirectory() + chooser.getFile();
        if (selected == null) {
            return null;
        }
        if (!selected.endsWith(".srt")) {
            selected += ".srt";
        }

        return new File(selected);
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(mainTextArea.getText());
    }

    public File getLoadFile() {
        FileDialog chooser = new FileDialog(frame, "Load Subtitle File", FileDialog.LOAD);
        SRTFileFilter filter = new SRTFileFilter();
        chooser.setFilenameFilter(filter);
        chooser.setVisible(true);
        String selected = chooser.getDirectory() + chooser.getFile();
        if (selected == null) {
            return null;
        }
        return new File(selected);
    }

    public static void main(String[] args) throws Exception {
        new SubEditor();
    }

    public class SRTFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".srt");
        }

    }

}
