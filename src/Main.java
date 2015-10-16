import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.*;
import java.awt.image.*;
import static java.lang.System.*;
import java.awt.Robot; //java library that lets us take screenshots
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.util.List;
import java.net.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        try {
            while(true) {
                long millis = System.currentTimeMillis();



                // Put the right screen size in here
                int screenW=2560;
                int screenH=1440;

                // Preview window size
                int windowW=100;
                int windowH=100;

                // Define the box where we want to capture and analyse colour from
// The start point of the box (top and left coordinate)
                int boxL=1025; // That's the middle of the screen
                int boxT=767;
                // Size of the box
                int boxW=100;
                int boxH=100;

                //How many pixels to skip while reading (the more you skip, it runs faster, but result might get worse)
                int pixelSkip=2;

                // Screen Area to be captured (usually the whole screen)
                Rectangle dispBounds;
                // creates object from java library that lets us take screenshots
                Robot bot;

                dispBounds=new Rectangle(new Dimension(screenW,screenH));

                bot=new Robot();

                int pixel;

                int r=0;
                int g=0;
                int b=0;

                // Take screenshot
                BufferedImage screenshot=bot.createScreenCapture(dispBounds);

                // Pass all the ARGB values of every pixel into an array
                int[]screenData=((DataBufferInt)screenshot.getRaster().getDataBuffer()).getData();

                //Find the RGB values of the region we want
                for(int i=boxT;i<(boxT+boxH);i+=pixelSkip){
                    for(int j=boxL;j<(boxL+boxW);j+=pixelSkip){

                        pixel=screenData[i*screenW+j];
                        r+=0xff&(pixel>>16);
                        g+=0xff&(pixel>>8);
                        b+=0xff&pixel;

                    }
                }

                // take average RGB values.
                r=r/(boxH/pixelSkip*boxW/pixelSkip);
                g=g/(boxH/pixelSkip*boxW/pixelSkip);
                b=b/(boxH/pixelSkip*boxW/pixelSkip);

        /*color rgb=color((short)r,(short)g,(short)b);
        fill(rgb);
        rect(0,0,100,100);

        println(frameRate);*/
                System.out.println(r);
                System.out.println(g);
                System.out.println(b);
                try {
                    URL url = new URL("https://api.lifx.com/v1/lights/all/state");
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestMethod("PUT");
                    httpCon.setRequestProperty  ("Authorization", "Bearer <token>");
                    OutputStreamWriter out = new OutputStreamWriter(
                            httpCon.getOutputStream());
                    out.write("{\"color\": \"rgb:"+r+","+g+","+b+"\",\n" +
                            "  \"brightness\": 0.8,\n" +
                            "  \"duration\": 1}");
                    out.close();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    httpCon.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null)
                        System.out.println(inputLine);
                    in.close();
                    System.out.println("Sent API Request");

                } catch (Exception e) {
                    e.printStackTrace();
                }


                Thread.sleep(1000 - millis % 1000);
            }//While






        }
        catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }

    }

}






