package zyx.existent.utils.changelog;

public class ChangeLog {
    private String changeName;
    private final ChangelogType type;

    public ChangeLog(String name, ChangelogType type) {
        this.changeName = name;
        this.type = type;

        switch (type) {
            case NONE:
                changeName = ": " + changeName;
                break;
            case ADD:
                changeName = "\247[\247+\247] Added \247" + changeName;
                break;
            case DELETE:
                changeName = "\247[\247-\247] Delete \247" + changeName;
                break;
            case IMPROVED:
                changeName = "\247[\247/\247] Improved \247" + changeName;
                break;
            case FIXED:
                changeName = "\247[\247/\247] Fixed \247" + changeName;
                break;
            case PROTOTYPE:
                changeName = "\247[\247?\247] Prototype \247" + changeName;
                break;
            case NEW:
                changeName = "\247[\247!\247] New \247" + changeName;
                break;
        }
    }

    public String getChangeName() {
        return changeName;
    }

    public ChangelogType getType() {
        return type;
    }
}