package NDSParser.Files.NARC;

import NDSParser.Utils.ByteUtils;
import NDSParser.Files.FAT;
import NDSParser.Files.FileHandle;

/**
 * Created by Spencer on 6/11/19.
 */
class BFAT extends FAT {
    private final NARC n;
    public BFAT(byte[] memory, NARC n, int base) throws BadNARCException {
        super(memory);
        this.n = n;

        if(!ByteUtils.getASCII(memory, base, base + 4).equals("BTAF")){
            throw new BadNARCException();
        }

        int fnum = ByteUtils.getUnsignedShort(memory, base + 8);

        int fatStart = base + 0xc;

        table = new FileHandle[fnum];

        for(int i = 0; i < table.length; i++){
            int addr = fatStart + i * 8;
            int start = ByteUtils.getInt(memory, addr);
            int end = ByteUtils.getInt(memory, addr + 4);
            FileHandle e = new FileHandle(start, end);
            //System.out.println(e);
            table[i] = e;
        }
    }

    public FileHandle getFileEntry(int id){
        FileHandle handle = table[id];
        return new FileHandle(handle.start + this.n.imgBase, handle.end + this.n.imgBase);
    }
}
