import java.util.Random;

public final class WhiteMage extends Player{
    private int mana, maxMana, magic, manaCost;
    private int pastMagic, pastMaxMana;

    public WhiteMage(String name){  //constructor
        //name, attack, defence, health, agility, exp, level
        super(name, 5, 7, 150, 5, 0, 1);
        magic = 10;
        mana = 100;
        maxMana = mana;
        manaCost = 7;
    }

    //getters
    public int getMana(){return mana;}  
    public int getMaxMana(){return maxMana;}
    public int getMagic(){return magic;}
    public int getManaCost(){return manaCost;}
    public int getPastMaxMana(){return pastMaxMana;}
    public int getPastMagic(){return pastMagic;}

    //setters
    public void setMana(int newMana){mana = newMana;}   
    public void setMaxMana(int newMaxMana){maxMana = newMaxMana;}
    public void setMagic(int newMagic){magic = newMagic;}
    public void setManaCost(int newManaCost){manaCost = newManaCost;}
    public void setPastMaxMana(int newMaxMana){pastMaxMana = newMaxMana;}
    public void setPastMagic(int newMagic){pastMagic = newMagic;}

    //make sure mana doesn't overflow
    public void checkMana(){if (mana > maxMana){mana = maxMana;}}

    public void checkLevelUp(){ //if exp > exp limit then level up
        if (this.getExp() > this.getExpLimit()){
            this.setExpLimit(this.getExpLimit() + (10*this.getLevel()));    //increase exp limit
            int newAttack, newDef, newMaxHealth, newAgility, newMagic, newMaxMana;  //random increments
            int oldLevel, oldAtk, oldDef, oldMaxH, oldAgi, oldMag, oldMaxMana;
            
            newAttack = (new Random()).nextInt(3);
            newDef = (new Random()).nextInt(3) + 1;
            newMaxHealth = (new Random()).nextInt(10) + 3; 
            newAgility = (new Random()).nextInt(3) + 1; 
            newMagic = (new Random()).nextInt(5) + 3; 
            newMaxMana = (new Random()).nextInt(5) + 5;
            
            oldLevel = this.getLevel();
            oldAtk = this.getAttack() ;
            oldDef = this.getDef();
            oldMaxH = this.getMaxHealth();
            oldAgi = this.getAgility();
            oldMag = this.getMagic();
            oldMaxMana = this.getMaxMana();

            this.setPastLevel(oldLevel);
            this.setPastAttack(oldAtk);
            this.setPastDef(oldDef);
            this.setPastMaxHealth(oldMaxH);
            this.setPastAgility(oldAgi);
            this.setPastMagic(oldMag);
            this.setPastMaxMana(oldMaxMana);

            //apply changes
            this.setLevel(oldLevel + 1); //increase level by 1
            this.setAttack(oldAtk+ newAttack);
            this.setDef(oldDef + newDef);
            this.setMaxHealth(oldMaxH + newMaxHealth);
            this.setAgility(oldAgi + newAgility);
            magic += newMagic;
            maxMana += newMaxMana;
            manaCost += 5;
        }
    }

    public boolean ability(Player member){
        if (this.getMana() >= this.getManaCost()){
            int amount = this.getMagic()*2;
            member.setHealth(member.getHealth() + amount);
            member.checkHealth();
            this.setMana(this.getMana() - this.getManaCost());
            return true;
        }
        else{
            return false;
        }
    }
}