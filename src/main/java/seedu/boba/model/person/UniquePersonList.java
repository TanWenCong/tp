package seedu.boba.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.boba.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.boba.model.person.exceptions.DuplicatePersonException;
import seedu.boba.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}. As such, adding and updating of
 * persons uses Person#isSamePerson(Person) for equality so as to ensure that the person being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a person uses Person#equals(Object) so
 * as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Returns the index of the Customer with the same phone number.
     *
     * @param toCheck Phone number to check against the list
     * @return index of the Person with the same phone number in the list
     * @throws PersonNotFoundException if no Customer with corresponding phone number found
     */
    public int findNum(Phone toCheck) throws PersonNotFoundException {
        requireNonNull(toCheck);
        int index = internalList.stream().map(Person::getPhone).collect(Collectors.toList()).indexOf(toCheck);
        if (index == -1) {
            throw new PersonNotFoundException();
        }
        return index;
    }

    /**
     * Returns the index of the Customer with the same email.
     *
     * @param toCheck Email to check against the list
     * @return index of the Person with the same email in the list
     * @throws PersonNotFoundException if no Customer with corresponding email found
     */
    public int findEmail(Email toCheck) throws PersonNotFoundException {
        requireNonNull(toCheck);
        int index = internalList.stream().map(Person::getEmail).collect(Collectors.toList()).indexOf(toCheck);
        if (index == -1) {
            throw new PersonNotFoundException();
        }
        return index;
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param phone Phone number of the Customer of interest
     * @return the current Reward points of a Customer
     */
    public Reward getCurrentReward(Phone phone) {
        requireNonNull(phone);
        Optional<Reward> currentReward = internalList.stream().filter(person -> person.getPhone().equals(phone))
                .map(Person::getReward).findFirst();
        return currentReward.get();
    }

    /**
     * Returns the current Reward points of a Customer
     *
     * @param email Email of the Customer of interest
     * @return the current Reward points of a Customer
     */
    public Reward getCurrentReward(Email email) {
        requireNonNull(email);
        Optional<Reward> currentReward = internalList.stream().filter(person -> person.getEmail().equals(email))
                .map(Person::getReward).findFirst();
        return currentReward.get();
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     */
    public void add(Person toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(persons);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                        && internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
