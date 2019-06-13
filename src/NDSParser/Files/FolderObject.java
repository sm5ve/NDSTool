package NDSParser.Files;

import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public class FolderObject extends AbstractFolder{
    public FolderObject(FNT fnt, FAT fat, int id) throws BadFileException {
        super(fnt, fat, id);
        if(fnt.getFileFromID(id).getType() != FNT.FNTEntryType.SUBDIRECTORY){
            throw new BadFileException();
        }
    }

    public ArrayList<FilesystemObject> ls() throws BadFileException {
        ArrayList<FilesystemObject> out = new ArrayList<>();
        for(int i : this.fnt.getFileIDsInDirectory(this.id)){
            if(i != 0xf000)
                out.add(FilesystemObject.fromID(this.fnt, this.fat, i));
        }
        return out;
    }

    public ArrayList<AbstractFolder> getSubdirs() throws BadFileException {
        ArrayList<AbstractFolder> out = new ArrayList<>();
        for(int i : this.fnt.getFileIDsInDirectory(this.id)){
            if(this.fnt.getFileFromID(i).getType() == FNT.FNTEntryType.SUBDIRECTORY & i != 0xf000)
                try {
                    out.add(new FolderObject(this.fnt, this.fat, i));
                } catch (BadFileException e) {
                    e.printStackTrace();
                }
        }
        return out;
    }

    public boolean isRoot(){
        return this.id == 0xf000;
    }

    public FilesystemObject getFileByName(String name) throws BadPathException, BadFileException {
        for(FilesystemObject o : this.ls()){
            if (o.getName().equals(name)){
                return o;
            }
        }
        throw new BadPathException();
    }
}
