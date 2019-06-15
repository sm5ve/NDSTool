package NDSParser.Sounds.SMDL.Player;
import java.io.*;
/**
 * Created by Spencer on 6/12/19.
 */

import NDSParser.Sounds.SMDL.SMDLTrkChunkFactory;

import javax.sound.midi.*;


public class SMDLPlayer {
    private static Sequencer sequencer;

    static {
        try {
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void play(SMDLTrkChunkFactory.SMDLTrkChunk[] trks) throws MidiUnavailableException, InvalidMidiDataException {

        sequencer.open();

        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        int tempo = 0;
        int i = 0;
        for(SMDLTrkChunkFactory.SMDLTrkChunk trk : trks){
            i++;
            if(i == 8){
                //break;
            }
            Track track = sequence.createTrack();

            //track.add(makeEvent(192, channel & 0xf, 0, 0, 1));


            tempo = sequence(track, trk);

            System.out.println("Track size: " + track.size());
            System.out.println("Track ticks: " + track.ticks());
        }

        sequencer.setSequence(sequence);

        // Specifies the beat rate in beats per minute.
        sequencer.setTempoInBPM(5000);

        try {
            MidiSystem.write(sequence, MidiSystem.getMidiFileTypes()[1], new File("output.mid"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sequencer starts to play notes
        sequencer.start();
    }

    public static void stop(){
        if(sequencer.isRunning()){
            sequencer.stop();
        }
    }

    private static int sequence(Track tr, SMDLTrkChunkFactory.SMDLTrkChunk trk){
        SMDLPlayerState state = new SMDLPlayerState();
        for(int i = 0; i < 400; i++){
            advanceSequence(state, tr, trk);
            if(state.ended){
                break;
            }
            //state.tickCount++;
        }
        return state.tempo;
    }

    private static void advanceSequence(SMDLPlayerState state, Track tr, SMDLTrkChunkFactory.SMDLTrkChunk trk){
        trk.events[state.currentEvent].addToSequence(state, tr);
    }

    public static MidiEvent makeEvent(int command, int channel,
                               int note, int velocity, long tick)
    {

        MidiEvent event = null;

        try {

            // ShortMessage stores a note as command type, channel,
            // instrument it has to be played on and its speed.
            ShortMessage a = new ShortMessage();
            a.setMessage(command, channel, note, velocity);

            // A midi event is comprised of a short message(representing
            // a note) and the tick at which that note has to be played
            event = new MidiEvent(a, tick);
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }
        return event;
    }
}
