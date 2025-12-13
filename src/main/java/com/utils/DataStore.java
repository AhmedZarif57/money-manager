package com.utils;

import com.models.Account;
import com.models.Budget;
import com.models.Transaction;
import com.models.User;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DataStore {
    private static final String DATA_FOLDER = "money-manager-data";
    private static final Path DATA_PATH = Paths.get(System.getProperty("user.home"), DATA_FOLDER);
    private static final Path USERS_FILE = DATA_PATH.resolve("users.json");
    private static final Path ACCOUNTS_FILE = DATA_PATH.resolve("accounts.json");
    private static final Path TRANSACTIONS_FILE = DATA_PATH.resolve("transactions.json");
    private static final Path BUDGETS_FILE = DATA_PATH.resolve("budgets.json");
    private static final Path CATEGORIES_FILE = DATA_PATH.resolve("categories.json");

    private List<User> users = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<Budget> budgets = new ArrayList<>();
    private java.util.Map<String, List<String>> categories = new java.util.HashMap<>();
    private User currentUser = null;

    private static DataStore instance;

    private DataStore() {
        // Create data folder if it doesn't exist
        try {
            Files.createDirectories(DATA_PATH);
            System.out.println("Data folder: " + DATA_PATH.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Load all data from JSON files
        loadAllData();
        
        // Initialize with demo data if empty
        if (users.isEmpty()) {
            initializeDemoData();
            saveAllData();
        }
    }

    private void initializeDemoData() {
        // Create demo user
        User demoUser = new User(UUID.randomUUID().toString(), "demo", "demo@example.com", "demo123");
        users.add(demoUser);
        
        // Initialize predefined categories by type
        categories.put("INCOME", new ArrayList<>(List.of("Salary", "Bonus", "Repay")));
        categories.put("EXPENSE", new ArrayList<>(List.of("Food", "Transport", "Social life", "Miscellaneous", "Loan", "Mobile recharge", "Saloon", "University")));
        
        // Create demo user's accounts with proper types: Wallet, Bkash, Card, Bank
        accounts.add(new Account(UUID.randomUUID().toString(), "Wallet", 5000.0, demoUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Bkash", 3000.0, demoUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Card", 10000.0, demoUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Bank", 25000.0, demoUser.getId()));

        // Add realistic monthly transactions for the past 12 months (for demo user only)
        LocalDate today = LocalDate.now();
        String walletId = accounts.get(0).getId();
        String bkashId = accounts.get(1).getId();
        String cardId = accounts.get(2).getId();
        String bankId = accounts.get(3).getId();
        
        for (int m = 11; m >= 0; m--) {
            LocalDate month = today.minusMonths(m);
            // Income on 1st of each month to Bank
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Salary", month.withDayOfMonth(1), 5000.0, Transaction.Type.INCOME, bankId, demoUser.getId()));
            // Expenses spread throughout month
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Food", month.withDayOfMonth(5), 1500.0, Transaction.Type.EXPENSE, bankId, demoUser.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Food", month.withDayOfMonth(10), 300.0, Transaction.Type.EXPENSE, walletId, demoUser.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Mobile recharge", month.withDayOfMonth(15), 150.0, Transaction.Type.EXPENSE, bkashId, demoUser.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Transport", month.withDayOfMonth(20), 200.0, Transaction.Type.EXPENSE, cardId, demoUser.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Social life", month.withDayOfMonth(25), 250.0, Transaction.Type.EXPENSE, cardId, demoUser.getId()));
        }

        // Sample budgets (for demo user)
        budgets.add(new Budget("Food", 500.0, demoUser.getId()));
        budgets.add(new Budget("Transport", 1500.0, demoUser.getId()));
        budgets.add(new Budget("Mobile recharge", 200.0, demoUser.getId()));
        budgets.add(new Budget("Social life", 400.0, demoUser.getId()));
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // JSON Save/Load Methods
    private void loadAllData() {
        users = loadListFromJson(USERS_FILE, User.class);
        accounts = loadListFromJson(ACCOUNTS_FILE, Account.class);
        transactions = loadListFromJson(TRANSACTIONS_FILE, Transaction.class);
        budgets = loadListFromJson(BUDGETS_FILE, Budget.class);
        categories = loadCategoriesFromJson();
    }

    private void saveAllData() {
        saveListToJson(USERS_FILE, users);
        saveListToJson(ACCOUNTS_FILE, accounts);
        saveListToJson(TRANSACTIONS_FILE, transactions);
        saveListToJson(BUDGETS_FILE, budgets);
        saveCategoriesToJson();
    }
    
    public void saveBudgets() {
        saveListToJson(BUDGETS_FILE, budgets);
    }
    
    public void saveAccounts() {
        saveListToJson(ACCOUNTS_FILE, accounts);
    }
    
    public void saveTransactions() {
        saveListToJson(TRANSACTIONS_FILE, transactions);
    }

    private <T> List<T> loadListFromJson(Path path, Class<T> clazz) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try {
            String json = Files.readString(path);
            return parseJsonArray(json, clazz);
        } catch (IOException e) {
            System.err.println("Error loading " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private <T> void saveListToJson(Path path, List<T> list) {
        try {
            String json = toJsonArray(list);
            Files.writeString(path, json);
        } catch (IOException e) {
            System.err.println("Error saving " + path + ": " + e.getMessage());
        }
    }

    private java.util.Map<String, List<String>> loadCategoriesFromJson() {
        java.util.Map<String, List<String>> map = new java.util.HashMap<>();
        if (!Files.exists(CATEGORIES_FILE)) {
            map.put("INCOME", new ArrayList<>(List.of("Salary", "Bonus", "Repay")));
            map.put("EXPENSE", new ArrayList<>(List.of("Food", "Transport", "Social life", "Miscellaneous", "Loan", "Mobile recharge", "Saloon", "University")));
            return map;
        }
        try {
            String json = Files.readString(CATEGORIES_FILE);
            // Parse simple JSON structure {"INCOME": [...], "EXPENSE": [...]}
            if (json.contains("\"INCOME\"")) {
                String incomeStr = extractJsonArray(json, "INCOME");
                map.put("INCOME", parseJsonStringArray(incomeStr));
            }
            if (json.contains("\"EXPENSE\"")) {
                String expenseStr = extractJsonArray(json, "EXPENSE");
                map.put("EXPENSE", parseJsonStringArray(expenseStr));
            }
            if (map.isEmpty()) {
                map.put("INCOME", new ArrayList<>(List.of("Salary", "Bonus", "Repay")));
                map.put("EXPENSE", new ArrayList<>(List.of("Food", "Transport", "Social life", "Miscellaneous", "Loan", "Mobile recharge", "Saloon", "University")));
            }
            return map;
        } catch (IOException e) {
            map.put("INCOME", new ArrayList<>(List.of("Salary", "Bonus", "Repay")));
            map.put("EXPENSE", new ArrayList<>(List.of("Food", "Transport", "Social life", "Miscellaneous", "Loan", "Mobile recharge", "Saloon", "University")));
            return map;
        }
    }
    
    private String extractJsonArray(String json, String key) {
        int startIdx = json.indexOf("\"" + key + "\":");
        if (startIdx == -1) return "[]";
        startIdx = json.indexOf("[", startIdx);
        if (startIdx == -1) return "[]";
        int endIdx = json.indexOf("]", startIdx);
        if (endIdx == -1) return "[]";
        return json.substring(startIdx, endIdx + 1);
    }

    private void saveCategoriesToJson() {
        try {
            StringBuilder json = new StringBuilder("{\n");
            json.append("  \"INCOME\": ");
            json.append(toJsonStringArray(categories.getOrDefault("INCOME", new ArrayList<>())));
            json.append(",\n  \"EXPENSE\": ");
            json.append(toJsonStringArray(categories.getOrDefault("EXPENSE", new ArrayList<>())));
            json.append("\n}");
            Files.writeString(CATEGORIES_FILE, json.toString());
        } catch (IOException e) {
            System.err.println("Error saving categories: " + e.getMessage());
        }
    }

    // Simple JSON parser (without external libraries)
    private <T> List<T> parseJsonArray(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (json.trim().isEmpty() || json.trim().equals("[]")) return list;
        
        try {
            json = json.trim();
            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);
            
            // Split by },{ to get individual objects
            String[] objects = json.split("\\},\\s*\\{");
            for (String obj : objects) {
                obj = obj.trim();
                if (!obj.startsWith("{")) obj = "{" + obj;
                if (!obj.endsWith("}")) obj = obj + "}";
                
                T item = parseJsonObject(obj, clazz);
                if (item != null) list.add(item);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON array: " + e.getMessage());
        }
        return list;
    }

    private <T> T parseJsonObject(String json, Class<T> clazz) {
        try {
            if (clazz == User.class) return clazz.cast(parseUser(json));
            if (clazz == Account.class) return clazz.cast(parseAccount(json));
            if (clazz == Transaction.class) return clazz.cast(parseTransaction(json));
            if (clazz == Budget.class) return clazz.cast(parseBudget(json));
        } catch (Exception e) {
            System.err.println("Error parsing object: " + e.getMessage());
        }
        return null;
    }

    private User parseUser(String json) {
        String id = extractValue(json, "id");
        String username = extractValue(json, "username");
        String email = extractValue(json, "email");
        String password = extractValue(json, "password");
        return new User(id, username, email, password);
    }

    private Account parseAccount(String json) {
        String id = extractValue(json, "id");
        String name = extractValue(json, "name");
        double balance = Double.parseDouble(extractValue(json, "balance"));
        String userId = extractValue(json, "userId");
        return new Account(id, name, balance, userId.isEmpty() ? null : userId);
    }

    private Transaction parseTransaction(String json) {
        String id = extractValue(json, "id");
        String title = extractValue(json, "title");
        LocalDate date = LocalDate.parse(extractValue(json, "date"));
        double amount = Double.parseDouble(extractValue(json, "amount"));
        Transaction.Type type = Transaction.Type.valueOf(extractValue(json, "type"));
        String accountId = extractValue(json, "accountId");
        String userId = extractValue(json, "userId");
        return new Transaction(id, title, date, amount, type, accountId, userId.isEmpty() ? null : userId);
    }

    private Budget parseBudget(String json) {
        String category = extractValue(json, "category");
        double limit = Double.parseDouble(extractValue(json, "limit"));
        String userId = extractValue(json, "userId");
        return new Budget(category, limit, userId.isEmpty() ? null : userId);
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"|\"" + key + "\"\\s*:\\s*([^,}]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1) != null ? m.group(1) : m.group(2).trim();
        }
        return "";
    }

    private <T> String toJsonArray(List<T> list) {
        if (list.isEmpty()) return "[]";
        return "[\n  " + list.stream()
            .map(this::toJsonObject)
            .collect(Collectors.joining(",\n  "))
            + "\n]";
    }

    private String toJsonObject(Object obj) {
        if (obj instanceof User) return toJsonUser((User) obj);
        if (obj instanceof Account) return toJsonAccount((Account) obj);
        if (obj instanceof Transaction) return toJsonTransaction((Transaction) obj);
        if (obj instanceof Budget) return toJsonBudget((Budget) obj);
        return "{}";
    }

    private String toJsonUser(User u) {
        return String.format("{\"id\":\"%s\",\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
            u.getId(), u.getUsername(), u.getEmail(), u.getPassword());
    }

    private String toJsonAccount(Account a) {
        String userId = a.getUserId() != null ? a.getUserId() : "";
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"balance\":%.2f,\"userId\":\"%s\"}",
            a.getId(), a.getName(), a.getBalance(), userId);
    }

    private String toJsonTransaction(Transaction t) {
        String userId = t.getUserId() != null ? t.getUserId() : "";
        return String.format("{\"id\":\"%s\",\"title\":\"%s\",\"date\":\"%s\",\"amount\":%.2f,\"type\":\"%s\",\"accountId\":\"%s\",\"userId\":\"%s\"}",
            t.getId(), t.getTitle(), t.getDate().toString(), t.getAmount(), t.getType().name(), t.getAccountId(), userId);
    }

    private String toJsonBudget(Budget b) {
        String userId = b.getUserId() != null ? b.getUserId() : "";
        return String.format("{\"category\":\"%s\",\"limit\":%.2f,\"userId\":\"%s\"}",
            b.getCategory(), b.getLimit(), userId);
    }

    private List<String> parseJsonStringArray(String json) {
        List<String> list = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]")) return list;
        
        json = json.substring(1, json.length() - 1); // Remove [ ]
        String[] items = json.split(",");
        for (String item : items) {
            item = item.trim();
            if (item.startsWith("\"")) item = item.substring(1);
            if (item.endsWith("\"")) item = item.substring(0, item.length() - 1);
            if (!item.isEmpty()) list.add(item);
        }
        return list;
    }

    private String toJsonStringArray(List<String> list) {
        if (list.isEmpty()) return "[]";
        return "[" + list.stream()
            .map(s -> "\"" + s + "\"")
            .collect(Collectors.joining(","))
            + "]";
    }

    // Authentication with secure password hashing
    public User authenticateUser(String username, String password) {
        User user = users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
        
        if (user == null) {
            return null;
        }
        
        String storedPassword = user.getPassword();
        
        // Check if password is hashed or plain text (for backward compatibility)
        if (PasswordSecurity.isHashed(storedPassword)) {
            // Use secure verification for hashed passwords
            if (PasswordSecurity.verifyPassword(password, storedPassword)) {
                return user;
            }
        } else {
            // Legacy plain text password - verify and upgrade to hashed
            if (storedPassword.equals(password)) {
                // Automatically upgrade to hashed password
                System.out.println("Upgrading password security for user: " + username);
                user.setPassword(PasswordSecurity.hashPassword(password));
                saveAllData();
                return user;
            }
        }
        
        return null;
    }

    public boolean registerUser(String username, String email, String password) {
        // Check if username already exists
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false;
        }
        
        // Hash the password before storing
        String hashedPassword = PasswordSecurity.hashPassword(password);
        User newUser = new User(UUID.randomUUID().toString(), username, email, hashedPassword);
        users.add(newUser);
        
        // Create fresh accounts for new user (Wallet, Bkash, Card, Bank with zero balance)
        accounts.add(new Account(UUID.randomUUID().toString(), "Wallet", 0.0, newUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Bkash", 0.0, newUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Card", 0.0, newUser.getId()));
        accounts.add(new Account(UUID.randomUUID().toString(), "Bank", 0.0, newUser.getId()));
        
        System.out.println("New user registered: " + username + " with fresh accounts");
        
        saveAllData();
        return true;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean changePassword(String username, String newPassword) {
        User user = users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
        
        if (user != null) {
            // Hash the new password before storing
            user.setPassword(PasswordSecurity.hashPassword(newPassword));
            saveAllData();
            return true;
        }
        return false;
    }
    
    public boolean deleteUser(String username) {
        User user = users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
        
        if (user != null) {
            // Remove user's accounts
            accounts.removeIf(a -> a.getUserId() != null && a.getUserId().equals(user.getId()));
            // Remove user's transactions
            transactions.removeIf(t -> {
                for (Account a : accounts) {
                    if (a.getId().equals(t.getAccountId())) {
                        return false; // Keep transactions for remaining accounts
                    }
                }
                return true; // Remove if account no longer exists
            });
            // Remove user's budgets
            budgets.clear(); // For simplicity, clear all budgets
            // Remove user
            users.remove(user);
            
            saveAllData();
            return true;
        }
        return false;
    }

    // Category Management
    public List<String> getCategoriesByType(String type) {
        List<String> categoryList = new ArrayList<>(categories.getOrDefault(type, new ArrayList<>()));
        Collections.sort(categoryList);
        return categoryList;
    }

    public void addCategory(String category, String type) {
        List<String> list = categories.getOrDefault(type, new ArrayList<>());
        if (!list.contains(category)) {
            list.add(category);
            categories.put(type, list);
            saveAllData();
        }
    }

    // Getters
    public List<User> getUsers() { return users; }
    
    public List<Account> getAccounts() {
        if (currentUser == null) return accounts; // Fallback for backward compatibility
        return accounts.stream()
            .filter(a -> a.getUserId() != null && a.getUserId().equals(currentUser.getId()))
            .collect(Collectors.toList());
    }
    
    public List<Transaction> getTransactions() {
        if (currentUser == null) return transactions; // Fallback for backward compatibility
        return transactions.stream()
            .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUser.getId()))
            .collect(Collectors.toList());
    }
    
    public List<Budget> getBudgets() {
        if (currentUser == null) return budgets; // Fallback for backward compatibility
        return budgets.stream()
            .filter(b -> b.getUserId() != null && b.getUserId().equals(currentUser.getId()))
            .collect(Collectors.toList());
    }

    public void addBudget(Budget b) {
        if (currentUser != null) {
            b.setUserId(currentUser.getId());
        }
        budgets.add(b);
        saveAllData();
    }
    
    public void deleteBudget(Budget b) {
        budgets.removeIf(budget -> budget.getCategory().equals(b.getCategory()) && 
                                  budget.getUserId().equals(b.getUserId()));
        saveAllData();
    }

    public void addTransaction(Transaction t) {
        if (currentUser != null) {
            t.setUserId(currentUser.getId());
        }
        transactions.add(t);
        // update account balance (skip for transfers as they handle balances manually)
        if (t.getType() != Transaction.Type.TRANSFER) {
            for (Account a : accounts) {
                if (a.getId().equals(t.getAccountId())) {
                    if (t.getType() == Transaction.Type.INCOME) a.setBalance(a.getBalance() + t.getAmount());
                    else a.setBalance(a.getBalance() - t.getAmount());
                    break;
                }
            }
        }
        saveAllData();
    }
    
    public void updateTransaction(Transaction t) {
        // Find and update the existing transaction
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(t.getId())) {
                Transaction old = transactions.get(i);
                
                // Reverse old transaction's effect on account balance
                for (Account a : accounts) {
                    if (a.getId().equals(old.getAccountId())) {
                        if (old.getType() == Transaction.Type.INCOME) {
                            a.setBalance(a.getBalance() - old.getAmount());
                        } else {
                            a.setBalance(a.getBalance() + old.getAmount());
                        }
                        break;
                    }
                }
                
                // Update transaction
                transactions.set(i, t);
                
                // Apply new transaction's effect on account balance
                for (Account a : accounts) {
                    if (a.getId().equals(t.getAccountId())) {
                        if (t.getType() == Transaction.Type.INCOME) {
                            a.setBalance(a.getBalance() + t.getAmount());
                        } else {
                            a.setBalance(a.getBalance() - t.getAmount());
                        }
                        break;
                    }
                }
                break;
            }
        }
        saveAllData();
    }
    
    public void deleteTransaction(Transaction t) {
        // Reverse transaction's effect on account balance (skip for transfers as they need special handling)
        if (t.getType() != Transaction.Type.TRANSFER) {
            for (Account a : accounts) {
                if (a.getId().equals(t.getAccountId())) {
                    if (t.getType() == Transaction.Type.INCOME) {
                        a.setBalance(a.getBalance() - t.getAmount());
                    } else {
                        a.setBalance(a.getBalance() + t.getAmount());
                    }
                    break;
                }
            }
        } else {
            // For transfers, restore both accounts
            for (Account a : accounts) {
                if (a.getId().equals(t.getAccountId())) {
                    // This is the source account - add back the amount
                    a.setBalance(a.getBalance() + t.getAmount());
                } else if (t.getToAccountId() != null && a.getId().equals(t.getToAccountId())) {
                    // This is the destination account - subtract the amount
                    a.setBalance(a.getBalance() - t.getAmount());
                }
            }
        }
        
        // Remove transaction from list
        transactions.removeIf(trans -> trans.getId().equals(t.getId()));
        saveAllData();
    }

    public double getTotalIncome() {
        if (currentUser == null) return 0.0;
        return transactions.stream()
            .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUser.getId()))
            .filter(t -> t.getType() == Transaction.Type.INCOME)
            .mapToDouble(Transaction::getAmount).sum();
    }
    
    public double getCurrentMonthIncome() {
        if (currentUser == null) return 0.0;
        int selectedMonth = com.controllers.SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        return transactions.stream()
            .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUser.getId()))
            .filter(t -> t.getType() == Transaction.Type.INCOME)
            .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
            .mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalExpenses() {
        if (currentUser == null) return 0.0;
        return transactions.stream()
            .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUser.getId()))
            .filter(t -> t.getType() == Transaction.Type.EXPENSE)
            .mapToDouble(Transaction::getAmount).sum();
    }
    
    public double getCurrentMonthExpenses() {
        if (currentUser == null) return 0.0;
        int selectedMonth = com.controllers.SettingsController.getSelectedMonth();
        int currentYear = java.time.LocalDate.now().getYear();
        return transactions.stream()
            .filter(t -> t.getUserId() != null && t.getUserId().equals(currentUser.getId()))
            .filter(t -> t.getType() == Transaction.Type.EXPENSE)
            .filter(t -> t.getDate().getMonthValue() == selectedMonth && t.getDate().getYear() == currentYear)
            .mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalBalance() {
        if (currentUser == null) return 0.0;
        return accounts.stream()
            .filter(a -> a.getUserId() != null && a.getUserId().equals(currentUser.getId()))
            .mapToDouble(Account::getBalance).sum();
    }
}
