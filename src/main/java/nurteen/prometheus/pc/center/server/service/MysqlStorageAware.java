package nurteen.prometheus.pc.center.server.service;

import nurteen.prometheus.pc.framework.StorageAware;
import nurteen.prometheus.pc.framework.entities.ThirdpartyAccountType;
import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.UserInfo;
import nurteen.prometheus.pc.framework.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class MysqlStorageAware extends StorageAware {
    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public Instant getInstant() throws Exception {
        Timestamp timestamp = this.nativeQueryUnique("select current_timestamp(6)", Timestamp.class);
        if (timestamp != null) {
            return timestamp.toInstant();
        }
        return null;
    }

    @Override
    public UserInfo fromNuid(String nuid) throws Exception {
        String sql = String.format("select nuid,account,nickname,phone,headimg from t_users a where a.nuid='%s'", nuid);
        return this.nativeQuery(sql, UserInfo.class);
    }

    @Override
    public UserInfo fromAccount(String account, String password) throws Exception {
        String sql = String.format("select a.nuid,a.account,a.nickname,a.phone,a.headimg from t_users a where a.account='%s' and a.passwd='%s'", account, password);
        return this.nativeQuery(sql, UserInfo.class);
    }

    @Override
    public UserInfo fromThirdpartyAccount(ThirdpartyAccountType type, String account) throws Exception {
        String sql = String.format("select a.nuid,a.account,a.nickname,a.phone,a.headimg from t_users a,t_thirdparty_users b where a.nuid=b.nuid and b.type='%d' and b.account='%s'", type.getValue(), account);
        return this.nativeQuery(sql, UserInfo.class);
    }

    @Override
    public UserInfo fromPhone(String phone, String password) throws Exception {
        String sql = String.format("select a.nuid,a.account,a.nickname,a.phone,a.headimg from t_users a where a.phone='%s' and a.passwd='%s'", phone, password);
        return this.nativeQuery(sql, UserInfo.class);
    }

    @Override
    public void insertNew(UserInfo userInfo) throws Exception {
        String sql = String.format("insert into t_users(nuid,account,nickname,phone,headimg) values('%s','%s','%s', '%s','%s')", userInfo.getNuid(), userInfo.getAccount(), userInfo.getNickname(), userInfo.getPhone(), userInfo.getHeadimg());
        this.nativeExecuteUpdate(sql);
    }

    @Override
    public void insertNewFromThirdparty(UserInfo userInfo, ThirdpartyAccountType type, String account) throws Exception {
        String sql1 = String.format("insert into t_thirdparty_users(nuid,type,account) values('%s','%d','%s')", userInfo.getNuid(), type.getValue(), account),
               sql2 = String.format("insert into t_users(nuid,account,nickname,phone,headimg) values('%s','%s','%s', '%s','%s')", userInfo.getNuid(), userInfo.getAccount(), userInfo.getNickname(), userInfo.getPhone(), userInfo.getHeadimg());
        this.nativeExecuteUpdate(sql1, sql2);
    }

    @Override
    public DeviceInfo fromNdId(String ndid) throws Exception {
        String sql = String.format("select ndid,type,platform,hid from t_devices a where a.ndid='%s'", ndid);
        return this.nativeQuery(sql, DeviceInfo.class);
    }

    @Override
    public DeviceInfo fromHid(String hid) throws Exception {
        String sql = String.format("select ndid,type,platform,hid from t_devices a where a.hid='%s'", hid);
        return this.nativeQuery(sql, DeviceInfo.class);
    }

    @Override
    public String getDeviceName(String nuid, String ndid) throws Exception {
        String sql = String.format("select a.name from t_user_devices a where a.nuid='%s' and a.ndid='%s'", nuid, ndid);
        return this.nativeQueryUnique(sql, String.class);
    }

    @Override
    public void insertNew(String nuid, String ndid, String name) throws Exception {
        String oldName = getDeviceName(nuid, ndid);
        if (oldName == null) {
            String sql = String.format("insert into t_user_devices(nuid,ndid,name) values('%s','%s','%s')", nuid, ndid, name);
            this.nativeExecuteUpdate(sql);
        }
        else if (!oldName.equals(name)) {
            String sql = String.format("update t_user_devices set name='%s' where nuid='%s' and ndid='%s'", name, nuid, ndid);
            this.nativeExecuteUpdate(sql);
        }
    }

    @Override
    public void insertNew(DeviceInfo deviceInfo) throws Exception {
        String sql = String.format("insert into t_devices(ndid,type,platform,hid) values('%s','%d','%d','%s')", deviceInfo.getNdid(), deviceInfo.getType(), deviceInfo.getPlatform(), deviceInfo.getHid());
        this.nativeExecuteUpdate(sql);
    }

    @Override
    public void insertNew(String nuid, String name, DeviceInfo deviceInfo) throws Exception {
        insertNew(deviceInfo);
        insertNew(nuid, deviceInfo.getNdid(), name);
    }

    private <T> T nativeQuery(String sql, Class<T> type) throws Exception {
        List<T> list = this.nativeQueryList(sql, type);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }
    private <T> T nativeQueryUnique(String sql, Class<T> type) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery query = session.createNativeQuery(sql);
            return (T) query.uniqueResult();
        }
        catch (Exception e) {
            ExceptionUtils.printAndThrow(e);
        }
        return null;
    }
    private <T> List<T> nativeQueryList(String sql, Class<T> type) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            NativeQuery<T> query = session.createNativeQuery(sql, type);
            List<T> list = query.list();
            if ((list != null) && (list.size() > 0)) {
                return list;
            }
            return null;
        }
        catch (Exception e) {
            ExceptionUtils.printAndThrow(e);
        }
        return null;
    }
    private void nativeExecuteUpdate(String... sqls) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                for (String sql : sqls) {
                    session.createNativeQuery(sql).executeUpdate();
                }
                transaction.commit();
            }
            catch (Exception e) {
                transaction.rollback();
                ExceptionUtils.printAndThrow(e);
            }
        }
        catch (Exception e) {
            ExceptionUtils.printAndThrow(e);
        }
    }
}
