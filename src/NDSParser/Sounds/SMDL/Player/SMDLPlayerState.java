package NDSParser.Sounds.SMDL.Player;

/**
 * Created by Spencer on 6/12/19.
 */
public class SMDLPlayerState {
    public int octave;
    public long tickCount;
    public int repeatMark = -1;
    public int currentNote = 0;
    public int currentEvent = 0;
    public int tempo = 5000;
    public boolean ended = false;
}
