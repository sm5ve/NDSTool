package NDSParser.GUI;

import NDSParser.Files.*;
import NDSParser.Cart;
import NDSParser.Files.NARC.BadNARCException;
import NDSParser.Files.NARC.CARC;
import NDSParser.Files.NARC.NARC;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.GUI.ContextOptions.Operations;
import NDSParser.Sounds.SMDL.BadSMDLException;
import NDSParser.Sounds.SMDL.Player.SMDLPlayer;
import NDSParser.Sounds.SMDL.SMDL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.io.*;

/**
 * Created by Spencer on 6/12/19.
 */
public class GUIContextForFile extends JPopupMenu {
    private final FilesystemObject obj;

    public GUIContextForFile(FilesystemObject obj){
        this.obj = obj;

        for(FileOperation op : Operations.getOps(obj)){
            JMenuItem item = new JMenuItem(op.getName());
            final FileOperation opr = op;
            item.addActionListener(e -> opr.execute(obj));
            this.add(item);
        }
    }
}
