import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class TextEditor extends JFrame
{
    private JTextArea textArea = new JTextArea(20,80);
    private JFileChooser fileChooser = new JFileChooser();

    public TextEditor()
    {
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        FileFilter txtFilter = new FileNameExtensionFilter("Plain Text", "txt");
        fileChooser.setFileFilter(txtFilter);

        add(scrollPane);
        JMenuBar mainMenu = new JMenuBar();
        setJMenuBar(mainMenu);
        JMenu fileMenu = new JMenu("File");
        mainMenu.add(fileMenu);
        fileMenu.add(Open);
        fileMenu.add(Save);
        fileMenu.add(Exit);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        System.out.println("constructor");
    }

    Action Open = new AbstractAction("Open File")
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            {
                openFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    };

    Action Save = new AbstractAction("Save File")
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            saveFile();
        }
    };

    Action Exit = new AbstractAction("Exit")
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Runtime.getRuntime().exit(0);
        }
    };


    public void openFile(String fileName)
    {
        FileReader fr= null;
        try
        {
            fr = new FileReader(fileName);
            textArea.read(fr, null);
            fr.close();
            setTitle(fileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void saveFile()
    {
        if (fileChooser.showSaveDialog(null)== JFileChooser.APPROVE_OPTION)
        {
            FileWriter fw = null;
            try
            {
                fw = new FileWriter(fileChooser.getSelectedFile().getAbsoluteFile()+".txt");
                textArea.write(fw);
                fw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
