package io.mathquiz.inputfromuser;

import io.mathquiz.buildingmenuandsubmenu.ApplicationMenu;
import io.mathquiz.buildingmenuandsubmenu.ApplicationSubMenu;
import io.mathquiz.checks.Auxiliary;
import io.mathquiz.createplayer.Player;
import io.mathquiz.generatequestionsandvalidateanswers.MathQuestion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Getter
public class CatchInputAndActOnIt {
    private final Scanner input;
    private String valueFromUser;
    private String difficultyLevel;
    private Player currentPlayerForMainMenu;

    private ApplicationSubMenu changePlayers;
    private ApplicationSubMenu printingPlayers;
    private ApplicationSubMenu registerAPlayerToPlay;
    private ApplicationSubMenu printingHelpFromMainMenu;
    private ApplicationMenu changeDifficulty;
    private ApplicationSubMenu askAMathQuestion;
    private ApplicationSubMenu printingRankingSystem;

    private List<Player> registeredPlayers;
    private List<Integer> pointsEarnedPerPlayer;
    private List<ApplicationMenu> modules;

    /**
     * As you can see we validate the input from the user for main menu option.
     */
    private static String validateValueFromUserMainMenuOption(String inputFromUser, int numberOfEntries, int emptySpaces, boolean ifMain, boolean ifMath) throws InterruptedException {
        String errorMessageOptionType = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR please use an option from those that were given!");
        int numericalValueToChoose;
        String processedString;

        if (inputFromUser.isBlank()) {
            System.out.printf("%s", errorMessageOptionType);
            TimeUnit.SECONDS.sleep(1);
            processedString = "0.1";
        } else {
            processedString = inputFromUser.trim().toLowerCase();

            try {
                if (!ifMath) {
                    numericalValueToChoose = Integer.parseInt(inputFromUser);

                    if ((numericalValueToChoose < 1) || (numericalValueToChoose > numberOfEntries)) {
                        System.out.printf("%s", errorMessageOptionType);
                        TimeUnit.SECONDS.sleep(1);
                        processedString = "0.1";
                    }
                }
            } catch (NumberFormatException e) {
                if ((ifMain) || (!inputFromUser.equals("back") && !inputFromUser.equals("quit"))) {
                    System.out.printf("%s", errorMessageOptionType);
                    TimeUnit.SECONDS.sleep(1);
                    processedString = "0.1";
                }
            }
        }

        return processedString;
    }

    /**
     * Here we create the modules, submenus and add them to modules list.
     * This comments came after a long period of time when the app was made. In the future I will redesign it and
     * made use of DB, SOLID and Gang of Four design patterns. In this case with modules it is a good ideea to use a main builder
     * composed by multiple builders, a builder hierarchy.
     */
    private void creatingObjectsForSubmenu() throws InterruptedException {
        String simpleLine = "back to return to main menu or quit to terminate";

        this.registerAPlayerToPlay = new ApplicationSubMenu("# math quiz # register new player #", 2, 2,
                "please provide the following info to register a player separated by a comma", 2);
        this.printingPlayers = new ApplicationSubMenu("# math quiz #", 2, 2,
                simpleLine, 2);
        this.changePlayers = new ApplicationSubMenu("# math quiz # change player #", 2, 2,
                "please choose an index from above to set the current player", 2);
        this.printingHelpFromMainMenu = new ApplicationSubMenu("# math quiz # help guide #", 2, 2,
                simpleLine, 2);

        this.changeDifficulty = new ApplicationMenu("# math quiz # change difficulty #", 4, 2);
        changeDifficulty.addMenuOption("one digit, two digits, back");

        this.printingRankingSystem = new ApplicationSubMenu("# math quiz # printing ranking system #", 2, 2,
                simpleLine, 2);
        this.askAMathQuestion = new ApplicationSubMenu("# math quiz # ask question #", 2, 2,
                simpleLine, 2);

        this.modules.addAll(List.of(registerAPlayerToPlay, printingPlayers, changePlayers,
                printingHelpFromMainMenu, changeDifficulty, askAMathQuestion, printingRankingSystem));
    }

