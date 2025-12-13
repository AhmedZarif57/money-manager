<div align="center">
  <img src="logo.png" alt="Money Manager Logo" width="200"/>
  
  # 💰 Smart Money Manager

  <p>A modern, feature-rich personal finance management application built with JavaFX</p>
  
  [![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://jdk.java.net/25/)
  [![JavaFX](https://img.shields.io/badge/JavaFX-25.0.1-blue.svg)](https://openjfx.io/)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
  
</div>

---

## ✨ Features

- 🔐 **Secure Authentication** - Industry-standard password hashing with PBKDF2-SHA256
- 💳 **Multiple Accounts** - Manage Wallet, Bank, Card, and Bkash accounts
- 📊 **Transaction Management** - Track income, expenses, and transfers
- 💸 **Budget Planning** - Set and monitor category-based budgets
- 📈 **Analytics Dashboard** - Visualize spending patterns with charts
- 🌍 **Multi-Currency Support** - USD, EUR, GBP, INR, JPY, BDT
- 🎨 **Light/Dark Theme** - Switch between themes
- 📅 **Monthly Filtering** - View data by specific months
- 🔄 **Account Transfers** - Transfer money between accounts
- 💾 **Data Persistence** - Encrypted JSON-based local storage
- 🛡️ **Security First** - 65,536 iterations password hashing, salt protection

## 🚀 Quick Start Guide

### Prerequisites

Before you begin, make sure you have:

1. **Java 25** installed
   - Download from: [https://jdk.java.net/25/](https://jdk.java.net/25/)
   - Verify installation: Open terminal/command prompt and run `java -version`

2. **JavaFX SDK 25.0.1**
   - Download from: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
   - Choose the version for your operating system (Windows, Mac, or Linux)

### 📥 Installation Steps

#### ⚡ Quick Start (Recommended):

1. **Clone or download this repository**
   ```bash
   git clone https://github.com/yourusername/money-manager.git
   cd money-manager
   ```

2. **Download and extract JavaFX SDK**
   - Download JavaFX SDK 25.0.1 for your operating system
   - Extract the zip file
   - Place the extracted `javafx-sdk-25.0.1` folder in the project root directory

3. **Run the application**
   - **Windows**: Double-click `start.bat`
   - **Mac/Linux**: Double-click `start.sh` or run `./start.sh` in terminal
   
   That's it! The script will automatically compile and run the application.

#### 📝 Manual Compilation (Optional):

If you prefer to compile and run separately:

**For Windows Users:**

1. **Clone or download this repository**
   ```bash
   git clone https://github.com/yourusername/money-manager.git
   cd money-manager
   ```

2. **Download and extract JavaFX SDK**
   - Download JavaFX SDK 25.0.1 for Windows
   - Extract the zip file
   - Place the extracted `javafx-sdk-25.0.1` folder in the project root directory
   - Your folder structure should look like:
     ```
     money-manager/
     ├── javafx-sdk-25.0.1/
     │   └── lib/
     ├── src/
     ├── start.bat
     ├── compile.bat
     └── run.bat
     ```

3. **Compile the application**
   - Double-click `compile.bat` OR
   - Open Command Prompt in the project folder and run:
     ```cmd
     compile.bat
     ```

4. **Run the application**
   - Double-click `run.bat` OR
   - Run in Command Prompt:
     ```cmd
     run.bat
     ```

#### For Mac/Linux Users:

1. **Clone or download this repository**
   ```bash
   git clone https://github.com/yourusername/money-manager.git
   cd money-manager
   ```

2. **Download and extract JavaFX SDK**
   - Download JavaFX SDK 25.0.1 for your OS (Mac/Linux)
   - Extract the file
   - Place the extracted `javafx-sdk-25.0.1` folder in the project root directory

3. **Make scripts executable**
   ```bash
   chmod +x compile.sh run.sh start.sh
   ```

4. **Compile the application**
   ```bash
   ./compile.sh
   ```

5. **Run the application**
   ```bash
   ./run.sh
   ```

---

**💡 Tip:** Use `start.bat` (Windows) or `start.sh` (Mac/Linux) to compile and run in one step!

## 📖 User Guide

### First Time Setup

1. **Create an Account**
   - When you first run the application, click "Create an account"
   - Enter your username, email, and password
   - Click "Sign Up"

2. **Login**
   - Use your credentials to log in
   - Default demo account: username: `demo`, password: `demo123`

### Managing Transactions

1. **Add Transaction**
   - Go to Transactions page
   - Click "Add Transaction"
   - Select type (Income/Expense/Transfer)
   - Choose category and account
   - Enter amount and date
   - Click "Add"

2. **Transfer Between Accounts**
   - Select "Transfer" as transaction type
   - Choose "From" and "To" accounts
   - Enter amount
   - Click "Add"

### Managing Budgets

1. **Create Budget**
   - Go to Budget page
   - Click "Add Budget"
   - Select category
   - Set budget limit
   - Click "Add"

2. **Edit Account Balance**
   - Click on any account tile (Wallet, Bank, Card, Bkash)
   - Enter new balance
   - Click "Save"

### Settings & Customization

1. **Change Currency**
   - Go to Settings page
   - Select your preferred currency
   - Click "Save Settings"
   - All amounts will be converted automatically

2. **Switch Theme**
   - Go to Settings page
   - Choose "Dark" or "Light" theme
   - Click "Save Settings"

3. **Select Month**
   - Go to Settings page
   - Choose any month from the dropdown
   - Click "Save Settings"
   - All pages will show data for that month

4. **Reset Data**
   - Go to Settings page
   - Click "Reset Data" button
   - Confirm to clear all transactions and reset account balances

## �️ Security Features

### Password Protection
This application implements **industry-standard security practices**:

- **PBKDF2 with SHA-256** - Industry-standard password hashing algorithm
- **65,536 Iterations** - Exceeds OWASP recommendations for password security
- **Random Salt** - Each password gets a unique 256-bit salt
- **Automatic Migration** - Existing passwords are automatically upgraded to secure hashing on next login
- **Timing Attack Prevention** - Constant-time comparison prevents side-channel attacks
- **No Plain Text Storage** - Passwords are never stored in readable format

### Data Storage
All your financial data is stored locally in JSON files at:
- **Windows**: `C:\Users\YourUsername\money-manager-data\`
- **Mac/Linux**: `~/money-manager-data/`

Files include:
- `users.json` - User accounts (passwords securely hashed)
- `accounts.json` - Account balances
- `transactions.json` - All transactions
- `budgets.json` - Budget settings
- `categories.json` - Income/Expense categories

**Privacy First**: Your data never leaves your computer. No cloud sync, no external servers.

## 🔧 Troubleshooting

### "Java is not installed" error
- Install Java 25 from [Java SE Downloads](https://jdk.java.net/25/)
- Make sure Java is added to your system PATH

### "JavaFX SDK not found" error
- Download JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/)
- Extract it to the project root folder
- Ensure the folder is named exactly `javafx-sdk-25.0.1`

### "Application not compiled" error
- Run `start.bat` (Windows) or `./start.sh` (Mac/Linux) to compile and run automatically
- Check for compilation errors in the terminal

### Application won't start
- Make sure you've compiled the project first
- Check that JavaFX SDK is in the correct location
- Try running from terminal to see error messages

## 🛠️ Technology Stack

- **Language**: Java 25
- **UI Framework**: JavaFX 25.0.1
- **Data Storage**: JSON files
- **Architecture**: MVC (Model-View-Controller)

## 📝 Project Structure

```
money-manager/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       ├── controllers/    # UI Controllers
│       │       ├── models/         # Data Models
│       │       └── utils/          # Utilities
│       └── resources/
│           ├── css/                # Stylesheets
│           ├── fxml/               # UI Layouts
│           └── images/             # Icons & Images
├── compile.bat / compile.sh        # Compilation scripts
├── run.bat / run.sh                # Run scripts
└── README.md                       # This file
```

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest new features
- Submit pull requests

## 📧 Support

If you encounter any issues or have questions:
- Open an issue on GitHub
- Check the troubleshooting section above

---

**Made with ❤️ using JavaFX**
