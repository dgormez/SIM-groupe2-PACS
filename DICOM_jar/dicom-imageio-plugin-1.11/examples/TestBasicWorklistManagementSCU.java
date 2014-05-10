import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;

import java.net.*;

import fr.apteryx.imageio.dicom.*;;

/**
 * Sends a query to a Basic Worklist Management
 * SCP and display the result. Change the address, port, AE title in the
 * PeerAE constructor call. Also try to change the matching keys and return keys
 * to control the result.
 */
class TestBasicWorklistManagementSCU {

  public static void main(String[] s) {

    try {
      
      ImageIO.scanForPlugins();
      
      // Change this to the SCP IP address and AE title
      PeerAE peer = new PeerAE(InetAddress.getByName("192.168.0.2"), "WLM_SCP");

      BasicWorklistManagementSCU scu = new BasicWorklistManagementSCU(peer);

      try {
	Identifier query = new Identifier(Identifier.MODEL_WL_MODALITY);

        // Matching keys
        // Here we are asking for procedure step schedule for a specific day
        // and for a specific modality
        DataSet procedure_step= new DataSet();
        procedure_step.add(Tag.ScheduledProcedureStepStartDate, 
            new Day(2008, 1, 26));
        procedure_step.add(Tag.Modality, "MA");
        query.setAttribute(Tag.ScheduledProcedureStepSequence, procedure_step);

        // Return keys : the information we are interested in
	query.requestAttribute(Tag.PatientID);

        // Sends the query
	Iterator list = scu.find(query);
	
	if (!list.hasNext()) {
	  System.out.println("No result");
	  return;
	}
        int i = 1;
	while (list.hasNext()) {
          System.err.println("\nResponse "+i++);
      	  Identifier id = (Identifier) list.next();
	  System.out.println(id);
	}
      } finally {
	scu.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
