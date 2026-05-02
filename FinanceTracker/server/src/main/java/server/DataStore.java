package server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<String>> transactions = new ConcurrentHashMap<>();

    static {
        // Add test users
        users.put("demo", "password123");
        users.put("john", "john123");
        users.put("sarah", "sarah123");

        // Initialize transaction lists
        transactions.put("demo", new ArrayList<>());
        transactions.put("john", new ArrayList<>());
        transactions.put("sarah", new ArrayList<>());

        // Add MANY sample transactions for demo user
        addTransactionToUser("demo", 2500.00, "INCOME", "Salary", "2024-01-15");
        addTransactionToUser("demo", 150.00, "EXPENSE", "Food", "2024-01-16");
        addTransactionToUser("demo", 60.00, "EXPENSE", "Transport", "2024-01-17");
        addTransactionToUser("demo", 500.00, "EXPENSE", "Shopping", "2024-01-18");
        addTransactionToUser("demo", 2000.00, "INCOME", "Freelance", "2024-01-20");
        addTransactionToUser("demo", 75.00, "EXPENSE", "Entertainment", "2024-01-21");
        addTransactionToUser("demo", 120.00, "EXPENSE", "Food", "2024-01-22");
        addTransactionToUser("demo", 3000.00, "INCOME", "Salary", "2024-02-15");
        addTransactionToUser("demo", 45.00, "EXPENSE", "Transport", "2024-02-16");
        addTransactionToUser("demo", 200.00, "EXPENSE", "Shopping", "2024-02-17");
        addTransactionToUser("demo", 85.00, "EXPENSE", "Entertainment", "2024-02-18");
        addTransactionToUser("demo", 3200.00, "INCOME", "Salary", "2024-03-15");
        addTransactionToUser("demo", 95.00, "EXPENSE", "Food", "2024-03-16");
        addTransactionToUser("demo", 55.00, "EXPENSE", "Transport", "2024-03-17");
        addTransactionToUser("demo", 180.00, "EXPENSE", "Bills", "2024-03-18");
        addTransactionToUser("demo", 1500.00, "INCOME", "Bonus", "2024-03-20");

        // Add sample for john
        addTransactionToUser("john", 3000.00, "INCOME", "Salary", "2024-01-15");
        addTransactionToUser("john", 200.00, "EXPENSE", "Food", "2024-01-16");
        addTransactionToUser("john", 50.00, "EXPENSE", "Transport", "2024-01-17");

        // Add sample for sarah
        addTransactionToUser("sarah", 2750.00, "INCOME", "Salary", "2024-01-15");
        addTransactionToUser("sarah", 120.00, "EXPENSE", "Food", "2024-01-16");
        addTransactionToUser("sarah", 80.00, "EXPENSE", "Shopping", "2024-01-17");
        addTransactionToUser("sarah", 2500.00, "INCOME", "Bonus", "2024-01-20");
    }

    public static boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        transactions.putIfAbsent(username, new ArrayList<>());
        System.out.println("✅ User registered: " + username);
        return true;
    }

    public static boolean login(String username, String password) {
        boolean success = users.containsKey(username) && users.get(username).equals(password);
        System.out.println("🔐 Login attempt for " + username + ": " + (success ? "SUCCESS" : "FAILED"));
        return success;
    }

    private static void addTransactionToUser(String username, double amount, String type, String category, String date) {
        String transaction = amount + "|" + type + "|" + category + "|" + date;
        transactions.get(username).add(transaction);
        System.out.println("💾 Added transaction for " + username + ": " + transaction);
    }

    public static boolean addTransaction(String username, double amount, String type, String category, String date) {
        transactions.putIfAbsent(username, new ArrayList<>());
        String transaction = amount + "|" + type + "|" + category + "|" + date;
        boolean added = transactions.get(username).add(transaction);
        System.out.println("➕ Added new transaction for " + username + ": " + transaction);
        return added;
    }

    public static String getTransactions(String username) {
        ArrayList<String> userTransactions = transactions.get(username);

        if (userTransactions == null || userTransactions.isEmpty()) {
            System.out.println("📭 No transactions for: " + username);
            return "EMPTY";
        }

        // Build the response string - use | between transactions
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userTransactions.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(userTransactions.get(i));
        }

        String result = sb.toString();
        System.out.println("📤 Sending " + userTransactions.size() + " transactions for " + username);
        System.out.println("📤 First 200 chars: " + result.substring(0, Math.min(200, result.length())));
        return result;
    }

    public static String getSummary(String username) {
        ArrayList<String> userTransactions = transactions.get(username);

        if (userTransactions == null || userTransactions.isEmpty()) {
            return "0.0|0.0|0.0";
        }

        double totalIncome = 0;
        double totalExpense = 0;

        for (String transaction : userTransactions) {
            String[] parts = transaction.split("\\|");
            double amount = Double.parseDouble(parts[0]);
            String type = parts[1];

            if (type.equals("INCOME")) {
                totalIncome += amount;
            } else if (type.equals("EXPENSE")) {
                totalExpense += amount;
            }
        }

        double balance = totalIncome - totalExpense;
        String summary = totalIncome + "|" + totalExpense + "|" + balance;
        System.out.println("📊 Summary for " + username + ": " + summary);
        return summary;
    }
}