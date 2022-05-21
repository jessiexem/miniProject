package sg.nus.iss.demoPAF.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sg.nus.iss.demoPAF.controller.MainController;
import sg.nus.iss.demoPAF.model.Word;
import sg.nus.iss.demoPAF.repository.WordRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class WordService {

    @Autowired
    private WordRepository wordRepo;

    //https://api.dictionaryapi.dev/api/v2/entries/en/joke

    private static final String SEARCH_DICTIONARY_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    public Optional<List<Word>> searchWord(String searchTerm) {

        String url = UriComponentsBuilder
                .fromUriString(SEARCH_DICTIONARY_URL+searchTerm)
                .toUriString();

        RequestEntity req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        try {
            resp = template.exchange(req,String.class);
        } catch (HttpClientErrorException ex) {
            logger.warning("no definition found");
        }

        if (resp!=null) {
            logger.info(">>>>WordService: "+ resp.getBody());
            try {
                List<Word> wordList = Word.create(resp.getBody());
                return Optional.of(wordList);
            } catch (Exception e) {
                logger.severe(">>>> WordService - searchWord: Error creating List<Word>");
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }


    public boolean addFavWord(int userId, String favWord) {

        //check if word exists in db
        int count = wordRepo.getWordByUser(userId,favWord);
        boolean isAdded = false;
        if (count ==0) {
            //add favWord to db
            isAdded = wordRepo.insertFavouriteWordByUserId(userId,favWord);
        }

        return isAdded;
    }

    public Optional<List<String>> getAllFavouriteByUser(int user_id) {
        return getAllFavouriteByUser(user_id,10,0);
    }

    public Optional<List<String>> getAllFavouriteByUser(int user_id,Integer offset) {
        return getAllFavouriteByUser(user_id,10,offset);
    }

    public Optional<List<String>> getAllFavouriteByUser(int userId, Integer limit, Integer offset) {
        logger.entering("WordService","getAllFavouriteByUser");
        return wordRepo.getAllFavouriteByUser(userId,limit,offset);
    }

    public boolean deleteAllFavouriteByUser(int userId) {
        return wordRepo.deleteFavouriteByUser(userId);
    }
}
