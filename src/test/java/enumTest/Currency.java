package enumTest;

public enum Currency {
    DOLLAR(1100), EURO(1500), WON(1);

    private int value;

    Currency(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
