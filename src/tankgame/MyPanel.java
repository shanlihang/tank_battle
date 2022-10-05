package tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.Vector;

public class MyPanel extends JPanel  implements KeyListener,Runnable{
    //定义玩家坦克
    Hero hero = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();//存放爆炸图片
    int enemyTankSize = 3;

    //定义三个炸弹图片对象
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    public MyPanel(){
        hero = new Hero(100,100);
        hero.setSpeed(2);
        for (int i=0;i<enemyTankSize;i++){
            EnemyTank enemyTank = new EnemyTank((100 * (i+1)),0);
            enemyTank.setDirect(2);
            new Thread(enemyTank).start();
            Shot shot = new Shot(enemyTank.getX() + 20,enemyTank.getY() + 60,enemyTank.getDirect());
            enemyTank.shots.add(shot);
            new Thread(shot).start();
            enemyTanks.add(enemyTank);
        }
        //初始化图片
        image1 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_3.gif");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1000,750);//填充矩形
        drawTank(hero.getX(),hero.getY(),g,hero.getDirect(),0);
        if(hero.shot != null && hero.shot.isLive != false){
            g.draw3DRect(hero.shot.x,hero.shot.y,2,2,false);
        }
        for (int i = 0;i<bombs.size();i++){
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6){
                g.drawImage(image1,bomb.x,bomb.y,60,60,this);
            } else if (bomb.life > 3){
                g.drawImage(image2,bomb.x,bomb.y,60,60,this);
            } else {
                g.drawImage(image3,bomb.x,bomb.y,60,60,this);
            }
            bomb.lifeDown();
            if(bomb.life == 0){
                bombs.remove(bomb);
            }
        }
        for (int i=0;i<enemyTanks.size();i++){
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
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
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+30,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y);
                break;
            case 1://向右
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x,y+30,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x+60,y+20);
                break;
            case 2://向下
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+30,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y+60);
                break;
            case 3://向左
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x,y+30,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x,y+20);
                break;
            default:
                System.out.println(123);
        }
    }

    //判断子弹是否击中
    public void hitTank(Shot s,EnemyTank enemyTank){
        switch (enemyTank.getDirect()){
            case 0:
            case 2:
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40 && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    Bomb bomb = new Bomb(enemyTank.getX(),enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60 && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    Bomb bomb = new Bomb(enemyTank.getX(),enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            hero.moveLeft();
        }
        if(e.getKeyCode() == KeyEvent.VK_J){
            hero.shotEnemyTank();
        }
        //重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //判断是否击中敌人坦克
            if ( hero.shot != null && hero.shot.isLive){
                for (int i=0;i<enemyTanks.size();i++){
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(hero.shot,enemyTank);
                }
            }
            this.repaint();
        }
    }
}
