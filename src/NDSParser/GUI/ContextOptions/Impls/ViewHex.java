package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileTypeProvider.Impls.BGP;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.BGPViewer;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.GUI.HexViewer;

/**
 * Created by Spencer on 6/14/19.
 */
public class ViewHex implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        if(obj instanceof AbstractFile){
            return true;
        }
        return false;
    }

    @Override
    public void execute(FilesystemObject obj) {
        try {
            new HexViewer(((AbstractFile)obj).copyFile());
        } catch (BadFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "View Hex";
    }
}
