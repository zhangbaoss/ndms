package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.AccountInfo;

public abstract class AccountDBAware {
    static AccountDBAware accountDBAware;

    public static AccountDBAware getAccountDBAware() {
        return accountDBAware;
    }

    public String genNuid(Integer type, String account) {
        return "nuid$" + type + ":" + account;
    }

    public abstract AccountInfo find(String nuid);
    public abstract AccountInfo find(Integer type, String account);
    public abstract void insertNew(AccountInfo accountInfo);

}
