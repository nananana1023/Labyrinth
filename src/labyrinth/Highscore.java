package labyrinth;

public class Highscore implements Comparable<Highscore>{
    private final String name;
    public final int level;
    public final double time;
    
    public Highscore(String name, int level, double time){
        this.name = name;
        this.level = level;
        this.time = time;
    }
    
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + ": " + level + " level " + time + " seconds";
    }

    @Override
    public int compareTo(Highscore o) {
        int levelDif = -Integer.compare(this.level, o.level);
        int timeDif = (int) Double.compare(this.time, o.time);
        int nameDif = this.name.compareTo(o.name);
        if(levelDif != 0){
            return levelDif;
        }
        else if(timeDif != 0){
            return timeDif;
        }
        else return nameDif;
    }
}
