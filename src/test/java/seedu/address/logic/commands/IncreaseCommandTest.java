package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCustomers.ALICE_INCREMENT;
import static seedu.address.testutil.TypicalCustomers.getTypicalAddressBook;
import static seedu.address.testutil.TypicalEmails.EMAIL_FIRST_PERSON;
import static seedu.address.testutil.TypicalEmails.EMAIL_SECOND_PERSON;
import static seedu.address.testutil.TypicalPhones.PHONE_FIRST_PERSON;
import static seedu.address.testutil.TypicalPhones.PHONE_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.customer.Customer;
import seedu.address.model.customer.Email;
import seedu.address.model.customer.Phone;

/**
 * Contains integration tests (interaction with the Model) and unit tests for IncreaseCommand.
 */
public class IncreaseCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_phoneAllFieldsSpecifiedUnfilteredList_success() {
        Customer editedCustomer = ALICE_INCREMENT;
        IncreaseCommand increaseCommand = new IncreaseCommand(PHONE_FIRST_PERSON, "100");

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCustomer);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedCustomer);
        assertCommandSuccess(increaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emailAllFieldsSpecifiedUnfilteredList_success() {
        Customer editedCustomer = ALICE_INCREMENT;
        IncreaseCommand increaseCommand = new IncreaseCommand(EMAIL_FIRST_PERSON, "100");

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedCustomer);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedCustomer);

        assertCommandSuccess(increaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_phoneInvalidPersonUnfilteredList_failure() {
        Phone outOfBoundPhone = new Phone("00000000");
        IncreaseCommand increaseCommand = new IncreaseCommand(outOfBoundPhone, "100");

        assertCommandFailure(increaseCommand, model, Messages.MESSAGE_INVALID_PERSON_INFORMATION);
    }

    @Test
    public void execute_emailInvalidPersonUnfilteredList_failure() {
        Email outOfBoundEmail = new Email("test@test.com");
        IncreaseCommand increaseCommand = new IncreaseCommand(outOfBoundEmail, "100");

        assertCommandFailure(increaseCommand, model, Messages.MESSAGE_INVALID_PERSON_INFORMATION);
    }

    @Test
    public void equals() {
        final IncreaseCommand standardCommand = new IncreaseCommand(PHONE_FIRST_PERSON, "100");

        // same values -> returns true
        IncreaseCommand commandWithSameValues = new IncreaseCommand(PHONE_FIRST_PERSON, "100");
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different phone -> returns false
        assertFalse(standardCommand.equals(new IncreaseCommand(PHONE_SECOND_PERSON, "100")));

        // different email -> returns false
        assertFalse(standardCommand.equals(new IncreaseCommand(EMAIL_SECOND_PERSON, "100")));

        // different reward value -> returns false
        assertFalse(standardCommand.equals(new IncreaseCommand(PHONE_FIRST_PERSON, "200")));
    }

}