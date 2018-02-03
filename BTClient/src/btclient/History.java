package btclient;

import com.sun.lwuit.Button;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class History extends Form implements ActionListener
{

    final Button clear;
    final Button back;
    final List list;
    Container c;
   RecordStore records;
    
    public History()
    {
        super("History...");
        

            clear = new Button("Clear All ");
            back = new Button("BACK");         
            list = new List();
        try
        {
            records = RecordStore.openRecordStore("history", true);
        } catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }
            c = new Container();
            c.setLayout(new GridLayout(2,1));
            HistoryThread ht = new HistoryThread();
            ht.start();

    }

    public void actionPerformed(ActionEvent ae)
    {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    class HistoryThread extends Thread implements ActionListener
    {

        public void run()
        {
            c.addComponent(list);
            c.addComponent(clear);
            c.addComponent(back);
            addComponent(c);
            list.addActionListener(this);
            clear.addActionListener(this);
            back.addActionListener(this);
        }

        public void actionPerformed(ActionEvent ae)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
            if (ae.getSource() == clear)
            {
                try
                {
                    Browser.history.deleteHistory();
                } catch (RecordStoreNotOpenException ex)
                {
                    ex.printStackTrace();
                } catch (InvalidRecordIDException ex)
                {
                    ex.printStackTrace();
                } catch (RecordStoreException ex)
                {
                    ex.printStackTrace();
                }
            }
            else if(ae.getSource() == list)
            {
                String url = (String) list.getSelectedItem();
                if (url != null)
                {
                    Browser.browser.show();
                    Browser.browser.navigate(url);
                }
            }
            else if(ae.getSource() == back)
            {
                Browser.mainbrowser.show();
            }
        }
    }
public Vector getHistory() throws RecordStoreException
    {
//        String str1 = Browser.urlinput.urltext;
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
            show();

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
    }

    public void show()
    {
        try
        {
            
            Vector temp = getHistory();
            list.setModel(new DefaultListModel(temp));
            super.show();
            
        } catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }
    }
}
