package NDSParser.Files.NARC;

import NDSParser.Cart;
import NDSParser.Files.FileHandle;
import NDSParser.Utils.ByteUtils;

/**
 * Created by Spencer on 6/12/19.
 */
public class NARC {
    public final int version;
    public final boolean littleEndian;
    public final int imgBase;
    public final int imgEnd;

    private BFAT bfat;
    private BFNT bfnt;

    public NARC(Cart c, FileHandle h) throws BadNARCException {
        this(c.data, h);
    }

    public NARC(byte[] data, FileHandle h) throws BadNARCException {
        this(data, h.start, h.end);
    }

    public NARC(byte[] data, int start, int end) throws BadNARCException {
        if(!ByteUtils.getASCII(data, start, start + 4).equals("NARC")){
            throw new BadNARCException();
        }

        this.imgEnd = end;

        this.littleEndian = ByteUtils.getUnsignedShort(data, start + 4) == 0xfffe;
        this.version = ByteUtils.getUnsignedShort(data, start + 6, littleEndian);

        int size = ByteUtils.getInt(data, start + 8, littleEndian);

        int chunkSize = ByteUtils.getUnsignedShort(data, start + 0xc, littleEndian);
        int chunkCount = ByteUtils.getUnsignedShort(data, start + 0xe, littleEndian);

        System.out.println(chunkCount);
        System.out.println(chunkSize);
        System.out.println(size);


        int chunkBase = start + 0x10;



        for(int i = 0; i < chunkCount; i++){
            chunkBase += parseChunk(data, chunkBase);
        }

        this.imgBase = this.ibase;
    }

    private int ibase;

    private int parseChunk(byte[] data, int addr) throws BadNARCException {
        System.out.println(ByteUtils.getASCII(data, addr, addr + 4));
        System.out.println(ByteUtils.getInt(data, addr + 4));
        switch (ByteUtils.getASCII(data, addr, addr + 4)){
            case "BTAF": bfat = new BFAT(data, this, addr); break;
            case "BTNF": this.bfnt = new BFNT(data, this, addr);
                System.out.println(ByteUtils.getInt(data, addr + 8));
                System.out.println(ByteUtils.getInt(data, addr + 12));
                break;
            case "GMIF": this.ibase = addr + 8; break;
            default: System.err.println("Unknown chunk header " + ByteUtils.getASCII(data, addr, addr + 4));
        }
        return ByteUtils.getInt(data, addr + 4);
    }

    public BFNT getBfnt(){
        return this.bfnt;
    }

    public BFAT getBfat(){
        return this.bfat;
    }

}
