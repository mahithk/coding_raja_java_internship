import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private boolean available;
    private LocalDate dueDate;

    public Book(int id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = true;
        this.dueDate = null;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void borrow() {
        if (available) {
            available = false;
        } else {
            System.out.println("This book is already borrowed.");
        }
    }

    public void returnBook() {
        if (!available) {
            available = true;
        } else {
            System.out.println("This book is already available.");
        }
    }

    public String toString() {
        return "Book ID: " + id + "\nTitle: " + title + "\nAuthor: " + author + "\nGenre: " + genre + "\nAvailable: "
                + available;
    }

    public boolean containsKeyword(String keyword) {
        return title.toLowerCase().contains(keyword.toLowerCase());
    }
}

class Patron {
    private int id;
    private String name;
    private String contactInfo;
    private List<Book> borrowedBooks;

    public Patron(int id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            book.borrow();
            borrowedBooks.add(book);
            System.out.println(name + " has borrowed " + book.getTitle());
        } else {
            System.out.println(book.getTitle() + " is not available for borrowing.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
            System.out.println(name + " has returned " + book.getTitle());
        } else {
            System.out.println(name + " did not borrow " + book.getTitle());
        }
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public String toString() {
        return "Patron ID: " + id + "\nName: " + name + "\nContact Info: " + contactInfo + "\nBorrowed Books: "
                + borrowedBooks.size();
    }

    public boolean containsKeyword(String keyword) {
        return name.toLowerCase().contains(keyword.toLowerCase());
    }
}

public class LibraryManagementSystem {
    private static void generateBookAvailabilityReport(List<Book> books) {
        System.out.println("Book Availability Report:");
        for (Book book : books) {
            System.out.println("Book Title: " + book.getTitle());
            System.out.println("Book Author: " + book.getAuthor());
            System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
            System.out.println("-------------------");
        }
    }

    private static void generateBorrowingHistoryReport(List<Patron> patrons) {
        System.out.println("Borrowing History Report:");
        for (Patron patron : patrons) {
            System.out.println("Patron Name: " + patron.getName());
            System.out.println("Borrowed Books:");
            List<Book> borrowedBooks = patron.getBorrowedBooks();
            if (borrowedBooks.isEmpty()) {
                System.out.println("No books borrowed.");
            } else {
                for (Book book : borrowedBooks) {
                    System.out.println("- " + book.getTitle());
                }
            }
            System.out.println("-------------------");
        }
    }

    // Generate a report on fines
    private static void generateFinesReport(List<Book> books) {
        System.out.println("Fines Report:");
        for (Book book : books) {
            double fine = calculateFine(book);
            if (fine > 0) {
                System.out.println("Book Title: " + book.getTitle());
                System.out.println("Fine Amount: $" + fine);
                System.out.println("-------------------");
            }
        }
    }

