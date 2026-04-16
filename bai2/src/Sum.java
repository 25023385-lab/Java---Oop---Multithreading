import java.util.Scanner;
public class Sum {

}

class Main  {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //lệnh thực thi phải được code trong main
        //Mọi code chạy (logic) phải nằm trong main
        int n =  sc.nextInt();
        int [] array =  new int[n];
        for (int i = 0; i < n; i++){
            array[i] = sc.nextInt();
        }
    }
}