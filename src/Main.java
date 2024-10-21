import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.*;
import repository.Repository;
import repository.file.UserRepository;
import repository.memory.InMemoryRepository;
import service.NetworkService;
import UI.UI;

public class Main {
    public static void main(String[] args) {
        ValidatorFactory factory = ValidatorFactory.getInstance();
        Validator userValidator = factory.createValidator(ValidatorStrategy.User);
        Validator friendValidator = factory.createValidator(ValidatorStrategy.Friendship);

        Repository<Integer, User> repoUser = new InMemoryRepository<Integer, User>(new UserValidator());
        Repository<Integer, User> repoUserFile = new UserRepository(new UserValidator(), "data/users.txt");

        Repository<Tuple<Integer, Integer>, Friendship> repoFriends = new InMemoryRepository<Tuple<Integer, Integer>, Friendship>(new FriendshipValidator());
        NetworkService service = new NetworkService(repoUserFile, repoFriends);

        UI ui = new UI(service);
        ui.launchUI();
    }
}