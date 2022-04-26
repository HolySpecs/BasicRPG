import java.util.Random;

public final class Warrior extends Player{
    //turn rage into pure damage
    private int rage, maxRage;
    private int pastMaxRage;

    public Warrior(String name){        //constructor
        //name, attack, defence, health, agility, exp, level
        super(name, 7, 10, 200, 5, 0, 1);
        rage = 0;
        maxRage = 50;
    }
    
    //getters
    public int getRage(){return rage;}
    public int getMaxRage(){return maxRage;}
    public int getPastMaxRage(){return pastMaxRage;}    
    
    //setters
    public void setRage(int newRage){rage = newRage;}
    public void setMaxRage(int newMaxRage){maxRage = newMaxRage;}
    public void setPastMaxRage(int newMaxRage){pastMaxRage = newMaxRage;}

    //make sure rage doesn't overflow
    public void checkRage(){if (rage > maxRage){rage = maxRage;}}

    //check if character has enough exp to level up
    public void checkLevelUp(){ 
        if (this.getExp() > this.getExpLimit()){
            this.setExpLimit(this.getExpLimit() + (10*this.getLevel()));    //increase exp limit for next level
            int newAttack, newDef, newMaxHealth, newAgility, newMaxRage;    //increase in stats being random
            int oldLevel, oldAtk, oldDef, oldMaxH, oldAgi, oldMaxRge;

            newAttack = (new Random()).nextInt(5) + 1;
            newDef = (new Random()).nextInt(5) + 3;
            newMaxHealth = (new Random()).nextInt(10) + 5; 
            newAgility = (new Random()).nextInt(3) + 1; 
            newMaxRage = (new Random()).nextInt(5);
            
            oldLevel = this.getLevel();
            oldAtk = this.getAttack();
            oldDef = this.getDef();
            oldMaxH = this.getMaxHealth();
            oldAgi = this.getAgility();
            oldMaxRge = this.getMaxRage();

            this.setPastLevel(oldLevel);
            this.setPastAttack(oldAtk);
            this.setPastDef(oldDef);
            this.setPastMaxHealth(oldMaxH);
            this.setPastAgility(oldAgi);
            this.setPastMaxRage(oldMaxRge);

            //apply changes
            this.setLevel(oldLevel + 1); //increase level by 1
            this.setAttack(oldAtk + newAttack);
            this.setDef(oldDef + newDef);
            this.setMaxHealth(oldMaxH + newMaxHealth);
            this.setAgility(oldAgi + newAgility);
            maxRage += newMaxRage;
        }
    }

    public boolean ability(Enemy enemy){
        if (this.getRage() >= 0){
            int damage = (this.getAttack() + (this.getRage() * 5) - (enemy.getDef() / 4));
            enemy.setHealth(enemy.getHealth() - damage);
            //check if dead
            if (enemy.getHealth() <= 0){
                enemy.setStatus("Dead");
            }
            //decrease Rage
            this.setRage(0);
            return true;
        }
        else{
            return false;
        }
    }
    
    //prints status of one character
    public void printStatus(Character obj){
        System.out.print(obj.getName() + ": "+ "Health: " + obj.getHealth() + "/" + obj.getMaxHealth());
        System.out.print("\n");
    }
}