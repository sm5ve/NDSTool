package NDSParser.GUI;

import NDSParser.Compressed.AT4PX;
import NDSParser.Compressed.BadCompressionException;
import NDSParser.Files.AbstractFile;
import NDSParser.Files.FileObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Spencer on 6/11/19.
 */
public class BGPViewer extends JFrame {
    private BufferedImage image = new BufferedImage(256, 196, BufferedImage.TYPE_INT_ARGB); //temp image
    private Graphics2D g2d;

    int[][] palettes = new int[16][16];

    public BGPViewer(AbstractFile o){
        this.g2d = this.image.createGraphics();

        byte[] data = new byte[0];
        try {
            data = AT4PX.decode(o.copyFile());
        } catch (BadCompressionException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 0x20; i++){
            System.out.printf("0x%x: 0x%x\n", i, data[i]);
        }

        decodepalette(data);

        this.setSize(new Dimension(image.getWidth() * 4, image.getHeight() * 4));
        this.setLocationRelativeTo(null);
        //this.setTitle(c.getUTF16(headerAddr + 0x340, headerAddr + 0x440));
        this.setVisible(true);
        //this.setResizable(false);

        //System.out.println(c.getUTF16(headerAddr + 0x340, headerAddr + 0x440));

        decodeBitmap(data);

        //debugShowPalette();

        this.update(this.image);
    }

    private void debugShowPalette(){
        for(int i = 0; i < 32; i++){
            for(int j = 0; j < 24; j++){
                for(int dx = 0; dx < 8; dx++){
                    for(int dy = 0; dy < 8; dy++){
                        this.image.setRGB(i * 8 + dx, j * 8 + dy, palettes[j % 16][i % 16]);
                    }
                }
            }
        }
    }

    private void decodepalette(byte[] data){
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                int red = (data[0x20 + i * 16 * 4 + j * 4] & 0xff);
                int green = (data[0x20 + i * 16 * 4 + j * 4 + 1] & 0xff);
                int blue = data[0x20 + i * 16 * 4 + j * 4 + 2] & 0xff;
                int c = (red << 16) | (green  << 8) | (blue) | (0xff << 24);
                        //red << 16 | (0xff << 24);
                palettes[i][j] = c;
            }
        }
    }

    private void decodeBitmap(byte[] data){
        for(int i = 0; i < 24; i++){
            for(int j = 0; j < 32; j++){
                int index = i * 32 + j;

                int addr = 0x420 + index * 2;

                //int entry = ((data[addr] & 0xff) << 8) | (data[addr + 1] & 0xff);
                int entry = ((data[addr + 1] & 0xff) << 8) | (data[addr] & 0xff);

                int tileIndex = entry & 0x3ff;
                boolean flipx = (entry & 0x400) != 0;
                boolean flipy = (entry & 0x800) != 0;
                int paletteIndex = (entry >> 12) & 0xf;

                //int tileIndex = (entry >> 10) & 0x3ff;
                //boolean flipx = (entry & 0x20) != 0;
                //boolean flipy = (entry & 0x10) != 0;
                //int paletteIndex = entry & 0xf;

                drawTile(data, palettes[paletteIndex], tileIndex, flipx, flipy, j * 8, i * 8);
                //drawTile(data, palettes[paletteIndex], tileIndex, false, false, j * 8, i * 8);
            }
        }
    }

    private void drawTile(byte[] data, int[] palette, int tileIndex, boolean flipx, boolean flipy, int x, int y){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j += 2){
                int addr = 0xc40 + 32 * tileIndex + (i * 8 + j) / 2;
                int d = data[addr];
                int c1 = d & 0xf;
                int c2 = (d >> 4) & 0xf;

                int dx = flipx ? 7 - j : j;
                int dxp = flipx ? 7 - j - 1 : j + 1;
                int dy = flipy ? 7 - i : i;

                this.image.setRGB(x + dx, y + dy, palette[c1]);
                this.image.setRGB(x + dxp, y + dy, palette[c2]);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, image.getWidth() * 4, image.getHeight() * 4, null);
    }

    public void update(BufferedImage frame) {
        this.g2d.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), null);
        this.repaint();
    }
}
