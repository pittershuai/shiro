package com.bupt.session;

import com.bupt.uilt.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    //session前缀
    private final String SHIRO_SESSION_PREFIX = "bupt-session:";

    private byte[] getKey(String key){
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    //提取一个保存session的方法
    private void saveSession(Session session){
        if(session != null && session.getId() != null){
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);  //将session序列化后放在byte数组，作为redis存储的值。
            jedisUtil.set(key,value);
            jedisUtil.expire(key,100000);
        }
    }

    //创建session
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);  //通过session获得sessionId
        assignSessionId(session, sessionId); //捆绑sessionId 与 session
        saveSession(session);
        return sessionId;
    }

    //获得session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session");
        if(sessionId == null){
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        return (Session)SerializationUtils.deserialize(value);
    }

    //更新session
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    //删除
    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    //获得到指定重活的session
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX); //通过前缀获得全部key
        Set<Session> sessions = new HashSet<>();
        if(CollectionUtils.isEmpty(keys)){
            return sessions;
        }
        for(byte[] key : keys){
            Session session = (Session)SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
