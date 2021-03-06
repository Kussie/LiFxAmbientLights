/*
Created by Ben Kuskopf
MIT License
October 16 2015

 */

import static java.lang.System.exit;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Lights {

    public static void main(String[] args) {


        if (args.length < 1) {
            System.out.println("Improper usage.  Lights <token> <selector> <screen width> <screen height>");
            exit(0);
        }

        // Most of the below is left over, i couldn't figure out how to make default arguments for all of them all except the token.  And it didn't throw it's own error either.  I need more java skills
        // Token
        String lifxToken = "";
        if ( args[0].length()<0) {
            System.out.println("You must supply a token");
            exit(0);
         } else {
            lifxToken = args[0];
        }

        // Light selector
        String lifxSelector = "";
        if ( args.length < 2 || args[1].length()<0) {
        	lifxSelector = "all";
        } else {
            lifxSelector = args[1];
        }

        // Screen size
        int screenW = 1920;
        if ( args.length < 3 || args[2].length()<0) {
            screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
        } else {
            screenW = Integer.parseInt(args[2]);
        }

        int screenH = 1080;
        if ( args.length < 4 || args[3].length()<0) {
            screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
        } else {
            screenH = Integer.parseInt(args[3]);
        }

        // Colour selection box
        int boxW = 200;
        //if ( args[4].length()<0) {
        //    boxW = Integer.parseInt(args[4]);
        //}

        int boxH = 200;
        //if ( args[5].length()<0) {
        //    boxH = Integer.parseInt(args[5]);
        //}

        //How many pixels to skip while reading (the more you skip, it runs faster, but result might get worse)
        int pixelSkip = 2;
        //if ( args[6].length()<0) {
        //    pixelSkip = Integer.parseInt(args[6]);
        //}

        // Define the box where we want to capture and analyse colour from
        // The start point of the box (top and left coordinate)
        int boxL = (screenW+boxW)/2;
        int boxT = (screenH+boxH)/2;

        try {
            while(true) {
                long millis = System.currentTimeMillis();

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

                System.out.println("rgb: "+r+","+g+","+b);
                try {
                    URL url = new URL("https://api.lifx.com/v1/lights/"+lifxSelector+"/state");
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setDoOutput(true);
                    httpCon.setRequestMethod("PUT");
                    httpCon.setRequestProperty  ("Authorization", "Bearer "+lifxToken);
                    OutputStreamWriter out = new OutputStreamWriter(
                            httpCon.getOutputStream());
                    out.write("{\"color\": \"rgb:"+r+","+g+","+b+"\",\n" +
                            "  \"brightness\": 0.7,\n" +
                            "  \"duration\": 0.5}");
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
                    System.out.println("LiFX API error");
                    e.printStackTrace();
                    exit(0);
                }

                screenshot.flush();

                Thread.sleep(1000 - millis % 1000);
            }//While

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}






