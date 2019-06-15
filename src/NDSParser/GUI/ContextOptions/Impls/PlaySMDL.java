package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileTypeProvider.Impls.BGP;
import NDSParser.Files.FileTypeProvider.Impls.SMDL;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.BGPViewer;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.Sounds.SMDL.BadSMDLException;
import NDSParser.Sounds.SMDL.Player.SMDLPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Created by Spencer on 6/14/19.
 */
public class PlaySMDL implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        if(obj instanceof AbstractFile){
            return SMDL.instance.matches((AbstractFile) obj);
        }
        return false;
    }

    @Override
    public void execute(FilesystemObject obj){
        try {
            NDSParser.Sounds.SMDL.SMDL song = new NDSParser.Sounds.SMDL.SMDL(((AbstractFile)obj).copyFile());
            SMDLPlayer.play(song.getTracks());
        } catch (BadSMDLException e1) {
            e1.printStackTrace();
        } catch (MidiUnavailableException e1) {
            e1.printStackTrace();
        } catch (InvalidMidiDataException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Play";
    }
}
