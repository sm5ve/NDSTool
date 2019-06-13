package NDSParser.Commands;

import NDSParser.*;
import NDSParser.Files.BadFileException;
import NDSParser.Files.FilesystemObject;
import NDSParser.Files.FolderObject;

/**
 * Created by Spencer on 6/11/19.
 */
public class CommandLS implements Command{

    @Override
    public FolderObject eval(FolderObject cd, Cart c, String[] args) {
        try {
            for(FilesystemObject f : cd.ls()){
                if(f instanceof FolderObject){
                    System.out.println(f.getName() + "/");
                }
                else{
                    System.out.println(f.getName());
                }
            }
        } catch (BadFileException e) {
            e.printStackTrace();
        }
        return cd;
    }
}
