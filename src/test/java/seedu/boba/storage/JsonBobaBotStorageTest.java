package seedu.boba.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.boba.testutil.Assert.assertThrows;
import static seedu.boba.testutil.TypicalPersons.ALICE;
import static seedu.boba.testutil.TypicalPersons.HOON;
import static seedu.boba.testutil.TypicalPersons.IDA;
import static seedu.boba.testutil.TypicalPersons.getTypicalBobaBot;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.boba.commons.exceptions.DataConversionException;
import seedu.boba.model.BobaBot;
import seedu.boba.model.ReadOnlyBobaBot;

public class JsonBobaBotStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonBobaBotStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readBobaBot_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readBobaBot(null));
    }

    private java.util.Optional<ReadOnlyBobaBot> readBobaBot(String filePath) throws Exception {
        return new JsonBobaBotStorage(Paths.get(filePath)).readBobaBot(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readBobaBot("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readBobaBot("notJsonFormatBobaBot.json"));
    }

    @Test
    public void readBobaBot_invalidPersonBobaBot_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readBobaBot("invalidPersonBobaBot.json"));
    }

    @Test
    public void readBobaBot_invalidAndValidPersonBobaBot_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readBobaBot("invalidAndValidPersonBobaBot.json"));
    }

    @Test
    public void readAndSaveBobaBot_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        BobaBot original = getTypicalBobaBot();
        JsonBobaBotStorage jsonAddressBookStorage = new JsonBobaBotStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveBobaBot(original, filePath);
        ReadOnlyBobaBot readBack = jsonAddressBookStorage.readBobaBot(filePath).get();
        assertEquals(original, new BobaBot(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveBobaBot(original, filePath);
        readBack = jsonAddressBookStorage.readBobaBot(filePath).get();
        assertEquals(original, new BobaBot(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveBobaBot(original); // file path not specified
        readBack = jsonAddressBookStorage.readBobaBot().get(); // file path not specified
        assertEquals(original, new BobaBot(readBack));

    }

    @Test
    public void saveBobaBot_nullBobaBot_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBobaBot(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveBobaBot(ReadOnlyBobaBot addressBook, String filePath) {
        try {
            new JsonBobaBotStorage(Paths.get(filePath))
                    .saveBobaBot(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveBobaBot_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBobaBot(new BobaBot(), null));
    }
}
