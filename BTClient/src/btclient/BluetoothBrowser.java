package btclient;

import java.util.*;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BluetoothBrowser implements DiscoveryListener
{

    private LocalDevice localDevice;
    private DiscoveryAgent agent;

    private final Object lock = new Object();
    public final Vector devicesDiscovered = new Vector();
    public String s_URL = null;
    Vector v = new Vector();
    boolean dev_flag = false;
    boolean ser_flag = false;

    public BluetoothBrowser() throws BluetoothStateException
    {
        localDevice = LocalDevice.getLocalDevice();
        agent = localDevice.getDiscoveryAgent();
    }

    public String getURL() throws BluetoothStateException
    {
        UUID u=new javax.bluetooth.UUID("11223344556677889900aabbccddeeff", false);
        String url=agent.selectService(u, ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        return url;
    }

    public void inquiry() throws BluetoothStateException
    {
        dev_flag = true;
        agent.startInquiry(DiscoveryAgent.GIAC, this);

        while (dev_flag)
        {
            synchronized (lock)
            {
                try
                {
                    lock.wait();
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    public void deviceDiscovered(RemoteDevice device1, DeviceClass devClass)
    {
        devicesDiscovered.addElement(device1);
        //BTClient.textArea.setText(BTClient.textArea.getText() + "\r\nDevice found");
        //dev_flag=true;
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord)
    {
        System.out.println("Discovered a service.....");
        //BTClient.textArea.setText(BTClient.textArea.getText() + "\r\n" + servRecord.length + " services");
        for (int i = 0; i < servRecord.length; i++)
        {
            s_URL = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
            System.out.println("The service URL is " + s_URL);
            v.addElement(s_URL);
        }
    }

    public void serviceSearchCompleted(int transID, int responseCode)
    {
        System.out.println("search completed.");
        System.out.println("URL:" + s_URL);

        ser_flag = false;
        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    public void inquiryCompleted(int discType)
    {
        dev_flag = false;
        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    public void serviceinquiry(int index) throws BluetoothStateException
    {
        s_URL = null;
        RemoteDevice remoteDevice = (RemoteDevice) devicesDiscovered.elementAt(index);
        javax.bluetooth.UUID[] uuidSet = new javax.bluetooth.UUID[1];
        //uuidSet[0] = new javax.bluetooth.UUID("1101", true);
        uuidSet[0] = new javax.bluetooth.UUID("11223344556677889900aabbccddeeff", false);
        System.out.println("\nSearching for service...");
        //BTClient.textArea.setText(BTClient.textArea.getText() + "\r\nSearching Service of dev " + index);
        agent.searchServices(null, uuidSet, remoteDevice, this);
        try
        {
            ser_flag = true;
            while (ser_flag)
            {
                synchronized (lock)
                {
                    lock.wait();
                }
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}//End of class

