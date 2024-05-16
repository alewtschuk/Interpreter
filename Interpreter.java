import eval.Environment;
import eval.EvalException;
import syntax.Parser;
import syntax.SyntaxException;


/**
 * A simple REPL for your interpreter
 */
public class Interpreter {

    private static final String EXIT_CMD = "E"; 
    private static final String CLEAR_CMD = "C"; 
    private static final String PROMPT = ">>>"; 

    public static void main(String[] args) {

        Parser parser = new Parser();
        Environment env = new Environment();

        /*
         * begin interactive interpreter
         */
        java.util.Scanner scan = new java.util.Scanner(System.in);

        System.out.print(PROMPT);

        String line;
        while (scan.hasNext()) {

            try {
                line = scan.nextLine();
                switch (line) {
                    case EXIT_CMD:
                        System.out.println("Exiting the interpreter."); 
                        System.exit(0);
                    case CLEAR_CMD:
                        System.out.println("Environment Cleared"); 
                        env = new Environment();
                        break;
                    default:
                        parser.parse(line).eval(env);
                        break;
                }
            } catch (SyntaxException | EvalException e) {
                System.out.println(PROMPT + e);
            }

            System.out.print(PROMPT);
        }
        scan.close();
    }
}

