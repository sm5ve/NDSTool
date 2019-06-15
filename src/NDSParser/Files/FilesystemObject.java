package NDSParser.Files;

/**
 * Created by Spencer on 6/11/19.
 */
public abstract class FilesystemObject {
    protected final FNT fnt;
    protected final FAT fat;
    protected final int id;
    public FilesystemObject(FNT fnt, FAT fat, int id){
        this.fnt = fnt;
        this.fat = fat;
        this.id = id;
    }

    public static FilesystemObject fromID(FNT fnt, FAT fat, int id) throws BadFileException {
        if(fnt == null){
            return new RawFileObject(fat, id);
        }
        switch (fnt.getFileFromID(id).getType()){
            case FILE: return new FileObject(fnt, fat, id);
            case SUBDIRECTORY: return new FolderObject(fnt, fat, id);
        }
        return null;
    }

    public String getName() throws BadFileException {
        return fnt.getFileFromID(this.id).getName();
    }

    public int getId(){
        return this.id;
    }

    public int getParentID(){
        if(this.fnt == null){
            return -1;
        }
        if(!this.fnt.hasEntry(this.id)){
            return -1;
        }
        try {
            return this.fnt.getFileFromID(this.id).getParent();
        } catch (BadFileException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public String toString(){
        try {
            return this.getPath();
        } catch (BadFileException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AbstractFolder getParent() throws BadFileException {
        if(this.id == 0xf000){
            return (FolderObject) this;
        }
        FilesystemObject out = FilesystemObject.fromID(fnt, fat, fnt.getFileFromID(id).getParent());
        if(!(out instanceof FolderObject)){
            throw new BadFileException();
        }
        return (FolderObject) out;
    }

    public String getPath() throws BadFileException {
        if(this.id == 0xf000){
            return "/";
        }
        String suffix = "";
        if(this.fnt.getFileFromID(this.id).getType() == FNT.FNTEntryType.SUBDIRECTORY){
            suffix = "/";
        }

        return this.getParent().getPath() + this.getName() + suffix;
    }

    public static FolderObject getRoot(FNT fnt, FAT fat) throws BadFileException {
        return new FolderObject(fnt, fat, 0xf000);
    }
}
