package sg.nus.iss.demoPAF.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.nus.iss.demoPAF.controller.MainController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Word {

    private String word;
    private String phonetic;
    private String audio;
    private List<Meaning> meaning;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<Meaning> getMeaning() { return meaning;}

    public void setMeaning(List<Meaning> meaning) { this.meaning = meaning;}

    private static final Logger logger = Logger.getLogger(Word.class.getName());

    public static List<Word> create(String json) {

        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonArray jsonArray = reader.readArray();

        DocumentContext jsonContext = JsonPath.parse(json);

        String jsonPathAudio = null;
        String jsonPathPhonetics = null;
        try {
            String jsonPathAudioPath = "$[0].phonetics.[0].audio";
            jsonPathAudio = jsonContext.read(jsonPathAudioPath);

            String jsonPathPhoneticsPath = "$[0].phonetics.[1].text";
            jsonPathPhonetics = jsonContext.read(jsonPathPhoneticsPath);
        } catch (PathNotFoundException e) {
            logger.warning("jsonPath for phonetic/audio not found");
        }

        List<Word> wordList = new ArrayList<>();
        String finalJsonPathPhonetics = jsonPathPhonetics;
        String finalJsonPathAudio = jsonPathAudio;

        jsonArray.stream()
                .map(v->(JsonObject) v)
                .forEach(v->{
                    Word word = new Word();
                    word.setWord(v.getString("word"));
                    if(finalJsonPathPhonetics!=null) {
                        word.setPhonetic(finalJsonPathPhonetics);
                    }
                    if (finalJsonPathAudio!=null) {
                        word.setAudio(finalJsonPathAudio);
                    }
                    JsonArray meaningsArr = v.getJsonArray("meanings");
                    List<Meaning> meaningList = Meaning.create(meaningsArr);
                    word.setMeaning(meaningList);
                    wordList.add(word);
                });
        return wordList;
    }
}
