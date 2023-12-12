package io.mathquiz;

public class App {
    public static void main(String[] args) throws InterruptedException {
        String toLoop = "none";
        CatchInputAndActOnIt action;

        ApplicationMenu mainMenu = new ApplicationMenu("# Math Quiz #", 11, 2);
        mainMenu.addMenuOption("addition, subtraction, multiplication, division, change difficulty, register player, change player, print all players, print ranking, help");
        Player currPlayer;

        action = new CatchInputAndActOnIt();

        while (!toLoop.equals(String.valueOf(mainMenu.getNumberOfOptions()))) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            mainMenu.printHeaderWithPredefineStyle(2, 2);

            mainMenu.prettyPrintMenu(2);
            action.inputFromUser();

            currPlayer = action.actOnInputForMainMenuSelectOperation(2, mainMenu.getNumberOfOptions(), mainMenu.getHeaderMessage().length());

            if (currPlayer != null) {
                mainMenu.setCurrentPlayer(currPlayer);
            }

            toLoop = action.getValueFromUser();
        }
    }
}
