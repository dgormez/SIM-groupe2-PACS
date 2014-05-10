import java.io.*;
import javax.imageio.*;
import java.util.*;

import fr.apteryx.imageio.dicom.*;

/**
 * Creates a DICOMDIR file in the current directory from a
 * supplied list of files.
 */
class FileSetWrite {

 public static void main(String[] s) {
   try {
      if (s.length < 1) {
        System.err.println("Please supply files to include in the file set");
        System.exit(1);
      }

      ImageIO.scanForPlugins();
      
      Iterator readers = ImageIO.getImageReadersByFormatName("dicom");
      DicomReader reader = (DicomReader)readers.next();
      reader.addIIOReadWarningListener(new WarningListener());

      FileSet fs = new FileSet("TEST FS");
      for (int i=0; i<s.length; i++)
   	fs.add(new File(s[i]), reader);
      
      fs.write(new File("."));
      
   } catch (Exception e) {
     e.printStackTrace();
   }
 }
}
