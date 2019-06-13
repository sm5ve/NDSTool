package NDSParser.Commands;

import NDSParser.*;
import NDSParser.Files.*;

import java.util.Arrays;

/**
 * Created by Spencer on 6/11/19.
 */
public class CommandPrint implements Command{
    @Override
    public FolderObject eval(FolderObject cd, Cart c, String[] args) {
        try {
            FilesystemObject file = cd.fromPath(args[0]);
            if(file instanceof FileObject){
                FileObject f = (FileObject) file;
                byte[] contents = f.copyFile();

                System.out.println(Arrays.toString(contents));
            }
            else {
                System.err.println("Error: cannot print out contents of folder");
            }
        } catch (BadPathException e) {
            e.printStackTrace();
        } catch (BadFileException e) {
            e.printStackTrace();
        }
        return cd;
    }
}
