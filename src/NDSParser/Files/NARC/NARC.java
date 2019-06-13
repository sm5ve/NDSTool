package NDSParser.Files.NARC;

import NDSParser.Cart;
import NDSParser.Files.FileHandle;

/**
 * Created by Spencer on 6/12/19.
 */
public class NARC {
    public final int version;
    public final boolean littleEndian;
    public final int imgBase;

    private BFAT bfat;
    private BFNT bfnt;

    public NARC(Cart c, FileHandle h) throws BadNARCException {
        this(c, h, 0);
    }

    public NARC(Cart c, FileHandle h, int offset) throws BadNARCException {
        int start = h.start + offset;
        if(!c.getASCII(start, start + 4).equals("NARC")){
            throw new BadNARCException();
        }

        this.littleEndian = c.getUnsignedShort(start + 4) == 0xfffe;
        this.version = c.getUnsignedShort(start + 6, littleEndian);

        int size = c.getInt(start + 8, littleEndian);

        int chunkSize = c.getUnsignedShort(start + 0xc, littleEndian);
        int chunkCount = c.getUnsignedShort(start + 0xe, littleEndian);

        System.out.println(chunkCount);
        System.out.println(chunkSize);
        System.out.println(size);

        int chunkBase = start + 0x10;

        for(int i = 0; i < chunkCount; i++){
            chunkBase += parseChunk(c, chunkBase);
        }

        this.imgBase = this.ibase;
    }

    private int ibase;

    private int parseChunk(Cart c, int addr) throws BadNARCException {
        System.out.println(c.getASCII(addr, addr + 4));
        System.out.println(c.getInt(addr + 4));
        switch (c.getASCII(addr, addr + 4)){
            case "BTAF": bfat = new BFAT(c, this, addr); break;
            case "BTNF": this.bfnt = new BFNT(c, this, addr);
                System.out.println(c.getInt(addr + 8));
                System.out.println(c.getInt(addr + 12));
                break;
            case "GMIF": this.ibase = addr + 8; break;
            default: System.err.println("Unknown chunk header " + c.getASCII(addr, addr + 4));
        }
        return c.getInt(addr + 4);
    }

    public BFNT getBfnt(){
        return this.bfnt;
    }

    public BFAT getBfat(){
        return this.bfat;
    }

}
