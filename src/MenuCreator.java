import java.security.InvalidParameterException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for creating a customisable menu in the terminal
 * @author Sean Fricke
 * @version 1.0
 */
public class MenuCreator{
    /**
     * A 3D list of menu options, in the following format:
     * FIRST index being menu layers, going up as you branch into the menu options;
     * SECOND index being the corresponding third (lowest level) index of the root (last layer) option for the options in this layer;
     * THIRD layer being the selection options of that specific branch
     */
    public String[][][] options;
    /**
     * String that is given before the options in the menu
     */
    public String prompt;

    /**
     * Creates an animated menu with the terminal window, from the "options" and "prompt" attributes
     * @return Integer list of three numbers, signifying the index of the final selection.
     * @since 1.0
     */
    public int[] make() {
        // Init settings in format {Confirmation, Menu layer, Menu branch, Branch root index}
        int[] settings = {0, 0, 0, 0};
        // Init variable for return value assignment later
        int[] finalSelect;
        while (true) {
            int ind = 0;
            try {
                for (int i : updateScreen(this.options[settings[1]][settings[2]])) {
                    // If first iteration AND the user canceled selection, reset the confirmation setting, and break;
                    if (ind == 0 && i == 0) {
                        settings[0] = 0;
                        break;
                    } else {
                        // On final iteration, save the old (root) selection to the last index of settings
                        if (ind == 2) {
                            settings[3] = settings[2];
                        }
                        /* Save next element in result of updateScreen to it's corresponding setting,
                        and increment the index */
                        settings[ind] = i;
                        ind += 2;
                    }

                }
                // If the user confirms the selection, move down a menu layer
                if (settings[0] == 1) {
                    settings[1]++;
                }
                // If user is selecting exit/back, branch here
                if ( settings[2] == this.options.length && settings[0] == 1){
                    // Terminate program if exiting on top layer of the menu
                    if (settings[1]  == 1){
                        System. exit(0);
                    // If in deeper layers, push user back to top layer
                    } else {
                        settings[2] = settings[3];
                        settings[1]--;
                        settings = new int[]{0, 0, 0, 0};
                    }
                }
            // If user confirms a "dead end" option, save the index of the final selection
            } catch (ArrayIndexOutOfBoundsException e) {
                finalSelect = new int[]{settings[1] -1, settings[3], settings[2]};
                break;
            }
        }
        // Return selection index as a list
        return finalSelect;
    }

    /**
     * Private method to call to create the terminal output for the menu
     * @param list 1-dimensional list of menu options to display in the terminal
     * @return updated settings list for the make function.
     * @since 1.0
     */
    private int[] updateScreen(String[] list){
        // Init scanners, option list, and variables
        Scanner mainScanner = new Scanner(System.in);
        Scanner confScanner = new Scanner(System.in);
        String[] formattedList = formatOptions(list);
        int selection;
        int confirmation;
        String strConf;

        // Clear terminal on Windows Command Prompt
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Print menu prompt first
        System.out.println(this.prompt);

        // Iterate and print menu options
        for (String i : formattedList){
            System.out.println(i);
        }

        // Input for menu option selection, throwing an error if not valid
        try {
            selection = mainScanner.nextInt() - 1;
        } catch (InputMismatchException e) {
            throw new RuntimeException("Please enter a valid selection");
        }

        //Clear terminal again
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Print menu prompt again
        System.out.println(this.prompt);

        // Iterate through menu options list, marking the user's selection
        for (int i = 0; i < formattedList.length; i++) {
            if(i == selection){
                System.out.println(formattedList[i] + " <");
            } else {
                System.out.println(formattedList[i]);
            }
        }

        // Prompt for confirmation
        System.out.println("Are you sure?(Y/n):");

        // Ask for input on confirmation, outputting binary option for result and throwing error if invalid
        strConf = confScanner.nextLine().toLowerCase();
        confirmation = switch (strConf) {
            case "y", "" -> 1;
            case "n" -> 0;
            default -> throw new InvalidParameterException("Input was not Y/n");
        };
        mainScanner.nextLine();

        // Return if selection was confirmed, along with the selected option's index
        return new int[]{confirmation, selection};
    }

    /**
     * Formats a basic list of options into a numbered list.
     * @param list 1-dimensional list of options to be formatted
     * @return A numbered list, from the inputted options.
     * @since 1.0
     */
    private String[] formatOptions(String[] list){
        // Init list of input size to reserve for assignment later
        String[] formattedList = new String[list.length];

        // Iterate through length of input, assigning that corresponding element and formatting to a list
        for(int i = 0; i < list.length; i++){
            formattedList[i] = (i + 1) + ": " + list[i];
        }

        // Return formatted list
        return formattedList;
    }
}
