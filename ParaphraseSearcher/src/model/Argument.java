package model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Argument {
    public final String name;
    public final ImmutableList<String> spans;

    Argument(String name, ImmutableList<String> spans) {
        this.name = name;
        this.spans = spans;
    }
}
