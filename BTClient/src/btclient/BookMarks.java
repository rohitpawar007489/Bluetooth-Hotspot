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
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class BookMarks extends Form implements ActionListener
{

    RecordStore records;
    final Button open;
    final Button back;
    final List list;
    final Button delete;
    Vector bookmarks;
    Container cp;

    BookMarks()
    {
        super("BOOKMARKS....");

        try
        {

            try
            {
                records = RecordStore.openRecordStore("bookmarks", true);
            } catch (RecordStoreException ex)
            {
                ex.printStackTrace();
            }

            System.out.println("READING BOOKMARKS");
            bookmarks = getAllBookMarks();
            Vector bnames = new Vector();
            for (int i = 0; i < bookmarks.size(); i++)
            {
                bnames.addElement(((String[]) bookmarks.elementAt(i))[0]);
            }
            list = new List(bnames);
            cp = new Container();
            open = new Button("OPEN");
            back = new Button("BACK");
            delete = new Button("DELETE");
            cp.setLayout(new GridLayout(4, 1));
            
            BookMarksThread bmt = new BookMarksThread();
            bmt.start();


        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    class BookMarksThread extends Thread implements ActionListener
    {

        public void run()
        {
            cp.addComponent(list);
            cp.addComponent(open);
            cp.addComponent(delete);
            cp.addComponent(back);
            addComponent(cp);
            open.addActionListener(this);
            delete.addActionListener(this);
            back.addActionListener(this);
            list.addActionListener(this);

        }

        public void actionPerformed(ActionEvent ae)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
            if (ae.getSource() == delete)
            {

                try
                {

                    deleteBookMark((String) list.getSelectedItem());
                } catch (RecordStoreNotOpenException ex)
                {
                    ex.printStackTrace();
                } catch (RecordStoreException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (ae.getSource() == open)
            {
                try
                {
                    int index = list.getSelectedIndex();
                    System.out.println("SELECTED INDEX : " + index);
                    if (index != -1)
                    {
                        String url = ((String[]) bookmarks.elementAt(index))[1];
                        Browser.browser.show();
                        Browser.browser.navigate(url);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    //ignore
                }
            }
            if (ae.getSource() == back)
            {
                Browser.mainbrowser.show();
            }
        }
    }
    /*
    public void addBookMark(String name, String url) throws RecordStoreException {
    String str = name + ":" + url;
    records.addRecord(str.getBytes(), 0, str.length());
    }*/

    public void addBookMark(String url) throws RecordStoreException
    {
        String str = url;
        records.addRecord(str.getBytes(), 0, str.length());

    }

    public Vector getAllBookMarks() throws RecordStoreNotOpenException, RecordStoreException
    {
        Vector v = new Vector();
        RecordEnumeration en = records.enumerateRecords(null, null, false);

        while (en.hasNextElement())
        {
            String str = new String(en.nextRecord());
            System.out.println("BOOKMARK : '" + str + "'");
            
            v.addElement(str);//.substring(0, index), str.substring(index + 1));
        }
        en.destroy();
        System.out.println("READ :" + v.size() + " BOOKMARKS");
        return v;
    }

    public Vector deleteBookMark(String name) throws RecordStoreNotOpenException, RecordStoreException
    {
        Vector v = new Vector();
        RecordEnumeration en = records.enumerateRecords(null, null, false);
        while (en.hasNextElement())
        {
            int recordid = en.nextRecordId();
            String str = new String(records.getRecord(recordid));
            System.out.println("BOOKMARK : '" + str + "'");
            // int index = str.indexOf(':');
            //String rname = str.substring(0, index);
            if (str.equals(name))
            {
                records.deleteRecord(recordid);
            }
        }
        en.destroy();
        show();
        return v;
    }

    public void actionPerformed(ActionEvent ae)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() throws RecordStoreException
    {
        records.closeRecordStore();
    }

    public void show()
    {
        try
        {
            super.show();
            Vector v = getAllBookMarks();

            list.setModel(new DefaultListModel(v));
        } catch (RecordStoreNotOpenException ex)
        {
            ex.printStackTrace();
        } catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }

    }
}
