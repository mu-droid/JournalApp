package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface JournalEntryService {

    void saveEntry (JournalEntry journalEntry, String userName) ;
    void saveEntry (JournalEntry journalEntry);
    List<JournalEntry> getAll () ;
    Optional<JournalEntry> getJournalEntryById (ObjectId journalEntryId) ;
    boolean deleteJournalEntryById (ObjectId myId, String userName) ;
    public List<JournalEntry> findByUserName(String userName);
}
