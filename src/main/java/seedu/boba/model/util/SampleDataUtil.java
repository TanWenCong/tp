package seedu.boba.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.boba.model.BobaBot;
import seedu.boba.model.ReadOnlyBobaBot;
import seedu.boba.model.person.BirthdayMonth;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Name;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.tag.Tag;

/**
 * Contains utility methods for populating {@code BobaBot} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new BirthdayMonth("1"), new Reward("4200"),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new BirthdayMonth("3"), new Reward("6900"),
                getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new BirthdayMonth("5"), new Reward("12345"),
                getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new BirthdayMonth("6"), new Reward("0"),
                getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new BirthdayMonth("12"), new Reward("4000"),
                getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new BirthdayMonth("9"), new Reward("5700"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyBobaBot getSampleBobaBot() {
        BobaBot sampleBoba = new BobaBot();
        for (Person samplePerson : getSamplePersons()) {
            sampleBoba.addPerson(samplePerson);
        }
        return sampleBoba;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
