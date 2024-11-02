import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.*;
import repository.Repository;
import repository.database.AbstractDataBaseRepo;
import repository.database.DataBaseAcces;
import repository.database.DataBaseRepoFactory;
import repository.database.DataBaseStrategy;
import repository.file.UserRepository;
import repository.memory.InMemoryRepository;
import service.NetworkService;
import UI.UI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
//        List<Person> people = new ArrayList<>();
//        people.add(new Person("Alice", 23));
//        people.add(new Person("Bob", 15));
//        people.add(new Person("Charlie", 30));
//        List<Person> adults = new ArrayList<>();
//        people.stream()
//                .filter(person -> person.getAge() > 18)
//                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
//                .map(Person::getName).forEach(p->System.out.println(p));
//
//        List<Integer> lista = new ArrayList<>();
//        lista.add(12);
//        lista.add(10);
//        lista.add(15);
//        lista.add(5);
//
//        lista.stream()
//                .sorted()
//                .skip(1)
//                .findFirst()
//                .ifPresent(System.out::println);
//
//        lista.stream()
//                .sorted((x, y) -> y - x)
//                .skip(1)
//                .findFirst()
//                .ifPresent(System.out::println);
//
//        int k = 10;
//
//        IntStream.range(2, k)
//                .filter(x ->
//                    IntStream.range(2, x)
//                            .noneMatch(y -> x % y == 0) // allMatch, anyMatch
//                ).forEach(System.out::println);


//        ValidatorFactory factory = ValidatorFactory.getInstance();
//        Validator userValidator = factory.createValidator(ValidatorStrategy.User);
//        Validator friendValidator = factory.createValidator(ValidatorStrategy.Friendship);
//
//        Repository<Integer, User> repoUser = new InMemoryRepository<Integer, User>(new UserValidator());
//        Repository<Integer, User> repoUserFile = new UserRepository(new UserValidator(), "data/users.txt");
//
//        Repository<Tuple<Integer, Integer>, Friendship> repoFriends = new InMemoryRepository<Tuple<Integer, Integer>, Friendship>(new FriendshipValidator());
//        NetworkService service = new NetworkService(repoUserFile, repoFriends);

        NetworkService service;
        String type = args[0];
        ValidatorFactory factory = ValidatorFactory.getInstance();
        Validator userValidator = factory.createValidator(ValidatorStrategy.User);
        Validator friendValidator = factory.createValidator(ValidatorStrategy.Friendship);

        if (type.equals("Database")) {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String username = "postgres";
            String password = "ivona2004";
            DataBaseAcces data = new DataBaseAcces(url, username, password);

            try {
                data.createConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            DataBaseRepoFactory repoFactory = new DataBaseRepoFactory(data);
            AbstractDataBaseRepo userRepo = repoFactory.createRepo(DataBaseStrategy.User, userValidator);
            AbstractDataBaseRepo friendRepo = repoFactory.createRepo(DataBaseStrategy.Friendship, friendValidator);
            service = new NetworkService(userRepo, friendRepo, "database");
        } else if (type.equals("InMemory")) {
            Repository<Integer, User> repoUser = new UserRepository(userValidator, "data/users.txt");
            Repository<Tuple<Integer, Integer>, Friendship> repoFriends = new InMemoryRepository<Tuple<Integer, Integer>, Friendship>(friendValidator);
            service = new NetworkService(repoUser, repoFriends, "InMemory");
        } else {
            throw new RuntimeException("Invalid type of repo");
        }

        UI ui = new UI(service);
        ui.launchUI();
    }
}