    private static void searchBooks(List<Book> books, String keyword) {
        boolean found = false;
        for (Book book : books) {
            if (book.containsKeyword(keyword)) {
                System.out.println(book);
                System.out.println("-------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found matching the keyword.");
        }
    }

    // Search and display patrons containing a keyword in their name
    private static void searchPatrons(List<Patron> patrons, String keyword) {
        boolean found = false;
        for (Patron patron : patrons) {
            if (patron.containsKeyword(keyword)) {
                System.out.println(patron);
                System.out.println("-------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No patrons found matching the keyword.");
        }
    }

    private static double calculateFine(Book book) {
        if (book.getDueDate() == null) {
            return 0.0; // No fine if due date is not set
        }

        LocalDate currentDate = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), currentDate);
        double finePerDay = 0.50; // Adjust the fine rate as needed

        if (daysOverdue <= 0) {
            return 0.0; // No fine if not overdue
        } else {
            return daysOverdue * finePerDay;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Book> books = new ArrayList<>();
        List<Patron> patrons = new ArrayList<>();
        while (true) {
            System.out.println("Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add Patron");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. List Available Books");
            System.out.println("6. Search Books");
            System.out.println("7. Search Patrons");
            System.out.println("8. Borrowing Histroy Report");
            System.out.println("9. Fines Report");
            System.out.println("10. Book Availability Report");
            System.out.println("11 Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Add a book
                    System.out.print("Enter Book Title: ");
                    String bookTitle = scanner.next();
                    System.out.print("Enter Author: ");
                    String bookAuthor = scanner.next();
                    System.out.print("Enter Genre: ");
                    String bookGenre = scanner.next();
                    Book book = new Book(books.size() + 1, bookTitle, bookAuthor, bookGenre);
                    books.add(book);
                    System.out.println("Book added successfully.");
                    break;

                case 2:
                    // Add a patron
                    System.out.print("Enter Patron Name: ");
                    String patronName = scanner.next();
                    System.out.print("Enter Contact Info: ");
                    String patronContactInfo = scanner.next();
                    Patron patron = new Patron(patrons.size() + 1, patronName, patronContactInfo);
                    patrons.add(patron);
                    System.out.println("Patron added successfully.");
                    break;

                case 3:
                    // Borrow a book
                    System.out.print("Enter Patron Name: ");
                    String borrowPatronName = scanner.next(); // Rename the variable to avoid conflicts
                    System.out.print("Enter Book Title: ");
                    String borrowBookTitle = scanner.next(); // Rename the variable to avoid conflicts

                    Patron selectedBorrowPatron = null;
                    for (Patron p : patrons) {
                        if (p.getName().equalsIgnoreCase(borrowPatronName)) {
                            selectedBorrowPatron = p;
                            break;
                        }
                    }

                    Book selectedBorrowBook = null;
                    for (Book b : books) {
                        if (b.getTitle().equalsIgnoreCase(borrowBookTitle)) {
                            selectedBorrowBook = b;
                            break;
                        }
                    }

                    if (selectedBorrowPatron != null && selectedBorrowBook != null) {
                        selectedBorrowPatron.borrowBook(selectedBorrowBook);
                    } else {
                        System.out.println("Invalid Patron or Book Name.");
                    }
                    break;

                case 4:
                    // Return a book
                    System.out.print("Enter Patron ID: ");
                    int patronIDReturn = scanner.nextInt();
                    System.out.print("Enter Book ID: ");
                    int bookIDReturn = scanner.nextInt();

                    Patron selectedPatronReturn = null;
                    for (Patron p : patrons) {
                        if (p.getId() == patronIDReturn) {
                            selectedPatronReturn = p;
                            break;
                        }
                    }

                    Book selectedBookReturn = null;
                    for (Book b : books) {
                        if (b.getId() == bookIDReturn) {
                            selectedBookReturn = b;
                            break;
                        }
                    }

                    if (selectedPatronReturn != null && selectedBookReturn != null) {
                        selectedPatronReturn.returnBook(selectedBookReturn);
                    } else {
                        System.out.println("Invalid Patron or Book ID.");
                    }
                    break;

                case 5:
                    // List available books
                    for (Book availableBook : books) {
                        if (availableBook.isAvailable()) {
                            System.out.println(availableBook);
                            System.out.println("-------------------");
                        }
                    }
                    break;

                case 6:
                    // Search Books
                    System.out.print("Enter a keyword to search for books: ");
                    String bookKeyword = scanner.next();
                    System.out.println("Search results for books:");
                    searchBooks(books, bookKeyword);
                    break;

                case 7:
                    // Search Patrons
                    System.out.print("Enter a keyword to search for patrons: ");
                    String patronKeyword = scanner.next();
                    System.out.println("Search results for patrons:");
                    searchPatrons(patrons, patronKeyword);
                    break;

                case 8:
                    // Generate a report on borrowing history
                    generateBorrowingHistoryReport(patrons);
                    break;

                case 9:
                    // Generate a report on fines
                    generateFinesReport(books);
                    break;

                case 10:
                    // Generate a report on book availability
                    generateBookAvailabilityReport(books);
                    break;

                case 11:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}