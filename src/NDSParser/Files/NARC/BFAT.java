package NDSParser.Files.NARC;

import NDSParser.Cart;
import NDSParser.Files.FAT;
import NDSParser.Files.FileHandle;

/**
 * Created by Spencer on 6/11/19.
 */
class BFAT extends FAT {
    private final NARC n;
    public BFAT(Cart c, NARC n, int base) throws BadNARCException {
        super(c, null);
        this.n = n;

        if(!c.getASCII(base, base + 4).equals("BTAF")){
            throw new BadNARCException();
        }

        int fnum = c.getUnsignedShort(base + 8);

        int fatStart = base + 0xc;

        table = new FileHandle[fnum];

        for(int i = 0; i < table.length; i++){
            int addr = fatStart + i * 8;
            int start = c.getInt(addr);
            int end = c.getInt(addr + 4);
            FileHandle e = new FileHandle(start, end);
            //System.out.println(e);
            table[i] = e;
        }
    }

    public FileHandle getFileEntry(int id){
        FileHandle handle = table[id];
        return new FileHandle(handle.start + this.n.imgBase, handle.end + this.n.imgBase);
    }

    public byte[] copyFile(int id){
        return this.c.getBytes(this.getFileEntry(id).start, this.getFileEntry(id).end);
    }
}
