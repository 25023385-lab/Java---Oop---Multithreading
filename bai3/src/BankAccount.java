import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BankAccount {
    private int balance = 0;

    public synchronized void deposit(int amount) {
        balance = balance + amount;
    }

    public synchronized void withdraw(int amount) {
        balance = balance - amount;
    }

    public int getBalance() {
        return balance;
    }
}
class deposit implements Runnable {
    private BankAccount bankAccount;
    public deposit(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    public void run(){
        for (int i = 0; i < 1000 ; i++) {
            bankAccount.deposit(100);
        }
    }
}
class withdraw implements Runnable {
    //implement runnable: công việc can lam
    private BankAccount bankAccount;
    public withdraw(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    public void run() {
        for (int i = 0; i < 1000 ; i++) {
            bankAccount.withdraw(100);
        }
    }
}
// race condition: Nhiều thread truy cập & thay đổi shared data cùng lúc → kết quả phụ thuộc thứ tự thực thi
//Executor Framework chỉ nhận runnable
class Main {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();
        //de dam bao rang 2 thread lam vien tren cung 1 dữ liệu
        Thread a = new Thread(new deposit(bankAccount));
        Thread b = new Thread(new withdraw(bankAccount));
        // tạo công nhân
        a.start();
        b.start();
        try {
            a.join(); // chờ thằng a làm xong mới b mới cha
            b.join();
        } catch (InterruptedException e) {
        }
    }
}