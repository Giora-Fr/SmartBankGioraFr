package com.example.smartbankgiorafridman;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView bankUser;
    EditText nameInput;
    EditText amountInput;
    BankAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bankUser = findViewById(R.id.bank);
        amountInput = findViewById(R.id.editTextNumber);
        nameInput = findViewById(R.id.editTextText);

        account = new BankAccount("User", 0);
    }

    public void getMoney(View view) {
        try {
            String inputStr = amountInput.getText().toString().trim();
            if (inputStr.isEmpty()) {
                throw new EmptyInputException("Please enter an amount");
            }

            int x = Integer.parseInt(inputStr);
            account.withdraw(x);

            Toast.makeText(this, "You took " + x + " money", Toast.LENGTH_SHORT).show();
            bankUser.setText("Your Bank:" + account.getBalance());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format", Toast.LENGTH_SHORT).show();
        } catch (EmptyInputException | NegativeAmountException | InsufficientFundsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void add_money(View view) {
        try {
            String inputStr = amountInput.getText().toString().trim();
            if (inputStr.isEmpty()) {
                throw new EmptyInputException("Please enter an amount");
            }

            int x = Integer.parseInt(inputStr);
            account.deposit(x);

            Toast.makeText(this, "You added " + x + " money", Toast.LENGTH_SHORT).show();
            bankUser.setText("Your Bank:" + account.getBalance());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format", Toast.LENGTH_SHORT).show();
        } catch (EmptyInputException | NegativeAmountException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void send_to_someone(View view) {
        try {
            String inputStr = amountInput.getText().toString().trim();
            String targetName = nameInput.getText().toString().trim();

            if (inputStr.isEmpty()) {
                throw new EmptyInputException("Please enter an amount");
            }
            if (targetName.isEmpty() || targetName.equals("Send to someone")) {
                throw new EmptyInputException("Please enter a valid recipient name");
            }

            int x = Integer.parseInt(inputStr);

            account.sendMoney(targetName, x);

            Toast.makeText(this, "You sent " + x + " money to user " + targetName, Toast.LENGTH_SHORT).show();
            bankUser.setText("Your Bank:" + account.getBalance());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format", Toast.LENGTH_SHORT).show();
        } catch (EmptyInputException | NegativeAmountException | InsufficientFundsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showHistory(View view) {
        List<Transaction> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            Toast.makeText(this, "No transactions yet", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Transaction t : history) {
            sb.append(t.toString()).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Transaction History")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    public void showOtherUsers(View view) {
        List<BankUser> otherUsers = account.getOtherUsers();
        if (otherUsers.isEmpty()) {
            Toast.makeText(this, "No other users registered", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (BankUser user : otherUsers) {
            sb.append(user.toString()).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Other Bank Users")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    public static class BankAccount {
        private String ownerName;
        private int balance;
        private List<Transaction> transactionHistory;
        private List<BankUser> otherUsers;

        public BankAccount(String ownerName, int initialBalance) {
            this.ownerName = ownerName;
            this.balance = initialBalance;
            this.transactionHistory = new ArrayList<>();
            this.otherUsers = new ArrayList<>();
        }

        public int getBalance() {
            return balance;
        }

        public List<Transaction> getTransactionHistory() {
            return transactionHistory;
        }

        public List<BankUser> getOtherUsers() {
            return otherUsers;
        }

        public void deposit(int amount) {
            if (amount < 0) {
                throw new NegativeAmountException("You cannot add a negative number of money");
            }
            balance += amount;
            transactionHistory.add(new Transaction("Deposit", amount));
        }

        public void withdraw(int amount) {
            if (amount < 0) {
                throw new NegativeAmountException("You cannot get a negative number of money");
            }
            if (amount > balance) {
                throw new InsufficientFundsException("You cannot get more money than you have");
            }
            balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount));
        }

        public void sendMoney(String recipient, int amount) {
            if (amount < 0) {
                throw new NegativeAmountException("You cannot send a negative number of money");
            }
            if (amount > balance) {
                throw new InsufficientFundsException("You cannot send more money than you have");
            }

            balance -= amount;
            transactionHistory.add(new Transaction("Transfer to " + recipient, amount));

            BankUser existingUser = null;
            for (BankUser u : otherUsers) {
                if (u.getName().equalsIgnoreCase(recipient)) {
                    existingUser = u;
                    break;
                }
            }

            if (existingUser != null) {
                existingUser.addBalance(amount);
            } else {
                otherUsers.add(new BankUser(recipient, amount));
            }
        }
    }

    public static class BankUser {
        private String name;
        private int balance;

        public BankUser(String name, int initialBalance) {
            this.name = name;
            this.balance = initialBalance;
        }

        public String getName() {
            return name;
        }

        public int getBalance() {
            return balance;
        }

        public void addBalance(int amount) {
            this.balance += amount;
        }

        @Override
        public String toString() {
            return "User: " + name + " | Balance: " + balance;
        }
    }

    public static class Transaction {
        private String type;
        private int amount;

        public Transaction(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return type + ": " + amount;
        }
    }

    public static class EmptyInputException extends RuntimeException {
        public EmptyInputException(String message) {
            super(message);
        }
    }

    public static class NegativeAmountException extends RuntimeException {
        public NegativeAmountException(String message) {
            super(message);
        }
    }

    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }
}
