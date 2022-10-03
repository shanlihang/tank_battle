package tankgame;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    //定义玩家坦克
    Hero hero = null;
    public MyPanel(){
        hero = new Hero(100,100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1000,750);//填充矩形
        drawTank(hero.getX(),hero.getY(),g,0,0);
    }

    //绘制坦克
    public void drawTank(int x,int y,Graphics g,int direct,int type){
        switch (type){
            case 0://玩家坦克
                g.setColor(Color.cyan);
                break;
            case 1://敌人坦克
                g.setColor(Color.orange);
                break;
        }
        //根据据坦克的方向绘制坦克
        switch (direct){
            case 0://向上
                g.fill3DRect(hero.getX(),hero.getY(),10,60,false);
                g.fill3DRect(x+30,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y);
                break;
            default:
                System.out.println(123);
        }
    }
}
