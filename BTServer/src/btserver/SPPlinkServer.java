package btserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.microedition.io.*;   
import java.io.*;
import javax.bluetooth.*;
import javax.bluetooth.RemoteDevice;

public class SPPlinkServer implements ActionListener, Runnable
{

    LocalDevice device;
    DiscoveryAgent agent;
    String HTBTurl = null;
    Boolean mServerState = false; // stop is default state
    Thread mServer = null;
    String msgOut = "srv out msg";
    String msgIn = "no msg rcv";
    StreamConnectionNotifier btServerNotifier;
    //UUID uuid = new UUID("1101", true);
    UUID uuid = new UUID("11223344556677889900aabbccddeeff", false);
    JLabel spacerlabel = new JLabel(" ");
    JButton startButton = new JButton("Start Server");
    JTextArea textarea = new JTextArea("", 20, 40);
    JButton endButton = new JButton("End Server");

    public SPPlinkServer()
    {

        //Give it the Java look and feel
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("FileServer ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(textarea);
        textarea.setEditable(false);

        Container cp = frame.getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(this);
        cp.add(startButton);

        endButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endButton.addActionListener(this);
        cp.add(endButton);


        spacerlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(spacerlabel);

        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        cp.add(scrollPane);

        frame.pack() ;
        frame.setVisible(true);

        updateStatus("[server:] FileServer Application started");
        updateStatus("[server:] Press the \"Start Server\" button to await for client devices");


    }

    private void startServer()
    {
        if (mServer != null)
        {
            return;
        }
        //start the server and receiver
        mServer = new Thread(this);
        mServer.start();
    }

    @SuppressWarnings("empty-statement")
    private void endServer()
    {
        if (mServer == null)
        {
            return;
        }
        try
        {
            mServer.interrupt();
            mServer.join();
        } catch (Exception ex)
        {
        }
        ;
        mServer = null;

        // mServer.stop();

    }

    public void run()
    {
        try
        {
            device = LocalDevice.getLocalDevice(); // obtain reference to singleton
            device.setDiscoverable(DiscoveryAgent.GIAC); // set Discover mode to LIAC
        } catch (Exception e)
        {
            System.err.println("Cant init set discvover");
            e.printStackTrace();
        }

        String url = "btspp://localhost:" + uuid + ";name=BTTP;authenticate=false;master=false;encrypt=false";
        System.out.println("URL : " + url);
        try
        {

            // obtain connection and stream to this service
            btServerNotifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        while (mServerState)
        {
            StreamConnection btConn = null;
            try
            {
                updateStatus("[server:] Now waiting for a client to connect");
                btConn = btServerNotifier.acceptAndOpen();
                RemoteDevice dev = RemoteDevice.getRemoteDevice(btConn);

                System.out.println("Remote device address: " + dev.getBluetoothAddress());

                updateStatus("Remote device " + dev.getFriendlyName(true) + "   connected");

            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            if (btConn != null)
            {
                processConnection(btConn);
            }
        }
    }

    void processConnection(StreamConnection conn)
    {
        // A client is now connected
        String str = null;
        DataInputStream in = null;
        DataInputStream d_in = null;
        Response res;
        try
        {


            //Reading data--------stream open
            in = conn.openDataInputStream();
            DataOutputStream out = conn.openDataOutputStream();
            while (true)
            {
                //reading URL and displaying it on the command prompt
                str = in.readUTF();
                System.out.println("Command : " + str);

                if (str.equals("GETURL"))
                {
                    String url = in.readUTF();
                    System.out.println("URL : "+url);
                    try
                    {
                        res = NetworkHandler.getResponse(url);
                        byte[] b = res.data;
                        out.writeUTF("OK");
                        out.writeInt(b.length);
                        out.write(b);
                        System.out.println("number of bytes write:"+b.length);
                        out.flush();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        out.writeUTF("ERROR");
                        out.flush();
                    }
                } else if (str.equals("CLOSE"))
                {
                    break;
                }

                updateStatus("request for url:" + str);

            }
            in.close();
            out.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        try
        {
            conn.close();
            updateStatus("[server:] Finished connection");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e)
    {

        if ((e.getActionCommand()).equals("Start Server"))
        {
            startButton.setEnabled(false);
            mServerState = true; // set server state started
            startServer();

        }



        if ((e.getActionCommand()).equals("End Server"))
        {
            endButton.setEnabled(false);
            startButton.setEnabled(true);
            mServerState = false;
            //endServer();
            System.exit(0);

// endButton.setEnabled(true);

        }



    }

    public void updateStatus(String message)
    {
        textarea.append("\n" + message);
    }

    public static void main(String[] args)
    {
        new SPPlinkServer();
    }
}
