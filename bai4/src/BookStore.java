import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class BookStore {
    private final Map<String, Integer> stock = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void getStock(String title) {
        readLock.lock(); // Khóa đọc
        try {
            System.out.println(Thread.currentThread().getName() + " đang kiểm tra sách: " + title);
            Thread.sleep(500);
            int count = stock.getOrDefault(title, 0);
            System.out.println(Thread.currentThread().getName() + " thấy có " + count + " quyển " + title);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            readLock.unlock();
        }
    }

    public void addBook(String title, int qty) {
        writeLock.lock(); // Khóa ghi
        try {
            System.out.println(">>> " + Thread.currentThread().getName() + " BẮT ĐẦU nhập " + qty + " quyển " + title);
            Thread.sleep(1000);
            stock.put(title, stock.getOrDefault(title, 0) + qty);
            System.out.println(">>> " + Thread.currentThread().getName() + " ĐÃ NHẬP XONG. Tổng: " + stock.get(title));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }

    public void borrow(String title, int qty) {
        writeLock.lock(); // Khóa ghi
        try {
            System.out.println("<<< " + Thread.currentThread().getName() + " BẮT ĐẦU mượn " + qty + " quyển " + title);
            Thread.sleep(1000);

            int currentQty = stock.getOrDefault(title, 0);
            if (currentQty >= qty) {
                stock.put(title, currentQty - qty);
                System.out.println("<<< " + Thread.currentThread().getName() + " MƯỢN THÀNH CÔNG. Còn lại: " + stock.get(title));
            } else {
                System.out.println("<<< " + Thread.currentThread().getName() + " MƯỢN THẤT BẠI. Không đủ sách!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }
}
class ReaderTask implements Runnable {
    private BookStore store;
    private String title;

    // Constructor để truyền cái nhà sách và tên sách vào
    public ReaderTask(BookStore store, String title) {
        this.store = store;
        this.title = title;
    }

    @Override
    public void run() {
        store.getStock(title); // Gọi hàm đọc sách
    }
}

class BorrowBookTask implements Runnable {
    private BookStore store;
    private String title;
    private int qty;

    public BorrowBookTask(BookStore store, String title, int qty) {
        this.store = store;
        this.title = title;
        this.qty = qty;
    }

    @Override
    public void run() {
        store.borrow(title, qty); // Gọi hàm mượn sách
    }
}
class AddBookTask implements Runnable {
    private BookStore store;
    private String title;
    private int qty;

    public AddBookTask(BookStore store, String title, int qty) {
        this.store = store;
        this.title = title;
        this.qty = qty;
    }

    @Override
    public void run() {
        store.addBook(title, qty); // Gọi hàm thêm sách
    }
}
class Main {
    public static void main(String[] args) {
        BookStore store = new BookStore();
        store.addBook("Java", 10); // Khởi tạo trước 10 quyển sách
        Runnable readJob = new ReaderTask(store, "Java");
        Runnable borrowJob = new BorrowBookTask(store, "Java", 5);
        Runnable addJob = new AddBookTask(store, "Java", 3);

        Thread r1 = new Thread(readJob, "Reader-1");
        Thread r2 = new Thread(readJob, "Reader-2");
        Thread r3 = new Thread(readJob, "Reader-3");

        Thread w1 = new Thread(borrowJob, "Writer-Borrow");
        Thread w2 = new Thread(addJob, "Writer-Add");

        r1.start();
        r2.start();
        w1.start();
        r3.start();
        w2.start();
    }
}