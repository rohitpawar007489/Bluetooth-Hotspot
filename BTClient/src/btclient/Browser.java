package btclient;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import com.sun.lwuit.html.HTMLComponent;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.rms.RecordStoreException;

public class Browser extends Form implements ActionListener
{

    public static Browser browser;
    public static URLInput urlinput;
    public static MainBrowser mainbrowser;
    public static BookMarks bookmarks;
    public static History history;
    //HttpRequestHandler handler=new HttpRequestHandler();
    HTMLComponent hc;
    Command back, exit;
    // final Button back;
    //final Button exit;

    Browser()
    {
        //    if(true) throw new RuntimeException();
        hc = new HTMLComponent(new BTDRH());

        hc.setIgnoreCSS(true);

        //hc.setIgnoreCSS(false);
        //hc.setShowImages(true);
        //hc.setCSSSupportedMediaTypes("handheld");
        back = new Command("BACK");
        exit = new Command("EXIT");

        setLayout(new BorderLayout());
        addCommand(back);
        addCommand(exit);
        addCommandListener(this);
        //setLayout(new GridLayout(1, 1));
        addComponent(BorderLayout.CENTER,hc);
        // hc.setHTML("<html><body>Hello</body></html>", "UTF-8", "Page1", true);


        /*
        //hc = new HTMLComponent();
        //setLayout(new GridLayout(1, 1));
        back=new Button("BACK");
        exit=new Button("EXIT");
        back.addActionListener(this);
        exit.addActionListener(this);

        addComponent(BorderLayout.NORTH,back);
        addComponent(BorderLayout.SOUTH,exit);
        addComponent(BorderLayout.CENTER,hc);



        hc.setHTML("<html><body>Hello</body></html>", "UTF-8", "Page1", true);

        if(true) return;

        //        Form f = new Form();
        //        f.setTitle("Server Browser ");
        //        f.setLayout(new BorderLayout());
        //        //f.addComponent(BorderLayout.NORTH, new Label("ADDRESS://"));

        //hc.setPage("http://www.google.com");

        // textArea =new TextArea(5, 20, TextArea.ANY);
        //textArea.setEditable(true);
        final Button button1 = new Button("Search Device");
        final Button button2 = new Button("Connect");
        final Button button3 = new Button("Exit");
        //        Container c = new Container();
        //        c.setLayout(new GridLayout(1, 2));
        //        c.addComponent(button1);
        //        c.addComponent(button2);
        //        c.addComponent(button3);
        //        addComponent(c);
        //   f.addComponent(BorderLayout.SOUTH, c);
        // f.addComponent(BorderLayout.CENTER, textArea);
        //textArea.setText("Hello");

        button1.addActionListener(new ActionListener()
        {

        public void actionPerformed(ActionEvent evt1)
        {
        button1.setText("Searching for Devices");
        }
        });

        button2.addActionListener(new ActionListener()
        {

        public void actionPerformed(ActionEvent evt2)
        {9
        button1.setText("Connecting....");
        }
        });
        button3.addActionListener(new ActionListener()
        {

        public void actionPerformed(ActionEvent evt3)
        {
        button2.setText("Exit from Browser");
        // System.exit(0);

        }
        });

        //show browser

         */
    }

    public void actionPerformed(ActionEvent ae)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        if (ae.getSource() == back)
        {

            Browser.mainbrowser.show();
        } else if (ae.getSource() == exit)
        {
            try
            {
                Browser.bookmarks.records.closeRecordStore();
                Browser.history.records.closeRecordStore();
                BTClient.instance.destroyApp(true);

            } catch (RecordStoreException ex)
            {
                ex.printStackTrace();
            }

        }
    }

    class BTDRH implements DocumentRequestHandler
    {
        //  Command c=new Command("back",BRB_OTHER, CENTER);

        public InputStream resourceRequested(DocumentInfo di)
        {

            try
            {
                //if(true) return new ByteArrayInputStream("<html><body>Test Message</body></html>".getBytes());
                System.out.println("RESOURCEREQUESTED : " + di.getFullUrl());
                //hc.setShowImages(true);
                //hc.setIgnoreCSS(false);
                /*         //
                if(di.getFullUrl().toLowerCase().endsWith(".css"))
                {
                return new ByteArrayInputStream(new byte[0]);
                }*/
                byte[] b = NetworkHandler.getInstance().fetchURL(di.getFullUrl());
                if (b != null)
                {
                    Dialog.show("RESP", new String(b), "OK", "Cancel");
                    System.out.println("RESPONSE: " + new String(b));
                    return new ByteArrayInputStream(b);
                } else
                {
                    //Dialog.show("RESP","NO RESP","OK","Cancel");
                    System.out.println("NO RESPONSE");
                    return new ByteArrayInputStream("<html><body>No Response</body></html>".getBytes());
                }
            } catch (Throwable ex)
            {
                //ex.printStackTrace();
                return new ByteArrayInputStream(("<html><body>Error</body></html>").getBytes());
            }
        }
    }

    public void navigate(String url)
    {
        hc.setPage(url);
        byte[] b = url.getBytes();
        try
        {
            Browser.history.records.addRecord(b, 0, url.length());
        } catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }

    }
}
