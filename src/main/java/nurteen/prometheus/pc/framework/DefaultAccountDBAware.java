package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.AccountInfo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultAccountDBAware extends AccountDBAware {
    ConcurrentMap<String, AccountInfo> accountInfoMap = new ConcurrentHashMap<>();

    @Override
    public AccountInfo find(String nuid) {
        return accountInfoMap.get(nuid);
    }

    @Override
    public AccountInfo find(Integer type, String account) {
        return find(AccountDBAware.getAccountDBAware().genNuid(type, account));
    }

    @Override
    public void insertNew(AccountInfo accountInfo) {
        accountInfoMap.put(accountInfo.getNuid(), accountInfo);
    }


}
