package NDSParser.GUI;

import NDSParser.Cart;
import NDSParser.Files.AbstractFolder;
import NDSParser.Files.BadFileException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;

/**
 * Created by Spencer on 6/12/19.
 */
public class HexViewer extends JFrame {
    public HexViewer(byte[] b) throws BadFileException {
        //this.setLocationRelativeTo(null);
        this.setTitle("Hex Viewer");
        this.setVisible(true);

        String[][] data = new String[b.length / 16 + 1][33];

        for(int i = 0; i < b.length; i++){
            data[i / 16][i % 16 + 1] = String.format("0x%02X", b[i]);
            data[i / 16][i % 16 + 17] = new String(new byte[]{b[i]}, StandardCharsets.US_ASCII);
            data[i / 16][0] = "0x" + String.format("0x%08X", i & (~0xf));
        }

        String[] labels = new String[33];

        labels[0] = "";

        for(int i = 0; i < 16; i++){
            labels[i + 1] = String.format("0x%02X", i);
            labels[i + 17] = String.format("0x%02X", i);
        }

        JTable hex = new JTable(data, labels);

        hex.getColumnModel().getColumn(0).setMinWidth(100);

        for(int i = 1; i < 33; i++){
            hex.getColumnModel().getColumn(i).setMinWidth(35);
        }


        this.setSize(new Dimension(1252, 400));

        JScrollPane pane = new JScrollPane(hex);

        //this.setLayout(new GridBagLayout());

        this.add(pane);
    }
}
