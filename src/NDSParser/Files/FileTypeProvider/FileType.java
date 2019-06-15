package NDSParser.Files.FileTypeProvider;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileObject;
import NDSParser.GUI.Icons.Icons;
import NDSParser.Utils.Tuple;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Spencer on 6/14/19.
 */
public abstract class FileType {
    public abstract boolean matches(AbstractFile object);
    public abstract String getName(AbstractFile o);

    public Icon getIcon(AbstractFile o){
        return Icons.DOC;
    }
}
