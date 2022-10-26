package seedu.boba.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.boba.commons.exceptions.IllegalValueException;
import seedu.boba.model.BobaBot;
import seedu.boba.model.ReadOnlyBobaBot;
import seedu.boba.model.person.Person;

/**
 * An Immutable BobaBot that is serializable to JSON format.
 */
@JsonRootName(value = "bobabot")
class JsonSerializableBobaBot {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableBobaBot} with the given persons.
     */
    @JsonCreator
    public JsonSerializableBobaBot(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyBobaBot} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableBobaBot}.
     */
    public JsonSerializableBobaBot(ReadOnlyBobaBot source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code BobaBot} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public BobaBot toModelType() throws IllegalValueException {
        BobaBot bobaBot = new BobaBot();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (bobaBot.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            bobaBot.addPerson(person);
        }
        return bobaBot;
    }

}
