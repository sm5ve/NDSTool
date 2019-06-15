package NDSParser.GUI;

import NDSParser.Files.BadFileException;
import NDSParser.Files.FilesystemObject;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class FileProps extends JFrame {
    public FileProps(FilesystemObject obj){
        this.setSize(new Dimension(200, 350));
        this.setLocationRelativeTo(null);
        try {
            this.setTitle(obj.getName() + " properties");
        } catch (BadFileException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
        //this.setResizable(false);

        String[][] properties = new String[0][];
        try {
            properties = new String[][]{
                new String[]{"Name:", obj.getName()},
                new String[]{"ID: ", String.format("0x%04x", obj.getId())},
                new String[]{"Parent ID: ", String.format("0x%04x", obj.getParentID())},
                new String[]{"Type: ", obj.getClass().getName()},
            };
        } catch (BadFileException e) {
            e.printStackTrace();
        }

        AbstractTableModel model = new DefaultTableModel(properties, new String[]{"Property", "Value"}) {

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };


        JTable props = new JTable(properties, new String[]{"Property", "Value"});
        props.setModel(model);
        this.add(props);
    }
}
