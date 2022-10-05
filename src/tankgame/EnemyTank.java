package tankgame;

import java.util.Vector;

public class EnemyTank extends Tank implements Runnable{
    Vector<Shot> shots = new Vector<>();
    boolean isLive = true;
    public EnemyTank(int x, int y) {
        super(x, y);
    }

    @Override
    public void run() {
        while (true){
            switch (getDirect()){
                case 0:
                    for (int i=0;i<30;i++){
                        moveUp();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i=0;i<30;i++){
                        moveRight();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i=0;i<30;i++){
                        moveDown();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    for (int i=0;i<30;i++){
                        moveLeft();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setDirect((int)(Math.random() * 4));
            if (!isLive){
                break;
            }
        }
    }
}
