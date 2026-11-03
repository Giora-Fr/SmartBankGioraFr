package com.example.smartbankgiorafridman;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

    TextView bankUser;
    EditText nameInput;
    EditText amountInput;
    int sum = 0;

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
    }

    public void getMoney(View view) {
        try {
            String inputStr = amountInput.getText().toString().trim();
            if (inputStr.isEmpty()) {
                throw new EmptyInputException("Please enter an amount");
            }

            int x = Integer.parseInt(inputStr);

            if (x < 0) {
                throw new NegativeAmountException("You cannot get a negative number of money");
            } else if (x > sum) {
                throw new InsufficientFundsException("You cannot get more money than you have");
            } else {
                sum = sum - x;
                Toast.makeText(this, "You took " + x + " money", Toast.LENGTH_SHORT).show();
                bankUser.setText("Your Bank:" + sum);
            }
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

            if (x < 0) {
                throw new NegativeAmountException("You cannot add a negative number of money");
            } else {
                sum = sum + x;
                Toast.makeText(this, "You added " + x + " money", Toast.LENGTH_SHORT).show();
                bankUser.setText("Your Bank:" + sum);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format", Toast.LENGTH_SHORT).show();
        } catch (EmptyInputException | NegativeAmountException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void send_to_someone(View view) {
        try {
            String inputStr = amountInput.getText().toString().trim();
            if (inputStr.isEmpty()) {
                throw new EmptyInputException("Please enter an amount");
            }

            int x = Integer.parseInt(inputStr);

            if (x < 0) {
                throw new NegativeAmountException("You cannot send a negative number of money");
            } else if (x > sum) {
                throw new InsufficientFundsException("You cannot send more money than you have");
            } else {
                sum = sum - x;
                String targetName = nameInput.getText().toString().trim();
                Toast.makeText(this, "You sent " + x + " money to user " + targetName, Toast.LENGTH_SHORT).show();
                bankUser.setText("Your Bank:" + sum);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid number format", Toast.LENGTH_SHORT).show();
        } catch (EmptyInputException | NegativeAmountException | InsufficientFundsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
