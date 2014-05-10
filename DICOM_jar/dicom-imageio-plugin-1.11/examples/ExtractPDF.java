import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.Iterator;

import fr.apteryx.imageio.dicom.*;

/**
 * Extracts a PDF file from a DICOM file.
 * The DICOM file should contain an instance of the Encapsulated PDF 
 * Storage SOP class.
 */
class ExtractPDF {
  public static void main(String[] s) {
    try {
      if (s.length != 1) {
        System.err.println("Please supply an input DICOM file and an output PDF file");
        System.exit(1);
      }

      ImageIO.scanForPlugins();

      File f = new File(s[0]);
      Iterator readers = ImageIO.getImageReadersByFormatName("dicom");
      DicomReader reader = (DicomReader)readers.next();
      reader.addIIOReadWarningListener(new WarningListener());
      reader.setInput(new FileImageInputStream(f));

      DicomMetadata dmd = reader.getDicomMetadata();

      if (!UID.EncapsulatedPDFStorage.equals(dmd.getSOPClass())) {
        System.err.println("The SOP Class of this DICOM file is not Encapsulated PDF Storage");
        System.exit(1);
      }

      FileOutputStream fos = new FileOutputStream(s[1]);
      dmd.copyAttributeStream(Tag.EncapsulatedDocument, fos);
      fos.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
