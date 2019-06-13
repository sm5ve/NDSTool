package NDSParser.Files;

/**
 * Created by Spencer on 6/11/19.
 */
public class FileHandle{
    public final int start, end;
    public FileHandle(int s, int e){
        this.start = s;
        this.end = e;
    }

    @Override
    public String toString(){
        return "FileEntry{start: " + Integer.toHexString(start) + ", end: " + Integer.toHexString(end) + "}";
    }
}