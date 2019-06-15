package NDSParser.Files;

import NDSParser.Utils.ByteUtils;

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

    public boolean hasHeader(byte[] bytes){
        FileHandle handle = this.getEntry();
        if(handle.end - handle.start < bytes.length){
            return false;
        }
        for(int i = 0; i < bytes.length; i++){
            if(this.fat.memory[i + handle.start] != bytes[i])
                return false;
        }
        return true;
    }

    public boolean hasHeader(String header){
        FileHandle handle = this.getEntry();
        if(handle.end - handle.start < header.length()){
            return false;
        }
        return ByteUtils.getASCII(fat.memory, handle.start, handle.start + header.length()).equals(header);
    }
}
