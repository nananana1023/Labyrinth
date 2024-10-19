package labyrinth;

import java.sql.*;
import java.util.*;

public class Database {
    private final int maxScores = 10;
    private Statement stmt;
    private PreparedStatement insertStatement;
    private PreparedStatement deleteStatement;
    private Connection connection;

    /**
     * Constructs a Database object, establishing a connection to the database with the specified properties.
     * Initializes prepared statements for inserting and deleting high scores.
     *
     * @throws SQLException if a database access error occurs or the url is null.
     */
    public Database() throws SQLException{    
        
        Properties connectionProps = new Properties(); 
        connectionProps.put("user", "root");
        connectionProps.put("password", "Alltoowell8");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/Labyrinth";

        connection = DriverManager.getConnection(dbURL, connectionProps);
        
        String insertQuery = "INSERT INTO HIGHSCORES (NAME, LEVEL, TIME) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String score = "1; DELETE FROM HIGHSCORES;";
        String deleteQuery = "DELETE FROM HIGHSCORES WHERE LEVEL=? AND TIME=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

     /**
     * Retrieves a list of high scores from the database, ordered by level in descending order.
     * Each high score is encapsulated in a Highscore object.
     *
     * @return a list of Highscore objects representing the high scores.
     * @throws SQLException if a database access error occurs or the SQL statement is incorrect.
     */
    public ArrayList<Highscore> getHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES ORDER BY LEVEL DESC";
        ArrayList<Highscore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("name");
            int level = results.getInt("level");
            double time = results.getDouble("time");
            highScores.add(new Highscore(name, level, time));
        }
        return highScores;
    }
    
     /**
     * Provides the names of the columns in the high scores table for UI display purposes.
     *
     * @return an array of Strings representing the names of the columns in the high scores table.
     */
    public String[] getColumnNamesArray (){
        String[] columnNames = {"#", "NAME", "COMPLETED     LEVELS", "TIME"};
        return columnNames;
    }
    
    /**
     * @return a two-dimensional String array containing the high score data.
     * @throws SQLException if a database access error occurs while fetching high scores.
     */
    public String[][] getDataMatrix () throws SQLException{
        String[][] columnNames = new String[10][4];
        ArrayList<Highscore> highscores = getHighScores();
        int cnt = 0;
        for(Highscore hs : highscores){
            columnNames[cnt][0] = String.valueOf(cnt+1); //id
            columnNames[cnt][1] = hs.getName();
            columnNames[cnt][2] = String.valueOf(hs.getLevel()); //current level 
            columnNames[cnt][3] = String.valueOf(hs.getTime()) + " seconds"; //timestamp 
            cnt++;
        }
        for(;cnt < 10; cnt++){
            columnNames[cnt][0] = String.valueOf(cnt+1);
            columnNames[cnt][1] = "";
            columnNames[cnt][2] = "";
            columnNames[cnt][3] = "";
        }
        return columnNames;
    }

     /**
     * Inserts a new high score into the database if the number of high scores is less than the maximum.
     * If the maximum is reached, it replaces the lowest high score if the new score is higher.
     *
     * @param name the name of the player.
     * @param level the level the player reached.
     * @param time the time taken by the player.
     */
    public void putHighScore(String name, int level, double time) {
        try {
            ArrayList<Highscore> highScores = getHighScores();
            if (highScores.size() < maxScores) {
                insertScore(name, level, time);
            }
            else {
                int leastLevel = highScores.get(highScores.size() - 1).getLevel();
                double leastTime = highScores.get(highScores.size() - 1).getTime();
                if (leastLevel < level || (leastLevel == level && leastTime > time) ) {
                    deleteScores(leastLevel, leastTime);
                    insertScore(name, level, time);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Runs SQL commands to insert a new entry into the database 
     */
    private void insertScore(String name, int level, double time) throws SQLException {
            insertStatement.setString(1, name);
            insertStatement.setInt(2, level);
            insertStatement.setDouble(3, time);
            insertStatement.executeUpdate();
    }
    
    /**
     * delete an entry from the database 
     */
    private void deleteScores(int level, double time) throws SQLException {
        deleteStatement.setInt(1, level);
        deleteStatement.setDouble(2, time);
        deleteStatement.executeUpdate();
    }
    /**
     *  Removes all entries from the high scores table and creates a new table with the same name and structure.
     */
    public void emptyTheTable(){
        try {
            stmt.execute("DROP TABLE HIGHSCORES");
            stmt.execute("CREATE TABLE HIGHSCORES(NAME VARCHAR(20), LEVEL INT, TIME DOUBLE)");
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }
    
}