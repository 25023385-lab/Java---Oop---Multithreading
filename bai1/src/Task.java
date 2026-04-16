public class Task extends Thread implements Runnable {
    String name;
    long durationMs;
    public Task(String name, long durationMs) {
        this.name = name;
        this.durationMs = durationMs;
    }
    public void run() {
        try {
            System.out.println("Start:" + "<" +this.name + ">");
            Thread.sleep(durationMs);
            System.out.println("End:" + "<" +this.name + ">");}
        catch (InterruptedException e) {
            // tai sao lại catch lỗi này
            //prinStackTrace la cai gi?
            e.printStackTrace();
        }
    }
}

class Main {
    public static void main(String[] args){
        Task t1 = new Task("Auction", 3000);
        Task t2 = new Task("Disrete Math", 2000);
        Thread  t3 = new Thread(t1);
        Thread t4 = new Thread(t2);
        //thread nhu kieu la worker
        t3.start();
        t4.start();
        try {
            t3.join();
            t4.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            // tai sao join lại có lỗi đấy nhỉ?
        }
        finally {
            System.out.println("All task done");
        }
    }
}