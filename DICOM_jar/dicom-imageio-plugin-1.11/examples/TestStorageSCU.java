import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.Iterator;

import java.net.*;

import fr.apteryx.imageio.dicom.*;

/**
 * Reads a file locally and send the DICOM object it contains to a
 * remote Storage SCP. Change the address, port and AE title in the PeerAE
 * constructor call. If the -secure flag is passed, a connexion to the SCP
 * using secure transport will be used. If the -raw flag is passed, the
 * file is read without any decoding of metadata and pixel data and
 * the object is sent to the SCP as is. If the -commit flag is passed, the SCP 
 * is requested to safekeep the object using the Storage Commitment Service 
 * Class.
 */
class TestStorageSCU {
  public static void main(String[] s) {
    try {
      String filename = null;
      boolean secure = false, raw = false, commit = false;
      for (int i=0; i<s.length; i++) {
	if ("-secure".equals(s[i])) secure = true;
        else if ("-raw".equals(s[i])) raw = true;
        else if ("-commit".equals(s[i])) commit = true;
	else filename = s[i];
      }

      if (filename == null) {
	System.err.println("Please supply a file to send");
	System.exit(1);
      }

      ImageIO.scanForPlugins();

      WarningListener listener = new WarningListener();

      File f = new File(filename);

      Iterator readers = ImageIO.getImageReadersByFormatName("dicom");
      DicomReader reader = (DicomReader)readers.next();
      reader.addIIOReadWarningListener(listener);
      reader.setInput(new FileImageInputStream(f));
      if (raw) reader.setRawValues(RawValues.ALL);

      Iterator writers = ImageIO.getImageWritersByFormatName("dicom");
      DicomWriter writer = (DicomWriter)writers.next();
      writer.addIIOWriteWarningListener(listener);

      // Change this to the target IP address and AE title
      InetAddress addr = InetAddress.getByName("192.168.0.2");
      PeerAE peer = secure ? 
	  new PeerAE(addr, "SCP", 
	    SecureTransport.JAVA_DEFAULT_AUTH_ACCEPTOR) :
	  new PeerAE(addr, "SCP");

      DicomMetadata dmd = (DicomMetadata) reader.getStreamMetadata();

      dmd.removeUnwritableElements();

      StorageSCU scu = new StorageSCU(peer);
      if (commit) {
        scu.storeAndRequestCommitment(dmd, writer);
      }	else {
        scu.store(dmd, writer);
      }
 
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
