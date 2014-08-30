/*
 * Copyright 2014 aarestu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grayscale;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author aarestu
 */
public class Grayscale extends JPanel {

    public final static int GRAY_AVERAGING = 1;
    public final static int GRAY_LUMINOSITY = 2;
    public final static int GRAY_DESATURATION = 3;
    public final static int GRAY_MAX = 4;
    public final static int GRAY_MIN = 5;
    public final static int GRAY_RED = 6;
    public final static int GRAY_GREEN = 7;
    public final static int GRAY_BLUE = 8;
    
    private BufferedImage image;
    private BufferedImage imageGray;

    @Override
    public void paint(Graphics g) {
        g.drawImage(imageGray, 20, 20, this);
    }

    public Image convertToGray(BufferedImage image, int grayType) {
        this.image = image;
        imageGray = new BufferedImage(this.getImage().getWidth(), this.getImage().getHeight(), this.getImage().getType());

        for (int y = 0; y < this.getImage().getHeight(); y++) {
            for (int x = 0; x < this.getImage().getWidth(); x++) {
                Color color = new Color(this.getImage().getRGB(x, y));
                int gray;
                switch (grayType) {
                    case GRAY_AVERAGING:
                        //( R + G + B ) / 3
                        gray = (int)(color.getRed() + color.getGreen() + color.getBlue()) / 3;
                        break;
                    case GRAY_DESATURATION:
                        // (max(r,g,b) + min (r, g, b)) / 2
                        gray = (int) (
                                ( 
                                    Math.max(Math.max(color.getRed(), color.getGreen()),color.getBlue()) 
                                    + 
                                    Math.min(Math.min(color.getRed(), color.getGreen()),color.getBlue())
                                )
                                / 2);
                        break;
                    case GRAY_MAX:
                        // max(r,g,b)
                        gray = (int) Math.max(Math.max(color.getRed(), color.getGreen()),color.getBlue());
                        break;
                    case GRAY_MIN:
                        // min(r,g,b)
                        gray = (int) Math.min(Math.min(color.getRed(), color.getGreen()),color.getBlue());
                        break;
                    case GRAY_RED:
                        gray = color.getRed();
                        break;
                    case GRAY_GREEN:
                        gray = color.getGreen();
                        break;
                    case GRAY_BLUE:
                        gray = color.getBlue();
                        break;
                    default:
                        //case GRAY_LUMINOSITY:
                        //R * 0.2126 + G * 0.7152 + B * 0.0722
                        gray = (int)(color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722);
                        break;
                        
                }

                imageGray.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return imageGray;

    }

    public BufferedImage getImage() {
        return image;
    }

    public static void main(String[] args) {
        Grayscale grayscale = new Grayscale();

        String filename = "D:\\lena.jpg";
        if (args.length > 0) {
            filename = args[0];
        }

        File input = new File(filename);
        try {
            
            grayscale.convertToGray(ImageIO.read(input), Grayscale.GRAY_DESATURATION);
            
        } catch (IOException ex) {
            
            System.out.println("Tidak bisa membaca file \"" + filename + "\"\n" + ex.getMessage());
            return;
        }

        JFrame frame = new JFrame();
        
        frame.add(grayscale);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(grayscale.getImage().getWidth() + 60, grayscale.getImage().getHeight() + 80);
        frame.setVisible(true);
    }
}
