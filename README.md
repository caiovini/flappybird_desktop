# Flappy bird game (desktop version)
Project to cornell institute (Application development)


1.	Overview.
A game version of the famous flappy bird game that was originally developed for android platform. This version of the game has the same functionalities and objectives but aims desktop platforms. 
Flappy bird consists of a unique level, there are no different levels where users can face new challenges and new scenarios. The bird moves forward and pipes appear randomically. Players must be able to fly a bird between a top and bottom pipe while the bird is constantly falling down. The farther you go the higher is your score.

        Software requirements:	

•	The game must use libgdx: the libgdx is an open source library used to render images for games. For the application the library will be used to move components of the game and do collision tests between pipes and birds.

        Functional requirements:	

•	User must be able to select between options on first screen: when the application is open, the options must be shown clearly to the, so the path to start a new game, change configurations or show should be intuitive.
•	The game must keep a record of all scores reached for each and every player: Whenever a player loses a game by hitting the pipes or going out of the screen borders, one’s score must be stored to database.
•	The game must have option to change screen brightness and sound volume: the configuration screen must be show these options to make the game more flexible for each and every user. 
•	The keys to control the game must be intuitive and close to each other on the keyboard: the game is supposed to have a good playability, this is an important deliverable of the product in order to increase the adoption by different customers.






        Nonfunctional requirements:	

•	The screen should not be resizable: the screens should not be resizable because the images have a maximum of pixels which guarantee their quality.
•	The game should be mute by default: when the game is initialized for the first time, it should be without any sound. This option can be changed in the future at the configurations screen.


2.	Screens division.
The application consists of 5 screens, the first one is the initial game screen which may lead to instructions screen, configuration screen and new game screen. The new game screen (screen of the game) has an option to pause the game pressing the key ESC, the options for pausing are resume game, which leads the player to continue playing normally, and quit which leads to another screen to show the top 10 highest scores, this screen will give the option for user to type one’s nickname letters of maximum size of three letters.
The configuration screen will give the user the option to change volume level as well as brightness level and option to make the game mute. For these options there will be the implementation of JSliders and Toggle button.
The instructions screen will present the user a series of pictures showing how the game works and how to control the bird.

3.	External library for game development.
For the new game screen, there will be an implementation of the libgdx library. This is an open source library used for game development, in this case there must not be the common event handlers normally used on the java swing and java awt libraries for this specific screen. For the other 4 screens, java swing, java awt and their respectively layouts will be implemented normally.

4.	Database information storage.
The database will store information about user configuration preferences, all scores along with the user’s initial letters, time and date of the score, user’s name, age and gender.
During the initialization of the game, all data related to each user will be configured accordingly, if new user is registered, new data will be inserted relating configurations and scores.
