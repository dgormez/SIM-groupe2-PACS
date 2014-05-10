import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.Iterator;

import fr.apteryx.imageio.dicom.*;

/**
 * This class converts a DICOM file into one JPEG file per frame.
 * 
 * Modality, Value of Interest and Presentation LUTs
 * are applied, leading to an image as close as possible
 * as what would have been shown on a DICOM workstation.
 * Results is painted in a 8-bit RGB image to ensure compatibility with 
 * any JPEG reader.
 */
class ConvertToJpeg {
  public static void main(String[] s) {
    try {
      if (s.length != 1) {
        System.err.println("Please supply an input file");
        System.exit(1);
      }

      ImageIO.scanForPlugins();

      File f = new File(s[0]);
      Iterator readers = ImageIO.getImageReadersByFormatName("dicom");
      DicomReader reader = (DicomReader)readers.next();
      reader.addIIOReadWarningListener(new WarningListener());
      reader.setInput(new FileImageInputStream(f));

      DicomMetadata dmd = reader.getDicomMetadata();

      int n = reader.getNumImages(true);

      for (int i=0; i<n; i++) {
        BufferedImage bi_stored = reader.read(i);
        if (bi_stored == null) {
          System.err.println("read error");
          System.exit(1);
        }

        BufferedImage bi = dmd.applyGrayscaleTransformations(bi_stored, i);
      
        BufferedImage bi_out = new BufferedImage(bi.getWidth(),
            bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        bi_out.createGraphics().drawImage(bi, 0, 0, null);

        File f_out = new File("i"+i+".jpg");
        f_out.delete();
        ImageIO.write(bi_out, "jpeg", f_out);
        System.out.println("written: "+f_out);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
