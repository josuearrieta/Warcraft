/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warcraft.UI;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Xelop
 */
public class BoatInterface {
    String _CurrentLife;
    Point _Coordenates;
    JLabel _Label;
    JLabel _MainLabel;
    double _CurrentAngle; //0: initial position, vertical boat. Values change + or - from there by decimals EX: <-- -0.1/0.1 --->
    OceanInterface _Screen;
    
    public BoatInterface(JLabel pLabel, OceanInterface pOcean, Point pCoordenates){
        _CurrentAngle = 0;
        _CurrentLife = "boat_secondhit.png";
        _MainLabel = pLabel;
        _Label = new JLabel();
        _Label.setSize(40, 100);
        _Screen = pOcean;
        ImageIcon newImage = new ImageIcon(createTransformedImage(_CurrentAngle, _CurrentLife,_Label));
        _Label.setIcon(newImage);
        _MainLabel.add(_Label);
        _Label.setLocation(pCoordenates);
        rotateBoat(5);
        shoot(400);
        
    }
    public BufferedImage createTransformedImage(double pAngle, String pFile, JLabel pLabel) { //radians
        try {
            URL resource = BoatInterface.class.getResource("/warcraft/Images/"+pFile); //url image in package
            BufferedImage image = ImageIO.read(resource); //type buffered image for rotation
            
            double sin = Math.abs(Math.sin(pAngle)); //value of the geometric circumference
            double cos = Math.abs(Math.cos(pAngle)); //value of the geometric circumference
            int width = image.getWidth();
            int heigth = image.getHeight();
            int newWidth = (int) Math.floor(width * cos + heigth * sin); //new value from the moved positions of th angle
            int newHeigth = (int) Math.floor(heigth * cos + width * sin);
            pLabel.setSize(newWidth, newHeigth);
            
            BufferedImage result = new BufferedImage(newWidth, newHeigth, Transparency.TRANSLUCENT); //creates the new image space
            Graphics2D g2d = result.createGraphics(); //gets graphics of the image space
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //value of the antialisng of the image manipulation, reducing strss on edges
            g2d.translate((newWidth - width) / 2, (newHeigth - heigth) / 2); //distances to translate of the picture along the axis (x,y)
            g2d.rotate(pAngle, width / 2, heigth / 2);//rotates and translates the image again
            g2d.drawRenderedImage(image, null); //sets the picture
            g2d.dispose(); //deletes graphics
            
            return result;
        } catch (IOException ex) {
            Logger.getLogger(BoatInterface.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public void rotateBoat(double pAngle){
        while(_CurrentAngle<pAngle){
            pAngle-=0.1;
            if(pAngle<0)
                _CurrentAngle += pAngle +0.1; //0.1 alambrado!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            else
                _CurrentAngle+=0.1;
            
            ImageIcon newImage = new ImageIcon(createTransformedImage(_CurrentAngle,_CurrentLife,_Label));
            _Label.setIcon(newImage);
            //falta algun tipo de stop
        }
    }
    public void moveBoat(int pPixels){
        double currentCos = 0;
        double currentSen = 0;
        while(pPixels > 0){
            currentCos += Math.cos(_CurrentAngle);
            currentSen += Math.sin(_CurrentAngle);
            if(currentCos >= 1){
                currentCos-=1;
                _Label.setLocation(_Label.getLocation().x, _Label.getLocation().y-1); //invertd logic
                pPixels-=1;
            }else if(currentCos <= -1){
                currentCos+=1;
                _Label.setLocation(_Label.getLocation().x, _Label.getLocation().y+1); //invertd logic
                pPixels-=1;
            }
            if(currentSen >= 1){
                currentSen -= 1;
                _Label.setLocation(_Label.getLocation().x+1, _Label.getLocation().y); //invertd logic
                pPixels-=1;
            }else if(currentSen <= -1){
                currentSen += 1;
                _Label.setLocation(_Label.getLocation().x-1, _Label.getLocation().y); //invertd logic
                pPixels-=1;
            }
            
        }
        
    }
    public void shoot(double pTime){
        JLabel shoot = new JLabel();
        shoot.setIcon(new ImageIcon(createTransformedImage(_CurrentAngle, "torpedo.png",shoot)));
        _MainLabel.add(shoot);
        shoot.setLocation(_Label.getLocation());
        
        double currentCos = 0;
        double currentSen = 0;
        while(pTime > 0){
            currentCos += Math.cos(_CurrentAngle);
            currentSen += Math.sin(_CurrentAngle);
            if(currentCos >= 1){
                currentCos-=1;
                shoot.setLocation(shoot.getLocation().x, shoot.getLocation().y-1); //invertd logic
                pTime-=1;
            }else if(currentCos <= -1){
                currentCos+=1;
                shoot.setLocation(shoot.getLocation().x, shoot.getLocation().y+1); //invertd logic
                pTime-=1;
            }
            if(currentSen >= 1){
                currentSen -= 1;
                shoot.setLocation(shoot.getLocation().x+1, shoot.getLocation().y); //invertd logic
                pTime-=1;
            }else if(currentSen <= -1){
                currentSen += 1;
                shoot.setLocation(shoot.getLocation().x-1, shoot.getLocation().y); //invertd logic
                pTime-=1;
            }
            
        }
    }
    
    
    
}