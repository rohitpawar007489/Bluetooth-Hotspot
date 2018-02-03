package btclient;

import com.sun.lwuit.Dialog;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class NetworkHandler
{

    private NetworkHandler()
    {
    }
    private static NetworkHandler instance = new NetworkHandler();

    public static NetworkHandler getInstance()
    {
        return instance;
    }
    BluetoothBrowser btb;
    String url;
    DataInputStream dis;
    DataOutputStream dos;
    StreamConnection con;

    public boolean connected = false;

    public void connect(String url)
    {
        try
        {
            this.url = url;
            con = (StreamConnection) Connector.open(url);
            dos = con.openDataOutputStream();
            dis = con.openDataInputStream();
            connected = true;
            //create browser

            //Browser b = new Browser();
            //b.show();
        } catch (IOException ex)
        {
            //display error dialog
            Dialog.show("Error", ex.getMessage(), "OK", "Error");
            connected = false;
        }
    }

    public byte[] fetchURL(String url) throws IOException
    {
        if (!connected)
        {
            return "<html><body>Not Connected Yet</body></html>".getBytes();
        }
        try
        {
            dos.writeUTF("GETURL");
            dos.writeUTF(url);
            dos.flush();

            String result = dis.readUTF();

            if (result.equals("OK"))
            {
                int l = dis.readInt();
                byte[] b = new byte[l];
                dis.readFully(b);
               Dialog.show("Byte Read",""+b.length, "OK","CANCEL");
                return b;
            }
        } catch (Exception e)
        {
            Dialog.show("Err:FETCHURL", e.toString(), null, null);
        }
        return "<html><body>Error</body></html>".getBytes();
    }
}
