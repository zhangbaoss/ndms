package nurteen.prometheus.pc.framework.entities;

public enum ThirdpartyAccountType {
    WX(1), QQ(2);

    private int value;

    ThirdpartyAccountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
