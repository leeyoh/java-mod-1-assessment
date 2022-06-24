import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthOptionPaneUI;


enum State{
    START,
    EASY,
    MEDIUM,
    HARD
}

class GameEngine {
    private String[][][] messages = {
        {   
            {
                "WELCOME TO GAME\n" +
                "Q to quit, Enter to continue: ",
                "Start",
                "(q|Q|$)",  //regex
                "Q  or Enter "
            },
            {
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
                "1.Easy - Higher or Equal you win (Default)\n" +
                "2.Medium - Higher you win\n" +
                "3.Hard - Guess Number\n" +
                "Select Game Mode (1 - 3): ",
                "Game",
                "(1|2|3|q|Q|$)",
                "1 2 3 or Q or Enter"
            },
        },
        {
            {
                "Guess a Number: ",
                "Guess",
                "(q|Q|[0-9]+|-[0-9]+)",
                "Any Number within Range or Q"
            },
            {
                "You Won!\n"+
                "[Play Again? (Enter), Change Difficulty ( C ), Quit (Q)]:",
                "Won",
                "(c|C|q|Q|$)",
                "C  Q or Enter"
            },
            {
                "You Lost!\n"+
                "[Play Again? (Enter), Change Difficulty ( C ), Quit (Q)]:",
                "Lost",
                "(c|C|q|Q|$)",
                "C  Q or Enter"
            },
        }, 
        {
            {"Medium: "}
        }, 
        {
            {"Hard: "}
        }
    };

    private State currentState;
    private int stateProgress, guessUser, pcNumber,lowerBound, upperBound;
    private String userinput;
 

    public GameEngine(){
        this.currentState = State.START;
        this.stateProgress = 0;
        this.lowerBound = 0;
        this.upperBound = 10;

        try(Scanner scanner = new Scanner(System.in)){
            String[][] tempMessageArr; 
            while(true){
                tempMessageArr = this.messages[this.currentState.ordinal()];
                System.out.print(tempMessageArr[this.stateProgress][0]);
                try{
                    userinput = scanner.nextLine();
                    if(userinput.matches(tempMessageArr[this.stateProgress][2])){ 
                        if(userinput.toLowerCase().equals("q")){
                            break;
                        }
                        stateMachine(tempMessageArr);
                    } else {
                        throw new InputMismatchException();
                    }
                } catch(InputMismatchException e){
                    System.out.println("Invalid Input: Needs to be in Approved List '" + tempMessageArr[this.stateProgress][3] + "'");
                } catch(NumberFormatException e){
                    System.out.println(e);
                }
            }
        }catch(Exception e){
            System.out.println("Issue with Scanner " + e);
        }finally{
            System.out.println("GAME OVER");
            System.exit(0);
        }
    }
   
    private void stateMachine(String[][] tempMessageArr){
        switch(this.currentState){
            case START:
                switch(tempMessageArr[this.stateProgress][1]){
                    case "Start":
                        this.stateProgress = 1;
                        break;
                    case "Lower":
                        if(userinput.equals("")){
                            lowerBound = 0;
                        } else {
                            lowerBound = Integer.parseInt(userinput);
                        }
                        System.out.println("Lower Bound set to " + lowerBound);
                        this.stateProgress = 2;
                        break;
                    case "Upper":
                        if(userinput.equals("")){
                            this.upperBound = this.lowerBound + 10;
                        } else {
                            this.upperBound = Integer.parseInt(userinput);
                        }
                        System.out.println("Upper Bound set to " + this.upperBound);
                        this.stateProgress = 3;
                        break;
                    case "Game":
                        if(userinput.equals("")){
                            this.currentState = State.EASY;
                        } else {
                            switch(Integer.parseInt(userinput)){
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
                switch(tempMessageArr[this.stateProgress][1]){
                    case "Guess":
                        this.guessUser = Integer.parseInt(userinput);
                        this.pcNumber = generateRandomNumber(lowerBound,upperBound);
                        if(this.guessUser > upperBound || this.guessUser < lowerBound){
                            System.out.println("Out of Range " + this.lowerBound + " - " + this.upperBound + " Try again \n");
                        } else {
                            System.out.println("PC Guessed : " + this.pcNumber);
                            if(this.guessUser >= this.pcNumber){
                                this.stateProgress = 1;
                            } else {
                                this.stateProgress = 2;
                            }
                        }
                        break;
                    case "Won":
                        if(userinput.toLowerCase().equals("c")){
                            this.currentState = State.START;
                        }
                        if(userinput.equals("")){
                            //Something
                        } 
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                    case "Lost":
                        if(userinput.toLowerCase().equals("c")){
                            this.currentState = State.START;
                        }
                        if(userinput.equals("")){
                            //Something
                        } 
                        this.stateProgress = 0;
                        clearConsole();
                        break;
                }
                break;
            case MEDIUM:
                break;
            case HARD:
                break;
            default:
                break;
        }
    }
    public int generateRandomNumber(int lower, int upper){
        return lower + (int)(Math.random() * ((upper - lower) + 1));
    }   

    public final static void clearConsole()
    {
        for(int i = 0; i < 100; i++){
            System.out.println("\n");
        }
    }

}
public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
    }
}
