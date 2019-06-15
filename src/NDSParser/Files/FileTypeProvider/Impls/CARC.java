package NDSParser.Files.FileTypeProvider.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FileObject;
import NDSParser.Files.FileTypeProvider.FileType;
import NDSParser.GUI.Icons.Icons;
import NDSParser.Utils.LZ77;

import javax.swing.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class CARC extends FileType {
    public static final CARC instance = new CARC();

    private CARC(){

    }

    @Override
    public boolean matches(AbstractFile object) {
        return object.hasHeader("NARC", 5);
    }

    @Override
    public String getName(AbstractFile o) {
        return "CARC (Compressed Nitro Archive)";
    }

    @Override
    public Icon getIcon(AbstractFile o){
        return Icons.CARC;
    }
}
