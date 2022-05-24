package sg.nus.iss.demoPAF.model;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Meaning {

    private String partOfSpeech;
    private List<Definition> definitions;
    private List<String> synonyms;
    private List<String> antonyms;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public static List<Meaning> create(JsonArray meaningsArr) {

        List<Meaning> meaningList = new ArrayList<>();
        meaningsArr.stream()
                .map(m->(JsonObject) m)
                .forEach(m->{
                    Meaning meaning = new Meaning();
                    meaning.setPartOfSpeech(m.getString("partOfSpeech"));
                    JsonArray definitionArr = m.getJsonArray("definitions");
                    List<Definition> definitionList = Definition.create(definitionArr);
                    meaning.setDefinitions(definitionList);

                    List<String> synonymsList = new ArrayList<>();
                    List<String> antonymsList = new ArrayList<>();
                    JsonArray synonymsArr = m.getJsonArray("synonyms");
                    JsonArray antonymsArr = m.getJsonArray("antonyms");
                    synonymsArr.stream().forEach(syn -> synonymsList.add(syn.toString()));
                    antonymsArr.stream().forEach(ant -> antonymsList.add(ant.toString()));
                    meaning.setSynonyms(synonymsList);
                    meaning.setAntonyms(antonymsList);
                    meaningList.add(meaning);
                });

        return meaningList;
    }
}
