package NDSParser; /**
 * Created by Spencer on 6/11/19.
 */
import java.io.*;
import NDSParser.Files.*;
import NDSParser.GUI.GUIFileBrowser;

public class Main {
    public static void main(String[] args) throws IOException, BadFileException, BadPathException {
        //File f = new File("./BlueRescueTeam.nds");
        //File f = new File("./SkyTeam.nds");
        //File f = new File("./diamond.nds");
        //File f = new File("./kart.nds");
        File f = new File("./hg.nds");
        Cart c = new Cart(f);
        System.out.println(c);

        FNT fnt = new FNT(c);
        FAT fat = new FAT(c);
        FolderObject root = FilesystemObject.getRoot(fnt, fat);

        //FileObject bgp = (FileObject) root.fromPath("/w_esrb.bgp");
        //new HexViewer(bgp.copyFile());
        //new BGPViewer(bgp);

        new GUIFileBrowser(root);

    }
}
