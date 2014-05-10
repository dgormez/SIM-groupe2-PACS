import javax.swing.*;

import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.*;

import fr.apteryx.imageio.dicom.*;

/**
 * Reads and writes a file with raw values.
 */
class Raw {
   public static void main(String[] s) {
     try {
       ImageIO.scanForPlugins();
       if (s.length != 2) {
  	 System.err.println("Please supply an input file and a output file");
   	 System.exit(1);
       }

       WarningListener listener = new WarningListener();

       Iterator readers = ImageIO.getImageReadersByFormatName("dicom");
       DicomReader reader = (DicomReader)readers.next();
       reader.addIIOReadWarningListener(listener);

       File f = new File(s[0]);
       FileImageInputStream fiis = new FileImageInputStream(f);

       reader.setInput(fiis);
       reader.setRawValues(RawValues.ALL);
       IIOMetadata md =  reader.getStreamMetadata();

       File f2 = new File(s[1]);
       f2.delete();

       Iterator writers = ImageIO.getImageWritersByFormatName("dicom");
       DicomWriter writer = (DicomWriter)writers.next();
       writer.addIIOWriteWarningListener(listener);

       writer.setOutput(new FileImageOutputStream(f2));

       writer.write(md);
       
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
}
