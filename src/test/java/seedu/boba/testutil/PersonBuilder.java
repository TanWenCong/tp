package seedu.boba.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.boba.model.person.BirthdayMonth;
import seedu.boba.model.person.Email;
import seedu.boba.model.person.Name;
import seedu.boba.model.person.Person;
import seedu.boba.model.person.Phone;
import seedu.boba.model.person.Reward;
import seedu.boba.model.tag.Tag;
import seedu.boba.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_BIRTHDAY_MONTH = "1";
    public static final String DEFAULT_REWARD = "6900";

    private Name name;
    private Phone phone;
    private Email email;
    private BirthdayMonth birthdayMonth;
    private Reward reward;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        birthdayMonth = new BirthdayMonth(DEFAULT_BIRTHDAY_MONTH);
        reward = new Reward(DEFAULT_REWARD);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        birthdayMonth = personToCopy.getBirthdayMonth();
        reward = personToCopy.getReward();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Reward} of the {@code Person} that we are building.
     */
    public PersonBuilder withReward(String reward) {
        this.reward = new Reward(reward);
        return this;
    }

    /**
     * Sets the {@code BirthdayMonth} of the {@code Person} that we are building.
     */
    public PersonBuilder withBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = new BirthdayMonth(birthdayMonth);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, birthdayMonth, reward, tags);
    }

}
