package seedu.boba.logic.commands;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_BIRTHDAY_MONTH;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_REWARD;
import static seedu.boba.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.boba.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;
import seedu.boba.commons.core.Messages;
import seedu.boba.commons.core.index.Index;
import seedu.boba.commons.util.CollectionUtil;
import seedu.boba.logic.commands.exceptions.CommandException;
import seedu.boba.logic.parser.exceptions.ParseException;
import seedu.boba.model.Model;
import seedu.boba.model.person.BirthdayMonth;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Name;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.person.exceptions.PersonNotFoundException;
import seedu.boba.model.tag.Tag;

/**
 * Edits the details of an existing Customer in bobaBot.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the phone number/ email address used to register for membership. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: p/PHONE_NUMBER or e/EMAIL \n"
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_BIRTHDAY_MONTH + "BIRTHDAY_MONTH] "
            + "[" + PREFIX_REWARD + "REWARD] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " p/98349032  or  " + COMMAND_WORD + " e/example@gmail.com"
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CUSTOMER = "This customer already exists in bobaBot";

    private final EditPersonDescriptor editPersonDescriptor;

    private Phone phoneIdentifier = null;
    private Email emailIdentifier = null;
    private Index index;

    /**
     * @param phoneIdentifier current phone number of the person
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Phone phoneIdentifier, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(phoneIdentifier);
        requireNonNull(editPersonDescriptor);

        this.phoneIdentifier = phoneIdentifier;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    /**
     * @param emailIdentifier current email address of the person
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Email emailIdentifier, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(emailIdentifier);
        requireNonNull(editPersonDescriptor);

        this.emailIdentifier = emailIdentifier;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException, ParseException {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        try {
            this.index = !isNull(this.phoneIdentifier)
                    ? Index.fromZeroBased(model.findNum(phoneIdentifier))
                    : Index.fromZeroBased(model.findEmail(emailIdentifier));
        } catch (PersonNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_INFORMATION);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_INFORMATION);
        }
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        Predicate<Person> filterPersonToEdit = p -> !p.equals(personToEdit);
        FilteredList<Person> filteredListWithoutTarget = model.getBobaBot().getPersonList()
                .filtered(filterPersonToEdit);

        if (filteredListWithoutTarget.contains(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_CUSTOMER);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        BirthdayMonth updatedBirthdayMonth = editPersonDescriptor.getBirthdayMonth()
            .orElse(personToEdit.getBirthdayMonth());
        Reward updatedReward = editPersonDescriptor.getReward().orElse(personToEdit.getReward());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedBirthdayMonth, updatedReward, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return (isNull(emailIdentifier) && phoneIdentifier.equals(e.phoneIdentifier)
                || isNull(phoneIdentifier) && emailIdentifier.equals(e.emailIdentifier))
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private BirthdayMonth birthdayMonth;
        private Reward reward;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setBirthdayMonth(toCopy.birthdayMonth);
            setReward(toCopy.reward);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, reward, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setBirthdayMonth(BirthdayMonth birthdayMonth) {
            this.birthdayMonth = birthdayMonth;
        }

        public Optional<BirthdayMonth> getBirthdayMonth() {
            return Optional.ofNullable(birthdayMonth);
        }

        public void setReward(Reward reward) {
            this.reward = reward;
        }

        public Optional<Reward> getReward() {
            return Optional.ofNullable(reward);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getBirthdayMonth().equals(e.getBirthdayMonth())
                    && getReward().equals(e.getReward())
                    && getTags().equals(e.getTags());
        }
    }
}
