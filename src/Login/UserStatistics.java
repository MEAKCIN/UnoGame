package Login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserStatistics {
    public HashMap getSimpleStatistics(){
        HashMap<String,ArrayList<Float>> scoreMap = new HashMap<>();
        File myObj = new File("src/data/logs.txt");
        try{
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] user = data.split(",");
                ArrayList<Float> userStats = new ArrayList<>();
                userStats.add(Float.parseFloat(user[1]));
                userStats.add(Float.parseFloat(user[2]));
                userStats.add(Float.parseFloat(user[3]));
                userStats.add(Float.parseFloat(user[4]));
                scoreMap.put(user[0],userStats);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return scoreMap;
    }
    public HashMap getAllStatistics(String username){
        HashMap<String,ArrayList<Float>> scoreMap = new HashMap<>();
        scoreMap= getSimpleStatistics();
        //username,winCount,loseCount,totalGameCount,score
        float score=scoreMap.get(username).get(3);
        float winCount=scoreMap.get(username).get(0);
        float loseCount=scoreMap.get(username).get(1);
        float totalGameCount=scoreMap.get(username).get(2);
        HashMap<String,ArrayList<Float>> allStats = scoreMap;
        allStats.get(username).add(averageScorePerGame(score,totalGameCount));//average score per game at index 4
        allStats.get(username).add(winLossRatio(winCount,loseCount));//win loss ratio at index 5
        return allStats;
    }
    public float averageScorePerGame(float score, float totalGameCount) {
        return score/totalGameCount;
    }
    public float winLossRatio(float winCount, float loseCount) {
        return winCount/loseCount;
    }









}
