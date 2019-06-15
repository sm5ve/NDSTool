package NDSParser.GUI;

import NDSParser.Files.*;
import NDSParser.Cart;
import NDSParser.Files.NARC.BadNARCException;
import NDSParser.Files.NARC.CARC;
import NDSParser.Files.NARC.NARC;
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
    private final Cart c;
    public GUIContextForFile(FilesystemObject obj, Cart c){
        this.obj = obj;
        this.c = c;
        JMenuItem save = new JMenuItem("Save");
        this.add(save);
        save.addActionListener(e -> {
            try {
                FileSaver.save(new File("."), obj, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (BadFileException e1) {
                e1.printStackTrace();
            }
        });
        JMenuItem props = new JMenuItem("Properties");
        this.add(props);
        props.addActionListener(e -> {
            new FileProps(obj);
        });
        if(obj instanceof AbstractFile){
            try {
                JMenuItem hex = new JMenuItem("View Hex");
                this.add(hex);
                hex.addActionListener(e -> {
                    try {
                        new HexViewer(((AbstractFile) obj).copyFile());
                    } catch (BadFileException e1) {
                        e1.printStackTrace();
                    }
                });
                if(obj.getName().endsWith(".smd")){
                    JMenuItem player = new JMenuItem("Play");
                    this.add(player);
                    player.addActionListener(e -> {
                        try {
                            SMDL song = new SMDL(c, ((AbstractFile)obj).getEntry());
                            SMDLPlayer.play(song.getTracks());
                        } catch (BadSMDLException e1) {
                            e1.printStackTrace();
                        } catch (MidiUnavailableException e1) {
                            e1.printStackTrace();
                        } catch (InvalidMidiDataException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
                if(obj.getName().endsWith(".bgp")){
                    JMenuItem player = new JMenuItem("Show");
                    this.add(player);
                    player.addActionListener(e -> {
                        new BGPViewer((AbstractFile) obj);
                    });
                }
                if(obj.getName().endsWith(".narc")){
                    JMenuItem player = new JMenuItem("Open");
                    this.add(player);
                    player.addActionListener(e -> {
                        try {
                            NARC narc = new NARC(c, ((AbstractFile) obj).getEntry());
                            AbstractFolder root;
                            if(narc.getBfnt().isEmpty()){
                                root = new RawFolderObject(narc.getBfat());
                            }
                            else{
                                root = FilesystemObject.getRoot(narc.getBfnt(), narc.getBfat());
                            }
                            new GUIFileBrowser(root, c);
                        } catch (BadNARCException e1) {
                            e1.printStackTrace();
                        } catch (BadFileException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
                if(obj.getName().endsWith(".carc")){
                    JMenuItem player = new JMenuItem("Open");
                    this.add(player);
                    player.addActionListener(e -> {
                        try {
                            NARC narc = CARC.decode((FileObject) obj);;
                            AbstractFolder root;
                            if(narc.getBfnt().isEmpty()){
                                root = new RawFolderObject(narc.getBfat());
                            }
                            else{
                                root = FilesystemObject.getRoot(narc.getBfnt(), narc.getBfat());
                            }
                            new GUIFileBrowser(root, c);
                        } catch (BadNARCException e1) {
                            e1.printStackTrace();
                        } catch (BadFileException e1) {
                            e1.printStackTrace();
                        }

                    });
                }
            } catch (BadFileException e) {
                e.printStackTrace();
            }
        }
    }
}
