package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.User;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void saveNewUser(User user);
    public void saveUser(User user);
    public List<User> getAll();
    public Optional<User> getUserById(ObjectId userId);
    public void deleteUserById(ObjectId journalEntryId);
    public User findUserByName (String UserName);
    public void saveAdmin(User user);
}
