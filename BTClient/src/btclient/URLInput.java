package btclient;

import com.sun.lwuit.Button;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BorderLayout;
import java.lang.String.*;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.GridLayout;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class URLInput extends Form implements ActionListener
{

    final CheckBox cb;
    final TextField text;
    Label lbl;
    String urltext;
    RecordStore records;
    final Button go;
    final Button back;
    //final Button back;
    Container cp;

    public URLInput()
    {


        super("MY URL");
        setLayout(new BorderLayout());
        cp = new Container();
        cp.setLayout(new GridLayout(4, 1));
        lbl = new Label("URL:");
        text = new TextField();
        text.setText("http://www.google.com");
        back = new Button("BACK");

        // cp.addComponent(txt);
        cb = new CheckBox("Add To BookMarks");

        //urltext = text.getText();
        go = new Button("GO..");

//        try
//        {
//            records = RecordStore.openRecordStore("history", true);
//        } catch (RecordStoreException ex)
//        {
//            ex.printStackTrace();
//        }
        URLInputThread uit = new URLInputThread();
        uit.start();

        //show();
    }

    class URLInputThread extends Thread implements ActionListener
    {

        public void run()
        {
            cp.addComponent(lbl);
            cp.addComponent(text);
            cp.addComponent(go);
            cp.addComponent(back);
            cp.addComponent(cb);
            addComponent(BorderLayout.CENTER, cp);
            go.addActionListener(this);
            //cb.addActionListener(this);
            back.addActionListener(this);

        }

        public void actionPerformed(ActionEvent ae)
        {
            // throw new UnsupportedOperationException("Not supported yet.");
            //Browser.browser.show();
           // Browser.browser.navigate("");
            urltext = text.getText();


            if (ae.getSource() == go)
            {
                if (cb.isSelected())
                {
                    try
                    {
                        System.out.println("Adding :" + urltext + " to bookmarks");
                        Browser.bookmarks.addBookMark(urltext);
                    } catch (RecordStoreException ex)
                    {
                        ex.printStackTrace();
                    }
                } else
                {
                }
                Browser.browser.navigate(urltext);
                Browser.browser.show();
            }
            if (ae.getSource() == back)
            {
                Browser.mainbrowser.show();
            }
        }
    }/*

    public Vector getHistory() throws RecordStoreException
    {
//        String str1 = urltext;
//        records.addRecord(str1.getBytes(), 0, str1.length());

        Vector v = new Vector();
        RecordEnumeration en = records.enumerateRecords(null, null, false);

        while (en.hasNextElement())
        {
            byte[] b = en.nextRecord();

            if (b == null)
            {
                continue;
            }
            String str = new String(b);
            //int index=str.indexOf( ':');
            //v.addElement(new String[]{str.substring(0,index),str.substring(index+1)});
            v.addElement(str);
        }
        en.destroy();
        return v;

    }

    public void deleteHistory() throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
    {
        try
        {
            records.closeRecordStore();
            RecordStore.deleteRecordStore("history");

            records = RecordStore.openRecordStore("history", true);
        } catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }
//        // Vector v=new Vector();
//        RecordEnumeration en = records.enumerateRecords(null, null, false);
//        while (en.hasNextElement())
//        {
//            int recordid = en.nextRecordId();
//            String str = new String(records.getRecord(recordid));
//            // int index=str.indexOf( ':');
//            //String rname=str.substring(0,index);
//            //if(rname.equals(name)) records.deleteRecord(recordid);
//                    }
//        en.destroy();
//        //return v;
    }*/

    public void actionPerformed(ActionEvent ae)
    {
        //if go
    }
}
