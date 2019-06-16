package NDSParser.GUI.ContextOptions.Impls;

import NDSParser.Files.AbstractFile;
import NDSParser.Files.FilesystemObject;
import NDSParser.GUI.ContextOptions.FileOperation;
import NDSParser.Sounds.SMDL.BadSMDLException;
import NDSParser.Sounds.SMDL.Player.SMDLExporter;
import NDSParser.Sounds.SMDL.SMDL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.*;

/**
 * Created by Spencer on 6/15/19.
 */
public class ExportSMDLWav implements FileOperation{
    @Override
    public boolean matches(FilesystemObject obj) {
        if(!(obj instanceof AbstractFile))
            return false;
        return NDSParser.Files.FileTypeProvider.Impls.SMDL.instance.matches((AbstractFile) obj);
    }

    @Override
    public void execute(FilesystemObject obj) {
        AbstractFile f = (AbstractFile) obj;

        try {
            SMDL smdl = new SMDL(f.copyFile());
            File out = new File("./" + smdl.name.replaceAll("\\P{Print}", "") + ".wav");
            if(!out.exists()){
                out.createNewFile();
            }
            SMDLExporter.exportSMDLWav(smdl, out);
        } catch (BadSMDLException e) {
            e.printStackTrace();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Export to WAV";
    }
}
