package tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.security.Key;
import java.util.Vector;

public class MyPanel extends JPanel  implements KeyListener,Runnable{
    //定义玩家坦克
    Hero hero = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();
    Vector<Node> nodes = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();//存放爆炸图片
    int enemyTankSize = 3;

    //定义三个炸弹图片对象
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    public MyPanel(String key) throws IOException {
        File file = new File(Recorder.getRecordFile());
        if (file.exists()){
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("文件不存在，只能重新开启游戏");
            key = "1";
        }

        Recorder.setEnemyTanks(enemyTanks);
        hero = new Hero(700,100);
        hero.setSpeed(2);
        switch (key){
            case "1":
                for (int i=0;i<enemyTankSize;i++){
                    EnemyTank enemyTank = new EnemyTank((100 * (i+1)),0);
                    enemyTank.setDirect(2);
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20,enemyTank.getY() + 60,enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                for (int i=0;i<nodes.size();i++){
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(),node.getY());
                    enemyTank.setDirect(node.getDirect());
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20,enemyTank.getY() + 60,enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("输入有误，请重新上输入");
        }
        //初始化图片
        image1 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage("src/img/bomb_3.gif");

        //背景音乐
        new AePlayWave("src/music/111.wav").start();
    }

    public void showInfo(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您已累计击毁敌方坦克",1020,30);
        drawTank(1020,60,g,0,0);
        g.setColor(Color.RED);
        g.drawString(Recorder.getAllEnemyTankNum() + "",1080,100);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1000,750);//填充矩形
        showInfo(g);
        if (hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        }
//        if(hero.shot != null && hero.shot.isLive != false){
//            g.draw3DRect(hero.shot.x,hero.shot.y,2,2,false);
//        }
        for (int i=0;i<hero.shots.size();i++){
            Shot shot = hero.shots.get(i);
            if (shot != null && shot.isLive == true){
                g.draw3DRect(hero.shot.x,hero.shot.y,1,1,false);
            } else {
                hero.shots.remove(shot);
            }
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
    public void hitTank(Shot s,Tank enemyTank){
        switch (enemyTank.getDirect()){
            case 0:
            case 2:
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40 && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
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
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(),enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }
    public void hitEnemyTank(){
        for (int j=0;j<hero.shots.size();j++){
            Shot shot = hero.shots.get(j);
            if(shot != null && shot.isLive){
                for (int i=0;i<enemyTanks.size();i++){
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(hero.shot,enemyTank);
                }
            }
        }
    }
    public void hitHero(){
        for (int i=0;i<enemyTanks.size();i++){
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j=0;j<enemyTank.shots.size();j++){
                Shot shot = enemyTank.shots.get(j);
                if (hero.isLive && shot.isLive){
                    hitTank(shot,hero);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            if (hero.getX()>0) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            if (hero.getX() + 60 < 1000)
                hero.moveLeft();
        }
        if(e.getKeyCode() == KeyEvent.VK_J){
            //发射一颗子弹
//            if (hero.shot == null || !hero.shot.isLive) {
//                hero.shotEnemyTank();}
            //发射多颗子弹
            hero.shotEnemyTank();

        }
        //重绘
        hitEnemyTank();
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
            hitEnemyTank();
            hitHero();
            this.repaint();
        }
    }
}
