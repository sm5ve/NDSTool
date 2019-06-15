package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileSaver;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.GUI.HexViewer;
import java.io.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class SaveFile implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        return true;
    }

    @Override
    public void execute(FilesystemObject obj) {
        try {
            FileSaver.save(new File("."), obj, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Save";
    }
}
