package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileTypeProvider.Impls.BGP;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.GUI.FileProps;
import NDSParser.GUI.HexViewer;

/**
 * Created by Spencer on 6/14/19.
 */
public class ViewProperties implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        return true;
    }

    @Override
    public void execute(FilesystemObject obj) {
        new FileProps(obj);
    }

    @Override
    public String getName() {
        return "Properties";
    }
}
