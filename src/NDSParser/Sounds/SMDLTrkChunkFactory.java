package NDSParser.Sounds;

import NDSParser.Cart;
import NDSParser.Sounds.Player.SMDLPlayer;
import NDSParser.Sounds.Player.SMDLPlayerState;
import NDSParser.Tuple;
import javax.sound.midi.*;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDLTrkChunkFactory implements SMDLChunkFactory {

    @Override
    public int getChunkLabel() {
        return 0x74726b20;
    }

    private static class ParseState{
        public int lastRest = 1, lastNote = 1;
    }

    @Override
    public Tuple<SMDLChunk, Integer> parseChunk(Cart c, int base) throws BadSMDLException {

        ParseState state = new ParseState();

        if(c.getInt(base, false) != 0x74726b20){
            throw new BadSMDLException();
        }
        int trkid = c.getUnsignedByte(base + 0x10);
        int chanid = c.getUnsignedByte(base + 0x11) & 0xf;

        int eventByteLen = c.getInt(base + 0xc);

        ArrayList<Event> events = new ArrayList<>();
        int addr = base + 0x14;
        while(true){
            int eventID = c.getUnsignedByte(addr);
            if(0 <= eventID && eventID < 0x80){
                Tuple<NoteEvent, Integer> event = NoteEvent.makeEvent(c, addr, chanid, state);
                events.add(event.a);
                //System.out.println(event.a.note);
                //System.out.println(event.a.duration);
                addr = event.b;
            }
            else if(0x80 <= eventID && eventID < 0x90){
                Tuple<RestEvent, Integer> event = RestEvent.makeEvent(c, addr, state);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0x90){
                Tuple<RepeatPauseEvent, Integer> event = RepeatPauseEvent.makeEvent(c, addr, state);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0x91){
                Tuple<AddPauseEvent, Integer> event = AddPauseEvent.makeEvent(c, addr, state);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xe0){
                Tuple<PrimaryVolumeEvent, Integer> event = PrimaryVolumeEvent.makeEvent(c, addr, chanid);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xe3){
                Tuple<SecondaryVolumeEvent, Integer> event = SecondaryVolumeEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xa4 || eventID == 0xa5){
                Tuple<TempoEvent, Integer> event = TempoEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0x98){
                Tuple<EndTrackEvent, Integer> event = EndTrackEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
                break;
            }
            else if(eventID == 0x99){
                Tuple<LoopPointEvent, Integer> event = LoopPointEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0x92 || eventID == 0x93 || eventID == 0x94){
                Tuple<PauseEvent, Integer> event = PauseEvent.makeEvent(c, addr, state);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xa8){
                addr += 3;
                //TODO figure out this command
            }
            else if(eventID == 0xa9){
                addr += 2;
                //TODO figure out this command
            }
            else if(eventID == 0xaa){
                addr += 2;
                //TODO figure out this command
            }
            else if(eventID == 0xb2){
                addr += 2;
                //TODO figure out this command
            }
            else if(eventID == 0xdb){
                addr += 2;
                //TODO figure out this command
            }else if(eventID == 0xc0){
                addr += 2;
                //TODO figure out this command

            }else if(eventID == 0xd8){
                addr += 3;
                //TODO figure out this command
            }else if(eventID == 0xd0){
                addr += 2;
                //TODO figure out this command
            }else if(eventID == 0xf6){
                addr += 2;
                //TODO figure out this command
            }else if(eventID == 0xb0){
                addr += 1;
                //TODO figure out this command
            }
            else if(eventID == 0xb0){
                addr += 6;
                //TODO figure out this command
            }
            else if(eventID == 0xd7){
                Tuple<PitchBendEvent, Integer> event = PitchBendEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xbe){
                Tuple<ModEvent, Integer> event = ModEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xa0){
                Tuple<SetOctaveEvent, Integer> event = SetOctaveEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xa1){
                Tuple<AddOctaveEvent, Integer> event = AddOctaveEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xac){
                Tuple<SetProgramEvent, Integer> event = SetProgramEvent.makeEvent(c, addr, chanid);
                events.add(event.a);
                addr = event.b;
            }
            else if(eventID == 0xe8){
                Tuple<PanEvent, Integer> event = PanEvent.makeEvent(c, addr);
                events.add(event.a);
                addr = event.b;
            }
            else{
                System.err.printf("UNKNOWN EVENT TYPE: 0x%x\n", eventID);
                throw new BadSMDLException();
            }
        }

        Event[] evts = new Event[events.size()];
        for(int i = 0; i < evts.length; i++){
            evts[i] = events.get(i);
        }

        addr = Math.max(addr, base + 0x10 + eventByteLen);

        addr += (4 - (addr) % 4) % 4;

        return new Tuple<>(new SMDLTrkChunk(trkid, chanid, evts), addr);
    }

    public static class SMDLTrkChunk implements SMDLChunk{
        public final int trkid, chanid;
        public final Event[] events;
        private SMDLTrkChunk(int trkid, int chanid, Event[] events) throws BadSMDLException{
            this.events = events;
            this.trkid = trkid;
            this.chanid = chanid;
        }
    }

    public static abstract class Event{
        public abstract void addToSequence(SMDLPlayerState state, Track track);
    }

    public static class NoteEvent extends Event{
        public final int duration, octaveMod, note, velocity, channel;
        private NoteEvent(int duration, int octaveMod, int note, int velocity, int channel){
            this.duration = duration * 2 ;
            this.octaveMod = octaveMod;
            this.note = note;
            this.velocity = velocity;
            this.channel = channel;
        }

        public static Tuple<NoteEvent, Integer> makeEvent(Cart c, int base, int channel, ParseState state) throws BadSMDLException{
            int velocity = c.getByte(base);
            int evt = c.getUnsignedByte(base + 1);
            if(!(velocity >= 0 && velocity < 0x80)){
                throw new BadSMDLException();
            }
            int paramBytes = (evt >> 6) & 3;
            int octaveMod = ((evt >> 4) & 3) - 2;
            int note = evt & 0xf;
            int duration = 0;

            if(paramBytes == 0){
                //duration = -1;
                duration = state.lastNote;
            }
            else{
                for(int i = 0; i < paramBytes; i++){
                    duration <<= 8;
                    duration += c.getUnsignedByte(base + 2 + i);
                }
                state.lastNote = duration;
            }

            return new Tuple<>(new NoteEvent(duration, octaveMod, note, velocity, channel), base + 2 + paramBytes);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //state.octave + this.octaveMod
            state.octave += this.octaveMod;
            int note = this.note + (state.octave) * 12;
            state.currentNote = note;
            track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_ON, this.channel, note, this.velocity, state.tickCount));

            //int dur = state.lastPlay;
            int dur = this.duration;
            state.tickCount += dur;//state.lastPlay;
            track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, this.channel, note, 0, state.tickCount - 1 ));

            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, note, 100, state.tickCount + dur - 1));
            state.currentEvent++;
        }
    }

    public static class PauseEvent extends Event{
        public final int duration;
        private PauseEvent(int duration){
            this.duration = duration * 2;
        }

        public static Tuple<PauseEvent, Integer> makeEvent(Cart c, int base, ParseState state) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            int paramSize;
            if(evt == 0x92){
                paramSize = 1;
            }
            else if(evt == 0x93){
                paramSize = 2;
            }
            else if(evt == 0x94){
                paramSize = 3;
            }
            else{
                throw new BadSMDLException();
            }

            int duration = 0;
            for(int i = paramSize; i > 0; i--){
            //for(int i = 0; i < paramSize; i++){
                duration <<= 8;
                duration |= c.getUnsignedByte(base + i);
                        //c.getUnsignedByte(base + i + 1);
            }

            state.lastRest = duration;
            return new Tuple<>(new PauseEvent(duration), base + 1 + paramSize);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.tickCount += this.duration;
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.currentEvent++;
        }
    }

    public static class RestEvent extends Event{
        public final int duration;
        private static final int[] times = {
            96, 72, 64, 48, 36, 32, 24, 18, 16, 12, 9, 8, 6, 4, 3, 2
        };
        private RestEvent(int duration){
            this.duration = duration * 2;
        }

        public static Tuple<RestEvent, Integer> makeEvent(Cart c, int base, ParseState state) throws BadSMDLException{
            int evt = c.getUnsignedByte(base);
            if(!(evt >= 0x80 && evt < 0x90)){
                throw new BadSMDLException();
            }

            state.lastRest = times[evt - 0x80];
            return new Tuple<>(new RestEvent(times[evt - 0x80]), base + 1);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.tickCount += this.duration;
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.currentEvent++;
        }
    }

    public static class PrimaryVolumeEvent extends Event{
        public final int volume;
        public final int channel;
        private PrimaryVolumeEvent(int volume, int channel){
            this.volume = volume;
            this.channel = channel;
        }

        public static Tuple<PrimaryVolumeEvent, Integer> makeEvent(Cart c, int base, int channel) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xe0){
                throw new BadSMDLException();
            }

            int volume = c.getUnsignedByte(base + 1) & 0x7f;

            return new Tuple<>(new PrimaryVolumeEvent(volume, channel), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            track.add(SMDLPlayer.makeEvent(ShortMessage.CONTROL_CHANGE, this.channel, 0x7, this.volume, state.tickCount));
            state.currentEvent++;
        }
    }

    public static class SecondaryVolumeEvent extends Event{
        public final int volume;
        private SecondaryVolumeEvent(int volume){
            this.volume = volume;
        }

        public static Tuple<SecondaryVolumeEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xe3){
                throw new BadSMDLException();
            }

            int volume = c.getUnsignedByte(base + 1) * 0x7f;

            return new Tuple<>(new SecondaryVolumeEvent(volume), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            state.currentEvent++;
        }
    }

    public static class TempoEvent extends Event{
        public final int tempo;
        private TempoEvent(int tempo){
            this.tempo = tempo;
        }

        public static Tuple<TempoEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xa4 && evt != 0xa5){
                throw new BadSMDLException();
            }

            int tempo = c.getUnsignedByte(base + 1) * 0x7f;

            return new Tuple<>(new TempoEvent(tempo), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            System.out.println("Setting tempo to " + tempo);
            state.tempo = tempo;
            state.currentEvent++;
        }
    }

    public static class LoopPointEvent extends Event{
        private LoopPointEvent(){
        }

        public static Tuple<LoopPointEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0x99){
                throw new BadSMDLException();
            }

            return new Tuple<>(new LoopPointEvent(), base + 1);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            state.repeatMark = state.currentEvent;
            state.currentEvent++;
        }
    }

    public static class EndTrackEvent extends Event{
        private EndTrackEvent(){
        }

        public static Tuple<EndTrackEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0x98){
                throw new BadSMDLException();
            }

            return new Tuple<>(new EndTrackEvent(), base + 1);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            if(state.repeatMark == -1){
                state.ended = true;
            }
            else {
                state.currentEvent = state.repeatMark;
            }
        }
    }

    public static class PitchBendEvent extends Event{
        public final int pitchbend;
        private PitchBendEvent(int duration){
            this.pitchbend = duration;
        }

        public static Tuple<PitchBendEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xd7){
                throw new BadSMDLException();
            }

            int pitchbend = c.getShort(base + 1);

            return new Tuple<>(new PitchBendEvent(pitchbend), base + 3);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            state.currentEvent++;
        }
    }

    public static class SetOctaveEvent extends Event{
        public final int octave;
        private SetOctaveEvent(int octave){
            this.octave = octave;
        }

        public static Tuple<SetOctaveEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xa0){
                throw new BadSMDLException();
            }

            int octave = c.getUnsignedByte(base + 1);

            if(!(octave >= 0 && octave < 10)){
                throw new BadSMDLException();
            }

            return new Tuple<>(new SetOctaveEvent(octave), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            state.octave = this.octave;
            state.currentEvent++;
        }
    }

    public static class AddOctaveEvent extends Event{
        public final int octave;
        private AddOctaveEvent(int octave){
            this.octave = octave;
        }

        public static Tuple<AddOctaveEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xa1){
                throw new BadSMDLException();
            }

            int octave = c.getUnsignedByte(base + 1);

            return new Tuple<>(new AddOctaveEvent(octave), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //state.octave = (state.octave + this.octave) % 10;
            state.currentEvent++;
        }
    }

    public static class PanEvent extends Event{
        public final int pan;
        private PanEvent(int pan){
            this.pan = pan;
        }

        public static Tuple<PanEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xe8){
                throw new BadSMDLException();
            }

            int pan = c.getUnsignedByte(base + 1);

            return new Tuple<>(new PanEvent(pan), base + 2);
        }
        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            state.currentEvent++;
        }
    }

    public static class SetProgramEvent extends Event{
        public final int prog, channel;
        private SetProgramEvent(int prog, int channel){
            this.prog = prog;
            this.channel = channel;
        }

        public static Tuple<SetProgramEvent, Integer> makeEvent(Cart c, int base, int channel) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xac){
                throw new BadSMDLException();
            }

            int prog = c.getUnsignedByte(base + 1);

            return new Tuple<>(new SetProgramEvent(prog, channel), base + 2);
        }
        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            track.add(SMDLPlayer.makeEvent(ShortMessage.PROGRAM_CHANGE, this.channel, this.prog, 0, state.tickCount));
            state.currentEvent++;
        }
    }

    public static class ModEvent extends Event{
        public final int mod;
        private ModEvent(int mod){
            this.mod = mod;
        }

        public static Tuple<ModEvent, Integer> makeEvent(Cart c, int base) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0xbe){
                throw new BadSMDLException();
            }

            int mod = c.getUnsignedByte(base + 1);

            return new Tuple<>(new ModEvent(mod), base + 2);
        }
        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //TODO
            state.currentEvent++;
        }
    }

    public static class RepeatPauseEvent extends Event{
        public final int pauseDur;
        private RepeatPauseEvent(int dur){
            pauseDur = dur;
        }

        public static Tuple<RepeatPauseEvent, Integer> makeEvent(Cart c, int base, ParseState state) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0x90){
                throw new BadSMDLException();
            }

            return new Tuple<>(new RepeatPauseEvent(state.lastRest * 2), base + 1);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.tickCount += this.pauseDur;
            System.out.println("Repeat pause");
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.currentEvent++;
        }
    }

    public static class AddPauseEvent extends Event{
        public final int duration;
        private AddPauseEvent(int dur){
            if(dur < 0){
                this.duration = 0;
            }
            else{
                this.duration = dur * 2;
            }
        }

        public static Tuple<AddPauseEvent, Integer> makeEvent(Cart c, int base, ParseState state) throws BadSMDLException {
            int evt = c.getUnsignedByte(base);
            if(evt != 0x91){
                throw new BadSMDLException();
            }

            return new Tuple<>(new AddPauseEvent(c.getByte(base + 1) + state.lastRest), base + 2);
        }

        @Override
        public void addToSequence(SMDLPlayerState state, Track track) {
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            System.out.println(0x91);
            //state.tickCount += state.lastPause;
            state.tickCount += this.duration;
            //track.add(SMDLPlayer.makeEvent(ShortMessage.NOTE_OFF, 1, state.currentNote, 100, state.tickCount));
            state.currentEvent++;
        }
    }
}
