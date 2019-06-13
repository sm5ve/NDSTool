package NDSParser.Commands;

import NDSParser.*;
import NDSParser.Files.FolderObject;
import NDSParser.Sounds.Player.SMDLPlayer;

/**
 * Created by Spencer on 6/11/19.
 */
public class CommandStop implements Command{

    @Override
    public FolderObject eval(FolderObject cd, Cart c, String[] args) {
        SMDLPlayer.stop();
        return cd;
    }
}
