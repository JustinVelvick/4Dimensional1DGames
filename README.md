# 4Dimensional1D Games BattleShip Project

## Running the BattleShip Game:
### Option 1: Jar Executable
-In the main directory of our repo (same as README.md), there is an executable jar file called "4Dimensional1DGames.jar". If everything went as planned and nothing unexpected occurs, opening this file should run the game just fine.
### Option 2: Through IDE
<ol>
  <li> Open Intellij</li>
  <li> Create a new run configuration with any Java version 15 or newer, with the main method being set to: "edu.colorado.fourdimensionalonedgames.Battleships" and set the working directory to be: "/insert_your_path_to_repo_here/4Dimensional1DGames"</li>
  <li> Now if nothing weird happens and our collective inexperience with projects didn't mess anything up, then you should be able to just hit the green run arrow in the rop right (or press Shift+F10). </li>
   <li> If all else fails with the above run config, then manually go to Battleships.java and click the green run arrow next to the Battleships class statement on line 19. </li>
 </ol>
 
 ## Running the Test Suite:
 ### Option 1: No build configuration for running tests
 - In Intellij, right click the root folder named "4Dimensional1DGames." This will be the highest folder in the project tree. After right clicking it, select (Run 'All Tests')
 ### Option 2: Configuring a build for all tests
<ol>
   <li> In Intellij, click the builds dropdown in the top right (next to the green hammer and arrow) and select "Edit configurations" </li>
 <li> Add a new configuration by clicking the plus sign in the top left and select JUnit</li>
 <li> Name it whatever you want, and select a java SDK version of at least 15</li>
 <li> Make sure that "4Dimensional1DGames" is selected for -cp (class path)</li>
 <li> Type "-ea" in the VM options</li>
 <li> Select "All in package" for type of resource to search for tests</li>
 <li> Change to the working directory to be the module directory by typing: $MODULE_WORKING_DIR$</li>
 <li> Click OK and run the newly created build config in the top right drop down, then run it by pressing the green arrow</li>
</ol>
 
 
## Where all of our PDF's are located: 
 ### All PDF's are located in our Repo's DOCUMENTS folder
 - UML of entire system: 4D1DGames_UML.pdf
 - UML of just the Factory and Command patterns: Factory+Command_Pattern_UMLs - UML Class.pdf
 - Sequence diagram for placing a ship: PlaceShipSequence.pdf
 - Sequence diagram for attacking: FireWeaponSequence.pdf
 - CodeSmells + Refactoring after milestone 5: M6_Refactoring_CodeSmells.pdf
 - Older refactoring: Refactoring_Before_After.pdf
 
 
 ## Names: 
 - Harry Ainsworth
 - Justin Velvick
 - Kaden Ostendorf
 - Luke Phillips  


 ## Copyright info:
  - Huge thanks to the open source framework JavaFX which our project's GUI is based on
  - Maven's project generation tools helped us early on, but became more useless after we were up and running
  - Jetbrains Intellij IDE, which we used to code our project
