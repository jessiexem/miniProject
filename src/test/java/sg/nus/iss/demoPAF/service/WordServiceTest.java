package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import sg.nus.iss.demoPAF.model.Word;
import sg.nus.iss.demoPAF.repository.WordRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class WordServiceTest {

    @Autowired
    private WordService wordService;

//    @SpyBean
    @MockBean
    private WordRepository wordRepo;

    @Test
    void shouldReturnListOfWords() {
        String searchTerm = "happy";
        Optional<List<Word>> opt = wordService.searchWord(searchTerm);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    void shouldNotReturnListOfWords() {
        String searchTerm = "sss";
        Optional<List<Word>> opt = wordService.searchWord(searchTerm);
        Assertions.assertFalse(opt.isPresent());
    }

    @Test
    void shouldNotAddFavWord() {
        int userId = 1;
        String favWord = "favourite";
        Mockito.when(wordRepo.getWordByUser(anyInt(),anyString())).thenReturn(1);
        Mockito.when(wordRepo.insertFavouriteWordByUserId(anyInt(),anyString())).thenReturn(false);
        boolean isAdded = wordService.addFavWord(userId,favWord);
        Assertions.assertFalse(isAdded);
    }

    @Test
    void shouldAddFavWord() {
        int userId = 1;
        String favWord = "favourite";
        Mockito.when(wordRepo.getWordByUser(anyInt(),anyString())).thenReturn(0);
        Mockito.when(wordRepo.insertFavouriteWordByUserId(anyInt(),anyString())).thenReturn(true);
        boolean isAdded = wordService.addFavWord(userId,favWord);
        Assertions.assertTrue(isAdded);
    }

//for SpyBean
//    @Test
//    void shouldNotAddFavWord() {
//        int userId = 1;
//        String favWord = "favourite";
//        Mockito.doReturn(1).when(wordRepo).getWordByUser(anyInt(),anyString());
//        boolean isAdded = wordService.addFavWord(userId,favWord);
//        Assertions.assertFalse(isAdded);
//    }

//for SpyBean
//    @Test
//    void shouldAddFavWord() {
//        int userId = 1;
//        String favWord = "favourite";
//        Mockito.doReturn(0).when(wordRepo).getWordByUser(anyInt(),anyString());
//        Mockito.doReturn(true).when(wordRepo).insertFavouriteWordByUserId(anyInt(),anyString());
//        boolean isAdded = wordService.addFavWord(userId,favWord);
//        Assertions.assertTrue(isAdded);
//    }
}
