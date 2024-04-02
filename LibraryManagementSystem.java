//ARAFAT EASIN- 202309010883

import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;
// Class representing a User in the library system
class User {
    // User properties
    String email;
    String password;
    String name;
    String secretWord;
    String contactInformation;
    List<Book> borrowingHistory = new ArrayList<>();
    Map<String, String> preferences = new HashMap<>(); // e.g., preferred genres

    // Constructor for User class
    public User(String email, String password, String name, String secretWord) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.secretWord = secretWord;
    }

    // Method to view user's profile
    public void viewProfile() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Contact Information: " + contactInformation);
        System.out.println("Preferences: " + preferences);
        System.out.println("Borrowing History: ");
        for (Book book : borrowingHistory) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }

    // Method to edit user's profile
    public void editProfile(String name, String contactInformation, Map<String, String> preferences) {
        this.name = name;
        this.contactInformation = contactInformation;
        this.preferences = preferences;
    }
}

// Class representing a Review in the library system
class Review {
    // Review properties
    User reviewer;
    String reviewText;

    // Constructor for Review class
    public Review(User reviewer, String reviewText) {
        this.reviewer = reviewer;
        this.reviewText = reviewText;
    }
}

// Class representing a Rating in the library system
class Rating {
    // Rating properties
    User rater;
    int ratingValue; // e.g., on a scale of 1 to 5

    // Constructor for Rating class
    public Rating(User rater, int ratingValue) {
        this.rater = rater;
        this.ratingValue = ratingValue;
    }
}

// Class representing a Reservation in the library system
class Reservation {
    // Reservation properties
    User user;
    LocalDateTime reservationTime;

    // Constructor for Reservation class
    public Reservation(User user, LocalDateTime reservationTime) {
        this.user = user;
        this.reservationTime = reservationTime;
    }
}

// Class representing a Book in the library system
class Book {
    // Book properties
    String title;
    String author;
    boolean available;
    LocalDateTime borrowTime;
    LocalDateTime returnTime;
    boolean renewed;
    List<Review> reviews = new ArrayList<>();
    List<Rating> ratings = new ArrayList<>();
    List<Reservation> reservations = new ArrayList<>();

    private static final long MAX_BORROW_DAYS = 20; // Maximum number of days a book can be borrowed
    private static final long RENEW_DAYS = 15; // Number of days a book can be renewed for

    // Constructor for Book class
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
        this.renewed = false;
    }

    // Getter methods for Book properties
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getBorrowTime() {
        return borrowTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    // Method to borrow a book
    public void borrow(User user) {
        if (available) {
            available = false;
            borrowTime = LocalDateTime.now();
            user.borrowingHistory.add(this);
            System.out.println("Book borrowed successfully at " + borrowTime);
        } else {
            System.out.println("Book is already borrowed.");
        }
    }

    // Method to return a book
    public void returnBook(User user) {
        if (!available) {
            available = true;
            returnTime = LocalDateTime.now();
            user.borrowingHistory.remove(this);
            System.out.println("Book returned successfully at " + returnTime);
        } else {
            System.out.println("Book is already available.");
        }
    }

    // Method to check if a book is overdue
    public boolean isOverdue() {
        if (!available) {
            long daysBorrowed = Duration.between(borrowTime, LocalDateTime.now()).toDays();
            return daysBorrowed > MAX_BORROW_DAYS;
        }
        return false;
    }

    // Method to renew a book
    public void renew() {
        if (!available && !renewed) {
            borrowTime = LocalDateTime.now();
            renewed = true;
            System.out.println("Book renewed successfully. It must be returned by " + borrowTime.plusDays(RENEW_DAYS));
        } else {
            System.out.println("Book cannot be renewed.");
        }
    }

    // Getter method for MAX_BORROW_DAYS
    public static long getMaxBorrowDays() {
        return MAX_BORROW_DAYS;
    }

    // Method to add a review
    public void addReview(User user, String reviewText) {
        reviews.add(new Review(user, reviewText));
    }

    // Method to add a rating
    public void addRating(User user, int ratingValue) {
        ratings.add(new Rating(user, ratingValue));
    }

    // Method to reserve a book
    public void reserve(User user) {
        if (!available) {
            reservations.add(new Reservation(user, LocalDateTime.now()));
            System.out.println("Book reserved successfully.");
        } else {
            System.out.println("Book is already available.");
        }
    }

    // Method to display reviews
    public void displayReviews() {
        System.out.println("Reviews for " + title + ":");
        for (Review review : reviews) {
            System.out.println(review.reviewer.name + ": " + review.reviewText);
        }
    }
}

