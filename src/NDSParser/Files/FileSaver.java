package NDSParser.Files;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by Spencer on 6/14/19.
 */
public class FileSaver {
    public static void save(File saveTo, FilesystemObject obj, boolean safe) throws IOException, BadFileException {
        if(!saveTo.isDirectory()){
            throw new IOException("Can only save to directory");
        }
        String name = obj.getName();
        File f;
        if(!isFilenameValid(name)){
            int i = 0;
            while((f = new File(saveTo.getAbsolutePath() + "/invalid_name_" + i)).exists())
                i++;
        }
        else if(name.isEmpty()){
            f = new File(saveTo.getAbsolutePath() + "/root");
        }
        else{
            f = new File(saveTo.getAbsolutePath() + "/" + obj.getName());
        }
        if(obj instanceof AbstractFolder){
            if(f.exists()){
                return;
            }
            f.mkdir();
            for(FilesystemObject o : ((AbstractFolder)obj).ls()){
                save(f, o, safe);
            }
        }
        else if(obj instanceof AbstractFile){
            if(f.exists()){
                return;
            }
            f.createNewFile();
            try (FileOutputStream stream = new FileOutputStream(f)) {
                stream.write(((AbstractFile) obj).copyFile());
            }
        }
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            f.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
