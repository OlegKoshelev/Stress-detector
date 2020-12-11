package sample.DataBase.Entities;

public enum TableType {
    AbbreviatedTable("Abbreviated table"),
    RegularTable("Regular table"),
    DetailedTable("Detailed table");

    private String name;

    TableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
