import java.util.Scanner;

public class Product{
    Scanner sc = new Scanner(System.in);

    String title, author;
    int copiesAvailable;

    void inputDetails(){
        System.out.print("Book title: ");
        title = sc.nextLine();
        System.out.print("Author name: ");
        author = sc.nextLine();
        System.out.print("Number of available copies: ");
        copiesAvailable = sc.nextInt();
    }

    void borrowBook(){
        System.out.print("Number of copies you want to borrow: ");
        int x = sc.nextInt();
        copiesAvailable-=x;
        System.out.println(x+" copies were borrowed!");
    }

    void displayBookDetails(){
        System.out.println("Book title: "+title);
        System.out.println("Author name: "+author);
        System.out.println("Remaining copies: "+copiesAvailable);
    }

    public static void main(String[] args){
        Product Book = new Product();
        Book.inputDetails();
        Book.displayBookDetails();
        Book.borrowBook();
        Book.displayBookDetails();
    }
}
