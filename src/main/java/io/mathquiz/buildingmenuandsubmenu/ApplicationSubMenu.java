package io.mathquiz.buildingmenuandsubmenu;

import io.mathquiz.createplayer.Player;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Each SubApplicationMenu it's a module which is in fact a submenu from the main menu and are being build
 * in CatchInputAndActOnIt class, but names for this modules (so name of a submenu/option) are constructed and validated
 * in ApplicationMenu object.
 */
@Getter
public final class ApplicationSubMenu extends ApplicationMenu {
    private final String messageForTheSubMenu;
    private final LinkedList<String> subMenuOptions;
    private final int numberOfSpacesInFront;

    /**
     * Here we validate the message for the submenu that appears in the header after the name of the game.
     */
    private static String validateMessageForTheMenu(String messageForTheMenu, int numberOfSpaces) throws InterruptedException {
        String errorMessage = String.format("%s%s%n", " ".repeat(numberOfSpaces),"ERROR please use a valid message/sentence for the submenu!");
        String messageProcessed;

        if (messageForTheMenu.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            messageProcessed = "None";
        } else {
            messageProcessed = messageForTheMenu.trim().toLowerCase();
        }

        return messageProcessed;
    }

    /**
     * We validate the number of spaces in front of the submenu.
     */
    private static int validateNumberOfSpacesInFront(int numberOfSpacesInFront) throws InterruptedException {
        String errorMessage;

        if (numberOfSpacesInFront < 0) {
            numberOfSpacesInFront = 2;
            errorMessage = String.format("\033[31m%s%s\033[0m%n", " ".repeat(numberOfSpacesInFront), "ERROR please use an integer greater or equal to zero!");
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
        }

        return numberOfSpacesInFront;
    }

    /**
     * Construction of the module/submenu in the main menu
     */
    public ApplicationSubMenu(String headerMessage, int numberOfEntriesInTheMenu, int numberOfSpacesForMainMenu,
                              String messageForTheSubMenu, int numberOfSpacesInFront) throws InterruptedException {
        super(headerMessage, numberOfEntriesInTheMenu, numberOfSpacesForMainMenu);

        this.messageForTheSubMenu = validateMessageForTheMenu(messageForTheSubMenu, numberOfSpacesInFront);
        this.subMenuOptions = new LinkedList<>(List.of("(back/quit)"));
        this.numberOfSpacesInFront = validateNumberOfSpacesInFront(numberOfSpacesInFront);
    }

    /**
     * Here we have the same thing, each submenu can have options/submenus....here we add a submenu in an secondary menu.
     */
    public boolean addSubMenuOption(String givenOptionToAdd, int numberOfSpaces) throws InterruptedException {
        boolean toBeReturned = true;

        String errorMessage = String.format("\033[31m%s%s\033[0m%n", " ".repeat(numberOfSpaces),"ERROR please use a valid option with at least a word or multiple options separated by a comma!");
        List<String> tempListWithOptions = new LinkedList<>(List.of(givenOptionToAdd.trim().toLowerCase().split(",")));
        List<String> itemsAlreadyInTheSubMenu = new LinkedList<>();

        if (givenOptionToAdd.isBlank()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            toBeReturned = false;
        } else {
            for (int i = tempListWithOptions.size() - 1; i >= 0; i--) {
                String item = tempListWithOptions.get(i).trim().toLowerCase();
                if (!subMenuOptions.contains(item)) {
                    subMenuOptions.push(item);
                } else {
                    itemsAlreadyInTheSubMenu.add(item);
                }
            }
        }

        return toBeReturned;
    }

    /**
     * Print the message for the submenu.
     */
    public void prettyPrintOfMessageSubMenu() {
        String toBePrinted = String.join(", ", subMenuOptions.subList(0, subMenuOptions.size() - 1)) + " ";

        System.out.printf("%s%s%n"," ".repeat(numberOfSpacesInFront), "-".repeat(getHeaderMessage().length() * 2));

        if (toBePrinted.isBlank()) {
            System.out.printf("%s%s %s%s", " ".repeat(numberOfSpacesInFront), messageForTheSubMenu, subMenuOptions.getLast(), ": ");
        } else {
            System.out.printf("%s%s %s%s%s", " ".repeat(numberOfSpacesInFront), messageForTheSubMenu, toBePrinted, subMenuOptions.getLast(), ": ");
        }
    }

    /**
     * Print a more beautified message in the header.
     */
    public void printHeaderMessage(int numberOfEmptyLinesAbove, boolean lineAbove, boolean lineBelow) {
        super.printPrettyHeaderMessage(getNumberOfSpacesForMainMenu(), numberOfEmptyLinesAbove, lineAbove, lineBelow);
    }

    /**
     * Print the message for the header in the submenu.
     */
    public void printHeaderSubMenuMessage(int numberOfEmptyLinesAbove) {
        this.printHeaderMessage(numberOfEmptyLinesAbove, true, true);
    }

    /**
     *
     * Change the player and choose one from the registerd playuers.
     */
    public Player changeCurrentPlayer(Player newPlayer) throws InterruptedException {
        Player oldValue = getCurrentPlayer();

        setCurrentPlayer(newPlayer);

        if (!getCurrentPlayer().equals(oldValue)) {
            System.out.printf("%n\033[32m%s%s%s%s%s\033[0m%n", " ".repeat(getNumberOfSpacesForMainMenu()), "player changed! current player: ",
                    getCurrentPlayer().getFirstName(), ", ", getCurrentPlayer().getLastName());
            TimeUnit.SECONDS.sleep(1);
            oldValue = newPlayer;
        } else {
            System.out.printf("%n\033[32m%s%s\033[0m%n", " ".repeat(getNumberOfSpacesForMainMenu()), "current player was not changed");
        }

        return oldValue;
    }
}
