package NDSParser.Commands;

import NDSParser.*;
import NDSParser.Files.BadFileException;
import NDSParser.Files.BadPathException;
import NDSParser.Files.FilesystemObject;
import NDSParser.Files.FolderObject;

/**
 * Created by Spencer on 6/11/19.
 */
public class CommandCD implements Command{

    @Override
    public FolderObject eval(FolderObject cd, Cart c, String[] args) {
        if(args.length > 0){
            try {
                FilesystemObject obj = cd.fromPath(args[0]);
                if(obj instanceof FolderObject){
                    return (FolderObject) obj;
                }
                else{
                    System.err.println("Error: cannot cd into file");
                }
            } catch (BadPathException e) {
                e.printStackTrace();
            } catch (BadFileException e) {
                e.printStackTrace();
            }
        }
        return cd;
    }
}
