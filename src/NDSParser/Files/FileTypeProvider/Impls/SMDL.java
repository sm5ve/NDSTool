package NDSParser.Files.FileTypeProvider.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileTypeProvider.FileType;
import NDSParser.GUI.Icons.Icons;

import javax.swing.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class SMDL extends FileType {
    public static final SMDL instance = new SMDL();

    private SMDL(){

    }

    @Override
    public boolean matches(AbstractFile object) {
        return object.hasHeader("smdl");
    }

    @Override
    public String getName(AbstractFile o) {
        return "SMDL Music Sequence";
    }

    @Override
    public Icon getIcon(AbstractFile o){
        return Icons.MUSIC;
    }
}