    /**
     *
     * We create the object with no args. As you can see registeredPlayers, modunles, points and currentPlayer are initialized.
     */
    public CatchInputAndActOnIt() throws InterruptedException {
        this.input = new Scanner(System.in);
        this.registeredPlayers = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.pointsEarnedPerPlayer = new ArrayList<>();
        this.currentPlayerForMainMenu = new Player("None", "None", 1, 2);

        creatingObjectsForSubmenu();
    }

    /**
     * Here we validate the input from register player and then add it to the registeredPlayers list.
     */
    private boolean validateInfoRegisterPlayersAndAddPlayer(int numberOfSpaces) throws InterruptedException {
        String errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(numberOfSpaces), "ERROR please use a proper input, firstname, lastname and age which should be an integer " +
                "separated by a comma to register a player properly!");
        int age;
        boolean isOk = false;
        String firstname, lastname;
        List<String> tempListWithValues = new ArrayList<>(List.of(getValueFromUser().trim().toLowerCase().split(",")));

        if (tempListWithValues.size() != 3) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            try {
                firstname = tempListWithValues.get(0).trim();
                lastname = tempListWithValues.get(1).trim();
                age = Integer.parseInt(tempListWithValues.get(2).trim());

                boolean alreadyExists = false;
                Player newPlayer = new Player(firstname, lastname, age, numberOfSpaces);

                for (Player element : registeredPlayers) {
                    if ((element.getLastName().toLowerCase().equals(lastname)) &&
                            (element.getFirstName().toLowerCase().equals(firstname)) && (element.getAge() == age)) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    registeredPlayers.add(newPlayer);
                    pointsEarnedPerPlayer.add(registeredPlayers.indexOf(newPlayer), 0);

                    isOk = true;
                    System.out.printf("%n\033[32m%s%s%s%s%s%s\033[0m%n", " ".repeat(numberOfSpaces), "Player ",
                            newPlayer.getFirstName(), ", ", newPlayer.getLastName(), " was added with success!");
                } else {
                    System.out.printf("%n\033[31m%s%s%s%s%s%s\033[0m%n", " ".repeat(numberOfSpaces), "Player ",
                            newPlayer.getFirstName(), ", ", newPlayer.getLastName(), " is already added!");
                }

                TimeUnit.SECONDS.sleep(1);
            } catch (NumberFormatException e) {
                System.out.printf("%s", errorMessage);
                TimeUnit.SECONDS.sleep(1);
            }
        }

        return isOk;
    }

    /**
     * We register the player. In other words here we have the submenu register player.
     */
    public String registerPlayer() throws InterruptedException {
        String toReturn = "false";
        registerAPlayerToPlay.addSubMenuOption("firstName, lastName, age", 2);

        while (!"back".equals(toReturn)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            registerAPlayerToPlay.printHeaderSubMenuMessage(2);
            System.out.println();
            registerAPlayerToPlay.prettyPrintOfMessageSubMenu();
            valueFromUser = this.input.nextLine();

            toReturn = Auxiliary.actOnQuitAndBack(valueFromUser, registerAPlayerToPlay.getNumberOfSpacesInFront());

            if ("back".equals(toReturn)) {
                toReturn = "back";
            } else {
                toReturn = String.valueOf(validateInfoRegisterPlayersAndAddPlayer(2));
            }
        }

        return toReturn;
    }

    /**
     * Running print all players module.
     */
    public boolean printAllPlayers(int emptySpaces, int headerLength) throws InterruptedException {
        String forLooping = "";
        boolean toReturn = false;

        if (registeredPlayers.isEmpty()) {
            System.out.printf("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR no player is registered!");
            TimeUnit.SECONDS.sleep(1);
        } else {
            while (!"back".equals(forLooping)) {
                System.out.print("\033[2J\033[H");
                System.out.flush();

                printingPlayers.printHeaderSubMenuMessage(2);
                System.out.printf("%n%s%s%n", " ".repeat(emptySpaces), "Registered players:");

                for (int i = 0; i < getRegisteredPlayers().size(); i++) {
                    System.out.printf("%s%s%s%s%s%s%s%s%n", " ".repeat(emptySpaces), (i + 1) + ". ", getRegisteredPlayers().get(i).getFirstName(), ", ",
                            getRegisteredPlayers().get(i).getLastName(), "; ", "Age: ", getRegisteredPlayers().get(i).getAge());
                }

                System.out.println();
                printingPlayers.prettyPrintOfMessageSubMenu();
                valueFromUser = validateValueFromUserMainMenuOption(Auxiliary.actOnQuitAndBack(inputFromUser(), emptySpaces), 0, emptySpaces, false, false);
                forLooping = getValueFromUser();
            }

            toReturn = true;
        }

        return toReturn;
    }

    /**
     * Here we return a player from the list but before this we check the index validity. Index is given by the variable "valueFromUser"
     */
    private Player checkIndexesAndReturnPlayer(int emptySpaces) throws InterruptedException {
        int indexFromUser;
        String errorMessageIndex = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR index is not available");
        Player valueToReturn = new Player("None", "None", 1, 2);

        if (!"0.1".equals(valueFromUser)) {
            indexFromUser = Integer.parseInt(valueFromUser) - 1;
            if ((indexFromUser > (registeredPlayers.size() - 1)) || (indexFromUser < 0)) {
                System.out.printf("%s", errorMessageIndex);
                TimeUnit.SECONDS.sleep(1);
            } else {
                valueToReturn = registeredPlayers.get(indexFromUser);
            }
         }

        return valueToReturn;
    }

    /**
     * Set the current player.
     */
    public Player setCurrentPlayer(int emptySpaces, int headerLength) throws InterruptedException {
        String toLoop = "";
        String errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR no player is registered!");
        Player valueToReturn = null;

        if (getRegisteredPlayers().isEmpty()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            valueToReturn = new Player("None", "None", 1, 2);
        } else {
            while (!"back".equals(toLoop)) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                changePlayers.printHeaderSubMenuMessage(2);
                System.out.printf("%n%s%s%n", " ".repeat(emptySpaces), "Registered players:");

                for (int i = 0; i < getRegisteredPlayers().size(); i++) {
                    System.out.printf("%s%s%s%s%s%s%s%s%n", " ".repeat(emptySpaces), (i + 1) + ". ", getRegisteredPlayers().get(i).getFirstName(), ", ",
                            getRegisteredPlayers().get(i).getLastName(), "; ", "Age: ", getRegisteredPlayers().get(i).getAge());
                }

                System.out.println();
                changePlayers.prettyPrintOfMessageSubMenu();
                valueFromUser = validateValueFromUserMainMenuOption(Auxiliary.actOnQuitAndBack(inputFromUser(), emptySpaces), registeredPlayers.size(), emptySpaces, false, false);

                if (!"back".equals(valueFromUser) && !"0.1".equals(valueFromUser)) {
                    valueToReturn = changePlayers.changeCurrentPlayer(checkIndexesAndReturnPlayer(emptySpaces));

                    for (ApplicationMenu element : modules) {
                        element.setCurrentPlayer(valueToReturn);
                    }
                } else {
                    toLoop = valueFromUser;
                }
            }
        }

        return valueToReturn;
    }

    /**
     * Print the help guide for the app.
     */
    public void printingHelp(int emptySpaces) throws InterruptedException {
        String toReturn = "none";

        while (!"back".equals(toReturn)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            printingHelpFromMainMenu.printHeaderSubMenuMessage(2);
            System.out.printf("%n%s%s%n%s%n%s%n%n", " ".repeat(2) , "As you can see from the menu you can add a player, change player that will play, print them, change difficulty (there are two levels, one or two digits).",
                    " ".repeat(emptySpaces) + "For math operations you have addition, subtraction, multiplication or division. Also a random options exists where a random operation will be chosen automatically.",
                    " ".repeat(emptySpaces) + "When a student is asked the result of an equation, the score is tracked and we can see how many times a student was right or not.");

            printingHelpFromMainMenu.prettyPrintOfMessageSubMenu();
            valueFromUser = validateValueFromUserMainMenuOption(Auxiliary.actOnQuitAndBack(inputFromUser(), 2), 2, 2, false, false);

            toReturn = valueFromUser;
        }
    }

    /**
     * Choose difficulty level for the game.
     */
    public String chooseDifficultyLevel(int emptySpaces) throws InterruptedException {
        String forLoop = "";

        while (!"back".equals(forLoop) && !"1".equals(forLoop) && !"2".equals(forLoop)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            changeDifficulty.printHeaderWithPredefineStyle(2, 2);
            changeDifficulty.prettyPrintMenu(2);

            forLoop = inputFromUser();

            switch (forLoop) {
                case "1" -> {
                    System.out.printf("%n%s%s%n", " ".repeat(emptySpaces), "difficulty level 1 is selected...");
                    TimeUnit.SECONDS.sleep(1);
                } case "2" -> {
                    System.out.printf("%n%s%s%n", " ".repeat(emptySpaces), "difficulty level 2 is selected...");
                    TimeUnit.SECONDS.sleep(1);
                } case "3" -> {
                    forLoop = Auxiliary.actOnQuitAndBack("back", emptySpaces);
                } case "4" -> {
                    Auxiliary.actOnQuitAndBack("quit", emptySpaces);
                } default -> forLoop = validateValueFromUserMainMenuOption(forLoop, 4, emptySpaces, true, false);
            }
        }

        return forLoop;
    }

    /**
     *
     * After each question answered correctly points are added to player.
     */
    private void addPointsPerPlayer(MathQuestion typeOfOperation, boolean valueToCheck) {
        if (valueToCheck) {
            Player toUse = getCurrentPlayerForMainMenu();

            for (int i = 0; i < registeredPlayers.size(); i++) {
                if (registeredPlayers.get(i).getFirstName().equals(toUse.getFirstName())) {
                    pointsEarnedPerPlayer.set(i, (pointsEarnedPerPlayer.get(i) + typeOfOperation.getNumberOfPoints()));
                    break;
                }
            }
        }
    }

    /**
     * Here we ask the math question and points are set for each type of question.
     */
    public String askQuestion(int emptySpaces, int difficultyLevel, String operation) throws InterruptedException {
        String toReturn = "", questionToAsk = "";
        int limitForValidation = Integer.MAX_VALUE;
        int numberPoints = 0;

        if (operation.equals("addition") || operation.equals("subtraction")) {
            numberPoints = 1;
        } else if (operation.equals("division")) {
            numberPoints = 5;
        } else {
            numberPoints = 3;
        }

        MathQuestion questionToBeUse = new MathQuestion(numberPoints, emptySpaces, difficultyLevel, operation);
        questionToBeUse.addMessageForQuestions("how much is, please calculate, " +
                "give us the answer for, you know the answer to");
        boolean isCorrect = true;

        while (!"back".equals(toReturn)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            askAMathQuestion.printHeaderSubMenuMessage(2);

            if (isCorrect) {
                questionToAsk = questionToBeUse.generateQuestion(null);
            } else {
                System.out.printf("%n%s%n", questionToAsk);
            }

            System.out.println();
            askAMathQuestion.prettyPrintOfMessageSubMenu();

            toReturn = validateValueFromUserMainMenuOption(Auxiliary.actOnQuitAndBack(inputFromUser(), emptySpaces), limitForValidation, emptySpaces, false, true);

            if (!"0.1".equals(toReturn) && !"back".equals(toReturn)) {
                isCorrect = questionToBeUse.validateInputAndCheckResults(toReturn);
                addPointsPerPlayer(questionToBeUse, isCorrect);
            }
        }

        return toReturn;
    }

    /**
     * Here we are running the askQuestion method.
     */
    public void implementQuestion(String questionType, int emptySpaces) throws InterruptedException {
        if ((this.getCurrentPlayerForMainMenu() == null || getCurrentPlayerForMainMenu().getFirstName().equals("None")) || this.difficultyLevel == null) {
            System.out.printf("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR please choose a difficulty level first, register a player and select it to play!");
            TimeUnit.SECONDS.sleep(1);
        } else {
            askQuestion(emptySpaces, Integer.parseInt(difficultyLevel), questionType);
        }
    }

    /**
     * We are running chooseDifficultyLevel method.
     */
    public String implementChangeDifficulty(int emptySpaces) throws InterruptedException {
        String toUseAsReturn;
        toUseAsReturn = chooseDifficultyLevel(emptySpaces);

        if ("1".equals(toUseAsReturn) || "2".equals(toUseAsReturn)) {
            this.difficultyLevel = toUseAsReturn;
        }

        return toUseAsReturn;
    }

    /**
     * Printing the ranking of all players.
     */
    public void printRanking(int emptySpaces) throws InterruptedException {
        String valueToReturn = "";
        String errorNoPlayer = String.format("%n%s\033[31m%s\033[0m%n", " ".repeat(emptySpaces), "ERROR no player is registered!");

        if (registeredPlayers.isEmpty()) {
            System.out.printf("%s", errorNoPlayer);
            TimeUnit.SECONDS.sleep(1);
        } else {
            while (!"back".equals(valueToReturn)) {
                System.out.print("\033[H\033[2J");
                System.out.flush();

                printingRankingSystem.printHeaderSubMenuMessage(2);

                System.out.printf("%n%s%s", " ".repeat(emptySpaces), "name/points");
                for (int i = 0; i < registeredPlayers.size(); i++) {
                    System.out.printf("%n%s%s%s %s", " ".repeat(emptySpaces), (i + 1) + ". ",
                            registeredPlayers.get(i).getFirstName() + ", " + registeredPlayers.get(i).getLastName(), pointsEarnedPerPlayer.get(i));
                }

                System.out.printf("%n%n");
                printingRankingSystem.prettyPrintOfMessageSubMenu();

                valueToReturn = validateValueFromUserMainMenuOption(Auxiliary.actOnQuitAndBack(inputFromUser(), emptySpaces), 0, 2, false, false);
            }
        }
    }

    /**
     * Act on the input from the user on main menu,
     */
    public Player actOnInputForMainMenuSelectOperation(int emptySpaces, int numberOfEntries, int headerLength) throws InterruptedException {
        switch (valueFromUser) {
            case "1" -> implementQuestion("addition", emptySpaces);
             case "2" -> implementQuestion("subtraction", emptySpaces);
             case "3" -> implementQuestion("multiplication", emptySpaces);
             case "4" -> implementQuestion("division", emptySpaces);
             case "5" -> implementChangeDifficulty(emptySpaces);
             case "6" -> registerPlayer();
             case "7" -> this.currentPlayerForMainMenu = setCurrentPlayer(emptySpaces, headerLength);
             case "8" -> printAllPlayers(emptySpaces, headerLength);
             case "9" -> printRanking(emptySpaces);
             case "10" -> printingHelp(emptySpaces);
             case "11" -> Auxiliary.toQuit(emptySpaces);
             default ->  validateValueFromUserMainMenuOption(valueFromUser, numberOfEntries, emptySpaces, true, false);
        }

        return currentPlayerForMainMenu;
    }

    /**
     * With this we take the input from user and save it in a variable.
     */
    public String inputFromUser() {
        this.valueFromUser = this.input.nextLine();
        return valueFromUser;
    }
}
