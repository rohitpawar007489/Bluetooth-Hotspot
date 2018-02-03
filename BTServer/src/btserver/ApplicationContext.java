
package btserver;


import java.util.List;
import javax.bluetooth.RemoteDevice;
import java.util.ArrayList;

 public class ApplicationContext
 {
    public static ApplicationContext instance;
    private ApplicationContext()
    {
       devices = new ArrayList<RemoteDevice>();
    }

    public static ApplicationContext getInstance()
    {
        if(instance==null)
        {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private List<RemoteDevice> devices;

    public void addDevice(RemoteDevice device)
    {
        devices.add(device);
    }

    public List<RemoteDevice> getDevices()
    {
        return devices;
    }
 }
