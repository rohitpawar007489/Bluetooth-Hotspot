package btserver;

/*For creating server side*/
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;

class NetworkHandler
{


    static public Response getResponse(String surl)//throws  MalformedURLException,IOException
    {

        Response r = new Response();
        try
        {

            URL  url = new URL(surl);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i;
            byte[] b = new byte[10240];
            while ((i = is.read(b)) != -1)
            {
                baos.write(b, 0, i);
            }

            r.contentType = ucon.getContentType();
            r.data = baos.toByteArray();

            if (r.contentType.toLowerCase().startsWith("image/"))
            {
                BufferedImage bi = ImageIO.read(new ByteArrayInputStream(r.data));
                Image si = scale(bi, 240, 320);
                RenderedImage ri=(RenderedImage)bi;
                //Object o=si;
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpeg", baos2);
                
                r.data = baos2.toByteArray();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            r.contentType="text/html";
            r.data=("<html><body>Error"+e.getMessage()+"</body></html>").getBytes();
        }
        return r;
    }

    public static Image scale(Image bi, int w, int h)
    {
        int iw = bi.getWidth(null);
        int ih = bi.getHeight(null);


        if (iw > ih)
        {
            h = (ih * w / iw);
        } else
        {
            w = (iw * h / ih);
        }
        return bi.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }

    public static void main(String args[]) throws IOException
    {
        Response r = getResponse("http://www.google.com");
        System.out.println("Data is:" + r.data);
        System.out.println("Content :" + r.contentType);

    }
}



