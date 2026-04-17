import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//design pattern và multithreading la phai chia ra để trị
public class Sum implements Callable<Integer> {
    int [] array;
    int start;
    int end;
    int localSum = 0;
    public Sum(int [] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
        //truyen vao start end va array để callable này làm việc
    }
    @Override
    public Integer call() throws Exception {
         for (int i = start; i <= end; i++) {
             localSum += array[i];
         }
         return localSum;
    }
}


        class Main  {
            public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);

                int n = sc.nextInt();
                int k = sc.nextInt();

                int[] array = new int[n];
                for (int i = 0; i < n; i++){
                    System.out.println("Nhập số phần tử thứ " + i + " của mảng:");
                    array[i] = sc.nextInt();
                }

                int segment_size = array.length / k;

                ExecutorService executor = Executors.newFixedThreadPool(k);
                List<Future<Integer>> futures = new ArrayList<>();

                int end = 0;
                int start = 0;

                for (int i = 0; i < k; i++){
                    start = i * segment_size;

                    if (i == k - 1){
                        end = n - 1;
                    } else {
                        end = (i + 1) * segment_size - 1;
                    }

                    // ✅ CHỈ TASK Ở TRONG FOR
                    Sum task = new Sum(array, start, end);
                    Future<Integer> future = executor.submit(task);
                    futures.add(future);
                }

                // ✅ SAU FOR MỚI LẤY KẾT QUẢ
                int total = 0;
                for (Future<Integer> f : futures) {
                    try {
                        total += f.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Tổng = " + total);

                executor.shutdown();
            }
        }