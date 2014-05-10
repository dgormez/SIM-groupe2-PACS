import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.*;
import fr.apteryx.imageio.dicom.*;

class EncapsulatePDF {
   public static void main(String[] s) {
     try {
       ImageIO.scanForPlugins();
       if (s.length != 2) {
  	 System.err.println("Please supply an input PDF file and an output DICOM file");
   	 System.exit(1);
       }

       WarningListener listener = new WarningListener();

       File f = new File(s[0]);
       FileInputStream fis = new FileInputStream(f);

       File f2 = new File(s[1]);
       f2.delete();

       Iterator writers = ImageIO.getImageWritersByFormatName("dicom");
       DicomWriter writer = (DicomWriter)writers.next();
       writer.addIIOWriteWarningListener(listener);

       writer.setOutput(new FileImageOutputStream(f2));
       DicomMetadata dmd = new DicomMetadata();
       dmd.setSOPClass(UID.EncapsulatedPDFStorage);
       dmd.setAttribute(Tag.EncapsulatedDocument, fis);
       writer.write(dmd);
       
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
}
