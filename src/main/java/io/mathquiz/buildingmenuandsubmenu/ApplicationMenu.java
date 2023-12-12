package io.mathquiz.buildingmenuandsubmenu;

import io.mathquiz.createplayer.Player;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Here we will construct the main menu for this app. The menu will have 11 entries, like 11 submenus, and they will be build like modules
 * so we can disable/enable the module we want but still the application will work.

 * When creating those modules we will populate a list that gathers them in order for us to be able to parse them quickly and use them
 * accordingly.

 * The list that hold the modules is build in the CatchInputAndActOnIt class and each module is in fact an object from ApplicationSubMenu Class,
 * objects that are accessed from this object, class ApplicationMenu.

 * Here you can see that we defined the header message and we build the header in this class, there is no separate class for header.

 * Also current player is defined here because we can register multiple players and we can browse these players and choose one of them and all games played are register.

 * Rankings and stats are kept for each player, and we can print them.
 */
@Getter
public class ApplicationMenu {
    private final String headerMessage;
    private final LinkedList<String> options;
    private final int numberOfEntriesInTheMenu;
    private final int numberOfSpacesForMainMenu;
    private Player currentPlayer;

    /**
     * Here we validate the header message and build the custom header for the app. This building phase of header needs to be optimized,
     * but due to the fact that it is executed only once when the header is being built, and then it is saved, is not alarming.
     */
    protected static String validateHeaderMessage(String headerMessage, int numberOfSpaces) throws InterruptedException {
        boolean isAdded;
        SecureRandom rnd = new SecureRandom();
        String errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(2), "ERROR please use a valid header message with at least two words!");
        StringBuilder processedHeaderMessage = new StringBuilder();
        String[][] leetChars = {{"A", "@"}, {"B", "|3 "}, {"C", "("}, {"D", "[)"}, {"E", "3"}, {"F", "ph"}, {"G", "gee"}, {"H", "|-|"},
                {"I", "1"}, {"M", "/V\\"}, {"O", "oh"}, {"S", "5"}, {"U", "|_|"}};
        List<String> wordsExtracted = new ArrayList<>(List.of(headerMessage.split(" ")));

