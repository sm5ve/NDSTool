package NDSParser.Files.FileTypeProvider.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileTypeProvider.FileType;
import NDSParser.GUI.Icons.Icons;

import javax.swing.*;

/**
 * Created by Spencer on 6/14/19.
 */
public class BGP extends FileType {
    public static final BGP instance = new BGP();

    private BGP(){

    }

    @Override
    public boolean matches(AbstractFile object) {
        return object.hasHeader("AT4PX");
    }

    @Override
    public String getName(AbstractFile o) {
        return "BGP Background Picture";
    }

    @Override
    public Icon getIcon(AbstractFile o){
        return Icons.BGP;
    }
}
