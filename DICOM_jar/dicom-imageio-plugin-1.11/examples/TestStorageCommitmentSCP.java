import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.util.Iterator;

import java.net.*;

import fr.apteryx.imageio.dicom.*;

/** Implements a simple Storage Commitment SCP that accepts to safekeep all
 * instances which it receives requests for.
 * The SCU must be know to the SCP so that an association can be established
 * by the SCP to send the result. Change the address, port and AE title
 * of the SCU.
 */
class TestStorageCommitmentSCP {
  public static void main(String[] s) {
    try {
      Plugin.setApplicationTitle("SCP");

      StorageCommitmentSCP scp = new StorageCommitmentSCP();

      // Change this to the SCU IP address, port and AE title
      InetAddress addr = InetAddress.getByName("192.168.0.2");
      scp.server.getAERegistry().registerAE("SCU", addr, 104, null);
      scp.start();

      for (;;) {
      	StorageCommitmentSCP.Request req = scp.receiveRequest();
        for (int i=0; i<req.getNumberOfSOPInstances(); i++) {
          System.out.println("Received request for "+req.getSOPInstance(i));
          req.commit(i);
        }
	req.sendResult();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
