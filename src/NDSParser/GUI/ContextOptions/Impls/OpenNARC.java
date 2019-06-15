package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.*;
import NDSParser.Files.FileTypeProvider.Impls.BGP;
import NDSParser.Files.FileTypeProvider.Impls.NARC;
import NDSParser.Files.NARC.BadNARCException;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.GUI.FileProps;
import NDSParser.GUI.GUIFileBrowser;

/**
 * Created by Spencer on 6/14/19.
 */
public class OpenNARC implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        if(obj instanceof AbstractFile){
            return NARC.instance.matches((AbstractFile) obj);
        }
        return false;
    }

    @Override
    public void execute(FilesystemObject obj) {
        try {
            NDSParser.Files.NARC.NARC narc = new NDSParser.Files.NARC.NARC(((AbstractFile) obj).copyFile());
            AbstractFolder root;
            if(narc.getBfnt().isEmpty()){
                root = new RawFolderObject(narc.getBfat());
            }
            else{
                root = FilesystemObject.getRoot(narc.getBfnt(), narc.getBfat());
            }
            new GUIFileBrowser(root);
        } catch (BadNARCException e1) {
            e1.printStackTrace();
        } catch (BadFileException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Open";
    }
}
