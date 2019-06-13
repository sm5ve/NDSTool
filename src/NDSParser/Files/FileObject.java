package NDSParser.Files;

/**
 * Created by Spencer on 6/11/19.
 */
public class FileObject extends AbstractFile{
    public FileObject(FNT fnt, FAT fat, int id) throws BadFileException {
        super(fnt, fat, id);
        if(fnt.getFileFromID(id).getType() != FNT.FNTEntryType.FILE){
            throw new BadFileException();
        }
    }
}
