package nurteen.prometheus.pc.framework.entities;

public enum DeviceType {
    App_Browser(1),
    App_Mobile(2),
    App_Pc(3),
    Controller_Mobile(4),
    Controller_Pc(5),
    Center_Pc(6);

    private int value;

    DeviceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
