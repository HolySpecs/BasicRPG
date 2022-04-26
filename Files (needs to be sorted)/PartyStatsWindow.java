import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;

public class PartyStatsWindow{
    TextArea statusWindow;
    ArrayList<Player> currentParty;
    JFrame frame;
    
    public PartyStatsWindow(ArrayList<Player> party){
        frame = new JFrame("Party Status");
        currentParty = party;
        //should have a text area and 4 buttons
        //the 4 buttons should be the members of the party (reduced if some have died)
        //when a button is pressed, the text area should be changed to show the status of the character
        
        frame.setLayout(new GridLayout(2,1));
        
        //buttons for the characters
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, currentParty.size(), 10, 10));
        for (Player member : currentParty){
            //make a button with the character's name
            JButton partyMemberButton = new JButton(member.getName());
            buttonPanel.add(partyMemberButton);
            partyMemberButton.addActionListener(new MemberListener());
        }
        frame.add(buttonPanel);
        
        //text area
        statusWindow = new TextArea("", 10, 30, TextArea.SCROLLBARS_NONE);
        statusWindow.setEditable(false);
        statusWindow.setText("Select a party member above");
        frame.add(statusWindow);
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    //whenever a button is pressed, show the status of the selected character
    public class MemberListener implements ActionListener{
        public void updateStatusWindow(Player member){
            String message = "";
            
            //show the name and level of the character
            message += "Name: " + member.getName() + "\n";
            message += "Level: " + member.getLevel() + "\n";
            
            //show the class
            if (member instanceof Warrior){message += "Class: Warrior";}
            else if (member instanceof Rogue){message += "Class: Rogue";}
            else if (member instanceof BlackMage){message += "Class: Black Mage";}
            else{message += "Class: White Mage";}
            message += "\n";
            
            //show the character's health
            message += "Health: " + member.getHealth() + "/" + member.getMaxHealth() + "\n";
            
            //special energy
            if (member instanceof Warrior){message += "Rage: " + ((Warrior) member).getRage() + "/" + ((Warrior) member).getMaxRage();}
            else if (member instanceof Rogue){message += "Energy: " + ((Rogue) member).getEnergy() + "/" + ((Rogue) member).getMaxEnergy() ;}
            else if (member instanceof BlackMage){message += "Mana: " + ((BlackMage) member).getMana() + "/" + ((BlackMage) member).getMaxMana();}
            else{message += "Mana: " + ((WhiteMage) member).getMana() + "/" + ((WhiteMage) member).getMaxMana();}
            message += "\n";
            
            message += "EXP: " + member.getExp() + "/" + member.getExpLimit() + "\n";
            message += "Attack: " + member.getAttack() + "\n";
            message += "Defence: " + member.getDef() + "\n";
            
            if (member instanceof BlackMage){message += "Magic: " + ((BlackMage) member).getMagic() + "\n";}
            else if (member instanceof WhiteMage){message += "Magic: " + ((WhiteMage) member).getMagic() + "\n";}
            
            message += "Agility: " + member.getAgility();
            
            statusWindow.setText(message);
        }
        
        public void actionPerformed(ActionEvent event){
            for (Player member : currentParty){
                if (event.getActionCommand().equals(member.getName())){
                    updateStatusWindow(member);
                }
            }
        }
    }
}