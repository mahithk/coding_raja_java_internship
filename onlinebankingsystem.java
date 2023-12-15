import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER, LOAN_PAYMENT
}

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private String accountType;
    private double balance;
    private int pin;
    private List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, String accountType, int pin) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.balance = 0.0;
        this.pin = pin;
        transactionHistory = new ArrayList<>();
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void recordTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction(type, amount);
        transactionHistory.add(transaction);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }

    public void deposit(double amount, int enteredPin) {
        if (enteredPin != pin) {
            System.out.println("Invalid PIN. Deposit not allowed.");
            return;
        }

        if (amount > 0) {
            balance += amount;
            recordTransaction(TransactionType.DEPOSIT, amount);
            System.out.println("Deposited $" + amount + " into " + accountType + " account.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void depositToRecipient(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount, int enteredPin) {
        if (enteredPin != pin) {
            System.out.println("Invalid PIN. Withdrawal not allowed.");
            return;
        }

        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recordTransaction(TransactionType.WITHDRAWAL, amount);
            System.out.println("Withdrawn $" + amount + " from " + accountType + " account.");
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public void transfer(BankAccount recipient, double amount, int enteredPin, Scanner scanner) {
        if (enteredPin != pin) {
            System.out.println("Invalid PIN. Transfer not allowed.");
            return;
        }

        // Use the same scanner object to capture the recipient's PIN
        System.out.print("Enter recipient's PIN (4 digits): ");
        int recipientPin = scanner.nextInt();

        if (recipientPin == recipient.getPin()) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                recipient.depositToRecipient(amount);
                recipient.deposit(amount, enteredPin);
                recordTransaction(TransactionType.TRANSFER, amount);
                System.out.println("Transferred $" + amount + " to account " + recipient.getAccountNumber());
            } else {
                System.out.println("Invalid transfer amount or insufficient funds.");
            }
        } else {
            System.out.println("Invalid recipient PIN. Transfer not allowed.");
        }
    }

    public void displayTransactionHistory() {
        for (Transaction transaction : transactionHistory) {
            System.out.println("Transaction Type: " + transaction.getType() + ", Amount: $" + transaction.getAmount());
        }
    }

}

class Transaction {
    private TransactionType type;
    private double amount;

