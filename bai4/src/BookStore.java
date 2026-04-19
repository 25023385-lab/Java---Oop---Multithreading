import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class BookStore {
    // 1. Khai báo kho chứa sách và bộ khóa
    private final Map<String, Integer> stock = new HashMap<>();
    //hashmap khong an toan trong multithreading
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    //khóa
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // 2. Phương thức xem số lượng sách (Đọc)
    public void getStock(String title) {
        readLock.lock(); // Xin khóa đọc
        try {
            System.out.println(Thread.currentThread().getName() + " đang kiểm tra sách: " + title);
            Thread.sleep(500); // Giả lập thời gian đọc dữ liệu mất 0.5s

            // Lấy số lượng, nếu không có thì trả về 0
            int count = stock.getOrDefault(title, 0);
            System.out.println(Thread.currentThread().getName() + " thấy có " + count + " quyển " + title);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            readLock.unlock(); // Bắt buộc phải trả khóa trong finally
        }
    }

    // 3. Phương thức nhập thêm sách (Ghi)
    public void addBook(String title, int qty) {
        writeLock.lock(); // Xin khóa ghi (Độc quyền)
        try {
            System.out.println(">>> " + Thread.currentThread().getName() + " BẮT ĐẦU nhập " + qty + " quyển " + title);
            Thread.sleep(1000); // Giả lập thời gian nhập sách mất 1s

            // Cập nhật số lượng
            stock.put(title, stock.getOrDefault(title, 0) + qty);
            System.out.println(">>> " + Thread.currentThread().getName() + " ĐÃ NHẬP XONG. Tổng cập nhật: " + stock.get(title));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock(); // Nhả khóa ghi
        }
    }

    // 4. Phương thức mượn sách (Ghi)
    public void borrow(String title, int qty) {
        writeLock.lock(); // Xin khóa ghi (Độc quyền)
        try {
            System.out.println("<<< " + Thread.currentThread().getName() + " BẮT ĐẦU mượn " + qty + " quyển " + title);
            Thread.sleep(1000); // Giả lập thời gian xử lý mượn sách

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
            writeLock.unlock(); // Nhả khóa ghi
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BookStore store = new BookStore();

        // Khởi tạo sách có sẵn trước khi chạy đa luồng
        store.addBook("Java", 10);

        // Tạo 3 luồng đọc (Sử dụng Lambda cho ngắn gọn)
        Thread r1 = new Thread(() -> store.getStock("Java"), "Reader-1");
        Thread r2 = new Thread(() -> store.getStock("Java"), "Reader-2");
        Thread r3 = new Thread(() -> store.getStock("Java"), "Reader-3");

        // Tạo 2 luồng ghi
        Thread w1 = new Thread(() -> store.borrow("Java", 5), "Writer-Borrow");
        Thread w2 = new Thread(() -> store.addBook("Java", 3), "Writer-Add");

        // Khởi động tất cả các luồng cùng lúc
        r1.start();
        r2.start();
        w1.start(); // Cố tình xen kẽ luồng ghi vào giữa
        r3.start();
        w2.start();
    }
}