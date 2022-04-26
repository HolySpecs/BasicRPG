import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class StartWindow {
    JFrame frame;
    JPanel cardPanel, startPanel, saveLoaderPanel;
    JList<String> saveFilesList;
    DefaultListModel<String> saveFiles = new DefaultListModel<String>();
    CardLayout cardLayout;
    ArrayList<File> savesDatabase = new ArrayList<File>();
    ArrayList<Player> party = new ArrayList<Player>();
    int roomsCleared;
    String selectedFile;

    //make the window
    public StartWindow(){
        frame = new JFrame("Start Screen");
        loadFiles();

        cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout(10,10));

        startPanelMaker();
        saveLoaderPanelMaker();

        cardPanel.add(startPanel, "Start Menu");
        cardPanel.add(saveLoaderPanel, "Load Save");

        cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "Start Menu");

        frame.add(cardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //start panel
    public void startPanelMaker(){
        startPanel = new JPanel();
        startPanel.setLayout(new FlowLayout());
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        newGameButton.addActionListener(new startPanelButtonListener());
        loadGameButton.addActionListener(new startPanelButtonListener());

        startPanel.add(newGameButton);
        startPanel.add(loadGameButton);
    }

    //load save panel
    public void saveLoaderPanelMaker(){
        saveLoaderPanel = new JPanel();
        saveLoaderPanel.setLayout(new GridLayout(3,1, 10, 10));

        JLabel label = new JLabel("Select a save file");
        saveLoaderPanel.add(label);

        saveFilesList = new JList<>(saveFiles);
        saveFilesList.setLayoutOrientation(JList.VERTICAL);
        saveFilesList.addListSelectionListener(new SaveListSelectionListener());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(saveFilesList);

        saveLoaderPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton confirmButton = new JButton("Confirm");
        JButton backButton = new JButton("Back");
        confirmButton.addActionListener(new loadPanelButtonListener());
        backButton.addActionListener(new loadPanelButtonListener());

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        saveLoaderPanel.add(buttonPanel);
    }

    public class startPanelButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getActionCommand().equals("New Game")){
                frame.dispose();
                new CharacterCreateWindow();
            }
            else if (event.getActionCommand().equals("Load Game")){
                saveFilesList.clearSelection();
                cardLayout.show(cardPanel, "Load Save");
            }
        }
    }

    public class loadPanelButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getActionCommand().equals("Confirm")){
                if (saveFilesList.getSelectedIndex() != -1){
                    //access the save files and then go to the main menu
                    Path path = Paths.get("save files");
                    File file = new File(path.toAbsolutePath() + "\\" + selectedFile);

                    loadFile(file);
                    frame.dispose();
                    new MainMenuWindow(party, selectedFile);
                }
            }
            else if (event.getActionCommand().equals("Back")){
                cardLayout.show(cardPanel, "Start Menu");
            }
        }
    }

    public class SaveListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent event){
            if (saveFilesList.getSelectedIndex() != -1){
                selectedFile = saveFilesList.getSelectedValue();
            }
        }
    }

    public void loadFiles(){
        Path dir = Paths.get("save files");
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
            for (Path fileName: stream){
                String option = fileName.toString();
                option = option.replace("save files\\", "");
                saveFiles.addElement(option);
            }
        }
        catch (IOException | DirectoryIteratorException e){
            System.err.println(e);
        }
    }

    public void loadFile(File file){
        try{
            Scanner reader = new Scanner(file);
            String data = reader.nextLine();
            roomsCleared = Integer.parseInt(data);
            while (reader.hasNextLine()){
                for (int i = 1; i < 5; i ++){
                    data = reader.nextLine();
                    String[] info = data.split(",");
                    String name = info[0];
                    Player member;
    
                    if (info[2].equals("Warrior")){
                        member = new Warrior(name);
    
                        member.setLevel(Integer.parseInt(info[1]));
                        member.setHealth(Integer.parseInt(info[3]));
                        member.setMaxHealth(Integer.parseInt(info[4]));
    
                        ((Warrior) member).setRage(Integer.parseInt(info[5]));
                        ((Warrior) member).setMaxRage(Integer.parseInt(info[6]));
    
                        member.setExp(Integer.parseInt(info[7]));
                        member.setExpLimit(Integer.parseInt(info[8]));
                        member.setAttack(Integer.parseInt(info[9]));
                        member.setDef(Integer.parseInt(info[10]));
                        member.setAgility(Integer.parseInt(info[11]));
                        party.add(member);
                    }
                    else if (info[2].equals("Rogue")){
                        member = new Rogue(name);
    
                        member.setLevel(Integer.parseInt(info[1]));
                        member.setHealth(Integer.parseInt(info[3]));
                        member.setMaxHealth(Integer.parseInt(info[4]));
    
                        ((Rogue) member).setEnergy(Integer.parseInt(info[5]));
                        ((Rogue) member).setMaxEnergy(Integer.parseInt(info[6]));
                        ((Rogue) member).setEnergyRegen(Integer.parseInt(info[7]));
                        ((Rogue) member).setEnergyCost(Integer.parseInt(info[8]));
    
                        member.setExp(Integer.parseInt(info[9]));
                        member.setExpLimit(Integer.parseInt(info[10]));
                        member.setAttack(Integer.parseInt(info[11]));
                        member.setDef(Integer.parseInt(info[12]));
                        member.setAgility(Integer.parseInt(info[13]));
                        party.add(member);
                    }
                    else if (info[2].equals("Black Mage")){
                        member = new BlackMage(name);
    
                        member.setLevel(Integer.parseInt(info[1]));
                        member.setHealth(Integer.parseInt(info[3]));
                        member.setMaxHealth(Integer.parseInt(info[4]));
    
                        ((BlackMage) member).setMana(Integer.parseInt(info[5]));
                        ((BlackMage) member).setMaxMana(Integer.parseInt(info[6]));
                        ((BlackMage) member).setManaCost(Integer.parseInt(info[7]));
    
                        member.setExp(Integer.parseInt(info[8]));
                        member.setExpLimit(Integer.parseInt(info[9]));
                        member.setAttack(Integer.parseInt(info[10]));
                        member.setDef(Integer.parseInt(info[11]));
                        member.setAgility(Integer.parseInt(info[12]));
    
                        ((BlackMage) member).setMagic(Integer.parseInt(info[13]));
                        party.add(member);
                    }
                    else if (info[2].equals("White Mage")){
                        member = new WhiteMage(name);
    
                        member.setLevel(Integer.parseInt(info[1]));
                        member.setHealth(Integer.parseInt(info[3]));
                        member.setMaxHealth(Integer.parseInt(info[4]));
    
                        ((WhiteMage) member).setMana(Integer.parseInt(info[5]));
                        ((WhiteMage) member).setMaxMana(Integer.parseInt(info[6]));
                        ((WhiteMage) member).setManaCost(Integer.parseInt(info[7]));
    
                        member.setExp(Integer.parseInt(info[8]));
                        member.setExpLimit(Integer.parseInt(info[9]));
                        member.setAttack(Integer.parseInt(info[10]));
                        member.setDef(Integer.parseInt(info[11]));
                        member.setAgility(Integer.parseInt(info[12]));
    
                        ((WhiteMage) member).setMagic(Integer.parseInt(info[13]));
                        party.add(member);
                    }
                }
            }
            
            reader.close();
        }
        catch (NumberFormatException | IOException e){
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }

    public static void main(String[] argv){
        new StartWindow();
    }
}
