package net.engineeringdigest.journalApp.Service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryServiceImpl implements JournalEntryService{

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryServiceImpl.class);

    @Override
      @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            System.out.println("==> saveEntry called");
            User user = userService.findUserByName(userName);
            System.out.println("==> User found: " + user.getUserName());

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);

            logger.info("==> Entry saved");
        } catch (Exception e) {
            logger.info("==> Exception in saveEntry: " + e.getMessage());
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }

    @Override
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    @Override
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    @Override
    public Optional<JournalEntry> getJournalEntryById(ObjectId journalEntryId) {

        return journalEntryRepository.findById(journalEntryId);
    }

    @Override
    @Transactional
    public boolean deleteJournalEntryById(ObjectId myId, String userName) {
        boolean removed = false;
        try{
            User user = userService.findUserByName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(myId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while deleting the entry.", e);
        }
        return removed;
    }

    @Override
    public List<JournalEntry> findByUserName(String userName){
      User user = userRepository.findByUserName(userName);
      List<JournalEntry> journalEntries = user.getJournalEntries();
      return journalEntries;
    }
}

