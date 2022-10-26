package seedu.boba.model;

import static java.util.Objects.requireNonNull;
import static seedu.boba.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.boba.commons.core.GuiSettings;
import seedu.boba.commons.core.LogsCenter;
import seedu.boba.model.exceptions.NextStateNotFoundException;
import seedu.boba.model.exceptions.PreviousStateNotFoundException;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.person.exceptions.PersonNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedBobaBot versionedBobaBot;
    private final BobaBot bobaBot;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given bobaBot and userPrefs.
     */
    public ModelManager(ReadOnlyBobaBot bobaBot, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(bobaBot, userPrefs);

        logger.fine("Initializing with address book: " + bobaBot + " and user prefs " + userPrefs);

        this.bobaBot = new BobaBot(bobaBot);
        this.versionedBobaBot = new VersionedBobaBot(bobaBot);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.bobaBot.getPersonList());
    }

    public ModelManager() {
        this(new BobaBot(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getBobaBotFilePath() {
        return userPrefs.getBobaBotFilePath();
    }

    @Override
    public void setBobaBotFilePath(Path bobaBotFilePath) {
        requireNonNull(bobaBotFilePath);
        userPrefs.setBobaBotFilePath(bobaBotFilePath);
    }

    //=========== BobaBot ================================================================================

    @Override
    public void setBobaBot(ReadOnlyBobaBot bobaBot) {
        this.bobaBot.resetData(bobaBot);
    }

    @Override
    public ReadOnlyBobaBot getBobaBot() {
        return bobaBot;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return bobaBot.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        bobaBot.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        bobaBot.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        bobaBot.setPerson(target, editedPerson);
    }

    @Override
    public void commitBobaBot() {
        versionedBobaBot.commit(this.bobaBot);
    }

    @Override
    public void undoBobaBot() throws PreviousStateNotFoundException {
        versionedBobaBot.undo(this.bobaBot);
    }

    @Override
    public void redoBobaBot() throws NextStateNotFoundException {
        versionedBobaBot.redo(this.bobaBot);
    }

    /**
     * Returns the index of the person with the same phone number.
     *
     * @param phone Phone number to search
     * @return index of the person with the same phone number
     * @throws PersonNotFoundException if no person with corresponding phone number found
     */
    @Override
    public int findNum(Phone phone) throws PersonNotFoundException {
        requireNonNull(phone);
        return bobaBot.findNum(phone);
    }

    /**
     * Returns the index of the person with the same email.
     *
     * @param email Email to search
     * @return index of the person with the same email
     * @throws PersonNotFoundException if no person with corresponding email found
     */
    @Override
    public int findEmail(Email email) throws PersonNotFoundException {
        requireNonNull(email);
        return bobaBot.findEmail(email);
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param phone Phone number of the Customer of interest
     * @return the current Reward points of a Customer
     */
    @Override
    public Reward getCurrentReward(Phone phone) {
        requireAllNonNull(phone);
        return bobaBot.getCurrentReward(phone);
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param email Email of the Customer of interest
     * @return the current Reward points of a Customer
     */
    @Override
    public Reward getCurrentReward(Email email) {
        requireAllNonNull(email);
        return bobaBot.getCurrentReward(email);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedBobaBot}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return bobaBot.equals(other.bobaBot)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }
}
