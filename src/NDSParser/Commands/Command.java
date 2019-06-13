package NDSParser.Commands;

import NDSParser.Cart;
import NDSParser.Files.FolderObject;

/**
 * Created by Spencer on 6/11/19.
 */
public interface Command {
    public FolderObject eval(FolderObject cd, Cart c, String[] args);
}
