# Money Manager - JSON Database Structure

## Database Location
All data is stored in JSON files in the following folder:
```
C:\Users\<YourUsername>\money-manager-data\
```

## Database Files

### 1. users.json
Stores user account information for authentication.
```json
[
  {
    "id": "uuid",
    "username": "demo",
    "email": "demo@example.com",
    "password": "demo123"
  }
]
```

### 2. accounts.json
Stores financial account information with four types: Wallet, Bkash, Card, Bank.
```json
[
  {
    "id": "uuid",
    "name": "Wallet",
    "balance": 5000.00
  },
  {
    "id": "uuid",
    "name": "Bkash",
    "balance": 3000.00
  },
  {
    "id": "uuid",
    "name": "Card",
    "balance": 10000.00
  },
  {
    "id": "uuid",
    "name": "Bank",
    "balance": 25000.00
  }
]
```

### 3. transactions.json
Stores all income and expense transactions.
```json
[
  {
    "id": "uuid",
    "title": "Salary",
    "date": "2025-12-01",
    "amount": 5000.00,
    "type": "INCOME",
    "accountId": "account-uuid"
  },
  {
    "id": "uuid",
    "title": "Groceries",
    "date": "2025-12-10",
    "amount": 300.00,
    "type": "EXPENSE",
    "accountId": "account-uuid"
  }
]
```

### 4. budgets.json
Stores budget limits for different categories.
```json
[
  {
    "category": "Groceries",
    "limit": 500.00
  },
  {
    "category": "Rent",
    "limit": 1500.00
  }
]
```

### 5. categories.json
Stores custom categories that users can create.
```json
[
  "Salary",
  "Rent",
  "Groceries",
  "Utilities",
  "Transport",
  "Entertainment",
  "Food",
  "Shopping",
  "Healthcare",
  "Education",
  "Saloon"
]
```

## Demo Account Credentials
- **Username:** demo
- **Password:** demo123

## Features

### 1. User Authentication
- Users must have valid credentials to login
- New users can register through the signup page
- Passwords are validated (minimum 6 characters)
- Username uniqueness is enforced

### 2. Account Types
Four predefined account types:
- **Wallet** - Cash on hand
- **Bkash** - Mobile banking (Bangladesh)
- **Card** - Credit/Debit cards
- **Bank** - Bank accounts

### 3. Custom Categories
- Users can create custom categories (e.g., "Saloon", "Gym", etc.)
- Categories are stored in `categories.json`
- When adding a transaction, select "+ Add New Category" to create new ones

### 4. Data Persistence
- All data is automatically saved to JSON files
- Data persists across application restarts
- Changes are immediately written to disk
- Human-readable JSON format for easy debugging

## Data Flow
1. **Login** → Authenticates against `users.json`
2. **Add Transaction** → Saves to `transactions.json` and updates `accounts.json` balance
3. **Add Budget** → Saves to `budgets.json`
4. **Add Category** → Saves to `categories.json`
5. **Dashboard** → Reads from all files to display statistics

## Database Initialization
On first run, the application creates:
- Demo user account (username: demo, password: demo123)
- Four account types with initial balances
- 12 months of sample transaction data
- Default budget categories
- 10 default expense/income categories
