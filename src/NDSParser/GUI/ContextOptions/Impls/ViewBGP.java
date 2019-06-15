package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileTypeProvider.Impls.BGP;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.BGPViewer;
import NDSParser.GUI.ContextOptions.FileOperation;

/**
 * Created by Spencer on 6/14/19.
 */
public class ViewBGP implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        if(obj instanceof AbstractFile){
            return BGP.instance.matches((AbstractFile) obj);
        }
        return false;
    }

    @Override
    public void execute(FilesystemObject obj) {
        new BGPViewer((AbstractFile) obj);
    }

    @Override
    public String getName() {
        return "View";
    }
}
