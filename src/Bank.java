import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.*;
import java.lang.Math;

public class Bank {
    private Hashtable<String, BankAccount> accounts;

    public Bank() {accounts = new Hashtable<>();}

    public Bank(String accName) {
        accounts = new Hashtable<>();
        load(accName);
    }

    public Bank (String[] accNames) {
        accounts = new Hashtable<>();
        for (String accName : accNames) {
            load(accName);
        }
    }

    public void addAccount(String accName) {
        Scanner s = new Scanner(System.in);
        String name = accName;

        if (accName.isEmpty()) {
            System.out.println("Enter Account Name: ");
            name = s.nextLine();
        }

        System.out.println("Enter current bal: ");
        double bal = Double.parseDouble(s.nextLine());

        System.out.println("Enter password: ");
        String pass = s.nextLine();

        accounts.put(name, new BankAccount(name, bal, pass));
    }


    public void login(String accName) {
        Scanner s = new Scanner(System.in);
        String usrInp;
        BankAccount acc;
        if (accounts.containsKey(accName)) {
            acc = accounts.get(accName);
        } else {
            System.out.println("Enter Account Name: ");

            usrInp = s.nextLine();
            int cnt = 1;

            while (usrInp.equalsIgnoreCase("quit") || !accounts.containsKey(usrInp) && cnt < 5) {
                usrInp = s.nextLine();
                System.out.println("Account not found, please try again.");
                cnt++;
            }

            if (!accounts.containsKey(usrInp)) {
                System.out.println("Account not found, try again later.");
                return;
            }
            acc = accounts.get(usrInp);
        }

        // Password Check
        if (!passwordCheck(acc)){
            System.out.println("Try again later.");
            return;
        }

        // Account loop
        System.out.printf("Welcome %s\n", acc.getName());
        printAccountMenu();
        do {
            System.out.println("\n-_-_-_-_-_-_-_-_-_-_-_-_");
            System.out.println("Enter: ");
            usrInp = s.nextLine();
        } while (accountMenu(usrInp, acc));
    }

    public boolean passwordCheck(BankAccount acc) {
        Scanner s = new Scanner(System.in);
        String usrInp;

        int attempts = 3;
        do {
            System.out.println("Enter password: ");
            usrInp = s.nextLine();
            if (!acc.alohomora(usrInp)) {
                attempts--;
                System.out.printf("Invalid password, %d attempts left\n", attempts);
            }
        } while (!acc.alohomora(usrInp) && attempts > 0);

        return acc.alohomora(usrInp) && attempts > 0;
    }

    public static void printAccountMenu() {
        System.out.println("\n-_-_-_-_-_-_-_-_-_-_-_-_");
        System.out.println("'A' -> Add transaction");
        System.out.println("'H' -> Help");
        System.out.println("'P' -> Print account information");
        System.out.println("'Q' -> Quit");
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_\n");
    }

    public static boolean accountMenu(String usrInp, BankAccount acc) {
        String[] commandArgs = usrInp.split(" ");

        switch (commandArgs[0]) {
            case "H", "h", "Help", "help" :
                printAccountMenu();
                break;
            case "A", "a", "Add", "add" :
                if (commandArgs.length == 2) {
                    add(acc, commandArgs[1]);
                } else {add(acc, "");}
                break;

            case "C", "c", "Clear", "clear" :
                acc.getTransactions().clear();
                break;

            case "P", "p", "Print", "print" :
                System.out.println(acc);
                break;
            case "Q", "q", "Quit", "quit" :
                return false;
            default:
                System.out.println(">> Invalid Input, 'H' for Help");
        }
        return true;
    }

    public static void add(BankAccount acc, String amt) {
        Scanner s = new Scanner(System.in);
        String usrInp;
        double amount;

        try {
            amount = Double.parseDouble(amt);
        } catch (NumberFormatException e) {
            do {
                System.out.println("Enter amount: ");
                amount = Math.round(Double.parseDouble(s.nextLine()) * 100.0) / 100.0;
                System.out.printf("Do you want to add a $%f transaction? (y/n)", amount);
                usrInp = s.nextLine();
            } while (!usrInp.equals("y"));
        }
        acc.add(amount);
        System.out.println("Transaction added.");
    }

