package NDSParser.GUI;

import NDSParser.Cart;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Spencer on 6/11/19.
 */
public class CartIconViewer extends JFrame {
    private final Cart c;

    private BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB); //temp image
    private Graphics2D g2d;


    public CartIconViewer(Cart c){
        this.g2d = this.image.createGraphics();

        this.c = c;

        int headerAddr = c.getInt(0x68);
        System.out.printf("Icon header: 0x%x\n", headerAddr);
        System.out.printf("Version: 0x%x\n", c.getUnsignedShort(headerAddr));

        this.setSize(new Dimension(image.getWidth() * 8, image.getHeight() * 8));
        this.setLocationRelativeTo(null);
        this.setTitle(c.getUTF16(headerAddr + 0x340, headerAddr + 0x440));
        this.setVisible(true);
        this.setResizable(false);

        System.out.println(c.getUTF16(headerAddr + 0x340, headerAddr + 0x440));

        decodeBitmap(headerAddr);
    }

    private void decodeBitmap(int headerAddr){
        int[] pallet = new int[16];
        for(int i = 0; i < pallet.length; i++){
            int colorAddr = headerAddr + 0x220 + i * 2;
            pallet[i] = decodeColor(c.getShort(colorAddr));
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int tileAddr = headerAddr + 0x20 + (i * 4 + j) * 32;
                drawTile(tileAddr, j * 8, i * 8, pallet);
            }
        }

        this.update(this.image);
    }

    private int decodeColor(short color){
        int r = (color >> 0) & 0x1f;
        int g = (color >> 5) & 0x1f;
        int b = (color >> 10) & 0x1f;

        int out = (0xff << 24) | (r << 19) | (g << 11) | (b << 3);
        return out;
    }

    private void drawTile(int addr, int x, int y, int[] pallet){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j += 2){
                int paddr = addr + (i * 8 + j) / 2;
                int data = c.getByte(paddr);
                int c1 = data & 0xf;
                int c2 = (data >> 4) & 0xf;

                this.image.setRGB(x + j, y + i, pallet[c1]);
                this.image.setRGB(x + j + 1, y + i, pallet[c2]);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, image.getWidth() * 8, image.getHeight() * 8, null);
    }

    public void update(BufferedImage frame) {
        this.g2d.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), null);
        this.repaint();
    }
}
