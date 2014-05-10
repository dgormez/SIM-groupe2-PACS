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
 * Sends a request to a Storage Commitment SCP.
 * Change the address, port and AE title in the PeerAE
 * constructor call.
 * Also change the SOP instance and SOP class of the object the
 * storage commitment is requested for. The SCP must have received
 * the object before, by TestStorageSCU or another mean.
 */
class TestStorageCommitmentSCU extends StorageCommitmentResultReceiver {

  public static void main(String[] s) {
    try {
      // Change this to the target IP address, port and AE title
      InetAddress addr = InetAddress.getByName("192.168.0.2");
      PeerAE peer = new PeerAE(addr, 104, "SCP");

      TestStorageCommitmentSCU receiver = new TestStorageCommitmentSCU();
      StorageCommitmentSCU scu = 
        new StorageCommitmentSCU(peer, receiver, false);
      Plugin.setServersAreDaemons(false);
      receiver.start();

      StorageCommitmentSCU.Request req = new StorageCommitmentSCU.Request();
      // Change this to the SOP class and instance to safekeep
      req.addSOPInstance(
          "1.2.840.10008.5.1.4.1.1.2", 
          "1.2.3.4.5.6.7");

      scu.sendRequest(req);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean resultReceived(StorageCommitmentSCU.Result result, DataSet rsp) {
    System.err.println(result.commits(0) ? "Success" : "Failure");
    try {
      stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }
}
