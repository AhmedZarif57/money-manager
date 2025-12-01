# Money Manager - Code Documentation

## 📚 Code Structure & Explanation

This document explains the code organization and key components of the Money Manager application for easy understanding during demonstration.

---

## 🏗️ Architecture Overview

The project follows the **Model-View-Controller (MVC)** pattern:

```
┌─────────────┐
│    View     │  ← FXML files (UI layout)
│   (FXML)    │
└──────┬──────┘
       │
┌──────▼──────┐
│ Controller  │  ← Java controllers (UI logic)
│   (Java)    │
└──────┬──────┘
       │
┌──────▼──────┐
│   Model     │  ← Data models (User, Account, etc.)
│   (Java)    │
└─────────────┘
```

---

## 📦 Package Structure

### `com.utils` - Utility Classes

#### `App.java` - Main Application Entry Point
```java
public class App extends Application
```

**Purpose:** Launches the JavaFX application

**Key Methods:**
- `start(Stage stage)` - Initializes the main window
  - Loads FXML file
  - Applies CSS styling
  - Sets window properties (title, size, resizable)
  
- `main(String[] args)` - Entry point that launches JavaFX

**What it does:**
1. Creates the main JavaFX window (Stage)
2. Loads the UI from FXML file
3. Applies custom CSS for styling
4. Sets window title and minimum size
5. Displays the window to the user

---

#### `NavigationManager.java` - Screen Navigation
**Purpose:** Handles switching between different screens (Dashboard, Transactions, etc.)

**Planned Methods:**
```java
public static void navigateTo(String fxmlFile)
public static void loadDashboard()
public static void loadTransactions()
public static void loadBudget()
public static void loadSettings()
```

---

### `com.models` - Data Models

#### `User.java` - User Information
```java
public class User {
    private String username;
    private String password; // Will be hashed
    private String email;
    private List<Account> accounts;
    
    // Constructor, getters, setters
}
```

**Purpose:** Stores user account information

**Key Features:**
- User credentials (username, password)
- Associated bank accounts
- Login/logout functionality

---

#### `Account.java` - Financial Account
```java
public class Account {
    private String accountId;
    private String accountName;
    private AccountType type; // WALLET, BANK, MOBILE_BANKING
    private double balance;
    
    // Methods for deposit, withdraw, getBalance
}
```

**Purpose:** Represents a financial account (bank, wallet, etc.)

**Account Types:**
- Wallet (cash)
- Bank Account
- Mobile Banking (bKash, Nagad, etc.)

---

#### `Transaction.java` - Financial Transaction
```java
public class Transaction {
    private String transactionId;
    private LocalDate date;
    private double amount;
    private TransactionType type; // INCOME, EXPENSE, TRANSFER
    private String category;
    private Account account;
    private String note;
}
```

**Purpose:** Records financial transactions

**Transaction Types:**
- **Income** - Money received
- **Expense** - Money spent
- **Transfer** - Money moved between accounts

---

#### `Category.java` - Transaction Category
```java
public class Category {
    private String categoryId;
    private String categoryName;
    private CategoryType type; // INCOME or EXPENSE
    private String icon;
}
```

**Default Categories:**
- **Income:** Salary, Business, Gift, Other
- **Expense:** Food, Transport, Bills, Shopping, Health, Entertainment

---

#### `Budget.java` - Budget Management
```java
public class Budget {
    private String budgetId;
    private Category category;
    private double amount;
    private Period period; // DAILY, WEEKLY, MONTHLY
    private double spent;
    
    public boolean isOverBudget() {
        return spent > amount;
    }
}
```

**Purpose:** Track spending limits by category

---

### `com.controllers` - UI Controllers

#### `MainController.java` - Main Window Controller
```java
@FXML
public class MainController {
    @FXML private BorderPane mainContainer;
    @FXML private VBox sidebar;
    
    @FXML
    public void onDashboardClick() {
        // Load dashboard view
    }
    
    @FXML
    public void onTransactionsClick() {
        // Load transactions view
    }
}
```

**Purpose:** Manages main window and navigation

---

#### `DashboardController.java` - Dashboard Controller
```java
@FXML
public class DashboardController {
    @FXML private Label totalBalance;
    @FXML private Label totalIncome;
    @FXML private Label totalExpense;
    @FXML private PieChart expenseChart;
    
    public void initialize() {
        // Load user data
        // Calculate totals
        // Display charts
    }
}
```

**Purpose:** Displays financial overview and charts

**Features:**
- Total balance calculation
- Income vs expense comparison
- Pie chart for expense breakdown
- Recent transactions list

---

#### `TransactionsController.java` - Transactions Controller
```java
@FXML
public class TransactionsController {
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private ComboBox<String> filterType;
    @FXML private DatePicker filterDate;
    @FXML private Button btnAddTransaction;
    
    @FXML
    public void onAddTransaction() {
        // Show add transaction dialog
    }
    
    @FXML
    public void onFilterChange() {
        // Filter transactions
    }
}
```

**Purpose:** Manages transaction list and CRUD operations

**Features:**
- Display all transactions in table
- Filter by type, account, date
- Add new transaction
- Edit existing transaction
- Delete transaction

---

#### `BudgetController.java` - Budget Controller
```java
@FXML
public class BudgetController {
    @FXML private TableView<Budget> budgetTable;
    
    @FXML
    public void onCreateBudget() {
        // Create new budget
    }
    
    private void checkBudgetAlerts() {
        // Notify if over budget
    }
}
```

