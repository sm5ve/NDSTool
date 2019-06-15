package NDSParser.Files.FileTypeProvider;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileTypeProvider.Impls.*;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/14/19.
 */
public class Types {
    private static final ArrayList<FileType> types = new ArrayList<>();

    static {
        addType(CARC.instance);
        addType(NARC.instance);
        addType(SMDL.instance);
        addType(BGP.instance);
    }

    public static FileType getType(AbstractFile f){
        for(FileType t : types){
            if(t.matches(f)){
                return t;
            }
        }
        return Unknown.instance;
    }

    public static void addType(FileType type){
        types.add(type);
    }
}
