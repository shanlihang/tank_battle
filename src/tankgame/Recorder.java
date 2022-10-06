package tankgame;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Recorder {
    private static int allEnemyTankNum = 0;
    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static String recordFile = "src/myRecord.txt";
    private static Vector<EnemyTank> enemyTanks = null;

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }

    public static void addAllEnemyTankNum(){
        Recorder.allEnemyTankNum++;
    }

    public static void keepRecorder() throws IOException {
        bw = new BufferedWriter(new FileWriter(recordFile));
        bw.write(allEnemyTankNum + "\r\n");
        for (int i=0;i<enemyTanks.size();i++){
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.isLive){
                String record = enemyTank.getX() + " " + enemyTank.getY() +" "+ enemyTank.getDirect();
                bw.write(record + "\r\n");
            }
        }
        bw.close();
    }
}
