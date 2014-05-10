import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;

import java.net.*;

import fr.apteryx.imageio.dicom.*;;

/**
 * Verifies that a remote application entity is running and reachable.
 */
class TestVerificationSCU {

  public static void main(String[] s) {

    try {
      
      ImageIO.scanForPlugins();
      
      // Change this to the target IP address and AE title
      PeerAE peer = new PeerAE(InetAddress.getByName("192.168.0.2"), "SCP");
      VerificationSCU scu = new VerificationSCU(peer);

      scu.verify();

      System.out.println("ok");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
