public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        MenuCreator menu = new MenuCreator();
        menu.prompt = "Please choose an option:";
        menu.options = new String[][][]{{{"Create a user", "Get a user", "Exit"}}, {{"From new", "From file", "back"}, {"By name", "By UID", "By DOB", "back"}}};
        int[] var2 = menu.make();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            System.out.println(i);
        }

    }
}
