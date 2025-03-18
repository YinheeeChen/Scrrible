# Scrrible

## Description
Scrrible is a Java-based game application that allows players to engage in a word game similar to Scrabble. The game supports 2 to 4 players and includes functionalities such as adding words to the board, validating words, scoring, and saving/loading game states.

## Features
- **Multiplayer Support**: Play with 2 to 4 players.
- **Word Validation**: Validate the words against a dictionary.
- **Scoring System**: Calculate and display scores for each player.
- **Save and Load Game**: Save the current game state and load it later.
- **User-friendly Interface**: Easy-to-use console-based interface.

## Installation
To install and set up Scrrible, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/YinheeeChen/Scrrible.git
    cd Scrrible
    ```

2. Ensure you have Java Development Kit (JDK) installed. You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

3. Compile the Java files:
    ```sh
    javac -d bin src/*.java
    ```

4. Run the application:
    ```sh
    java -cp bin Main
    ```

## Usage
To start playing Scrrible, run the main class and follow the on-screen instructions to set up the game, add players, and begin playing.

## Main Functionalities
### Player Class
- **rowEnter()**: Allows players to choose the row position to place their words.
- **columnEnter()**: Allows players to choose the column position to place their words.
- **checkMoveValid()**: Validates the move based on the game's rules.
- **checkWordValid()**: Checks if the entered word is a valid English word.
- **placeWord()**: Places the word on the board and checks its validity.
- **readWordAndStore()**: Stores the entered words in a file.

### Dictionary Class
- **readInputWord()**: Reads the input word and adds it to an array list.
- **dictionaryCheck()**: Checks if the word exists in the dictionary and calculates the score.
- **wordScore()**: Calculates the score for the entered word.
- **charPoint()**: Converts the input letter into corresponding points.

### Game Class
- **gameSet()**: Sets up the game with player numbers and names.
- **operation()**: Main game loop that handles menu options and game flow.
- **playerRank()**: Sorts and displays players based on their scores.
- **gamePlay()**: Controls the process of playing the game.
- **setScore()**: Allows players to set and validate the score limit.
- **exitGame()**: Allows players to save and exit the game.
- **saveGame()**: Saves the current game state to a file.

### Menu Class
- **displayMenu()**: Displays the main menu of the game.

## Contribution
We welcome contributions! To contribute to this project, follow these steps:

1. Fork this repository.
2. Create a new branch with a descriptive name (e.g., `feat_add_feature`).
3. Commit your changes.
4. Create a Pull Request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Acknowledgements
- Special thanks to all the contributors and supporters of this project.

## Contact
For any questions or inquiries, please contact [YinheeeChen](https://github.com/YinheeeChen).
