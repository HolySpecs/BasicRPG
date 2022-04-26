import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BattleWindow{
    //the window
    JFrame frame;
    //the contents
    JPanel cardPanel, battlePanel, selectTargetPanel, selectAllyPanel, abilityTargetPanel, levelUpPanel;
    CardLayout cardLayout;
    ArrayList<Player> party;
    ArrayList<Player> temp = new ArrayList<Player>();
    ArrayList<Enemy> enemies;
    ArrayList<Character> queue = new ArrayList<Character>();
    Character attacker;
    int turns = 0;
    int exp = 0;
    int roomsCleared;
    int memberPointer = 0;
    boolean turnComplete = false;
    boolean defending = false;
    String fileName;

    TextArea globalStatusArea, enemyTextArea, playerTextArea, levelUpTextArea;

    public BattleWindow(ArrayList<Player> party, ArrayList<Enemy> enemies, String fileName, int roomsCleared){
        this.party = party;
        temp.addAll(party);
        this.enemies = enemies;
        this.fileName = fileName;
        this.roomsCleared = roomsCleared;
        queue.addAll(party); queue.addAll(enemies);
        sortAgility(queue);

        frame = new JFrame("Battle");
        //so can switch between battle stages and selecting a target
        cardPanel = new JPanel();
        
        cardPanel.setLayout(new CardLayout(10,10));
        
        //make card panels
        battlePanelMaker(); selectTargetPanelMaker(); abilityTargetPanelMaker(); selectAllyPanelMaker(); levelUpPanelMaker();

        //add the panels to the cardlayout
        cardPanel.add(battlePanel, "Battle Phase");
        cardPanel.add(selectTargetPanel, "Select Target");
        //add card for ability
        cardPanel.add(abilityTargetPanel, "Ability Target Select");
        //add card for ally
        cardPanel.add(selectAllyPanel, "Select Ally");
        //add card to show level up results
        cardPanel.add(levelUpPanel, "Level Up");

        cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "Battle Phase");

        attacker = queue.get(turns%queue.size());
        globalStatusArea.append("It is " + attacker.getName() + "'s turn.\n");

        //add the card panel to the frame
        frame.add(cardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void battlePanelMaker(){
        //battle panel
        battlePanel = new JPanel();
        //the three columns are:
        //status
        //party and enemy health and energy
        //buttons for actions
        battlePanel.setLayout(new GridLayout(3, 1, 10, 10));

        //text area to show the actions and history
        globalStatusArea = new TextArea();
        globalStatusArea.setEditable(false);

        JPanel memberStatus = new JPanel();
        memberStatus.setLayout(new FlowLayout());
        //two text areas to show the health and energy of the players and the enemies
        enemyTextArea = new TextArea();
        playerTextArea = new TextArea();
        enemyTextArea.setEditable(false);
        playerTextArea.setEditable(false);

        ArrayList<Character> tempCharacters = new ArrayList<Character>();
        tempCharacters.addAll(party);
        tempCharacters.addAll(enemies);

        updateStatus(tempCharacters);

        memberStatus.add(enemyTextArea);
        memberStatus.add(playerTextArea);

        //button panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        //buttons being attack, use ability, defend
        String[] actions = {"Attack", "Ability", "Defend"};
        for (String action: actions){
            JButton button = new JButton(action);
            button.addActionListener(new PlayerActionListener());
            actionPanel.add(button);
        }

        //add the contents to the battle panel
        battlePanel.add(globalStatusArea);
        battlePanel.add(memberStatus);
        battlePanel.add(actionPanel);
    }

    public void selectTargetPanelMaker(){
        selectTargetPanel = new JPanel();
        selectTargetPanel.setLayout(new GridLayout(2,1,10,10));
        //select target panel for attacking
        JPanel targetButtonPanel = new JPanel();
        targetButtonPanel.setLayout(new FlowLayout());
        for (int i = 0; i < 4; i ++){
            JButton target;
            try{
                Enemy enemy = enemies.get(i);
                target = new JButton(enemy.getName());
                target.addActionListener(new TargetSelectListener());
            }
            catch(IndexOutOfBoundsException e){
                target = new JButton(" ");
            }
            targetButtonPanel.add(target);
        }
        selectTargetPanel.add(targetButtonPanel);

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new TargetSelectListener());
        backPanel.add(backButton);
        selectTargetPanel.add(backPanel);
    }

    public void abilityTargetPanelMaker(){
        abilityTargetPanel = new JPanel();
        //damaging ability target selct panel
        abilityTargetPanel.setLayout(new GridLayout(2,1,10,10));
        JPanel abilityTargetButtonPanel = new JPanel();
        abilityTargetButtonPanel.setLayout(new FlowLayout());
        for (int i = 0 ; i< 4; i ++){
            JButton target;
            try{
                Enemy enemy = enemies.get(i);
                target = new JButton(enemy.getName());
                target.addActionListener(new DamagingAbilityListener());
            }
            catch(IndexOutOfBoundsException e){
                target = new JButton(" ");
            }
            abilityTargetButtonPanel.add(target);
        }
        abilityTargetPanel.add(abilityTargetButtonPanel);

        JPanel backPanel1 = new JPanel();
        backPanel1.setLayout(new FlowLayout());
        JButton backButton1 = new JButton("Back");
        backButton1.addActionListener(new TargetSelectListener());
        backPanel1.add(backButton1);
        abilityTargetPanel.add(backPanel1);
    }

    public void selectAllyPanelMaker(){
        selectAllyPanel = new JPanel();
        selectAllyPanel.setLayout(new GridLayout(2,1,10,10));
        //select ally panel
        JPanel allyButtonPanel = new JPanel();
        allyButtonPanel.setLayout(new FlowLayout());
        for (int i = 0; i < 4; i ++){
            JButton target;
            try{
                Player player = party.get(i);
                target = new JButton(player.getName());
                target.addActionListener(new AllySelectListener());
            }
            catch (IndexOutOfBoundsException e){
                target = new JButton(" ");
            }
            allyButtonPanel.add(target);
        }
        selectAllyPanel.add(allyButtonPanel);

        JPanel backPanel2 = new JPanel();
        backPanel2.setLayout(new FlowLayout());
        JButton backButton2 = new JButton("Back");
        backButton2.addActionListener(new TargetSelectListener());
        backPanel2.add(backButton2);
        
        selectAllyPanel.add(backPanel2);
    }

    public void levelUpPanelMaker(){
        levelUpPanel = new JPanel();
        levelUpPanel.setLayout(new GridLayout(2,1,10,10));
        levelUpTextArea = new TextArea();
        levelUpTextArea.setEditable(false);
        levelUpPanel.add(levelUpTextArea);

        //add back buttons and next buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        String[] options = {"Back", "Next"};
        for (String option : options){
            JButton button = new JButton(option);
            button.addActionListener(new LevelUpButtonListener());
            buttonPanel.add(button);
        }

        levelUpPanel.add(buttonPanel);
    }

    public class PlayerActionListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            attacker = queue.get(turns%queue.size());
            
            if (attacker instanceof Player){ 
                if ((attacker.getStatus()).equals("Defending")){
                    attacker.setDef(attacker.getDef() /2);
                    attacker.setStatus("Alive");
                }

                if (attacker instanceof Rogue){
                    Rogue rogue = (Rogue) attacker;
                    rogue.setEnergy(rogue.getEnergy() + rogue.getEnergyRegen());
                    rogue.checkEnergy();
                    updateStatus(queue);
                }

                if (event.getActionCommand().equals("Attack")){
                    //look at target select listener for more details
                    cardLayout.show(cardPanel, "Select Target");
                }
                else if (event.getActionCommand().equals("Ability")){
                    if (attacker instanceof BlackMage){
                        ArrayList<Character> temp = new ArrayList<Character>();
                        temp.addAll(enemies);
                        boolean result = ((BlackMage) attacker).ability(temp);
                        if (!result){
                            globalStatusArea.append("You do not have enough mana!\n");
                        }
                        else{
                            int damage = ((BlackMage) attacker).getMagic() * 2;
                            globalStatusArea.append(String.format("%s performed Meteor Shower, doing %s damage to the enemy party\n", attacker.getName(), damage));
                            for (Enemy enemy : enemies){
                                ifDeadThenRemove(enemy);
                            }
                            turnComplete = true;
                        }
                    }
                    else if (attacker instanceof WhiteMage){
                        //look at the ally select panel for more 
                        cardLayout.show(cardPanel, "Select Ally");
                    }
                    else{
                        //look at the ability target select panel
                        cardLayout.show(cardPanel, "Ability Target Select");
                    }
                }
                else if (event.getActionCommand().equals("Defend")){
                    attacker.setDef(attacker.getDef() * 2);
                    attacker.setStatus("Defending");
                    globalStatusArea.append(attacker.getName() + " is defending\n");
                    turnComplete = true;
                    defending = true;
                }
            }
            else{
                turnComplete = true;
            }
            if (turnComplete){
                nextTurn();
            }
        }
    }

    //target an enemy to attack
    public class TargetSelectListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getActionCommand().equals("Back")){
                cardLayout.show(cardPanel, "Battle Phase");
                frame.pack();
            }
            //locate the target, then deal damage to selected target
            else{
                for (Enemy enemy : enemies){
                    if (event.getActionCommand().equals(enemy.getName())){
                        dealDamage(attacker, enemy);
                        if (enemies.size() == 0){
                            victory();
                        }
                        cardLayout.show(cardPanel, "Battle Phase");
                        turnComplete = true;
                        if (enemies.size() == 0){
                            victory();
                        }
                        nextTurn();
                        break;
                    }
                }
                updateStatus(queue);
            }
        }
    }

    //for warrior and rogue classes
    public class DamagingAbilityListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            for (Enemy enemy : enemies){
                if (event.getActionCommand().equals(enemy.getName())){
                    if (attacker instanceof Rogue){
                        boolean result = ((Rogue) attacker).ability(enemy);
                        if (result){
                            int timesStriked = ((Rogue) attacker).getTimesStruck();
                            int damage = (timesStriked * (attacker.getAttack() - (enemy.getDef() /4)));
                            globalStatusArea.append(String.format("%s hits %s time(s) \n%s deals %s damage to %s using Rapid Strike\n", attacker.getName(), timesStriked, attacker.getName(), damage, enemy.getName()));
                            ifDeadThenRemove(enemy);
                            turnComplete = true;
                            if (enemies.size() == 0){
                                victory();
                            }
                            nextTurn();
                        }
                        else{
                            globalStatusArea.append("You don't have enough energy for that!");
                        }
                    }
                    else{
                        boolean result = ((Warrior) attacker).ability(enemy);
                        if (result){
                            int damage = (attacker.getAttack() + (((Warrior)attacker).getRage() * 5) - (enemy.getDef() / 4));
                            globalStatusArea.append(String.format("%s used Empowered Strike to %s, dealing %s damage\n", attacker.getName(), enemy.getName(), damage));
                            ifDeadThenRemove(enemy);
                            turnComplete = true;
                            if (enemies.size() == 0){
                                victory();
                            }
                            nextTurn();
                        }
                        else{
                            globalStatusArea.append("You have no rage, just attack!");
                        }
                    }
                    break;
                }
            }
            cardLayout.show(cardPanel, "Battle Phase");
            frame.pack();
            updateStatus(queue);
        }
    }

    //select a target to use the ability
    public class AllySelectListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            for (Player player : party){
                if (event.getActionCommand().equals(player.getName())){
                    boolean result = ((WhiteMage) attacker).ability(player);
                    if (result){
                        int amount = ((WhiteMage) attacker).getMagic() * 2;
                        globalStatusArea.append(String.format("%s restored %s HP to %s\n", attacker.getName(), amount, player.getName()));
                        turnComplete = true;
                        nextTurn();
                    }
                    else{
                        globalStatusArea.append("You don't have enough mana for that!\n");
                    }
                    break;
                }
            }
            cardLayout.show(cardPanel, "Battle Phase");
            frame.pack();
            updateStatus(queue);
        }
    }

    public class LevelUpButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getActionCommand().equals("Back")){
                memberPointer --;
                if (memberPointer < 0){memberPointer = 0;}
            }
            else if (event.getActionCommand().equals("Next")){
                memberPointer ++;
                if (memberPointer >= party.size()){memberPointer = party.size()-1;}
            }
            showLevelChanges(party.get(memberPointer));
        }
    }

    public void nextTurn(){
        if (enemies.size() == 0){
            victory();
        }
        else if (party.size() == 0){
            defeat();
        }
        else{
            turns ++;
            attacker = queue.get(turns%queue.size());
            globalStatusArea.append("It is " + attacker.getName() + "'s turn.\n");
            if (attacker instanceof Enemy){
                int randInt = (new Random()).nextInt(party.size());
                Character target = party.get(randInt);
                dealDamage(attacker, target);
                //check for remaining party members
                if (party.size() == 0){
                    defeat();
                }
                turns ++;
                attacker = queue.get(turns%queue.size());
                globalStatusArea.append("It is " + attacker.getName() + "'s turn.\n");
            }
            
            updateStatus(queue);
            turnComplete = false;
        }
        
    }

    public void updateStatus(ArrayList<Character> group){
        enemyTextArea.setText("");
        playerTextArea.setText("");
        for (Character member : group){
            String message = "";
            message += member.getName() + ": " + member.getHealth() + "/" + member.getMaxHealth() + " ";
            if (member instanceof Enemy){
                message += "\n";
                enemyTextArea.append(message);
            }
            else{
                //special energies
                if (member instanceof Warrior){message += "Rage: " + ((Warrior) member).getRage() + "/" + ((Warrior) member).getMaxRage();}
                else if (member instanceof Rogue){message += "Energy: " + ((Rogue) member).getEnergy() + "/" + ((Rogue) member).getMaxEnergy() ;}
                else if (member instanceof BlackMage){message += "Mana: " + ((BlackMage) member).getMana() + "/" + ((BlackMage) member).getMaxMana();}
                else{message += "Mana: " + ((WhiteMage) member).getMana() + "/" + ((WhiteMage) member).getMaxMana();}
                message += "\n";
                playerTextArea.append(message);
            }
        }
    }

    public void sortAgility(ArrayList<Character> queue){ //looks at agility and sorts queue, highest first
        Character temp = null;
        for (int i = 0; i < queue.size() ; i ++){
            for (int j = i + 1; j < queue.size(); j ++){
                if ((queue.get(i)).getAgility() < (queue.get(j)).getAgility()){
                    temp = queue.get(i);
                    queue.set(i, queue.get(j));
                    queue.set(j, temp);
                }
            }
        }
    }

    //when the player or enemy attacks
    public void dealDamage(Character attacker, Character defender){
        int damage = attacker.getAttack() - (defender.getDef() / 4);
        if (damage < 0){
            damage = 0;
        }
        globalStatusArea.append(attacker.getName() + " did " + damage + " damage to " + defender.getName() + ".\n");
        defender.setHealth(defender.getHealth() - damage);

        //increase rage when taking damage
        if (defender instanceof Warrior){
            Warrior member = (Warrior) defender;
            member.setRage(member.getRage() + damage);
            member.checkRage();
        }

        if (defender.getHealth() <= 0){
            if (defender instanceof Enemy){
                exp += defender.getExp();
                enemies.remove(defender);
            }
            else{
                party.remove(defender);
            }
            queue.remove(defender);
        }
    }

    public boolean isFinished(){
        if (party.size() == 0 || enemies.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public void victory(){
        for (Player member : party){
            member.setExp(member.getExp() + exp);
            member.checkLevelUp();
            showLevelChanges(party.get(memberPointer));
            cardLayout.show(cardPanel, "Level Up");
        }
        //save here
        save();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void defeat(){
        System.out.println("Game Over");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //delete the save file
        Path path = Paths.get("save files");
        File file = new File(path.toAbsolutePath() + "\\" + fileName);
        file.delete();
        frame.dispose();
    }

    public void ifDeadThenRemove(Character entity){
        if ((entity.getStatus()).equals("Dead")){
            if (entity instanceof Enemy){
                exp += entity.getExp();
                enemies.remove(entity);
            }
            else if (entity instanceof Player){
                party.remove(entity);
            }
        }
    }

    public void showLevelChanges(Player player){
        levelUpTextArea.setText("Level Up!\n");
        String message = "";

        //each line has a stat change
        message += "Name: " + player.getName() + "\n";
        message += "Level: " + player.getPastLevel() + " -> " + player.getLevel() + "\n";
        message += "Max Health: " + player.getPastMaxHealth() + " -> "+ player.getMaxHealth() + "\n";

        //special energy limits
        if (player instanceof BlackMage){message += "Max Mana: " + ((BlackMage) player).getPastMaxMana() + " -> " + ((BlackMage) player).getMaxMana() + "\n";}
        else if (player instanceof WhiteMage){message += "Max Mana: " + ((WhiteMage) player).getPastMaxMana() + " -> " + ((WhiteMage) player).getMaxMana() + "\n";}
        else if (player instanceof Rogue){message += "Max Energy: " + ((Rogue) player).getPastMaxEnergy() + " -> " + ((Rogue) player).getMaxEnergy() + "\n";}
        else{message += "Max Rage: " + ((Warrior) player).getPastMaxRage() + " -> " + ((Warrior) player).getMaxRage() + "\n";}

        message += " \n";
        message += "Attack: " + player.getPastAttack() + " -> " + player.getAttack() + "\n";
        message += "Defence: " + player.getPastDef() + " -> "+ player.getDef() + "\n";
        message += "Agility: " + player.getPastAgility() + " -> "+ player.getAgility() + "\n";

        //special energy
        if (player instanceof Rogue){message += "Energy Regen per turn: " + ((Rogue) player).getPastEnergyRegen() + " -> " + ((Rogue) player).getEnergyRegen() + "\n";}
        else if (player instanceof BlackMage){message += "Magic: " + ((BlackMage) player).getPastMagic() + " -> " + ((BlackMage) player).getMagic() + "\n";}
        else if (player instanceof WhiteMage){message += "Magic: " + ((WhiteMage) player).getPastMagic() + " -> " + ((WhiteMage) player).getMagic() + "\n";}

        levelUpTextArea.append(message);
    }

    public void save(){
        try{
            //get the folder path
            Path path = Paths.get("save files");
            //write the save file in that location
            FileWriter writer = new FileWriter(path.toAbsolutePath()+ "\\" +fileName);
            //write the save file
            String message = "";
            message += (roomsCleared) + "\n";
            for (Player member : party){
                message += member.getName() + "," + member.getLevel() + ",";

                if (member instanceof Warrior){message += "Warrior,";}
                else if (member instanceof Rogue){message += "Rogue,";}
                else if (member instanceof BlackMage){message += "Black Mage,";}
                else{message += "White Mage,";}

                message += member.getHealth() + "," + member.getMaxHealth() + ",";

                if (member instanceof Warrior){message += ((Warrior) member).getRage() + "," + ((Warrior) member).getMaxRage();}
                else if (member instanceof Rogue){message += ((Rogue) member).getEnergy() + "," + ((Rogue) member).getMaxEnergy() + "," + ((Rogue) member).getEnergyRegen() + "," + ((Rogue) member).getEnergyCost();}
                else if (member instanceof BlackMage){message += ((BlackMage) member).getMana() + "," + ((BlackMage) member).getMaxMana() + "," + ((BlackMage) member).getManaCost();}
                else{message += ((WhiteMage) member).getMana() + "," + ((WhiteMage) member).getMaxMana()+ "," + ((WhiteMage) member).getManaCost();}
                message += ",";

                message += String.format("%s,%s,%s,%s,%s",member.getExp(), member.getExpLimit(), member.getAttack(), member.getDef(), member.getAgility());

                if (member instanceof BlackMage){message += "," + ((BlackMage) member).getMagic();}
                else if (member instanceof WhiteMage){message += "," + ((WhiteMage) member).getMagic();}
                message+= "\n";
            }
            writer.write(message);
            writer.close();
        }
        catch (IOException e){
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}