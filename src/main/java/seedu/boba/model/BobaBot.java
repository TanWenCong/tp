package seedu.boba.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.person.UniquePersonList;
import seedu.boba.model.person.exceptions.PersonNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class BobaBot implements ReadOnlyBobaBot {

    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public BobaBot() {}

    /**
     * Creates an BobaBot using the Persons in the {@code toBeCopied}
     */
    public BobaBot(ReadOnlyBobaBot toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code BobaBot} with {@code newData}.
     */
    public void resetData(ReadOnlyBobaBot newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code BobaBot}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BobaBot // instanceof handles nulls
                && persons.equals(((BobaBot) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    /**
     * Returns the index of the person with the same phone number.
     *
     * @param phone Phone number to search
     * @return index of the person with the same phone number
     * @throws PersonNotFoundException if no person with corresponding phone number found
     */
    public int findNum(Phone phone) throws PersonNotFoundException {
        return persons.findNum(phone);
    }

    /**
     * Returns the index of the person with the same email.
     *
     * @param email Email to search
     * @return index of the person with the same email
     * @throws PersonNotFoundException if no person with corresponding email found
     */
    public int findEmail(Email email) throws PersonNotFoundException {
        return persons.findEmail(email);
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param phone Phone number of the Customer of interest
     * @return the current Reward points of a Customer
     */
    public Reward getCurrentReward(Phone phone) {
        return persons.getCurrentReward(phone);
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param email Email of the Customer of interest
     * @return the current Reward points of a Customer
     */
    public Reward getCurrentReward(Email email) {
        return persons.getCurrentReward(email);
    }
}
