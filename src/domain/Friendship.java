package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Class for a friendship
 */
public class Friendship extends Entity<Tuple<Integer, Integer>>{
    private User user1;
    private User user2;
    private LocalDateTime since;

    /**
     * Constructor for a friendship
     * @param user1 - the first user
     * @param user2 - the second user
     */
    public Friendship(User user2, User user1) {
        this.user2 = user2;
        this.user1 = user1;
        Integer uuID1 = user1.getId();
        Integer uuID2 = user2.getId();
        Tuple<Integer, Integer> uuIDComb = new Tuple<>(uuID1, uuID2); // the id of the friendship is a tuple of the ids of the two users
        this.setId(uuIDComb);
        this.since = LocalDateTime.now();
    }

    /**
     * Getter for the first user
     * @return user1 - the first user
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Setter for the first user
     * @param user1 - the new first user
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Getter for the second user
     * @return user2 - the second user
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Setter for the second user
     * @param user2 - the new second user
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    /**
     * Getter for the time since the friendship was created
     * @return since - the time since the friendship was created
     */
    public LocalDateTime getSince() {
        return since;
    }

    /**
     * Setter for the time since the friendship was created
     * @param since - the new time since the friendship was created
     */
    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    /**
     * Formats the time since the friendship was created
     * @param time - the time since the friendship was created
     * @return timePrinted - the formatted time since the friendship was created
     */
    private String formatter(LocalDateTime time)
    {
        String timePrinted;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        timePrinted=time.format(formatter);
        return timePrinted;
    }

    /**
     * Returns the string representation of the friendship
     * @return string - the string representation of the friendship
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2) && Objects.equals(since, that.since);
    }

    /**
     * Returns the hash code of the friendship
     * @return hash - the hash code of the friendship
     */
    @Override
    public int hashCode() {
        return Objects.hash(user1, user2) + Objects.hash(user2, user1);
    }

    /**
     * Returns the string representation of the friendship
     * @return string - the string representation of the friendship
     */
    @Override
    public String toString() {
        return "Between \u001B[37m" + user1.getUsername() + "\u001B[0m and \u001B[37m" + user2.getUsername() + "\u001B[0m since " + formatter(since);
    }
}