// Class representing a Library in the library system
class Library {
    // Library properties
    private Map<String, Book> books;

    // Constructor for Library class
    public Library() {
        this.books = new HashMap<>();
    }



    // Method to add a book
    public void addBook(Book book) {
        books.put(book.getTitle(), book);
    }
    // Method to find a book
    public Book findBook(String title) {
        return books.get(title);
    }

    // Method to display all books
    public void displayAllBooks() {
        System.out.println("Available books:");
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    // Method to display all overdue books
    public void displayOverdueBooks() {
        System.out.println("Overdue books:");
        for (Book book : books.values()) {
            if (!book.isAvailable() && book.isOverdue()) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    // Method to recommend books based on a user's borrowing history and book ratings
    public void recommendBooks(User user) {
        // This is a simple recommendation system that recommends the most rated books
        List<Book> recommendedBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.ratings.size() > 0) {
                recommendedBooks.add(book);
            }
        }
        System.out.println("Recommended book for you: Clean Code written by Robert C. Martin");
        for (Book book : recommendedBooks) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }
}




public class LibraryManagementSystem {
    // Property to store users
    static Map<String, User> users = new HashMap<>();

    // Main method
    public static void main(String[] args) {
        // Add initial users
        users.put("easinarafat.bn@gmail.com", new User("easinarafat.bn@gmail.com", "ARAFAT1213", "ARAFAT EASIN", "Barcelona"));
        users.put("asma.zubaida@gmail.com", new User("asma.zubaida@gmail.com", "ASMA1111", "ASMA ZUBAIDA", "4_KIDS"));
        users.put("user3@example.com", new User("user3@example.com", "password3", "User 3", "WORD"));

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Library");


        // Prompt user for email and password
        System.out.print("Enter your email: ");
        String email = sc.nextLine();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        // Check if user exists and password is correct
        User user = users.get(email);
        if (user != null && user.password.equals(password)) {
            // Welcome user
            System.out.println("Welcome, " + user.name + "!");





            // Initialize library
            Library library = new Library();

            // Add books to library
            Book book1 = new Book("Clean Code", "Robert C. Martin");
            book1.addReview(new User("reviewer1@example.com", "password1", "Reviewer 1", "WORD"), "This book is Excellent for learning programming and very helpfull for beginners who want learn JAVA");
            library.addBook(book1);
            Book book2 = new Book("Design Patterns", "Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides");
            book2.addReview(new User("reviewer2@example.com", "password2", "Reviewer 2", "WORD"), "Good");
            library.addBook(book2);
            // Add more books to library
            Book book3 = new Book("Patterns of Enterprise Application Architecture", "Martin Fowler");
            book3.addReview(new User("reviewer3@example.com", "password3", "Reviewer 3", "WORD"), "Good");
            library.addBook(book3);
            Book book4 = new Book("Code Complete: A Practical Handbook of Software Construction", "Steve McConnell");
            book4.addReview(new User("reviewer4@example.com", "password4", "Reviewer 4", "WORD"), "Good");
            library.addBook(book4);
            Book book5 = new Book("Software Engineering: A Practitioner’s Approach", "Roger S. Pressman");
            book5.addReview(new User("reviewer5@example.com", "password5", "Reviewer 5", "WORD"), "Good");
            library.addBook(book5);




            // Continue with the library management system
            while (true) {
                // Display the main menu
                System.out.println("Main Menu:");
                System.out.println("1. View Profile");
                System.out.println("2. Edit Profile");
                System.out.println("3. Display All Books");
                System.out.println("4. Search for a Book");
                System.out.println("5. Renew/Return a Book");
                System.out.println("6. Recommend Books");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character

                // Perform the corresponding action based on the user's choice
                switch (choice) {
                    case 1:
                        // View profile
                        user.viewProfile();
                        break;
                    case 2:
                        // Edit profile
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter your contact information: ");
                        String contactInformation = sc.nextLine();
                        System.out.print("Enter your preferences (e.g., genre=horror): ");
                        String preferencesString = sc.nextLine();
                        Map<String, String> preferences = new HashMap<>();
                        String[] pairs = preferencesString.split(",");
                        for (String pair : pairs) {
                            String[] keyAndValue = pair.split("=");
                            preferences.put(keyAndValue[0], keyAndValue[1]);
                        }
                        user.editProfile(name, contactInformation, preferences);
                        System.out.println("Profile edited successfully.");
                        break;





                    case 3:
                        // Display all books
                        library.displayAllBooks();
                        System.out.print("Do you want to borrow a book or return to the main menu? (borrow/menu): ");
                        String borrowOrMenuChoice = sc.nextLine();
                        if (borrowOrMenuChoice.equalsIgnoreCase("borrow")) {
                            System.out.print("Enter the title of the book you want to borrow: ");
                            String borrowTitle = sc.nextLine();
                            Book borrowBook = library.findBook(borrowTitle);
                            if (borrowBook != null) {
                                System.out.print("Do you want to see reviews for this book? (Y/N): ");
                                String seeReviewsChoice = sc.nextLine();
                                if (seeReviewsChoice.equalsIgnoreCase("Y")) {
                                    borrowBook.displayReviews(); // Display reviews
                                }
                                System.out.print("After seeing the reviews, do you still want to borrow this book? (Y/N): ");
                                String stillBorrowChoice = sc.nextLine();
                                if (stillBorrowChoice.equalsIgnoreCase("Y")) {
                                    borrowBook.borrow(user); // Borrow the book
                                    LocalDateTime returnDate = LocalDateTime.now().plusDays(Book.getMaxBorrowDays());
                                    System.out.println("You have successfully borrowed the book. Please remember to return it by " + returnDate + ".");
                                } else {
                                    System.out.println("You chose not to borrow the book.");
                                }
                            } else {
                                System.out.println("Book not found.");
                            }
                        }
                        break;

                    case 4:
                        // Search for a book
                        System.out.print("Enter book title to search: ");
                        String searchTitle = sc.nextLine();
                        Book foundBook = library.findBook(searchTitle);
                        if (foundBook != null) {
                            System.out.println("Book found: " + foundBook.getTitle());
                            System.out.print("Do you want to see reviews for this book? (Y/N): ");
                            String seeReviewsChoice = sc.nextLine();
                            if (seeReviewsChoice.equalsIgnoreCase("Y")) {
                                foundBook.displayReviews(); // Display reviews
                            }
                            if (!foundBook.isAvailable()) {
                                LocalDateTime returnDate = foundBook.getBorrowTime().plusDays(Book.getMaxBorrowDays());
                                System.out.println("This book is currently borrowed and will be available on " + returnDate);
                                System.out.print("Do you want to reserve this book? (Y/N): ");
                                String reserveChoice = sc.nextLine();
                                if (reserveChoice.equalsIgnoreCase("Y")) {
                                    foundBook.reserve(user);
                                    System.out.println("You have reserved this book. It will be held for you until " + returnDate.plusDays(1));
                                    System.out.println("Would you like to choose another book? (Y/N): ");
                                    String anotherBookChoice = sc.nextLine();
                                    if (anotherBookChoice.equalsIgnoreCase("N")) {
                                        System.out.println("Thank you for using the library management system. Goodbye!");
                                        System.exit(0);
                                    }
                                }
                            } else {
                                System.out.println("If you borrow this book, the deadline for returning it will be " + LocalDateTime.now().plusDays(Book.getMaxBorrowDays()));
                                System.out.print("Do you want to borrow this book? (Y/N): ");
                                String borrowChoice = sc.nextLine();
                                if (borrowChoice.equalsIgnoreCase("Y")) {
                                    foundBook.borrow(user); // Borrow the book
                                }
                            }
                        } else {
                            System.out.println("Book not found.");
                        }
                        break;
                    case 5:
                        // Renew/Return a book
                        System.out.print("Enter the book title to renew/return: ");
                        String returnTitle = sc.nextLine();
                        Book returnedBook = library.findBook(returnTitle);
                        if (returnedBook != null) {
                            System.out.print("Do you want to renew or return this book? (renew/return): ");
                            String returnChoice = sc.nextLine();
                            if (returnChoice.equalsIgnoreCase("renew")) {
                                returnedBook.renew(); // Renew the book
                            } else if (returnChoice.equalsIgnoreCase("return")) {
                                returnedBook.returnBook(user); // Return the book
                                System.out.println("You have returned this book.");
                            }
                        } else {
                            System.out.println("Book not found.");
                        }
                        break;
                    case 6:
                        // Recommend books
                        library.recommendBooks(user);
                        break;
                    case 7:
                        // Exit
                        System.out.println("Thank you for using the library management system. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        // Invalid choice
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                        break;
                }
            }


        } else {
            System.out.println("Incorrect email or password.");
            System.out.print("Do you want to create a new account? (Y/N): ");
            String createAccountChoice = sc.nextLine();
            if (createAccountChoice.equalsIgnoreCase("Y")) {
                System.out.print("Enter your name: ");
                String name = sc.nextLine();
                System.out.print("Enter your secret word: ");
                String secretWord = sc.nextLine();
                users.put(email, new User(email, password, name, secretWord));
                System.out.println("Account created successfully. Please log in.");
                System.out.print("Enter your email: ");
                email = sc.nextLine();
                System.out.print("Enter your password: ");
                password = sc.nextLine();
                user = users.get(email);
                if (user != null && user.password.equals(password)) {
                    System.out.println("Welcome, " + user.name + "!");
                    // Continue with the library management system
                } else {
                    System.out.println("Incorrect email or password.");
                }
            } else {
                System.out.print("Forgot your password? (Y/N): ");
                String forgotPasswordChoice = sc.nextLine();
                if (forgotPasswordChoice.equalsIgnoreCase("Y")) {
                    System.out.print("Enter your email: ");
                    email = sc.nextLine();
                    user = users.get(email);
                    if (user != null) {
                        System.out.print("What is your secret word? ");
                        String secretWord = sc.nextLine();
                        if (secretWord.equals(user.secretWord)) {
                            System.out.println("Your password is: " + user.password);
                        } else {
                            System.out.println("Incorrect secret word.");
                        }
                    } else {
                        System.out.println("Email not recognized.");
                    }
                } else {
                    System.out.print("Enter your email: ");
                    email = sc.nextLine();
                    System.out.print("Enter your password: ");
                    password = sc.nextLine();
                    user = users.get(email);
                    if (user != null && user.password.equals(password)) {
                        System.out.println("Welcome, " + user.name + "!");
                        // Continue with the library management system
                        while (true) {
                            // Display the main menu
                            System.out.println("Main Menu:");
                            System.out.println("1. View Profile");
                            System.out.println("2. Edit Profile");
                            System.out.println("3. Display All Books");
                            System.out.println("4. Search for a Book");
                            System.out.println("5. Renew/Return a Book");
                            System.out.println("6. Recommend Books");
                            System.out.println("7. Exit");
                            System.out.print("Enter your choice: ");
                            int choice = sc.nextInt();
                            sc.nextLine(); // Consume the newline character

                            // Perform the corresponding action based on the user's choice

                            switch (choice) {
                                //case for menu
                            }
                        }
                    } else {
                        System.out.println("Incorrect email or password.");
                    }
                }
            }

        }
        sc.close();
    }
}

