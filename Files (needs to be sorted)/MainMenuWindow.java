import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
//to generate the monsters
import java.util.Random;

public class MainMenuWindow{
    JFrame frame;
    ArrayList<Player> currentParty;
    TextArea status;
    int roomsCleared = 0;   
    boolean rested = false;
    String fileName;

    public MainMenuWindow(ArrayList<Player> party, String fileName){
        currentParty = party;
        frame = new JFrame("Main Menu");
        frame.setLayout(new GridLayout(2,1,10,10));
        this.fileName = fileName;

        status = new TextArea();
        status.setEditable(false);
        status.setText("What do you want to do?");
        frame.add(status);

        //buttons for the user to select
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        String[] options = {"View Party", "Go to the next room", "Rest", "Exit Game"};
        for (String option : options){
            JButton button = new JButton(option);
            button.addActionListener(new ButtonListener());
            buttonPanel.add(button); 
        }

        frame.add(buttonPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //opens a new window to show the player the current status of the party members
            if (event.getActionCommand().equals("View Party")){
                new PartyStatsWindow(currentParty);
            }
            //to begin a battle
            else if (event.getActionCommand().equals("Go to the next room")){
                frame.setVisible(false);
                ArrayList<Enemy> enemies = generateRoom(roomsCleared);
                new BattleWindow(currentParty, enemies, fileName, roomsCleared);
                frame.setVisible(true);
                roomsCleared++;
                rested = false;
            }
            //to recover health and mana
            else if (event.getActionCommand().equals("Rest")){
                if (rested){
                    status.setText("You have already rested, select a different option!");
                }
                else{
                    recover(currentParty, roomsCleared);
                    status.setText("Party rested, what do you want to do now?");
                    rested = true;
                }
            }
            //to leave the game
            else if (event.getActionCommand().equals("Exit Game")){
                frame.dispose();
                System.exit(0);
            }
        }
    }

    public void setRoomsCleared(int newRoomsCleared){roomsCleared = newRoomsCleared;}

    //recover health and mana
    public void recover(ArrayList<Player> party, int roomsCleared){
        int regen = 10 + (new Random()).nextInt(4);
        for (Player member : party){
            member.setHealth(member.getHealth() + regen);
            //make sure health doesn't go over
            member.checkHealth();

            //recover mana only for mages
            if (member instanceof BlackMage){
                ((BlackMage) member).setMana(((BlackMage) member).getMana() + regen);
                ((BlackMage) member).checkMana();
            }
            else if (member instanceof WhiteMage){
                ((WhiteMage) member).setMana(((WhiteMage) member).getMana() + regen);
                ((WhiteMage) member).checkMana();
            }

        }
    }

    //generates enemies
    public ArrayList<Enemy> generateRoom(int roomsCleared){
        ArrayList<Enemy> temp = new ArrayList<Enemy>();
        int randInt;
        //starts off with beginner monsters and then gets harder
        if (roomsCleared < 4){
            randInt = roomsCleared + 1;
        }
        else{
            //can have a max of 4 monsters per room
            randInt = (new Random()).nextInt(4) + 1;
        }

        for (int i = 0; i < randInt; i ++){
            temp.add(genMonster(randInt, i + 1));
        }
        
        return temp;
    }

    //monster can either be a rat, goblin, troll or dragon
    public Enemy genMonster(int choice, int num){
        Enemy temp = null;
        switch (choice){
            case 1: //spawn rat
                temp = new Rat(num);
                break;
            case 2: //spawn goblin
                temp = new Goblin(num);
                break;
            case 3: //spawn troll
                temp = new Troll(num);
                break;
            case 4: //spawn dragon
                temp = new Dragon(num);
                break;
        }
        return temp;
    }

}