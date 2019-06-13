package NDSParser.Files;

/**
 * Created by Spencer on 6/11/19.
 */
public class RawFileObject extends AbstractFile{
    public static final String PREFIX = "RawFile_";
    public RawFileObject(FAT fat, int id) throws BadFileException {
        super(null, fat, id);
    }

    @Override
    public String getName() throws BadFileException {
        return PREFIX + this.id;
    }

    @Override
    public String toString(){
        return PREFIX + this.id;
    }

    @Override
    public AbstractFolder getParent() throws BadFileException {
        return new RawFolderObject(this.fat);
    }

    @Override
    public String getPath() throws BadFileException {
        return "/" + this.getName();
    }
}
