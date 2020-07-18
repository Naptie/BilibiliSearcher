package me.naptie.bilibili.objects;

public enum Sex {

    MALE("男"), FEMALE("女"), UNSET("保密");

    private final String text;

    Sex(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
