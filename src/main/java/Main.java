import java.util.InputMismatchException;
import java.util.Scanner;

enum State {
    START,
    EASY,
    MEDIUM,
    HARD
}

class GameEngine {
    /*
     * String Array that stores all the possible conditions in the game.
     */
    private String[][][] messages = {
            {
                    {
                            "\nWELCOME TO GAME\n" +                       // Messege to User
                            "   _   \n" +                                 // Ascii Requirement
                            " _(\")_ \n" +
                            "(_ . _)\n" +
                            " / : \\ \n" +
                            "(_/ \\_)\n" +
                                    "\n[Q] to quit, [Enter] to continue: ",
                            "Start",                                    // Used to classify state
                            "(q|Q|$)",                                  // regex
                            "Q  or Enter "                              // Error Text
                    },
                    {
                        "-------------------------------------\n" +
                            "Lower Bound (Default = 0): ",
                            "Lower",
                            "(q|Q|[0-9]+|-[0-9]+|$)",
                            "Any Number, Q, Enter"
                    },
                    {   
                            "Upper Bound (Default = Lower + 10) : ",
                            "Upper",
                            "(q|Q|[0-9]+|-[0-9]+|$)",
                            "Any Number Greater than Lower Bound,  Q  Enter"
                    },
                    {   
                        "-------------------------------------\n" +
                            "Game Modes\n"+
                                    "1.Easy - Higher or Equal you win (Default)\n" +
                                    "2.Medium - Higher you win\n" +
                                    "3.Hard - Guess Number\n\n" +
                                    "Select Mode [1 - 3]: ",
                            "Game",
                            "(1|2|3|q|Q|$)",
                            "1 2 3 or Q or Enter"
                    },
            },
            {
                    {
                            "[Easy] Guess a Number: ",
                            "Guess",
                            "(q|Q|[0-9]+|-[0-9]+)",
                            "Any Number within Range or Q"
                    },
                    {
                            "You Won!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Won",
                            "(r|R|q|Q|$)",
                            "R  Q or Enter"
                    },
                    {
                            "You Lost!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Lost",
                            "(r|R|q|Q|$)",
                            "C  Q or Enter"
                    },
            },
            {
                    {
                            "[Medium] Guess a Number: ",
                            "Guess",
                            "(q|Q|[0-9]+|-[0-9]+)",
                            "Any Number within Range or Q"
                    },
                    {
                            "You Won!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Won",
                            "(r|R|q|Q|$)",
                            "R Q or Enter"
                    },
                    {
                            "You Lost!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Lost",
                            "(r|R|q|Q|$)",
                            "R Q or Enter"
                    },
            },
            {
                    {
                            "[HARD] Guess a Number: ",
                            "Guess",
                            "(q|Q|[0-9]+|-[0-9]+)",
                            "Any Number within Range or Q"
                    },
                    {
                            "You Won!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Won",
                            "(r|R|q|Q|$)",
                            "R Q or Enter"
                    },
                    {
                            "You Lost!\n" +
                                    "[Play Again? (Enter), Restart ( R ), Quit (Q)]:",
                            "Lost",
                            "(r|R|q|Q|$)",
                            "R Q or Enter"
                    },
            }
    };

    private State currentState;
    private int stateProgress, guessUser, pcNumber, lowerBound, upperBound, scoreE, scoreM,scoreH;
    private String userinput;

