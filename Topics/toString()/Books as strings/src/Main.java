class Book {

    private String title;
    private int yearOfPublishing;
    private String[] authors;

    public Book(String title, int yearOfPublishing, String[] authors) {
        this.title = title;
        this.yearOfPublishing = yearOfPublishing;
        this.authors = authors;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "title=" + title + ",yearOfPublishing=" + yearOfPublishing + ",authors=" + java.util.Arrays.toString(authors).replace(", ", ",");
    }
}