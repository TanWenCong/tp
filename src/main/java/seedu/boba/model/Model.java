package seedu.boba.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.boba.commons.core.GuiSettings;
import seedu.boba.model.exceptions.NextStateNotFoundException;
import seedu.boba.model.exceptions.PreviousStateNotFoundException;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.person.exceptions.PersonNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getBobaBotFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setBobaBotFilePath(Path bobaBotFilePath);

    /**
     * Replaces address book data with the data in {@code bobaBot}.
     */
    void setBobaBot(ReadOnlyBobaBot bobaBot);

    /** Returns the BobaBot */
    ReadOnlyBobaBot getBobaBot();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns the index of the person with the same phone number.
     *
     * @param phone Phone number to search
     * @return index of the person with the same phone number
     * @throws PersonNotFoundException if no person with corresponding phone number found
     */
    int findNum(Phone phone) throws PersonNotFoundException;

    /**
     * Returns the index of the person with the same email.
     *
     * @param email Email to search
     * @return index of the person with the same email
     * @throws PersonNotFoundException if no person with corresponding email found
     */
    int findEmail(Email email) throws PersonNotFoundException;

    /**
     * Returns the current Reward points of a Customer
     *
     * @param phone Phone number of the Customer of interest
     * @return the current Reward points of a Customer
     */
    Reward getCurrentReward(Phone phone);

    /**
     * Returns the current Reward points of a Customer
     *
     * @param email Email of the Customer of interest
     * @return the current Reward points of a Customer
     */
    Reward getCurrentReward(Email email);

    void commitBobaBot();

    void undoBobaBot() throws PreviousStateNotFoundException;

    void redoBobaBot() throws NextStateNotFoundException;
}
