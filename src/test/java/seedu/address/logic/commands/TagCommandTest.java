/*package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.index.Index.fromOneBased;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.TagCommand.MESSAGE_TAG_PERSONS_SUCCESS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;


/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
/*public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Index[] indices1 = {fromOneBased(1), fromOneBased(2), fromOneBased(3)};
    private final Index[] indices2 = {fromOneBased(4), fromOneBased(5)};
    private static final String DEFAULT_TAG_1 = "banker";
    private static final String DEFAULT_TAG_2 = "joker";

    @Test
    public void execute() throws Exception {
        Set<Tag> tagSet = SampleDataUtil.getTagSet(DEFAULT_TAG_1);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addTag(indices1, new Tag(DEFAULT_TAG_1));

        assertCommandSuccess(prepareCommand(indices1, tagSet), model, String.format(MESSAGE_TAG_PERSONS_SUCCESS),
                expectedModel);

    }

    @Test
    public void equal() throws Exception {
        Set<Tag> tagSet1 = SampleDataUtil.getTagSet(DEFAULT_TAG_1);
        Set<Tag> tagSet2 = SampleDataUtil.getTagSet(DEFAULT_TAG_2);
        final TagCommand standardCommand = new TagCommand(indices1, tagSet1);

        // same values -> returns true
        TagCommand commandWithSameValues = new TagCommand(indices1, tagSet1);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new TagCommand(indices2, tagSet1)));

        // different remarks -> returns false
        assertFalse(standardCommand.equals(new TagCommand(indices1, tagSet2)));
    }

    /**
     * Returns an {@code TagCommand}.
     */
    /*private TagCommand prepareCommand(Index[] indices, Set<Tag> tag) {
        TagCommand tagCommand = new TagCommand(indices, tag);
        tagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagCommand;
    }
}*/