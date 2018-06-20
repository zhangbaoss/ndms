package nurteen.prometheus.pc.framework.entities;

public enum ApplicationType {
    App_Mobile(1),
    App_Website(2);

    private int value;

    ApplicationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
