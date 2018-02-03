package btclient;

import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import javax.microedition.midlet.*;
import javax.bluetooth.BluetoothStateException;

public class BTClient extends MIDlet
{
    static public BTClient instance;
    //static TextArea textArea;
      BluetoothBrowser btb;
//    private StreamConnection m_StrmConn = null;
//    private InputStream m_Input = null;
//    private OutputStream m_Output = null;

/*//    VirtualKeyboard vkb=new VirtualKeyboard();
    class ThreadDemo extends Thread
    {

        public void run()
        {
            try
            {
                Dialog.show("Start", "Starting Enquiry", "OK", "Cancel");
                btb.inquiry();
                Dialog.show("Complete", "Enquiry Completed" + btb.devicesDiscovered.size(), "OK", "Cancel");

                if (btb.devicesDiscovered.isEmpty())
                {
                    //Dialog d = new Dialog();
                    Dialog.show("Device", "No Dev Found", "OK", "Cancel");
                } else
                {
                    Dialog.show("Device", btb.devicesDiscovered.size() + "Dev Found", "OK", "Cancel");
                    for (int i = 0; i < btb.devicesDiscovered.size(); i++)
                    {
                        //System.out.println(btb.devicesDiscovered.elementAt(i));
                        //Dialog d = new Dialog();
                        btb.serviceinquiry(i);
                    }
                    if (btb.v.isEmpty())
                    {
                        Dialog.show("Device", "No services found", "OK", "Cancel");
                    } else
                    {
                        Dialog.show("Device", btb.v.size() + " services found", "OK", "Cancel");
                        for (int i = 0; i < btb.v.size(); i++)
                        {
                            Dialog.show("Device", (String) btb.v.elementAt(i), "OK", "Cancel");
                        }
                        NetworkHandler.getInstance().connect((String) btb.v.elementAt(0));
                    }
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                Dialog.show("Errorstr", ex.toString(), "OK", "Cancel");
            }

        }
    }
*/
    class ConnectionThread extends Thread
    {

        public void run()
        {
            try
            {
                Dialog.show("Connecting..", "SELECTING URL", "OK", "Cancel");
                String url = btb.getURL();
                Dialog.show("Connecting..", "SELECTED URL : " + url, "OK", "Cancel");
                NetworkHandler.getInstance().connect(url);
                Dialog.show("Connecting..", "Connected", "OK", "Cancel");


            } catch (BluetoothStateException ex)
            {
                ex.printStackTrace();
                Dialog.show("CTError", ex.toString(), "OK", "Cancel");
            }
        }
    }

    public void startApp()
    {
        instance=this;
        try
        {

            Display.init(this);

            try
            {
                Resources r = Resources.open("/theme1.res");
                UIManager.getInstance().setThemeProps(r.getTheme("Theme1"));
            } catch (Exception ioe)
            {
                ioe.printStackTrace();
                System.out.println("Couldn't load theme.");
            }
            Browser.mainbrowser = new MainBrowser();
            Browser.mainbrowser.show();

            Thread.sleep(1000);


            Browser.browser = new Browser();
            Browser.urlinput = new URLInput();
            Browser.bookmarks = new BookMarks();
            Browser.history=new History();



            Thread.sleep(1000);

            btb = new BluetoothBrowser();


//            ThreadDemo t = new ThreadDemo();
//            t.start();

            ConnectionThread t = new ConnectionThread();
            t.start();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void pauseApp()
    {
    }

    public void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }
}
