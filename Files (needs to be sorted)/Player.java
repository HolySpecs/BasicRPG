import java.util.ArrayList;
//make abstract
public abstract class Player extends Character implements LevelUp{
    //add private attribute
    private int level, expLimit;
    private int pastLevel;
   
    //maybe change magic, mana depending on class
    public Player(String name, int attack, int def, int health,int agility,int exp, int level){
        super(name, attack, def, health,agility, exp);
        this.level = level;
        expLimit = 10;
    }
    
    //getters
    public int getLevel(){return level;} 
    public int getExpLimit(){return expLimit;}
    public int getPastLevel(){return pastLevel;}
    
    //setters
    public void setLevel(int newLevel){level = newLevel;} 
    public void setExpLimit(int newExpLimit){expLimit = newExpLimit;}
    public void setPastLevel(int newLevel){pastLevel = newLevel;}
    
    //Check if can level up (must be defined in subclass)
    public abstract void checkLevelUp();

    //makes sure health doesn't exceed maxHealth
    public void checkHealth(){     
        if (this.getHealth() > this.getMaxHealth()){
            this.setHealth(this.getMaxHealth());
        }
    }
    
    //perform ability (different in subclass)
    public boolean ability(ArrayList<Character> group){
        return true;
    }

    public boolean ability(Character target){
        return true;
    }
}