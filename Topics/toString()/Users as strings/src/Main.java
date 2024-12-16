class User {

    private String login;
    private String firstName;
    private String lastName;

    public User(String login, String firstName, String lastName) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "login=" + login + ",firstName=" + firstName + ",lastName=" + lastName;
    }
}