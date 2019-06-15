package NDSParser.GUI;

import NDSParser.*;
import NDSParser.Files.*;
import NDSParser.GUI.Icons.Icons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Spencer on 6/12/19.
 */
public class GUIFileBrowser extends JFrame implements MouseListener{
    JTree tree;
    private final Cart c;
    public GUIFileBrowser(AbstractFolder obj, Cart c) throws BadFileException {
        this.c = c;
        this.setSize(new Dimension(200, 200));
        this.setLocationRelativeTo(null);
        this.setTitle("Cart file system browser");
        this.setVisible(true);

        tree = new JTree(populateFileTree(obj));
        tree.setCellRenderer(new FileCellRenderer());

        JScrollPane pane = new JScrollPane(tree);
        tree.addMouseListener(this);

        this.add(pane);
    }

    private DefaultMutableTreeNode populateFileTree(FilesystemObject obj) throws BadFileException {
        if(obj instanceof AbstractFile){
            return new DefaultMutableTreeNode(obj);
        }
        if(obj instanceof AbstractFolder){
            DefaultMutableTreeNode folder = new DefaultMutableTreeNode(obj);
            for(FilesystemObject o : ((AbstractFolder) obj).ls()){
                folder.add(populateFileTree(o));
            }
            return folder;
        }
        return null;
    }

    private class FileCellRenderer extends DefaultTreeCellRenderer {
        FileCellRenderer() {
            super ();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            // set label text and tool tips
            FilesystemObject object = (FilesystemObject) ((DefaultMutableTreeNode)value).getUserObject();
            try {
                setText(object.getName());
                setToolTipText(object.getPath());
                if(object instanceof AbstractFolder){
                    setIcon(Icons.FOLDER);
                }
                else if(object instanceof FileObject){
                    FileObject file = (FileObject)object;
                    if(file.getName().endsWith(".smd")){
                        setIcon(Icons.MUSIC);
                    }
                    else{
                        setIcon(Icons.DOC);
                    }
                }
            } catch (BadFileException e) {
                e.printStackTrace();
            }

            return this;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {

            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
            tree.setSelectionRow(row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();

            GUIContextForFile context = new GUIContextForFile((FilesystemObject) node.getUserObject(), c);
            context.show(e.getComponent(), e.getX(), e.getY());
            //popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
