package NDSParser.GUI.Icons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Spencer on 6/12/19.
 */
public class Icons {
    public static final Icon DOC, FOLDER, MUSIC, NARC, CARC, BGP;
    static {
        DOC = getIcon("NDSParser/GUI/Icons/doc.png", 16);
        FOLDER = getIcon("NDSParser/GUI/Icons/folder.png", 16);
        MUSIC = getIcon("NDSParser/GUI/Icons/music.png", 16);
        NARC = getIcon("NDSParser/GUI/Icons/NARC.png", 16);
        CARC = getIcon("NDSParser/GUI/Icons/CARC.png", 16);
        BGP = getIcon("NDSParser/GUI/Icons/Picture.png", 16);
    }

    private static ImageIcon getIcon(String resource, int height){
        try {
            BufferedImage img = ImageIO.read( ClassLoader.getSystemResource( resource) );
            return new ImageIcon(img.getScaledInstance(img.getWidth() * height / img.getHeight(), height, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
