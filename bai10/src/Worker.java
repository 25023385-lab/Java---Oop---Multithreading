public class Worker implements Runnable {
    volatile Boolean running = true;
    // tính hiện thị bộ nhớ
    // chạy chương trình ở nhiều core khác nhau; bộ nhớ đệm của các core khi thay đổi giá trị thì 2 thằng sẽ không biết
    // volatile bắt phải chạy trên RAM
    @Override
    public void run() {
        while (running) {
            System.out.println("Working");
        }
    }
    public void stop() {
        running = false;
        System.out.println("Worker stopped");
    }
}
class Main {
    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread t1 = new Thread(worker);
        t1.start();
        try{
        Thread.sleep(1000);
        worker.stop();
        t1.join();
        // ép luồng main phải chờ t1 chạy xong mới được chạy tiếp

    }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}