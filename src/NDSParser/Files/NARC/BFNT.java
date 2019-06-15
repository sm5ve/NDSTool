package NDSParser.Files.NARC;

import NDSParser.Utils.ByteUtils;
import NDSParser.Files.FNT;

/**
 * Created by Spencer on 6/11/19.
 */
public class BFNT extends FNT {

    private final byte[] memory;

    public BFNT(byte[] memory, NARC n, int base) throws BadNARCException {
        super();
        this.memory = memory;

        if(!ByteUtils.getASCII(memory, base, base + 4).equals("BTNF")){
            throw new BadNARCException();
        }

        int addr = base + 8;

        //int addr = n.imgBase + c.getInt(base + 8);

        int subdirs = ByteUtils.getUnsignedShort(memory, addr + 0x6);

        //System.out.println(subdirs);

        FNTEntry root = new FNTEntry("", 0xf000, FNTEntryType.SUBDIRECTORY, 0xf000);
        this.addFNTEntry(root);

        processSubtable(addr + ByteUtils.getInt(memory, addr), ByteUtils.getUnsignedShort(memory, addr + 4), 0xf000);
        for(int i = 0; i < subdirs - 1; i++){
            processSubtable(addr + ByteUtils.getInt(memory, addr + (i + 1) * 8), ByteUtils.getUnsignedShort(memory, addr + (i + 1) * 8 + 4), 0xf000 | (i + 1));
        }

    }

    protected void processSubtable(int addr, int fileBase, int parent){
        while(true){
            int type = ByteUtils.getUnsignedByte(this.memory, addr);
            if(type == 0){
                break;
            }
            if(type == 0x80){
                continue;
            }
            if(type < 0x80){
                int len = type;
                String name = ByteUtils.getASCII(this.memory, addr + 1, addr + 1 + len);
                addr += len + 1;
                FNTEntry entry = new FNTEntry(name, fileBase, FNTEntryType.FILE, parent);
                fileBase++;
                addFNTEntry(entry);
            }
            if(type > 0x80){
                int len = type - 0x80;
                String name = ByteUtils.getASCII(this.memory, addr + 1, addr + 1 + len);
                int id = ByteUtils.getUnsignedShort(this.memory, addr + 1 + len);
                addr += len + 3;
                FNTEntry entry = new FNTEntry(name, id, FNTEntryType.SUBDIRECTORY, parent);
                addFNTEntry(entry);
            }
        }
    }

}
