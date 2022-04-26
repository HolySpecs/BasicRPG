Student name: Nicholas Henryanto
Student number: 200061168

Please complete this README file for your level 3 mini-project submission.

LEVEL THREE

GUI

I'll provide information on two different buttons, however there are more buttons and interactive components included in the program.

Button 1: confirmButton in CharacterCreateWindow.java , initialized in line 88
When this button is pressed, it first checks if there are any text entered in the text field, where the user should put the name of the member.
Then it also checks if the person has chosen a class.
When the above are done, it then checks whether or not if 4 members have been added. If it has, it send the party to the main menu screen
and if not, it repeats the whole process again.

Button 2: Button set for seeing the party stats in PartyStatsWindow.java initialized in line 27
These sets of buttons are used to show the stats of the member in the party. When the player presses the corresponding button,
their stats show there. When a different button is pressed, it shows the stats of the other member.

Exceptions

IOException in BattleWindow.java in function save(), save() located at line 576 and IOException at line 610:
This exception is necessary because it needs to check if the file can be opened or not.
This is needed in this case because whenever the person has finished a battle, they need to be able to save 
their progress so that they can resume it at a later point.

Collections

ArrayList<Enemy> enemeis present in BattleWindow.java
The reason why I used ArrayList in this file is beacuse I wanted a more convenient way of removing characters from the queue
when they died. This is because my game works on a "queue" system, where it is sorted based on the person's agility
and it's how the turn system works

File I/O

File Input: Loading save files in StartWindow.java:
Here, the system looks at the the save files in the "save files" folder and then gets the names of the files and lists them out
for the user to see. Then when the user selects a save file, the game imports the character details and the amount of rooms cleared
, allowing the person to continue where they left off.

File Output: Making save data after each battle in BattleWIndow.java
Here, whenever the player wins in a battle, the game checks if the party members can level up and then when finished, it updates
the file to hold the amount of rooms the player has cleared and the stats that each party member has.

'Something impressive'

Use of Swing's cardLayout:
This is used in BattleWindow.java, CharacterCreateWindow.java and StartWindow.java, where there are multiple "panels" that the frame
can be. BattleWindow.java uses it to allow the user to target the enemies/allies, CharacterCreateWindow.java uses it to add the details
of the party members and to write the name of the save file and StartWindow.java uses it to differentiate if a new game is going to be
started or if we are loading a new game.

Technically it has other things as well such as being able to use abilities and characters being able to level up but that was previously
mentioned in the previous read me file.