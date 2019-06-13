package NDSParser.Sounds;

import NDSParser.Cart;
import NDSParser.Files.FileHandle;
import NDSParser.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDL {

    private final String name;
    private final int size;

    private static HashMap<Integer, SMDLChunkFactory> factories = new HashMap<>();


    private static void addFactory(SMDLChunkFactory factory){
        factories.put(factory.getChunkLabel(), factory);
    }

    static{
        addFactory(new SMDLSongChunkFactory());
        addFactory(new SMDLTrkChunkFactory());
        addFactory(new SMDLEndChunkFactory());
    }

    private ArrayList<SMDLChunk> chunks = new ArrayList<>();

    public SMDL(Cart c, FileHandle handle) throws BadSMDLException{
        int base = handle.start;
        if(c.getInt(base, false) != 0x736D646C){
            throw new BadSMDLException();
        }
        name = c.getASCII(base + 0x20, base + 0x30);
        size = c.getInt(base + 0x8);

        base += 64;

        System.out.println(this.name);

        while ((base - handle.start) < size){
            Tuple<SMDLChunk, Integer> out = makeChunk(c, base);
            base = out.b;
            chunks.add(out.a);
        }
    }

    public SMDLTrkChunkFactory.SMDLTrkChunk[] getTracks(){

        ArrayList<SMDLTrkChunkFactory.SMDLTrkChunk> tracks = new ArrayList<>();
        for(SMDLChunk chunk : chunks){
            if(chunk instanceof SMDLTrkChunkFactory.SMDLTrkChunk){
                tracks.add((SMDLTrkChunkFactory.SMDLTrkChunk) chunk);
            }
        }
        SMDLTrkChunkFactory.SMDLTrkChunk[] tks = new SMDLTrkChunkFactory.SMDLTrkChunk[tracks.size()];
        for(int i = 0; i < tracks.size(); i++){
            tks[i] = tracks.get(i);
        }
        return tks;
    }

    private Tuple<SMDLChunk, Integer> makeChunk(Cart c, int base) throws BadSMDLException{
        int label = c.getInt(base, false);
        if(!factories.containsKey(label)){
            System.err.printf("Unknown label: 0x%x\n",label);
            throw new BadSMDLException();
        }
        System.out.println("Creating chunk with factory " + factories.get(label).getClass().getCanonicalName());
        return factories.get(label).parseChunk(c, base);
    }
}
