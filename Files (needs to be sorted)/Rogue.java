import java.util.Random;

public final class Rogue extends Player{
    private int energy, maxEnergy, energyCost, energyRegen, timesStruck;
    private int pastMaxEnergy, pastEnergyRegen;

    public Rogue(String name){  //constructor
        //name, attack, defence, health, agility, magic, mana, exp, level, manaCost
        super(name, 10, 4, 150, 10, 0, 1);
        energy = 70;
        maxEnergy = energy;
        energyCost = 3;
        energyRegen = 2;
        timesStruck = 1;
    }

    public int getEnergy(){return energy;}  //getters
    public int getMaxEnergy(){return maxEnergy;}
    public int getEnergyCost(){return energyCost;}
    public int getEnergyRegen(){return energyRegen;}
    public int getTimesStruck(){return timesStruck;}
    public int getPastMaxEnergy(){return pastMaxEnergy;}
    public int getPastEnergyRegen(){return pastEnergyRegen;}

    public void setEnergy(int newEnergy){energy = newEnergy;}   //setters
    public void setMaxEnergy(int newMaxEnergy){maxEnergy = newMaxEnergy;}
    public void setEnergyCost(int newEnergyCost){energyCost = newEnergyCost;}
    public void setEnergyRegen(int newEnergyRegen){energyRegen = newEnergyRegen;}
    public void setTimesStruck(int newTimesStruck){timesStruck = newTimesStruck;}
    public void setPastMaxEnergy(int newMaxEnergy){pastMaxEnergy = newMaxEnergy;}
    public void setPastEnergyRegen(int newEnergyRegen){pastEnergyRegen = newEnergyRegen;}

    //make sure energy doesn't overflow
    public void checkEnergy(){if (energy > maxEnergy){energy = maxEnergy;}}

    //level up if exp >= exp limit
    public void checkLevelUp(){ 
        if (this.getExp() > this.getExpLimit()){
            this.setExpLimit(this.getExpLimit() + (10*this.getLevel()));    //increase exp limit to next level
            int newAttack, newDef, newMaxHealth, newAgility, newMaxEnergy;  //random stat increments
            int oldLevel, oldAtk, oldDef, oldMaxH, oldAgi, oldMaxEn, oldEnReg;

            oldLevel = this.getLevel();
            oldAtk = this.getAttack();
            oldDef = this.getDef();
            oldMaxH = this.getMaxHealth();
            oldAgi = this.getAgility();
            oldMaxEn = this.getMaxEnergy();
            oldEnReg = this.getEnergyRegen();
            
            newAttack = (new Random()).nextInt(5) + 2;
            newDef = (new Random()).nextInt(3) + 1;
            newMaxHealth = (new Random()).nextInt(10) + 3; 
            newAgility = (new Random()).nextInt(5) + 2; 
            newMaxEnergy= (new Random()).nextInt(5) + 2; 
            
            this.setPastLevel(oldLevel);
            this.setPastAttack(oldAtk);
            this.setPastDef(oldDef);
            this.setPastMaxHealth(oldMaxH);
            this.setPastAgility(oldAgi);
            this.setPastMaxEnergy(oldMaxEn);
            this.setPastEnergyRegen(oldEnReg);

            //apply changes
            this.setLevel(oldLevel + 1); //increase level by 1
            this.setAttack(oldAtk + newAttack);
            this.setDef(oldDef + newDef);
            this.setMaxHealth(oldMaxH + newMaxHealth);
            this.setAgility(oldAgi + newAgility);
            maxEnergy += newMaxEnergy;
            energyCost += 3;
            energyRegen += 2;
        }
    }

    public boolean ability(Enemy enemy){
        if (this.getEnergy() >= this.getEnergyCost()){
            //perform the ability
            int count = 1;
            boolean complete = false;
            int damage = 0;
            
            //can strike at least 1 time and at most 5 times, chance depends on agility
            do{
                damage += this.getAttack() - (enemy.getDef() / 4);
                int randInt = (new Random()).nextInt(20+(5*(this.getLevel()-1)));
                if (randInt <= this.getAgility()){
                    count++;
                }
                else{
                    complete = true;
                }
            } while(!complete && count <= 5);

            if (count > 5){count = 5;}
            if (count == 0){count = 1;}
            this.setTimesStruck(count);
            //deals damage
            enemy.setHealth(enemy.getHealth() - damage);
            //checks if dead
            if (enemy.getHealth() <= 0){
                enemy.setStatus("Dead");
            }
            //decrease Energy
            this.setEnergy(this.getEnergy() - this.getEnergyCost());
            return true;
        }
        else{
            return false;
        }
    }
}