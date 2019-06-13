package NDSParser.Files;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public class RawFolderObject extends AbstractFolder{
    public RawFolderObject(FAT fat) throws BadFileException {
        super(null, fat, 0xf000);
    }

    public ArrayList<FilesystemObject> ls() throws BadFileException {
        ArrayList<FilesystemObject> out = new ArrayList<>();
        for(int i = 0; i < this.fat.getNumberIDs(); i++){
            out.add(new RawFileObject(this.fat, i));
        }
        return out;
    }

    public ArrayList<AbstractFolder> getSubdirs() throws BadFileException {
        ArrayList<AbstractFolder> out = new ArrayList<>();
        return out;
    }

    public boolean isRoot(){
        return true;
    }

    public FilesystemObject getFileByName(String name) throws BadPathException, BadFileException {
        for(FilesystemObject o : this.ls()){
            if (o.getName().equals(name)){
                return o;
            }
        }
        throw new BadPathException();
    }

    @Override
    public String getName() throws BadFileException {
        return "/";
    }

    @Override
    public String toString(){
        return "/";
    }

    @Override
    public AbstractFolder getParent() throws BadFileException {
        return this;
    }

    @Override
    public String getPath() throws BadFileException {
        return "/";
    }
}
