package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Service.JournalEntryService;
import net.engineeringdigest.journalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
    @RequestMapping("/journal")
    public class JouralEntryController {

    @Autowired
    private JournalEntryService service;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?>  getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findUserByName(userName);
      List<JournalEntry> all = user.getJournalEntries();
      if(all!=null && !all.isEmpty()) {
          return new ResponseEntity<>(all, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findUserByName(userName);
        ArrayList<JournalEntry> listOfEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toCollection(ArrayList::new));
        if(!listOfEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = service.getJournalEntryById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
              }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            service.saveEntry(myEntry,userName);
            return new ResponseEntity<JournalEntry>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Incorrect Request",HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        boolean removed = service.deleteJournalEntryById(myId, userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("JournalEntry with the corresponding Id either does not exist or it's already deleted",HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateEntry(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry entry)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findUserByName(userName);
        ArrayList<JournalEntry> listOfEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toCollection(ArrayList::new));
        if(!listOfEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = service.getJournalEntryById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : old.getTitle());
                old.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : old.getContent());
                service.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}

