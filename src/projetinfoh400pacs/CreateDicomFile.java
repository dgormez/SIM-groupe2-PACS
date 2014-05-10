/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetinfoh400pacs;



import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.exceptions.EIDException;
import com.pixelmed.dicom.DicomException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Date;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeFactory;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomOutputStream;
import com.pixelmed.dicom.TransferSyntax;
import java.io.OutputStream;
/**
 *
 * @author david
 */
public class CreateDicomFile {
    
    private BeID eID;
    int age;
    Date birthDate;
    String firstName;
    String thirdName;
    char sex;
    BufferedImage jpg;
    String natNumb;
    
    AttributeList list;
    
    OutputStream dcmOutStream;
    
    public CreateDicomFile(BeID eID_patient) throws IOException{
        this.eID = eID_patient;
        //dcmFile = new File("test");
        try{
        this.firstName =  eID.getIDData().get1stFirstname().toString();
        this.thirdName =  eID.getIDData().get3rdFirstname().toString();
        this.sex =  eID.getIDData().getSex();
        this.birthDate = eID.getIDData().getBirthDate();
        this.natNumb = eID.getIDData().getNationalNumber().toString();
        }
        catch(Exception EIDException){
            System.out.println("An Unknown Error has Occured When loading eID info");
        }
                


        this.jpg = ImageIO.read(new File("dav.jpg"));
        
        
        //???
        //Convert the image to a byte array 
        DataBufferUShort buff = (DataBufferUShort) jpg.getData().getDataBuffer();
        short[] data = buff.getData();
        ByteBuffer byteBuf = ByteBuffer.allocate(2*data.length);
        int i = 0;
        while (data.length > i) {
            byteBuf.putShort(data[i]);
            i++;
        }
        
    }
    
    public void geteIDinfo() throws EIDException{
        this.age = 2014 - eID.getIDData().getBirthDate().getYear();
    }
            
    public void DicomAttribute(){
        this.list = new AttributeList();
        AttributeTag patientNameTag = new AttributeTag(0010,0010);
        AttributeTag PatientBirthDateTag = new AttributeTag(0010,0030);
        AttributeTag PatientSexTag = new AttributeTag(0010,0040);
        AttributeTag PatientAgeTag = new AttributeTag(0010,1010);	
        AttributeTag PatientAddressTag = new AttributeTag(0010,1040);
        
        Attribute name;
        try {
            list.putNewAttribute(patientNameTag).addValue(firstName);
            //this.list.putNewAttribute(PatientBirthDateTag).addValue(this.birthDate.toString());
            //this.list.putNewAttribute(PatientSexTag).addValue(this.sex);
            //this.list.putNewAttribute(PatientAgeTag).addValue(this.age);
            //Attribute name = new AttributeFactory(patientName);
        } catch (DicomException ex){
            Logger.getLogger(CreateDicomFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Dicom Error has Occured When adding attributes and tags");
            print();
        }                       
    }
    
    public void print(){
        System.err.print(this.list.toString());
    }
    
    public void writeDicomFile() throws IOException, DicomException{
//        TransferSyntax ts = new TransferSyntax(null);
//        TransferSyntax ts2 = new TransferSyntax("attribs");

        DicomOutputStream dcmo = new DicomOutputStream(dcmOutStream,null,null);
        //dcmo.writeFileMetaInformation(meta);
        list.write(dcmo);
        
        //rajouter image dans liste des attributs???
        
        
        dcmo.close();
    }
    
}
    /*
    //Copy a header 
    DicomInputStream dis = new DicomInputStream(new File("fileToCopyheaderFrom.dcm"));
    Attributes meta = dis.readFileMetaInformation();
    Attributes attribs = dis.readDataset(-1, Tag.PixelData);
    dis.close();
    
    //Change the rows and columns
    attribs.setInt(Tag.Rows, VR.US, jpg.getHeight());
    attribs.setInt(Tag.Columns, VR.US, jpg.getWidth());
    System.out.println(byteBuf.array().length);
    //Attributes attribs = new Attributes();

    //Write the file
    attribs.setBytes(Tag.PixelData, VR.OW, byteBuf.array());
    DicomOutputStream dcmo = new DicomOutputStream(new File("myDicom.dcm"));
    dcmo.writeFileMetaInformation(meta);
    attribs.writeTo(dcmo);
    dcmo.close();*/
    
