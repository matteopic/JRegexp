/**
 * FileTransferHandler.java
 *
 * Creato il 28/nov/07 14:44:41
 */
package it.matteopic.jrb;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

/**
 * 
 * @author Matteo Piccinini
 */
public class FileTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 3977829884772037356L;

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		super.canImport(comp, transferFlavors);
		return comp instanceof JTextComponent;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t) {
//		 Component comp = support.getComponent();
	        if(!(comp instanceof JTextComponent))return false;
	        
//	        Transferable t = support.getTransferable();

	        JTextComponent textComp = (JTextComponent) comp;
	        try {
	            Reader reader = null;
	            if(t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
	                List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
	                File file = files.get(0);
	                reader = new FileReader(file);
	            }
	            else if(t.isDataFlavorSupported(DataFlavor.stringFlavor)){
	                DataFlavor[] transferFlavors = t.getTransferDataFlavors();
	                DataFlavor df = DataFlavor.selectBestTextFlavor(transferFlavors);

	                //KDE file dropped list
	                if(df.getSubType().equals("uri-list")){
	                    Reader r = (Reader)t.getTransferData(df);
	                    File file = getFile(r);
	                    reader = new FileReader(file);
	                    r.close();
	                }
	            }

	            if(reader == null)return false;
	            else{
	                String txt = read(reader);
	                reader.close();
	                textComp.setText(txt);
	                return true;
	            }
	        } catch (Exception e) {
	            return false;
	        }
	}

    private File getFile(Reader r) throws IOException, URISyntaxException {
        String firstFile = new BufferedReader(r).readLine();
        return new File(new URI(firstFile));
    }

    private String read(Reader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        int charLetti;
        char[] buffer = new char[1024];
        while ((charLetti = r.read(buffer)) > 0) {
            sb.append(buffer, 0, charLetti);
        }
        return sb.toString();
    }
}