    public Transaction(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class Loan {
    private String loanID;
    private String borrower;
    private double loanAmount;
    private double interestRate;
    private double remainingAmount;

    public Loan(String loanID, String borrower, double loanAmount, double interestRate) {
        this.loanID = loanID;
        this.borrower = borrower;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.remainingAmount = loanAmount;
    }

    public String getLoanID() {
        return loanID;
    }

    public String getBorrower() {
        return borrower;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void makePayment(double amount) {
        if (amount > 0 && remainingAmount > 0) {
            double interest = remainingAmount * (interestRate / 100);
            double totalPayment = amount + interest;
            if (totalPayment <= remainingAmount) {
                remainingAmount -= totalPayment;
                System.out.println("Payment of $" + amount + " made. Remaining loan balance: $" + remainingAmount);
            } else {
                System.out.println("Payment exceeds the remaining loan balance.");
            }
        } else {
            System.out.println("Invalid payment or loan already paid off.");
        }
    }
}

class Bank {
    private List<BankAccount> accounts;
    private List<Loan> loans;
    private Map<String, Integer> accountPINs;

    public Bank() {
        accounts = new ArrayList<>();
        loans = new ArrayList<>();
        accountPINs = new HashMap<>();
    }

    public void createAccount(String accountNumber, String accountHolder, String accountType, int pin) {
        BankAccount account = new BankAccount(accountNumber, accountHolder, accountType, pin);
        accounts.add(account);
        accountPINs.put(accountNumber, pin);
        System.out.println("Account created successfully.");
    }

    public BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public boolean verifyPIN(String accountNumber, int enteredPin) {

        Integer storedPin = accountPINs.get(accountNumber);
        return storedPin != null && storedPin == enteredPin;
    }

    public void applyForLoan(String loanID, String borrower, double loanAmount, double interestRate) {
        Loan loan = new Loan(loanID, borrower, loanAmount, interestRate);
        loans.add(loan);
        System.out.println("Loan application approved. Loan ID: " + loan.getLoanID());
    }

    public Loan findLoan(String loanID) {
        for (Loan loan : loans) {
            if (loan.getLoanID().equals(loanID)) {
                return loan;
            }
        }
        return null;
    }
}

public class OnlineBankingSystem {

    public static void main(String[] args) {
        Map<String, BankAccount> accounts = new HashMap<>();
        Scanner trans = new Scanner(System.in);
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Apply for Loan");
            System.out.println("5. Make Loan Payment");
            System.out.println("6. Fund Transfer");
            System.out.println("7. Balance Inquiry");
            System.out.println("8. Transaction History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accHolder = scanner.nextLine();
                    System.out.print("Enter account type: ");
                    String accType = scanner.nextLine();
                    System.out.print("Enter your PIN (4 digits): ");
                    int pin = scanner.nextInt();
                    bank.createAccount(accNumber, accHolder, accType, pin);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    String depositAccNumber = scanner.nextLine();
                    BankAccount depositAccount = bank.findAccount(depositAccNumber);
                    if (depositAccount != null) {
                        System.out.print("Enter deposit amount: ");
                        double amount = scanner.nextDouble();
                        System.out.print("Enter your PIN (4 digits): ");
                        int enteredPin = scanner.nextInt();
                        depositAccount.deposit(amount, enteredPin);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    String withdrawAccNumber = scanner.nextLine();
                    BankAccount withdrawAccount = bank.findAccount(withdrawAccNumber);
                    if (withdrawAccount != null) {
                        System.out.print("Enter withdrawal amount: ");
                        double amount = scanner.nextDouble();
                        System.out.print("Enter your PIN (4 digits): ");
                        int enteredPin = scanner.nextInt();
                        withdrawAccount.withdraw(amount, enteredPin);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter loan ID: ");
                    String loanID = scanner.nextLine();
                    System.out.print("Enter borrower name: ");
                    String borrower = scanner.nextLine();
                    System.out.print("Enter loan amount: ");
                    double loanAmount = scanner.nextDouble();
                    System.out.print("Enter interest rate (%): ");
                    double interestRate = scanner.nextDouble();
                    bank.applyForLoan(loanID, borrower, loanAmount, interestRate);
                    break;
                case 5:
                    System.out.print("Enter loan ID: ");
                    String paymentLoanID = scanner.nextLine();
                    Loan paymentLoan = bank.findLoan(paymentLoanID);
                    if (paymentLoan != null) {
                        System.out.print("Enter payment amount: ");
                        double paymentAmount = scanner.nextDouble();
                        paymentLoan.makePayment(paymentAmount);
                    } else {
                        System.out.println("Loan not found.");
                    }
                    break;
                case 6:
                    System.out.print("Enter sender's account number: ");
                    String senderAccNumber = trans.nextLine();
                    BankAccount senderAccount = bank.findAccount(senderAccNumber);

                    System.out.print("Enter recipient's account number: ");
                    String recipientAccNumber = trans.nextLine();
                    BankAccount recipientAccount = bank.findAccount(recipientAccNumber);

                    if (senderAccount != null && recipientAccount != null) {
                        System.out.print("Enter your PIN (4 digits): ");
                        int enteredPin = scanner.nextInt();
                        System.out.print("Enter transfer amount: ");
                        double amount = trans.nextDouble();
                        senderAccount.transfer(recipientAccount, amount, enteredPin, scanner); // Pass scanner here
                    } else {
                        System.out.println("Sender or recipient account not found.");
                    }
                    break;

                case 7:
                    System.out.print("Enter account number for balance inquiry: ");
                    String inquiryAccNumber = scanner.nextLine();
                    BankAccount inquiryAccount = bank.findAccount(inquiryAccNumber);
                    if (inquiryAccount != null) {
                        System.out.print("Enter your PIN (4 digits): ");
                        int enteredPin = scanner.nextInt();
                        if (inquiryAccount.getPin() == enteredPin) {
                            double balance = inquiryAccount.getBalance();
                            System.out.println("Account " + inquiryAccNumber + " has a balance of $" + balance);
                        } else {
                            System.out.println("Invalid PIN. Balance inquiry not allowed.");
                        }
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 8:
                    System.out.print("Enter account number for transaction history: ");
                    String historyAccNumber = scanner.nextLine();
                    BankAccount historyAccount = bank.findAccount(historyAccNumber);
                    if (historyAccount != null) {
                        System.out.print("Enter your PIN (4 digits): ");
                        int enteredPin = scanner.nextInt();
                        if (historyAccount.getPin() == enteredPin) {
                            historyAccount.displayTransactionHistory();
                        } else {
                            System.out.println("Invalid PIN. Transaction history not allowed.");
                        }
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");

            }
        }
    }
}