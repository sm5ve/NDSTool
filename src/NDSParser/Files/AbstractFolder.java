package NDSParser.Files;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public abstract class AbstractFolder extends FilesystemObject{

    public AbstractFolder(FNT fnt, FAT fat, int id) {
        super(fnt, fat, id);
    }

    public abstract ArrayList<FilesystemObject> ls() throws BadFileException;

    public abstract ArrayList<AbstractFolder> getSubdirs() throws BadFileException;

    public abstract boolean isRoot();

    public abstract FilesystemObject getFileByName(String name) throws BadPathException, BadFileException;

    public FilesystemObject fromPath(String path) throws BadPathException, BadFileException {
        if(path.equals("")){
            return this;
        }
        if(path.startsWith("/")){
            if(this.isRoot()){
                return this.fromPath(path.substring(1));
            }
            else{
                return this.getRoot(this.fnt, this.fat).fromPath(path);
            }
        }
        else if(path.startsWith(".")){
            if(path.startsWith("../")){
                return this.getParent().fromPath(path.substring(3));
            }
            else if(path.startsWith("./")){
                return this.fromPath(path.substring(2));
            }
            else if(path.equals(".")){
                return this;
            }
            else if(path.equals("..")){
                return this.getParent();
            }
            else{
                throw new BadPathException();
            }
        }
        else{
            if(path.contains("/")){
                String pre = path.substring(0, path.indexOf('/'));
                String post = path.substring(path.indexOf('/') + 1);

                FilesystemObject immediate = this.getFileByName(pre);
                if(!(immediate instanceof RawFolderObject)){
                    throw new BadPathException();
                }
                return ((RawFolderObject) immediate).fromPath(post);
            }
            else{
                return this.getFileByName(path);
            }
        }
    }
}
