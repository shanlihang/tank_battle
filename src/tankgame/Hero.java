package tankgame;

import java.util.Vector;

//玩家坦克
public class Hero extends Tank{
    Shot shot = null;
    public Hero(int x, int y) {
        super(x, y);
    }
    Vector<Shot> shots  = new Vector<>();
    public void shotEnemyTank(){
        if(shots.size() == 5){//设置同一时间最多发射5颗子弹
            return;
        }
        switch (getDirect()){

            case 0://向上
                shot = new Shot(getX() + 20,getY(),0);
                break;
            case 1://向右
                shot = new Shot(getX() + 60,getY() + 20,1);
                break;
            case 2://向下
                shot = new Shot(getX() + 20,getY() + 60,2);
                break;
            case 3://向左
                shot = new Shot(getX(),getY() + 20,3);
                break;
        }
        shots.add(shot);
        new Thread(shot).start();
    }
}
