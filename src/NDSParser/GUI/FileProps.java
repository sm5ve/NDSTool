package NDSParser.GUI;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileTypeProvider.Types;
import NDSParser.Files.FilesystemObject;
import NDSParser.Utils.Tuple;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

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

        ArrayList<Tuple<String, String>> props = new ArrayList<>();

        try {
            props.add(new Tuple<>("Name:", obj.getName()));
            props.add(new Tuple<>("ID: ", String.format("0x%04x", obj.getId())));
            props.add(new Tuple<>("Parent ID: ", String.format("0x%04x", obj.getParentID())));
            props.add(new Tuple<>("Node Class: ", obj.getClass().getName()));
            if(obj instanceof AbstractFile){
                AbstractFile f = (AbstractFile) obj;
                props.add(new Tuple<>("File Type: ", Types.getType(f).getName(f)));
                props.add(new Tuple<>("File start: ", String.format("0x%08x", f.getEntry().start)));
                props.add(new Tuple<>("File End: ", String.format("0x%08x", f.getEntry().end)));
            }
        } catch (BadFileException e) {
            e.printStackTrace();
        }


        String[][] properties = new String[props.size()][2];

        for(int i = 0; i < props.size(); i++){
            properties[i][0] = props.get(i).a;
            properties[i][1] = props.get(i).b;
        }

        AbstractTableModel model = new DefaultTableModel(properties, new String[]{"Property", "Value"}) {

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };


        JTable tab = new JTable(properties, new String[]{"Property", "Value"});
        tab.setModel(model);
        this.add(tab);
    }
}