    public GameEngine() {
        this.currentState = State.START;
        this.stateProgress = 0;
        this.lowerBound = 0;
        this.upperBound = 10;
        this.scoreE = 0;
        this.scoreM = 0;
        this.scoreH = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            String[][] tempMessageArr;
            while (true) {
                tempMessageArr = this.messages[this.currentState.ordinal()];
                System.out.print(tempMessageArr[this.stateProgress][0]);
                try {
                    userinput = scanner.nextLine();
                    if (userinput.matches(tempMessageArr[this.stateProgress][2])) {
                        if (userinput.toLowerCase().equals("q")) {
                            break;
                        }
                        stateMachine(tempMessageArr);
                    } else {
                        throw new InputMismatchException();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input: Needs to be in Approved List '"
                            + tempMessageArr[this.stateProgress][3] + "'");
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Issue with Scanner " + e);
        } finally {
            System.out.println("GAME OVER");
            System.exit(0);
        }
    }

    private void stateMachine(String[][] tempMessageArr) {
        switch (this.currentState) {
            case START:
                switch (tempMessageArr[this.stateProgress][1]) {
                    case "Start":
                        this.stateProgress = 1;
                        break;
                    case "Lower":
                        if (userinput.equals("")) {
                            lowerBound = 0;
                        } else {
                            lowerBound = Integer.parseInt(userinput);
                        }
                        System.out.println("    LB set to " + lowerBound + "\n");
                        this.stateProgress = 2;
                        break;
                    case "Upper":
                        if (userinput.equals("")) {
                            this.upperBound = this.lowerBound + 10;
                        } else {
                            this.upperBound = Integer.parseInt(userinput);
                        }
                        System.out.println("    UB set to " + this.upperBound + "\n");
                        this.stateProgress = 3;
                        break;
                    case "Game":
                        if (userinput.equals("")) {
                            this.currentState = State.EASY;
                        } else {
                            switch (Integer.parseInt(userinput)) {
                                case 1:
                                    this.currentState = State.EASY;
                                    break;
                                case 2:
                                    this.currentState = State.MEDIUM;
                                    break;
                                case 3:
                                    this.currentState = State.HARD;
                                    break;
                            }
                        }
                        System.out.println("Game Mode " + this.currentState);
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                }
                break;

            case EASY:
                switch (tempMessageArr[this.stateProgress][1]) {
                    case "Guess":
                        this.guessUser = Integer.parseInt(userinput);
                        this.pcNumber = generateRandomNumber(lowerBound, upperBound);
                        if (this.guessUser > upperBound || this.guessUser < lowerBound) {
                            System.out.println(
                                    "Out of Range " + this.lowerBound + " - " + this.upperBound + " Try again \n");
                        } else {
                            System.out.println("System Guessed : " + this.pcNumber);
                            if (this.guessUser >= this.pcNumber) {
                                this.stateProgress = 1;
                            } else {
                                this.stateProgress = 2;
                            }
                        }
                        break;
                    case "Won":
                        this.scoreE += 1;
                    case "Lost":
                        if (userinput.toLowerCase().equals("r")) {
                            this.currentState = State.START;
                        }
                        if (userinput.equals("")) {
                            // Something
                        }
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                }
                break;

            case MEDIUM:
                switch (tempMessageArr[this.stateProgress][1]) {
                    case "Guess":
                        this.guessUser = Integer.parseInt(userinput);
                        this.pcNumber = generateRandomNumber(lowerBound, upperBound);
                        if (this.guessUser > upperBound || this.guessUser < lowerBound) {
                            System.out.println(
                                    "Out of Range " + this.lowerBound + " - " + this.upperBound + " Try again \n");
                        } else {
                            System.out.println("System Guessed : " + this.pcNumber);
                            if (this.guessUser > this.pcNumber) {
                                this.stateProgress = 1;
                            } else {
                                this.stateProgress = 2;
                            }
                        }
                        break;
                    case "Won":
                        this.scoreM += 1;
                    case "Lost":
                        if (userinput.toLowerCase().equals("r")) {
                            this.currentState = State.START;
                        }
                        if (userinput.equals("")) {
                            // Something
                        }
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                }
                break;

            case HARD:
                switch (tempMessageArr[this.stateProgress][1]) {
                    case "Guess":
                        this.guessUser = Integer.parseInt(userinput);
                        this.pcNumber = generateRandomNumber(lowerBound, upperBound);
                        if (this.guessUser > upperBound || this.guessUser < lowerBound) {
                            System.out.println(
                                    "Out of Range " + this.lowerBound + " - " + this.upperBound + " Try again \n");
                        } else {
                            System.out.println("System Guessed : " + this.pcNumber);
                            if (this.guessUser == this.pcNumber) {
                                this.stateProgress = 1;
                            } else {
                                this.stateProgress = 2;
                            }
                        }
                        break;
                    case "Won":
                        this.scoreH += 1;
                    case "Lost":
                        if (userinput.toLowerCase().equals("r")) {
                            this.currentState = State.START;
                        }
                        if (userinput.equals("")) {
                            // Something
                        }
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                }
                break;
            default:
                break;
        }
    }

    public int generateRandomNumber(int lower, int upper) {
        return lower + (int) (Math.random() * ((upper - lower) + 1));
    }

    /*
     * Pushes everything up and displays the current score
     */
    public void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println("\n");
        }
        System.out.println("Easy: " + this.scoreE + " | Medium: " + this.scoreM + " | Hard: " + this.scoreH );
    }
}

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
    }
}
