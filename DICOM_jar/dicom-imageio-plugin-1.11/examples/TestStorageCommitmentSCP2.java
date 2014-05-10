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
 * Like TestStorageCommitmentSCP but using a listener to receive requests.
 */
class TestStorageCommitmentSCP2 implements StorageCommitmentRequestListener {
  public static void main(String[] s) {
    try {
      Plugin.setApplicationTitle("SCP");
      Plugin.setServersAreDaemons(false);

      StorageCommitmentSCP scp = new StorageCommitmentSCP(104, 
          new TestStorageCommitmentSCP2());

      // Change this to the SCU IP address, port and AE title
      InetAddress addr = InetAddress.getByName("192.168.0.2");
      scp.server.getAERegistry().registerAE("SCU", addr, 104, null);

      scp.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void requestReceived(StorageCommitmentSCP.Request req,
        DataSet response) {
    for (int i=0; i<req.getNumberOfSOPInstances(); i++) {
      System.out.println("Received request for "+req.getSOPInstance(i));
      req.commit(i);
    }
    try {
      req.sendResult();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }


}
