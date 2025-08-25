package chatbot.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ToDoTest {

    @Test
    public void testToDo_completeAndUnCompleteTask_success() {
        ToDo todo = new ToDo("read");

        Assertions.assertFalse(todo.isCompleted);

        todo.setCompleted();
        Assertions.assertTrue(todo.isCompleted);

        todo.unComplete();
        Assertions.assertFalse(todo.isCompleted);
    }

    @Test
    public void testToDo_toString_correctFormat() {
        ToDo todo = new ToDo("read");

        Assertions.assertEquals("[T][X] read", todo.toString());

        todo.setCompleted();

        Assertions.assertEquals("[T][✓] read", todo.toString());
    }
}
