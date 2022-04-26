public abstract class Character{
    //character stats
    //add private attribute
    private int attack, def, health, maxHealth,agility, exp;
    private String status, name;

    private int beforeAttk, beforeDef, beforeMaxHp, beforeAgi;
    
    public Character(String name, int attack, int def, int health, int agility, int exp){
        this.name = name;
        this.exp = exp;
        this.attack = attack;
        this.def = def;
        this.health = health;
        this.agility = agility;
        
        status = "Alive";
        maxHealth = health;
        
    }
    
    //getters
    public String getName(){return name;}
    public int getExp(){return exp;}
    public int getAttack(){return attack;}
    public int getDef(){return def;}
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public int getAgility(){return agility;}
    public String getStatus(){return status;}

    public int getPastAttack(){return beforeAttk;}
    public int getPastDef(){return beforeDef;}
    public int getPastMaxHealth(){return beforeMaxHp;}
    public int getPastAgility(){return beforeAgi;}
    
    //setters
    public void setName(String newName){name = newName;}
    public void setExp(int newExp){exp = newExp;}
    public void setAttack(int newAttack){attack = newAttack;}
    public void setDef(int newDef){def = newDef;}
    public void setHealth(int newHealth){health = newHealth;}
    public void setMaxHealth(int newMaxHealth){maxHealth = newMaxHealth;}
    public void setAgility(int newAgility){agility = newAgility;}
    public void setStatus(String newStatus){status = newStatus;}
    
    public void setPastAttack(int newAttack){beforeAttk = newAttack;}
    public void setPastDef(int newDef){beforeDef = newDef;}
    public void setPastMaxHealth(int newMaxHealth){beforeMaxHp = newMaxHealth;}
    public void setPastAgility(int newAgility){beforeAgi = newAgility;}
}