package chatbot.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ToDoTest {

    @Test
    public void setComplete_success() {
        ToDo todo = new ToDo("read");

        Assertions.assertFalse(todo.isCompleted);

        todo.setCompleted();
        Assertions.assertTrue(todo.isCompleted);
    }

    @Test
    public void unComplete_success() {
        ToDo todo = new ToDo("read");

        todo.setCompleted();
        Assertions.assertTrue(todo.isCompleted);

        todo.unComplete();
        Assertions.assertFalse(todo.isCompleted);
    }

    @Test
    public void toString_unCompleted_correctFormat() {
        ToDo read = new ToDo("read");

        Assertions.assertEquals("[T][X] read", read.toString());
    }

    @Test
    public void toString_completed_correctFormat() {
        ToDo study = new ToDo("study");
        study.setCompleted();

        Assertions.assertEquals("[T][✓] study", study.toString());
    }
}