        if (wordsExtracted.size() < 2) {
            System.out.println(errorMessage);
            TimeUnit.SECONDS.sleep(1);
            System.out.printf("%s%s%n", " ".repeat(numberOfSpaces), "Quiting...");
            System.exit(0);
        } else {
            for (String word : wordsExtracted) {
                if (!word.isBlank()) {
                    for (int j = 0; j < word.length(); j++) {
                        isAdded = false;
                        for (String[] element : leetChars) {
                            if (String.valueOf(word.trim().toLowerCase().charAt(j)).equals(element[0].toLowerCase()) &&
                                    (j != rnd.nextInt(word.length() - 1))) {
                                isAdded = true;
                                processedHeaderMessage.append(element[1]);
                            }
                        }
                        if (!isAdded) {
                            processedHeaderMessage.append(word.toLowerCase().charAt(j));
                        }
                    }
                    processedHeaderMessage.append("  ");
                } else {
                    System.out.println(errorMessage);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
        return processedHeaderMessage.toString();
    }


    /**
     *
     * Here we take how many entries to have in the menu and we validate the number.
     */
    protected static int validateNumberOfEntriesInMenu(int numberOfEntriesInTheMenu) throws InterruptedException {
        String errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(2), "ERROR please we should have at least 1 option along with \"quit\" in the menu!");

        if (numberOfEntriesInTheMenu < 1) {
            System.out.printf(errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
            return (numberOfEntriesInTheMenu + 1);
        }

        return 1;
    }

    /**
     *Here we validate the numbers of spaces in front of the menu.
     */
    private static int validateNumberOfSpacesInFront(int numberOfSpacesInFront) throws InterruptedException {
        String errorMessage;

        if (numberOfSpacesInFront < 0) {
            numberOfSpacesInFront = 2;
            errorMessage = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(numberOfSpacesInFront), "ERROR please use an integer greater or equal to zero!");
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        }

        return numberOfSpacesInFront;
    }

    /**
     * We validate the player given which is an object from Player class.
     */
    private Player validateCurrentPlayer(Player currentPlayer) {
        List<String> listWithAttributes = new ArrayList<>(List.of(currentPlayer.getFirstName(),
                currentPlayer.getLastName(), String.valueOf(currentPlayer.getAge())));
        int intCounters = 0;

        try {
            for (String listWithAttribute : listWithAttributes) {
                Double.parseDouble(listWithAttribute);
                intCounters++;
            }
        } catch (Exception e) {
            intCounters++;
        }

        if (intCounters == 1) {
            return currentPlayer;
        } else {
            return null;
        }
    }

    /**
     *
     * We are building the applicationMenu object.
     */
    public ApplicationMenu(String headerMessage, int numberOfEntriesInTheMenu,
                           int numberOfSpacesForMainMenu) throws InterruptedException {
        this.headerMessage = validateHeaderMessage(headerMessage, numberOfSpacesForMainMenu);
        this.numberOfEntriesInTheMenu = validateNumberOfEntriesInMenu(numberOfEntriesInTheMenu);
        this.options = new LinkedList<>(Arrays.asList("quit"));
        this.numberOfSpacesForMainMenu = validateNumberOfSpacesInFront(numberOfSpacesForMainMenu);
        this.currentPlayer = null;
    }

    public List<String> getMenuOptions() {
        return options;
    }

    public int getNumberOfOptions() {
        return getMenuOptions().size();
    }

    /**
     * In this class we register the menu options/submenu as options and put them in a list
     * then are build in the CatchInputAndActOnIt class.
     */
    public boolean addMenuOption(String menuOption) throws InterruptedException {
        String errorMessage = String.format("\033[31m%s%s\033[0m%n", " ".repeat(2), "ERROR please use a valid option with at least one word!");
        boolean valueToBeReturned = false;

        if (menuOption.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        } else {
                ArrayList<String> tempOptionsSplit = new ArrayList<>(List.of(menuOption.split(",")));

                for (int i = tempOptionsSplit.size() - 1; i >= 0; i--) {
                    if (options.size() <= numberOfEntriesInTheMenu) {
                        options.push(tempOptionsSplit.get(i).trim().toLowerCase());
                        tempOptionsSplit.remove(i);
                    } else {
                        System.out.printf("%s%s%s%s%n", " ".repeat(2), (numberOfEntriesInTheMenu + 1) - tempOptionsSplit.size()," menu options were added - success, options remaining, ", tempOptionsSplit);
                        TimeUnit.SECONDS.sleep(1);
                        break;
                    }
                }

                valueToBeReturned = true;
        }

        return valueToBeReturned;
    }

    /**
     *
     * Print the menu in a more beautified state.
     */
    public boolean prettyPrintMenu(int numberOfSpacesInFront) throws InterruptedException {
        boolean valueToBeReturned = false;
        String errorEmptyMenuOptionsList = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(2), "ERROR menu options list is empty!");

        if (numberOfSpacesInFront < 0) {
            numberOfSpacesInFront = 0;
        }

        if (options.size() <= 1) {
            System.out.printf("%s", errorEmptyMenuOptionsList);
            TimeUnit.SECONDS.sleep(1);
        } else {
            System.out.println();
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("%s%s%-2s%s%s%n", " ".repeat(numberOfSpacesInFront), "[ ", (i + 1),  " ] ", options.get(i));
            }

            System.out.printf("%n%s%s"," ".repeat(numberOfSpacesInFront), "-".repeat(getHeaderMessage().length() * 2));
            System.out.printf("%n%s%s", " ".repeat(numberOfSpacesInFront), "please choose an option: ");
            valueToBeReturned = true;
        }

        return valueToBeReturned;
    }

    /**
     * Print the beautified header message.
     */
    public void printPrettyHeaderMessage(int numberOfSpacesInFront, int numberOfEmptyLinesAbove, boolean lineAbove, boolean lineBelow) {
        if (numberOfSpacesInFront < 0) {
            numberOfSpacesInFront = 0;
        }

        System.out.printf("%n".repeat(numberOfEmptyLinesAbove));
        if (lineAbove) {
            System.out.printf("%s%s%n", " ".repeat(numberOfSpacesInFront), "-".repeat((getHeaderMessage().length() * 2)));
        }

        System.out.printf("%s%s%n",  " ".repeat(numberOfSpacesInFront + (getHeaderMessage().length() / 2)), getHeaderMessage());
        if (lineBelow) {
            System.out.printf("%s%s%n", " ".repeat(numberOfSpacesInFront), "-".repeat((getHeaderMessage().length() * 2)));

            if (null != getCurrentPlayer() && !"none".equals(getCurrentPlayer().getFirstName())) {
                System.out.printf("%s%s%s%n", " ".repeat(numberOfSpacesInFront), "current_player: ", getCurrentPlayer().getFirstName() + ", " + getCurrentPlayer().getLastName());
            } else {
                System.out.printf("%s%s%n", " ".repeat(numberOfSpacesInFront), "current_player: not_selected");
            }
        }
    }

    public void printHeaderWithPredefineStyle(int numberOfSpacesInFront, int numberOfEmptyLinesAbove) {
        this.printPrettyHeaderMessage(numberOfSpacesInFront, numberOfEmptyLinesAbove, true, true);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = validateCurrentPlayer(currentPlayer);
    }
}
