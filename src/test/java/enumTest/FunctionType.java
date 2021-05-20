package enumTest;

public enum FunctionType {
    COLUMN("COLUMN"), VALUE("VALUE");

    private String type;
    private FunctionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
