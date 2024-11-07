package Login;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class User {
    private String username ;
    private String password;
    public boolean isLogged;
    public int winCount;
    public int loseCount;
    public int totalGameCount;
    public int score;


    public void winMatch(int matchScore){
        winCount++;
        totalGameCount++;
        score+=matchScore;
        updateScore(username,winCount,loseCount,totalGameCount,score);
    }
    public void loseMatch(int matchScore){
        loseCount++;
        totalGameCount++;
        score-=matchScore;
        updateScore(username,winCount,loseCount,totalGameCount,score);
    }
    public void drawMatch(int matchScore){
        totalGameCount++;
        score+=matchScore;
        updateScore(username,winCount,loseCount,totalGameCount,score);
    }




    public void login(String username, String password) {
        try {
            File myObj = new File("src/data/user.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] user = data.split(",");
                if (user[0].equals(username) && user[1].equals(password)) {
                    isLogged = true;
                    System.out.println("User "+username+" Logged in successfully");
                    break;
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            File myObj = new File("src/data/logs.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] user = data.split(",");
                if (user[0].equals(username)) {
                    winCount = Integer.parseInt(user[1]);
                    loseCount = Integer.parseInt(user[2]);
                    totalGameCount = Integer.parseInt(user[3]);
                    score = Integer.parseInt(user[4]);
                    break;
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
    public boolean register(String username, String password) {
        try {
            File myObj = new File("src/data/user.txt");
            Scanner myReader = new Scanner(myObj);
            Boolean isExist = false;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] user = data.split(",");
                if (user[0].equals(username)) {
                    System.out.println("Username already exists");
                    isExist = true;

                    return false;
                }

            }
            myReader.close();
            Writer output = new BufferedWriter(new FileWriter(myObj, true));

            output.append("\n"+username + "," + password );
            output.close();
            updateScore(username, 0,0,0,0);//username,winCount,loseCount,totalGameCount,score
            System.out.println("User "+ username+" registered successfully");
            return true;

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public  void updateScore(String username, int winCount,int loseCount,int totalGameCount,int score) {
        File myObj = new File("src/data/logs.txt");
        List<String> fileContent = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(myObj);
            boolean updated = false;

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] user = data.split(",");
                if (user[0].equals(username)) {
                    // Assuming the score is in the second column
                    String updatedData = user[0] + "," + winCount+","+loseCount+","+totalGameCount+","+score;
                    fileContent.add(updatedData);
                    updated = true;
                    System.out.println("User "+username+" score is updated successfully");
                } else {
                    fileContent.add(data);
                }
            }
            myReader.close();

            // If the user does not exist, add them to the file
            if (!updated) {
                fileContent.add(username + "," + winCount+","+loseCount+","+totalGameCount+","+score);
            }

            // Write the new content back to the file
            FileWriter writer = new FileWriter(myObj);
            for (String str : fileContent) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("The file was not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }











    //Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLogged = false;
        HashMap scoreMap = new UserStatistics().getSimpleStatistics();
        ArrayList<Float> stats= (ArrayList) scoreMap.get(username);
        this.winCount = stats.get(0).intValue();
        this.loseCount = stats.get(1).intValue();
        this.totalGameCount = stats.get(2).intValue();
        this.score=stats.get(3).intValue();
    }
        // Getter and Setter
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
