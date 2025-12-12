package com.utils;

import com.models.Account;
import com.models.Budget;
import com.models.Transaction;
import com.models.User;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Path STORE_PATH = Path.of(System.getProperty("user.home"), ".money-manager-data.dat");

    private List<User> users = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<Budget> budgets = new ArrayList<>();

    private static DataStore instance;

    private DataStore() {
        // create a demo account and transactions if empty
        Account demo = new Account(UUID.randomUUID().toString(), "Main Account", 15000.0);
        accounts.add(demo);

        // Add realistic monthly transactions for the past 12 months
        LocalDate today = LocalDate.now();
        for (int m = 11; m >= 0; m--) {
            LocalDate month = today.minusMonths(m);
            // Income on 1st of each month
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Salary", month.withDayOfMonth(1), 5000.0, Transaction.Type.INCOME, demo.getId()));
            // Expenses spread throughout month
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Rent", month.withDayOfMonth(5), 1500.0, Transaction.Type.EXPENSE, demo.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Groceries", month.withDayOfMonth(10), 300.0, Transaction.Type.EXPENSE, demo.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Utilities", month.withDayOfMonth(15), 150.0, Transaction.Type.EXPENSE, demo.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Transport", month.withDayOfMonth(20), 200.0, Transaction.Type.EXPENSE, demo.getId()));
            transactions.add(new Transaction(UUID.randomUUID().toString(), "Entertainment", month.withDayOfMonth(25), 250.0, Transaction.Type.EXPENSE, demo.getId()));
        }

        // sample budget
        budgets.add(new Budget("Groceries", 500.0));
        budgets.add(new Budget("Rent", 1500.0));
        budgets.add(new Budget("Utilities", 200.0));
        budgets.add(new Budget("Entertainment", 400.0));
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            // try load
            if (Files.exists(STORE_PATH)) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORE_PATH.toFile()))) {
                    instance = (DataStore) in.readObject();
                } catch (Exception e) {
                    instance = new DataStore();
                }
            } else {
                instance = new DataStore();
            }
        }
        return instance;
    }

    public synchronized void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORE_PATH.toFile()))) {
            out.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() { return users; }
    public List<Account> getAccounts() { return accounts; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<Budget> getBudgets() { return budgets; }

    public void addBudget(Budget b) {
        budgets.add(b);
        save();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        // update account balance
        for (Account a : accounts) {
            if (a.getId().equals(t.getAccountId())) {
                if (t.getType() == Transaction.Type.INCOME) a.setBalance(a.getBalance() + t.getAmount());
                else a.setBalance(a.getBalance() - t.getAmount());
                break;
            }
        }
        save();
    }

    public double getTotalIncome() {
        return transactions.stream().filter(t -> t.getType() == Transaction.Type.INCOME).mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalExpenses() {
        return transactions.stream().filter(t -> t.getType() == Transaction.Type.EXPENSE).mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalBalance() {
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }
}
