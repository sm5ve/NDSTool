package NDSParser.Files.FileTypeProvider.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileTypeProvider.FileType;
import NDSParser.GUI.Icons.Icons;

import javax.swing.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class Unknown extends FileType {
    public static final Unknown instance = new Unknown();

    private Unknown(){

    }

    @Override
    public boolean matches(AbstractFile object) {
        return true;
    }

    @Override
    public String getName(AbstractFile o) {
        return "Unknown File Type";
    }
}