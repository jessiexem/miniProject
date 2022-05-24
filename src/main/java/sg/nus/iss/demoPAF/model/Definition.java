package sg.nus.iss.demoPAF.model;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Definition {

    private String definition;

    private String example;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

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
                    definitionList.add(definition);
                });
        return definitionList;
    }
}
