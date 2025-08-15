public class Bubbles {
    public static String line = "\t____________________________________________________________";
    public static String name = "Bubbles";

    public static void Greetings() {
        System.out.println(line + "\n\tHello! I'm " + name);
        System.out.println("\tWhat can I do for you?\n" + line);
    }

    public static void Bye() {
        System.out.println("\tBye. Hope to see you again soon!\n" + line);
    }

    public static void main(String[] args) {
        Greetings();
        Bye();
    }
}
