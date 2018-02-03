

package btserver;

import java.util.*;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;


 public class BluetoothBrowser implements DiscoveryListener
 {
    private LocalDevice localDevice;
    private DiscoveryAgent agent;
//    private btframe bf;
    private ApplicationContext context;
    private static final Object lock=new Object();
    public static final Vector devicesDiscovered = new Vector();
    public static String s_URL=null;

/*    public BluetoothBrowser(btframe bf) throws BluetoothStateException
    {
        localDevice = LocalDevice.getLocalDevice();
        agent = localDevice.getDiscoveryAgent();
        this.bf = bf;
        context = ApplicationContext.getInstance();
    }
*/
    public void inquiry() throws BluetoothStateException
    {
        agent.startInquiry(DiscoveryAgent.GIAC, this);
    }

    public void deviceDiscovered(RemoteDevice device1, DeviceClass devClass)
    {
        context.addDevice(device1);
        devicesDiscovered.addElement(device1);
        synchronized(lock)
        {
          lock.notify();
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord)
    {
      System.out.println("Discovered a service.....");
      for(int i=0;i<servRecord.length;i++)
      {
	   s_URL=servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,true);
	   System.out.println("The service URL is "+s_URL);
      }
    }

    public void serviceSearchCompleted(int transID, int responseCode)
    {
     System.out.println("search completed.");
     System.out.println("URL:"+s_URL);

      synchronized(lock)
      {
        lock.notify();
      }
    }

    public void inquiryCompleted(int discType)
    {

//      bf.refreshList();
      synchronized(lock)
      {
        lock.notify();
      }
    }

    public void serviceinquiry(int index) throws BluetoothStateException
    {
      s_URL=null;
      RemoteDevice remoteDevice=(RemoteDevice)devicesDiscovered.elementAt(index);
      javax.bluetooth.UUID[] uuidSet = new javax.bluetooth.UUID[1];
      uuidSet[0]=new javax.bluetooth.UUID("1105",true);
      System.out.println("\nSearching for service...");
      agent.searchServices(null,uuidSet,remoteDevice,this);
      try
      {
            synchronized(lock)
            {
                lock.wait();
            }
      }
      catch (InterruptedException e)
      {
            e.printStackTrace();
      }
   }
 }//End of class