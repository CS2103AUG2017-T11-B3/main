package seedu.address.ui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.task.ReadOnlyTask;

public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskCard.fxml";

    public final ReadOnlyTask task;

    @FXML
    private HBox cardPanel;

    @FXML
    private Label id;

    @FXML
    private Label description;

    @FXML
    private Label startDate;

    @FXML
    private Label deadline;

    public TaskCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText("  " + displayedIndex + ". ");
        bindListeners(task);
    }

    private void bindListeners(ReadOnlyTask task) {
        description.textProperty().bind(Bindings.convert(task.descriptionProperty()));
        startDate.textProperty().bind(Bindings.convert(task.startDateProperty()));
        deadline.textProperty().bind(Bindings.convert(task.deadlineProperty()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
            && task.equals(card.task);
    }
}
