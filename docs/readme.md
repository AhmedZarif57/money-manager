# Money Manager - JavaFX Personal Finance Tracker

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)]()

A modern, user-friendly personal finance management application built with JavaFX. Track your income, expenses, budgets, and financial goals all in one place.

---

## 📋 Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Team Members](#team-members)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Usage](#usage)
- [Development](#development)
- [Roadmap](#roadmap)
- [Contributing](#contributing)

---

## ✨ Features

### Current Features (v1.0 - UI Complete)
- ✅ Modern, responsive user interface
- ✅ Multiple view screens (Dashboard, Transactions, Budget, Settings)
- ✅ Custom dark theme with CSS styling
- ✅ Professional table layouts with filters
- ✅ Ikonli icon integration for better UX

### Planned Features (In Development)
- 🔄 User authentication (Login/Signup)
- 🔄 Multi-account support (Wallet, Bank, Mobile Banking)
- 🔄 Transaction management (Income, Expense, Transfer)
- 🔄 Budget tracking and alerts
- 🔄 Financial charts and analytics
- 🔄 Data persistence with JSON
- 🔄 Export/Import functionality

---

## 🖼️ Screenshots

### Transactions View
The main transaction management screen with filtering options.

### Dashboard
Financial overview with charts and statistics.

### Budget Management
Track and manage your budgets by category.

---

## 👥 Team Members

| Member | Role | GitHub |
|--------|------|--------|
| **Zarif** | Project Leader & UI Integration | [@AhmedZarif57](https://github.com/AhmedZarif57) |
| **Akif** | Backend Developer | - |
| **Mushfiq** | UI/UX Developer | - |
| **Hemonto** | Data Persistence Engineer | - |
| **Rafi** | QA & Assets Manager | - |

---

## 🛠️ Technology Stack

### Core Technologies
- **Java SE 11+** - Core programming language
- **JavaFX 21** - Modern UI framework
- **Maven** - Build automation and dependency management

### Libraries & Dependencies
- **JavaFX Controls** - UI components (buttons, tables, etc.)
- **JavaFX FXML** - Declarative UI design
- **Ikonli JavaFX 12.3.1** - Icon library
- **Ikonli FontAwesome5 Pack** - Font Awesome icons
- **Gson 2.10.1** - JSON serialization/deserialization

---

## 📁 Project Structure

```
money-manager/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       ├── models/           # Data models (User, Account, Transaction)
│   │   │       │   ├── User.java
│   │   │       │   └── Account.java
│   │   │       │
│   │   │       ├── controllers/      # FXML Controllers
│   │   │       │   ├── MainController.java
│   │   │       │   ├── DashboardController.java
│   │   │       │   ├── TransactionsController.java
│   │   │       │   ├── BudgetController.java
│   │   │       │   └── SettingsController.java
│   │   │       │
│   │   │       └── utils/           # Utility classes
│   │   │           ├── App.java
│   │   │           └── NavigationManager.java
│   │   │
│   │   └── resources/
│   │       ├── fxml/                # FXML view files
│   │       │   ├── main.fxml
│   │       │   ├── dashboard.fxml
│   │       │   ├── transactions.fxml
│   │       │   ├── budget.fxml
│   │       │   └── settings.fxml
│   │       │
│   │       ├── css/                 # Stylesheets
│   │       │   └── styles.css
│   │       │
│   │       └── images/              # Application icons and images
│   │
│   └── test/                        # Unit tests (future)
│
├── docs/                            # Documentation
│   └── readme.md
│
├── pom.xml                          # Maven configuration
├── PROJECT_PROGRESS.md              # Progress report
├── DEMO_GUIDE.md                    # Demonstration guide
└── README.md                        # This file
```

---

## 🚀 Installation & Setup

### Prerequisites
- **Java JDK 11 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - Usually bundled with IDE
- **IntelliJ IDEA** (Recommended) or any Java IDE

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/AhmedZarif57/money-manager.git
   cd money-manager
   ```

2. **Open in IntelliJ IDEA**
   - File → Open → Select the `money-manager` folder
   - Wait for Maven to download dependencies

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   - Right-click `App.java` → Run 'App.main()'
   
   Or use Maven:
   ```bash
   mvn javafx:run
   ```

---

## 💻 Usage

### Running the Application

**From IDE:**
1. Navigate to `src/main/java/com/utils/App.java`
2. Right-click and select "Run 'App.main()'"

**From Terminal:**
```bash
mvn clean javafx:run
```

### Navigation
- Use the sidebar menu to switch between different views
- Dashboard: Financial overview and charts
- Transactions: Manage all transactions
- Budget: Track and set budgets
- Settings: Configure preferences

---

## 🔧 Development

### Setting Up Development Environment

1. **Fork and clone the repository**
2. **Create a new branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow the existing code style
   - Add comments and documentation
   - Test your changes

4. **Commit and push**
   ```bash
   git add .
   git commit -m "Add: your feature description"
   git push origin feature/your-feature-name
   ```

5. **Create a Pull Request**

### Code Style Guidelines
- Use meaningful variable and method names
- Add JavaDoc comments for classes and methods
- Follow Java naming conventions
- Keep methods focused and small
- Separate UI logic from business logic

### Building from Source
```bash
# Clean previous builds
mvn clean

# Compile
mvn compile

# Package
mvn package

# Run
mvn javafx:run
```

---

## 🗺️ Roadmap

### Phase 1: Foundation & UI ✅ COMPLETE
- [x] Project setup and configuration
- [x] Maven integration
- [x] FXML view designs
- [x] CSS styling
- [x] Basic navigation

### Phase 2: Core Functionality 🔄 IN PROGRESS
- [ ] User authentication system
- [ ] Data model implementation
- [ ] Controller logic
- [ ] Transaction CRUD operations
- [ ] JSON data persistence

### Phase 3: Features & Analytics ⏳ UPCOMING
- [ ] Dashboard charts (Pie, Bar, Line)
- [ ] Budget tracking and alerts
- [ ] Multi-account support
- [ ] Category management
- [ ] Search and filtering

### Phase 4: Polish & Advanced Features ⏳ FUTURE
- [ ] Export/Import (CSV, PDF)
- [ ] Recurring transactions
- [ ] Multi-currency support
- [ ] Cloud synchronization
- [ ] Mobile app integration

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Report Bugs** - Open an issue with details
2. **Suggest Features** - Share your ideas
3. **Submit Pull Requests** - Fix bugs or add features
4. **Improve Documentation** - Help others understand the project

### Areas Needing Help
- Backend service implementation
- Unit test coverage
- Additional themes
- Internationalization (i18n)
- Performance optimization

---

## 📄 License

This project is developed for educational purposes as part of our coursework.

---

## 📞 Contact & Support

- **Repository:** [github.com/AhmedZarif57/money-manager](https://github.com/AhmedZarif57/money-manager)
- **Issues:** [Report a bug](https://github.com/AhmedZarif57/money-manager/issues)
- **Branch:** mushfiq (development)

---

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- Ikonli project for beautiful icons
- Our instructor for guidance and support
- All team members for their dedication

---

## 📊 Project Status

**Current Version:** 1.0-SNAPSHOT  
**Status:** Active Development  
**Last Updated:** December 1, 2025  
**Progress:** Phase 1 Complete, Phase 2 In Progress

---

**Made with ❤️ by the Money Manager Team**

