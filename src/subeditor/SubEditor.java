/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subeditor;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
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
        JTextArea mainTextArea = new JTextArea();
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(a -> {
            file = getLoadFile();
            if(file == null) {
                return;
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while(br.ready()) {
                    wholeText += br.readLine() + "\n";
                }
                mainTextArea.setText(wholeText);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenu editMenu = new JMenu("Edit");
        JMenuItem refactorMenuItem = new JMenuItem("Refactor");
        refactorMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
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
