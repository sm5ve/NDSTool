package NDSParser; /**
 * Created by Spencer on 6/11/19.
 */
import java.io.*;
import NDSParser.Commands.*;
import NDSParser.Files.*;
import NDSParser.Files.NARC.BadNARCException;
import NDSParser.Files.NARC.NARC;
import NDSParser.GUI.GUIMain;
import NDSParser.GUI.HexViewer;

public class Main {
    public static void main(String[] args) throws IOException, BadFileException, BadPathException {
        //File f = new File("./BlueRescueTeam.nds");
        //File f = new File("./SkyTeam.nds");
        File f = new File("./diamond.nds");
        //File f = new File("./kart.nds");
        Cart c = new Cart(f);
        System.out.println(c);

        FNT fnt = new FNT(c);
        FAT fat = new FAT(c);
        FolderObject root = FilesystemObject.getRoot(fnt, fat);
        //tree(root);
        //CartIconViewer civ = new CartIconViewer(c);

        //FileObject sound = (FileObject) root.fromPath("/SOUND/bgm0038.smd");
        //FileObject sound = (FileObject) root.fromPath("/SOUND/bgm0002.smd");
        //FileObject sound = (FileObject) root.fromPath("/SOUND/bgm0065.smd");
        //FileObject sound = (FileObject) root.fromPath("/SOUND/bgm0100.smd");
        //FileObject sound = (FileObject) root.fromPath("/SOUND/me0003.smd");
        /*try {
            SMDL music = new SMDL(c, sound.getEntry());
            try {
                SMDLPlayer.play(music.getTracks());
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        } catch (BadSMDLException e) {
            e.printStackTrace();
        }*/

        //FileObject bgp = (FileObject) root.fromPath("/w_esrb.bgp");
        //new HexViewer(bgp.copyFile());
        //new BGPViewer(bgp);

        new GUIMain(root, c);

        CommandLine commandLine = new CommandLine(root, c);
        while(true) {
            commandLine.eval();
        }
    }

    private static void tree(FolderObject folder) throws BadFileException {
        for(FilesystemObject o : folder.ls()){
            System.out.println(o);
            if(o instanceof FolderObject){
                tree((FolderObject) o);
            }
        }
    }
}
