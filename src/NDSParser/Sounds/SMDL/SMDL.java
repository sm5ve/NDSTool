package NDSParser.Sounds.SMDL;

import NDSParser.Cart;
import NDSParser.Files.FileHandle;
import NDSParser.Utils.ByteUtils;
import NDSParser.Utils.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Spencer on 6/11/19.
 */
public class SMDL {

    public final String name;
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

    public SMDL(byte[] data) throws BadSMDLException{
        int base = 0;
        if(ByteUtils.getInt(data, base, false) != 0x736D646C){
            throw new BadSMDLException();
        }
        name = ByteUtils.getASCII(data, base + 0x20, base + 0x30);
        size = ByteUtils.getInt(data, base + 0x8);

        base += 64;

        System.out.println(this.name);

        while (base < size){
            Tuple<SMDLChunk, Integer> out = makeChunk(data, base);
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

    private Tuple<SMDLChunk, Integer> makeChunk(byte[] data, int base) throws BadSMDLException{
        int label = ByteUtils.getInt(data, base, false);
        if(!factories.containsKey(label)){
            System.err.printf("Unknown label: 0x%x\n",label);
            throw new BadSMDLException();
        }
        System.out.println("Creating chunk with factory " + factories.get(label).getClass().getCanonicalName());
        return factories.get(label).parseChunk(data, base);
    }
}
