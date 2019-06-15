package NDSParser.Files.FileTypeProvider.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileObject;
import NDSParser.Files.FileTypeProvider.FileType;
import NDSParser.GUI.Icons.Icons;
import NDSParser.Utils.ByteUtils;

import javax.swing.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class NARC extends FileType {
    public static final NARC instance = new NARC();

    private NARC(){

    }

    @Override
    public boolean matches(AbstractFile object) {
        return object.hasHeader("NARC");
    }

    @Override
    public String getName(AbstractFile o) {
        return "NARC (Nitro Archive)";
    }

    @Override
    public Icon getIcon(AbstractFile o){
        return Icons.NARC;
    }
}
