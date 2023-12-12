package io.mathquiz.generatequestionsandvalidateanswers;

import io.mathquiz.checks.Auxiliary;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * With this we generate the questions with random numbers and we evaluate the answers given by the player.
 * Custom messages are given in this interaction between player and the game.
 */
@Getter
public class MathQuestion {
    private final Random rnd;
    private Integer firstNumber;
    private Integer chooseIndex;
    private Integer secondNumber;
    private final Integer numberOfPoints;
    private Integer emptySpaces;
    private Integer difficultyLevel;
    private final List<String> messagesToUse;
    protected List<String> correctTypeAnswers;
    protected List<String> wrongTypeAnswers;
    private String typeOfEquation;

    /**
     * We defined each question with a message to use, difficulty level (numbers with 1 or 2 integers), number of points
     * per question, and what type of answers app will give in case of wrong or correct answers.
     */
    public MathQuestion(Integer numberOfPoints, Integer emptySpaces,
                        Integer difficultyLevel, String typeOfEquation) throws InterruptedException {

        this.numberOfPoints = Auxiliary.validateNumberOfPointsMathQuestions(numberOfPoints, 2);
        this.messagesToUse = new ArrayList<>();
        this.rnd = new Random();
        this.chooseIndex = 0;
        this.emptySpaces = Auxiliary.validateEmptySpaces(emptySpaces);
        this.difficultyLevel = Auxiliary.validateNumberOfPointsMathQuestions(difficultyLevel, 2);
        this.correctTypeAnswers = new ArrayList<>(List.of("Very Good!", "Excellent!", "Nice work!", "Keep up the good work!"));
        this.wrongTypeAnswers = new ArrayList<>(List.of("No. Please try again.", "Wrong. Try once more.", "Don't give up!", "No. Keep trying."));
        this.typeOfEquation = typeOfEquation;
    }

    public Integer getFirstNumber() {
        setFirstNumber();
        return firstNumber;
    }

    public Integer getSecondNumber() {
        setSecondNumber();
        return secondNumber;
    }

    public Integer getUnalteredFirstNumber() {
        return firstNumber;
    }

    public Integer getUnalteredSecondNumber() {
        return secondNumber;
    }

    public List<String> getMessages() {
        return messagesToUse;
    }

    /**
     * Get a random message per question from a list.
     */
    public String getRandomMessage() throws InterruptedException {
        String errorMessage = String.format("%n%s%s%n", " ".repeat(emptySpaces), "ERROR there are no messages to be used in the list!");
        String messageToUse;

        if (getMessages().isEmpty()) {
            System.out.printf("%s", errorMessage);
            TimeUnit.SECONDS.sleep(1);
            messageToUse = "none";
        } else {
            setChooseIndex();
            messageToUse = messagesToUse.get(getChooseIndex());
        }

        return messageToUse;
    }

    /**
     * Set/random generate the first number used in the question.
     */
    public void setFirstNumber() {
        if (getDifficultyLevel() == 1) {
            this.firstNumber = rnd.nextInt(8) + 1;
        } else {
            this.firstNumber = rnd.nextInt(90) + 10;
        }
    }

    /**
     * Set/random generate the second number in the qquestion.
     */
    public void setSecondNumber() {
        if (getDifficultyLevel() == 1) {
            this.secondNumber = rnd.nextInt(8) + 1;
        } else {
            this.secondNumber = rnd.nextInt(90) + 10;
        }
    }

    /**
     * Choose which item to choose from the messages list for the question.
     */
    public void setChooseIndex() {
        this.chooseIndex = rnd.nextInt(messagesToUse.size());
    }

    public String generateQuestion(String value) throws InterruptedException {
        String question = null;

        if (value != null) {
            typeOfEquation = value;
        }

        switch (typeOfEquation) {
            case "addition" -> question = " ".repeat(getEmptySpaces()) + getRandomMessage() + " " + getFirstNumber() + " + " + getSecondNumber() + " ?";
             case "subtraction" -> question = " ".repeat(getEmptySpaces()) + getRandomMessage() + " " + getFirstNumber() + " - " + getSecondNumber() + " ?";
             case "multiplication" -> question = " ".repeat(getEmptySpaces()) + getRandomMessage() + " " + getFirstNumber() + " * " + getSecondNumber() + " ?";
             case "division" -> question = " ".repeat(getEmptySpaces()) + getRandomMessage() + " " + getFirstNumber() + " / " + getSecondNumber() + " ?";
        }

        System.out.printf("%n%s%n", question);
        return question;
    }

    /**
     * We validate input like what type of question we want, addition, subtraction and so on.
     * And then we check the result given by the user.
     */
    public boolean validateInputAndCheckResults(String valueFromUser) throws InterruptedException {
        int valueToCompareTo;
        boolean valueToReturn = true;
        int indexToUse = rnd.nextInt((correctTypeAnswers.size() - 1));
        String errorMessageOptionType = String.format("%n\033[31m%s%s\033[0m%n", " ".repeat(emptySpaces), "ERROR please use an option from those that were given!");

        switch (typeOfEquation) {
            case "addition" -> valueToCompareTo = (getUnalteredFirstNumber() + getUnalteredSecondNumber());
             case "subtraction" -> valueToCompareTo = (getUnalteredFirstNumber() - getUnalteredSecondNumber());
             case "multiplication" -> valueToCompareTo = (getUnalteredFirstNumber() * getUnalteredSecondNumber());
             case "division" -> valueToCompareTo = (getUnalteredFirstNumber() / getUnalteredSecondNumber());
             default -> valueToCompareTo = 0;
        }

        try {
            if (valueToCompareTo == Integer.parseInt(valueFromUser)) {
                System.out.printf("%n%s\033[32m%s\033[0m%n", " ".repeat(getEmptySpaces()), correctTypeAnswers.get(indexToUse));
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.printf("%n%s\033[31m%s\033[0m%n", " ".repeat(getEmptySpaces()), wrongTypeAnswers.get(indexToUse));
                TimeUnit.SECONDS.sleep(1);
                valueToReturn = false;
            }
        } catch (NumberFormatException ignored) {
            System.out.printf("%s", errorMessageOptionType);
            TimeUnit.SECONDS.sleep(1);
            valueToReturn = false;
        }

        return valueToReturn;
    }

    /**
     * Add more custom messages for questions.
     */
    public boolean addMessageForQuestions(String messages) throws InterruptedException {
        boolean toReturn = false;
        List<String> toWorkOn = Auxiliary.validateMessageToBeAddedForQuestions(messages, getEmptySpaces());

        if (!toWorkOn.isEmpty()) {
            for (String element : toWorkOn) {
                if (!element.isBlank()) {
                    getMessages().add(element.trim());
                }
            }
            toReturn = true;
        } else {
            getMessages().add("No valid message for question have been added");
        }

        return toReturn;
    }
}
