package NDSParser.GUI.ContextOptions;

import NDSParser.Files.FilesystemObject;

/**
 * Created by Spencer on 6/14/19.
 */
public interface FileOperation {
    boolean matches(FilesystemObject obj);
    void execute(FilesystemObject obj);
    String getName();
}
