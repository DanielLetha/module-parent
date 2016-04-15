package com.simpletour.biz.traveltrans.enums;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mario on 2015/12/3.
 */
public enum NodeStatus {
    arrived("到达","arrived","finished"),
    finished("完成","finished");


    private String name, value;

    private List<String> transistions;

    NodeStatus(String name, String value) {
        this.name = name;
        this.value = value;
        this.transistions = Collections.emptyList();
    }

    NodeStatus(String name, String value, String... transistions) {
        this.name = name;
        this.value = value;
        this.transistions = Arrays.asList(transistions);
    }

    public boolean accept(NodeStatus status){
        return this.transistions.stream().anyMatch(t->t.matches(status.getValue()));
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public List<String> getTransistions() {
        return transistions;
    }

    public static NodeStatus of(String value){
        Optional<NodeStatus> status = Arrays.stream(values()).filter(s->s.getValue().equals(value)).findFirst();
        return status.isPresent()?status.get():null;
    }
}