    public void saveAccountKeys() {
        File file = new File("Keys.txt");
        PrintWriter pw;

        try {
            file.createNewFile();
            pw = new PrintWriter(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (String name : accounts.keySet()) {
            pw.println(name);
        }
    }


    public void save() {
        // saves information to files/file
        for (BankAccount account : accounts.values()) {
            PrintWriter pw;

            // make file if not already made
            File file = new File(account.getName() + "Report.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }

            try {
                pw = new PrintWriter(account.getName() + "Report.txt");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            pw.println(toFile(account));
            pw.close();
        }
        System.out.println("Successfully saved the bank");
    }

    public static String toFile(BankAccount acc) {
        StringBuilder out = new StringBuilder();
        out.append(acc.getName()).append("\n").append("Bal:\n").append(acc.getVault()).append("\n");
        out.append("Password:\n").append(acc.getPassword()).append("\n").append("Transactions:\n");

        for (String[] transaction : acc.getTransactions()) {
            out.append(transaction[0]).append(" | ").append(transaction[1]).append("\n");
        }
        return out.toString();
    }

    public void load(String accName) {
        // override parameter for hardcoding
        File file;
        Scanner s;
        if (!accName.isEmpty()) {
            file = new File(accName + "Report.txt");
            if (!file.exists()) {System.out.println("File not found.");return;}

        // User enters account name
        } else {
            s = new Scanner(System.in);
            String usrInp;

            do {
                System.out.println("Enter account you'd like to load... ");
                usrInp = s.nextLine();
                file = new File(usrInp + "Report.txt");
            } while (!file.exists());
        }

        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        // Account information
        String name = s.nextLine(); s.nextLine();
        double bal = Double.parseDouble(s.nextLine()); s.nextLine();
        String password = BankAccount.encrypt(s.nextLine(), BankAccount.KEY); s.nextLine();
        BankAccount acc = new BankAccount(name, bal, password);

        // Transaction history
        String[] line;
        while (s.hasNextLine()) {
            line = s.nextLine().replace(" ", "").split("\\|");
            System.out.println(Arrays.toString(line));
            if(line.length == 2) {
                acc.getTransactions().push(line);
            }
        }
        accounts.put(acc.getName(), acc);
        System.out.println("Successfully loaded the bank");
    }

    public void loadAll(){
        File file = new File("Keys.txt");
        if (!file.exists()) {System.out.println("File not found.");return;}

        Scanner s;
        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        String line;
        while (s.hasNextLine()) {
            line = s.nextLine();
            if (!line.isEmpty()) {
                load(s.nextLine() + "Report.txt");
            }
        }
    }


    public static void printMenu() {
        System.out.println("\n-_-_-_-_-_-_-_-_-_-_-_-_");
        System.out.println("'A' -> add account");
        System.out.println("'S' -> Sign in to account");
        System.out.println("'L' -> Load account file");
        System.out.println("'P' -> Print all accounts");
        System.out.println("'Q' -> Quit");
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_\n");
    }

    public boolean menu(String inp) {
        String[] commandArgs = inp.split(" ");

        switch (commandArgs[0]) {
            case "A", "Add", "add":
                if (commandArgs.length != 2) {
                    System.out.println(">> Add requires 1 argument <account name>");
                } else {
                    addAccount(commandArgs[1]);}
                break;

            case "L", "Load", "load" :
                if (commandArgs.length == 2) {
                    load(commandArgs[1]);
                } else if (commandArgs.length == 1) {
                    load("");
                } else {
                    System.out.println(">> Load requires 1 argument <account name>");}
                break;

            case "S", "Sign-in", "signin", "sign-in", "Signin", "Sign", "sign":
                if (commandArgs.length == 3 && commandArgs[1].equalsIgnoreCase("in")) {
                    login(commandArgs[2]);
                } else if (commandArgs.length == 2) {
                    login(commandArgs[1]);
                } else if (commandArgs.length == 1) {
                    login("");
                } else {
                    System.out.println(">> Sign in requires 1 argument <account name>");}
                break;

            case "P", "Print", "print" :
                System.out.println(this);
                break;
            case "Q", "quit", "Quit":
                return false;
            default:
                System.out.println(">> Invalid command, 'H' for Help");
        }
        return true;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("Accounts:\n");
        for (BankAccount acc : accounts.values()) {
            out.append(acc.getName()).append("\n");
        }
        return out.toString();
    }


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Bank bank = new Bank(new String[] {"Damani", "Nicky"});
        String usrInp;

        printMenu();
        do {
            System.out.println("\n_-_-_-_-_-_-_-_-_-_-_-_-_-_\n");
            System.out.println("Enter: ");
            usrInp = s.nextLine();
        } while (bank.menu(usrInp));
        bank.save();
        bank.saveAccountKeys();
        s.close();
    }
}
