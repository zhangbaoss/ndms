package nurteen.prometheus.pc.framework.entities;

public enum DevicePlatform {
    Browser(1),
    Windows(2),
    Linux(3),
    Unix(4),
    MacOS(5),
    IOS(6),
    Android(7);

    private int value;

    DevicePlatform(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
