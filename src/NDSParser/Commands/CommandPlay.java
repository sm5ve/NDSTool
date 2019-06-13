package NDSParser.Commands;

import NDSParser.*;
import NDSParser.Files.*;
import NDSParser.Sounds.BadSMDLException;
import NDSParser.Sounds.Player.SMDLPlayer;
import NDSParser.Sounds.SMDL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Created by Spencer on 6/11/19.
 */
public class CommandPlay implements Command{

    @Override
    public FolderObject eval(FolderObject cd, Cart c, String[] args) {
        if(args.length > 0){
            try {
                FilesystemObject obj = cd.fromPath(args[0]);
                SMDLPlayer.play(new SMDL(c, ((FileObject)obj).getEntry()).getTracks());
            } catch (BadPathException e) {
                e.printStackTrace();
            } catch (BadFileException e) {
                e.printStackTrace();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            } catch (BadSMDLException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
        return cd;
    }
}
