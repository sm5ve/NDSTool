package NDSParser.GUI.ContextOptions;

import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.ContextOptions.Impls.*;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/14/19.
 */
public class Operations {
    private static final ArrayList<FileOperation> ops = new ArrayList<>();
    static {
        addOperation(new OpenCARC());
        addOperation(new OpenNARC());
        addOperation(new PlaySMDL());
        addOperation(new SaveFile());
        addOperation(new ViewBGP());
        addOperation(new ViewHex());
        addOperation(new ViewProperties());
        addOperation(new ExportSMDLWav());
    }

    public static void addOperation(FileOperation op){
        ops.add(op);
    }

    public static ArrayList<FileOperation> getOps(FilesystemObject o){
        ArrayList<FileOperation> out = new ArrayList<>();
        for(FileOperation op : ops){
            if(op.matches(o)){
                out.add(op);
            }
        }
        return out;
    }
}
