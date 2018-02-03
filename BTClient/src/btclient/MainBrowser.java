package btclient;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.GridLayout;
import javax.microedition.rms.RecordStoreException;

public class MainBrowser extends Form implements ActionListener
{

    final Button button1;
    final Button button2;
    final Button button3;
    String urltxt;
    Container buttonbar;
    Command exit;
    //Dimension d;

    public MainBrowser()
    {

        super("MY BROWSER");
        //     Display.init(this);
        buttonbar = new Container(new GridLayout(4, 1));
        button1 = new Button("Enter URL...");
        button2 = new Button("BookMarks...");
        button3 = new Button("History...");
        exit = new Command("EXIT");
        //Browser.browser.show();
        MainBrowserThread mbt = new MainBrowserThread();
        mbt.start();

//init the LWUIT Display







//        show();



    }

    class MainBrowserThread extends Thread
    {

        public void run()
        {
            //d=new Dimension(150, 100);
            //  button1.setSize(d);
            // button2.setSize(new Dimension(150,50));
            buttonbar.addComponent(button1);
            buttonbar.addComponent(button2);
            buttonbar.addComponent(button3);
            addCommand(exit);
            addComponent(buttonbar);
            button1.addActionListener(MainBrowser.this);
            button2.addActionListener(MainBrowser.this);
            button3.addActionListener(MainBrowser.this);
            addCommandListener(MainBrowser.this);



        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        //throw new UnsupportedOperationException("Not supported yet.");


        if (ae.getSource() == button1)
        {
            Browser.urlinput.show();
        } else if (ae.getSource() == button2)
        {
            Browser.bookmarks.show();

        } else if (ae.getSource() == button3)
        {
            Browser.history.show();
        } else if (ae.getSource() == exit)
        {
            try
            {
                Browser.bookmarks.records.closeRecordStore();
                Browser.history.records.closeRecordStore();
            } catch (RecordStoreException ex)
            {
                ex.printStackTrace();
            }
            BTClient.instance.destroyApp(true);
        }
    }
}
