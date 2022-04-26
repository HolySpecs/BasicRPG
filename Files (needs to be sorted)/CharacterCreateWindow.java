import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;

public class CharacterCreateWindow{
    JFrame frame;
    //array to hold the party members
    ArrayList<Player> party = new ArrayList<Player>();
    //to tell the player what to do
    TextArea status, status2;
    //to let the player enter a name for a party member
    JTextField nameField, nameField2;
    //to let the player select a class for the party member
    ButtonGroup radioButtonGroup;
    JPanel cardPanel, createCharacterPanel, nameSavePanel;
    CardLayout cardLayout;

    //for choosing the class
    boolean classChosen = false;
    String[] classes = {"Warrior", "Rogue", "Black Mage", "White Mage"};
    String characterClass;
    String fileName;

    public CharacterCreateWindow(){
        frame = new JFrame("Choose your party members");

        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout(10,10));

        createCharacterPanelMaker();
        nameSavePanelMaker();

        cardPanel.add(createCharacterPanel, "Create Party");
        cardPanel.add(nameSavePanel, "Name Save File");

        cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "Create Party");
        
        frame.add(cardPanel);
        //close when the x button is pressed in the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //basically compile for the frame
        frame.pack();
        //show the frame
        frame.setVisible(true);
    }

    public void createCharacterPanelMaker(){
        createCharacterPanel = new JPanel();
        createCharacterPanel.setLayout(new GridLayout(4,1));
        //where it will have the status window, the text field for the member's name, the class select and then a confirm button

        status = new TextArea();
        //to not let the player change the status window by typing
        status.setEditable(false);
        status.setText("Create your characters");
        //add the status to the frame
        createCharacterPanel.add(status);

        //text field for the name
        JPanel textFieldPanel = new JPanel();
        nameField = new JTextField("",20);
        textFieldPanel.add(nameField);
        createCharacterPanel.add(textFieldPanel);

        //radio buttons for the classes, one character can have one class only
        JPanel radioButtonPanel = new JPanel();
        radioButtonGroup = new ButtonGroup();
        
        //add buttons for however many classes there are
        for (String characterClass : classes){
            JRadioButton classChoice = new JRadioButton(characterClass);
            classChoice.addActionListener(new RadioButtonListener());
            radioButtonGroup.add(classChoice);
            radioButtonPanel.add(classChoice);
        }
        createCharacterPanel.add(radioButtonPanel);

        //confirm button to add them to the party
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ConfirmListener());
        createCharacterPanel.add(confirmButton);
    }

    public void nameSavePanelMaker(){
        nameSavePanel = new JPanel();
        nameSavePanel.setLayout(new GridLayout(3,1,10,10));

        status2 = new TextArea();
        //to not let the player change the status window by typing
        status2.setEditable(false);
        status2.setText("Name your save file");
        //add the status to the frame
        nameSavePanel.add(status2);

        //text field for the name
        JPanel textFieldPanel = new JPanel();
        nameField2 = new JTextField("",20);
        textFieldPanel.add(nameField2);
        nameSavePanel.add(textFieldPanel);

        JButton button = new JButton("Confirm");
        button.addActionListener(new ConfirmListener2());
        nameSavePanel.add(button);
    }

    public class RadioButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            classChosen = true;
            characterClass = event.getActionCommand();
        }
    }

    //adding the member to the array
    public class ConfirmListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getActionCommand().equals("Confirm")){
                //if the name was not entered
                if (nameField.getText().equals("")){
                    status.setText("Please Enter a Name");
                }
                //if the class was not chosen
                else if (!classChosen){
                    status.setText("Please Select a Class");
                }
                else{
                    //adds the member to the array
                    if (characterClass.equals("Warrior")){party.add(new Warrior(nameField.getText()));}
                    else if (characterClass.equals("Rogue")){party.add(new Rogue(nameField.getText()));}
                    else if (characterClass.equals("Black Mage")){party.add(new BlackMage(nameField.getText()));}
                    else {party.add(new WhiteMage(nameField.getText()));}
                    //checks if the party has 4 characters
                    if (party.size() == 4){
                        cardLayout.show(cardPanel, "Name Save File");
                        
                    }
                    else{
                        status.setText(nameField.getText() + " has been added, add another character");
                        nameField.setText("");
                        radioButtonGroup.clearSelection();
                        classChosen = false;
                    }
                }
            }
        }
    }

    public class ConfirmListener2 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (nameField2.getText().equals("")){
                status2.setText("Please Enter a Name");
            }
            else if (event.getActionCommand().equals("Confirm")){
                fileName = nameField2.getText() + ".txt";
                Path path = Paths.get("save files");
                File file = new File(path.toAbsolutePath() + "\\" + fileName);
                if (file.exists()){
                    status2.setText("Enter a different name");
                }
                else{
                    //closes window and then goes to the main game
                    frame.dispose();
                    //save the game here
                    makeSaveFile();
                    new MainMenuWindow(party, fileName);
                }
                
            }
        }

    }

    public void makeSaveFile(){
        try{
            //get the folder path
            Path path = Paths.get("save files");
            //write the save file in that location
            FileWriter writer = new FileWriter(path.toAbsolutePath()+ "\\" +fileName);
            //write the save file
            String message = "";
            message += "0\n";
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