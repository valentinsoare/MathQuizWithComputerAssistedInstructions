package io.mathquiz.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Here are auxiliary checks to validate input.
 */
public class Auxiliary {

    private Auxiliary() {}

    /**
     * As you can see you catch the input, we validate it and in case user is using "quit" or "back"
     * we act on it and print a message accordingly. In case that input is blank we print an error return none.
     * In case input is not an error, nor quit or back we just forward the input, like return it.
     */
    public static String actOnQuitAndBack(String valueFromUserAsInput, int numberOfSpaces) throws InterruptedException {
        String errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(numberOfSpaces), "ERROR please use proper input or quit/back to return to main menu or quit!");
        String valueToReturn = valueFromUserAsInput;

        if (valueFromUserAsInput.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            valueToReturn = "none";
        } else {
            if ("quit".equals(valueFromUserAsInput.trim())) {
                System.out.printf("%n%s%s%n", " ".repeat(numberOfSpaces), "Quiting...");
                TimeUnit.SECONDS.sleep(1);
                System.exit(0);
            } else if ("back".equals(valueFromUserAsInput.trim())) {
                System.out.printf("%n%s%s%n", " ".repeat(numberOfSpaces), "Going back...");
                TimeUnit.SECONDS.sleep(1);
                valueToReturn = "back";
            }
        }

        return valueToReturn;
    }

    /**
     * Whith this we just quit, and it is used in the menu option, it is the quit option.
     */
    public static void toQuit(int numberOfSpaces) throws InterruptedException {
        System.out.printf("%n%s%s%n", " ".repeat(numberOfSpaces), "Quiting...");
        TimeUnit.SECONDS.sleep(1);
        System.exit(0);
    }

    /**
     * Each category of questions has a specific number of points, here we validate that number.
     * This check is optional.
     */
    public static int validateNumberOfPointsMathQuestions(int inputFromUser, int emptySpaces) throws InterruptedException {
        String errorMessage = String.format("%n%s%s%n", " ".repeat(emptySpaces), "ERROR please for points use a value greater than zero, if not 1 will be given!");

        if (inputFromUser <= 0) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            inputFromUser = 1;
        }

        return inputFromUser;
    }

    /**
     * Here we validate the difficulty level. Same like above, this can be an optional check.
     */
    public static int validateDifficultyLevel(int level, int emptySpaces) throws InterruptedException {
        String errorMessage = String.format("%n%s%s%n", " ".repeat(emptySpaces), "ERROR please use difficulty level of one or two digits!");

        if ((level < 1) || (level > 2)) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            level = 1;
        }

        return level;
    }

    /**
     *
     * Here we validate the empty spaces from the left of the screen.
     */
    public static int validateEmptySpaces(int emptySpaces) throws InterruptedException {
        String errorMessage = String.format("%n%s%s%n", " ".repeat(2), "ERROR please use a value greater or equal to zero for empty spaces!");

        if (emptySpaces < 0) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            emptySpaces = 2;
        }

        return emptySpaces;
    }

    /**
     *We validate the message for each question.
     */
    public static List<String> validateMessageToBeAddedForQuestions(String messagesFromUser, int emptySpaces) throws InterruptedException {
        String errorMessage = String.format("%n%s%s%n", " ".repeat(emptySpaces), "ERROR please use a string with messages split by a comma!");
        List<String> tempListWithMessages = new ArrayList<>();

        if (messagesFromUser.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            tempListWithMessages.addAll(List.of(messagesFromUser.trim().toLowerCase().split(",")));
        }

        return tempListWithMessages;
    }
}
