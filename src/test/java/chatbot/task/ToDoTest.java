package chatbot.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ToDoTest {

    @Test
    public void ToDoTest_completeAndUnCompleteTask() {
        ToDo todo = new ToDo("read");

        Assertions.assertFalse(todo.isCompleted);

        todo.setCompleted();
        Assertions.assertTrue(todo.isCompleted);

        todo.unComplete();
        Assertions.assertFalse(todo.isCompleted);
    }

    @Test
    public void ToDoTest_toString() {
        ToDo todo = new ToDo("read");

        Assertions.assertEquals("[T][X] read", todo.toString());

        todo.setCompleted();

        Assertions.assertEquals("[T][✓] read", todo.toString());
    }
}
