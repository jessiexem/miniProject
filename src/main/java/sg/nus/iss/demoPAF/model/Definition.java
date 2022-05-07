package sg.nus.iss.demoPAF.model;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Definition {

    private String definition;
//    private List<String> synonyms;
    private String example;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

//    public List<String> getSynonyms() {
//        return synonyms;
//    }
//
//    public void setSynonyms(List<String> synonyms) {
//        this.synonyms = synonyms;
//    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public static List<Definition> create (JsonArray definitionArr) {

        List<Definition> definitionList = new ArrayList<>();

        definitionArr.stream().map(d->(JsonObject) d)
                .forEach(d->{
                    Definition definition = new Definition();
                    definition.setDefinition(d.getString("definition"));
                    if(d.containsKey("example")) {
                        definition.setExample(d.getString("example"));
                    }
//                    List<String> synonyms = new ArrayList<>();
//                    JsonArray synonymArr = d.getJsonArray("synonyms");
//                    synonymArr.stream()
//                            .forEach(s->synonyms.add(s.toString()));
                    definitionList.add(definition);
                });
        return definitionList;
    }
}
