package net.sf.fc.gui.v.widget;

import javax.swing.JTextField;

import javax.swing.text.Document;

/**
 *
 * @author david
 */
public class FileAutoCompleteTextField extends JTextField {

    public FileAutoCompleteTextField() {
        super();
        new FileAutoCompleter(this);
        //this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
        //System.out.println(this.getLayout());
        //this.add(new javax.swing.JButton("OK"));
        //java.awt.Dimension dims = this.getPreferredSize();
        //dims.setSize(dims.getWidth(), dims.getHeight()*2);
        //this.setPreferredSize(dims);
    }

    public FileAutoCompleteTextField(String text) {
        super(text);
        new FileAutoCompleter(this);
    }

    public FileAutoCompleteTextField(int columns) {
        super(columns);
        new FileAutoCompleter(this);
    }

    public FileAutoCompleteTextField(String text, int columns) {
        super(text, columns);
        new FileAutoCompleter(this);
    }

    public FileAutoCompleteTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        new FileAutoCompleter(this);
    }

}