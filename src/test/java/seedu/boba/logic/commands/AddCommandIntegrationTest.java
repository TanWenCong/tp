package seedu.boba.logic.commands;

import static seedu.boba.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.boba.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.boba.testutil.TypicalPersons.getTypicalBobaBot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.boba.model.Model;
import seedu.boba.model.ModelManager;
import seedu.boba.model.UserPrefs;
import seedu.boba.model.person.Person;
import seedu.boba.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalBobaBot(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getBobaBot(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validPerson), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getBobaBot().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model, AddCommand.MESSAGE_DUPLICATE_CUSTOMER);
    }

}
