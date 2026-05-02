# Finance Tracker

A JavaFX client-server finance tracking application.

---

## ⚙️ ONE-TIME SETUP (required before running)

### Step 1 — Mark Resources Root
Right-click `client/src/main/resources` → **Mark Directory As → Resources Root**

### Step 2 — Fix the VM argument path
Open **Run → Edit Configurations → MainApp**
In the **VM options** field, replace `PATH_TO_FX` with your actual JavaFX lib path:

```
--module-path "C:\path\to\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics --enable-native-access=javafx.graphics
```

Example on Windows:
```
--module-path "D:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics --enable-native-access=javafx.graphics
```

### Step 3 — Run
1. Run **Server** first
2. Then run **MainApp**

---

## Project Structure

```
FinanceTracker/
├── client/
│   └── src/main/
│       ├── java/client/
│       │   ├── module-info.java          ← JPMS module declaration
│       │   ├── MainApp.java
│       │   ├── ClientConnection.java
│       │   ├── models/
│       │   │   ├── User.java
│       │   │   └── Transaction.java
│       │   └── controllers/
│       │       ├── LoginController.java
│       │       ├── RegisterController.java
│       │       ├── DashboardController.java
│       │       ├── AddTransactionController.java
│       │       ├── ViewTransactionsController.java
│       │       └── SummaryController.java
│       └── resources/
│           ├── fxml/                     ← All 6 screens
│           └── css/style/style.css
── server/
   └── src/main/java/server/
       ├── Server.java
       ├── ClientHandler.java
       └── DataStore.java


```

## Test Accounts

| Username | Password    |
|----------|-------------|
| demo     | password123 |
| john     | john123     |
| sarah    | sarah123    |
