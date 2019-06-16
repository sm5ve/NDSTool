package NDSParser.Sounds.SMDL.Player;

import NDSParser.Sounds.SMDL.SMDL;
import com.sun.media.sound.AudioSynthesizer;
import sun.audio.AudioStream;

import javax.sound.midi.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.*;

/**
 * Created by Spencer on 6/15/19.
 */
public class SMDLExporter {
    public static void exportSMDLWav(SMDL song, File out) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        AudioSynthesizer synth = getSynth();

        AudioInputStream stream = synth.openStream(new AudioFormat(44100, 16, 2, true, false), null);

        double duration = SMDLPlayer.loadSong(song.getTracks()) + 2;

        duration = Math.min(duration, 5 * 60);

        long length = (long) (duration * stream.getFormat().getFrameRate());

        //synth.

        System.out.println("Duration: " + duration);

        AudioInputStream stream2 = new AudioInputStream(stream, stream.getFormat(), length);

        AudioSystem.write(stream2, AudioFileFormat.Type.WAVE, out);
    }

    private static AudioSynthesizer getSynth() throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        if(synth instanceof AudioSynthesizer){
            return (AudioSynthesizer) synth;
        }

        for(MidiDevice.Info inf : MidiSystem.getMidiDeviceInfo()){
            MidiDevice dev = MidiSystem.getMidiDevice(inf);
            if(dev instanceof AudioSynthesizer)
                return (AudioSynthesizer) dev;
        }
        throw new MidiUnavailableException();
    }
}
