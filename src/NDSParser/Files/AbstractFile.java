package NDSParser.Files;

/**
 * Created by Spencer on 6/11/19.
 */
public abstract class AbstractFile extends FilesystemObject{
    public AbstractFile(FNT fnt, FAT fat, int id) throws BadFileException {
        super(fnt, fat, id);
    }

    public FileHandle getEntry(){
        return this.fat.getFileEntry(this.id);
    }

    public byte[] copyFile(){
        return this.fat.copyFile(this.id);
    }
}