**Purpose:** Manages budget creation and tracking

---

#### `SettingsController.java` - Settings Controller
```java
@FXML
public class SettingsController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> themeSelector;
    
    @FXML
    public void onSaveSettings() {
        // Save user preferences
    }
}
```

**Purpose:** User preferences and configuration

---

## 📐 FXML Views Explained

### `main.fxml` - Main Window Layout
```xml
<BorderPane>
    <left>
        <VBox styleClass="sidebar">
            <!-- Navigation buttons -->
        </VBox>
    </left>
    <center>
        <!-- Dynamic content area -->
    </center>
</BorderPane>
```

**Structure:**
- Left sidebar for navigation
- Center area for dynamic content
- Responsive layout

---

### `transactions.fxml` - Transactions View
```xml
<VBox>
    <HBox><!-- Title + Add Button --></HBox>
    <HBox><!-- Filters: Type, Account, Date --></HBox>
    <TableView>
        <TableColumn text="Date"/>
        <TableColumn text="Category"/>
        <TableColumn text="Type"/>
        <TableColumn text="Amount"/>
    </TableView>
</VBox>
```

**Components:**
- Add Transaction button
- Filter controls (ComboBox, DatePicker)
- TableView for transaction list

---

## 🎨 CSS Styling

### `styles.css` - Custom Theme
```css
.transactions-root {
    -fx-background-color: #1e1e1e;
}

.page-title {
    -fx-font-size: 24px;
    -fx-font-weight: bold;
}

.add-btn {
    -fx-background-color: #4CAF50;
    -fx-text-fill: white;
}

.table-dark {
    -fx-background-color: #2d2d2d;
}
```

**Theme Features:**
- Dark background (#1e1e1e)
- Modern button styles
- Custom table appearance
- Consistent color scheme

---

## 🔧 Maven Configuration

### `pom.xml` - Dependencies
```xml
<dependencies>
    <!-- JavaFX Controls -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21</version>
    </dependency>
    
    <!-- JavaFX FXML -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21</version>
    </dependency>
    
    <!-- Ikonli Icons -->
    <dependency>
        <groupId>org.kordamp.ikonli</groupId>
        <artifactId>ikonli-javafx</artifactId>
        <version>12.3.1</version>
    </dependency>
    
    <!-- Gson for JSON -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

---

## 🚀 Application Flow

### Startup Sequence:
```
1. main() method in App.java
   ↓
2. JavaFX Application.launch()
   ↓
3. start() method called
   ↓
4. Load FXML file (e.g., transactions.fxml)
   ↓
5. Apply CSS styles
   ↓
6. Create and show Stage (window)
   ↓
7. User interacts with UI
   ↓
8. Controllers handle events
```

---

## 💾 Data Flow (Planned)

### Saving a Transaction:
```
User clicks "Add Transaction"
   ↓
TransactionsController.onAddTransaction()
   ↓
Create new Transaction object
   ↓
Add to User's transaction list
   ↓
FileIOService.saveUserData()
   ↓
Convert to JSON with Gson
   ↓
Write to file (data/users.json)
```

### Loading Data:
```
Application starts
   ↓
FileIOService.loadUserData()
   ↓
Read JSON file
   ↓
Parse with Gson
   ↓
Create User and Account objects
   ↓
Display in UI
```

---

## 🔐 Security Features (Planned)

### Password Hashing:
```java
public class EncryptionService {
    public static String hashPassword(String password) {
        // Use BCrypt or SHA-256
        return hashedPassword;
    }
    
    public static boolean verifyPassword(String password, String hash) {
        // Compare password with stored hash
        return isMatch;
    }
}
```

---

## 📊 Charts Implementation (Planned)

### Pie Chart - Expense Breakdown:
```java
PieChart pieChart = new PieChart();
pieChart.getData().addAll(
    new PieChart.Data("Food", 500),
    new PieChart.Data("Transport", 300),
    new PieChart.Data("Bills", 200)
);
```

### Bar Chart - Monthly Comparison:
```java
CategoryAxis xAxis = new CategoryAxis();
NumberAxis yAxis = new NumberAxis();
BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

XYChart.Series series1 = new XYChart.Series();
series1.setName("Income");
series1.getData().add(new XYChart.Data("Jan", 5000));
```

---

## 🧪 Testing Strategy (Future)

### Unit Tests:
```java
@Test
public void testAddTransaction() {
    Account account = new Account("Wallet", 1000);
    Transaction t = new Transaction(500, TransactionType.EXPENSE);
    
    account.withdraw(t.getAmount());
    
    assertEquals(500, account.getBalance());
}
```

---

## 📝 Code Comments Standard

```java
/**
 * Processes a new financial transaction
 * 
 * @param transaction The transaction to process
 * @param account The account to debit/credit
 * @return true if successful, false otherwise
 * @throws InsufficientFundsException if balance is too low
 */
public boolean processTransaction(Transaction transaction, Account account) 
    throws InsufficientFundsException {
    // Implementation
}
```

---

## 🎯 Key Points for Demo

1. **Clean Code:** Well-commented, organized, professional
2. **MVC Pattern:** Separation of concerns
3. **Modern UI:** FXML + CSS for design
4. **Maven:** Industry-standard dependency management
5. **Scalable:** Easy to add new features

---

**This documentation helps explain the code to your teacher and demonstrates your understanding of software architecture!**
