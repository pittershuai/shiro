package com.bupt.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * 自定义SessionManager
 * 使用DefaultSessionManager时，访问一个页面会重复读取redis中session。为了减少Redis的压力只访问一次，需要自定义SessionManager
 */
public class CustomSessionManager extends DefaultSessionManager {
    @Override
    //SessionKey中存储request对象
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if(sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            Session session = (Session)request.getAttribute(sessionId.toString());
            if(session != null){
                return session;
            }
        }
        Session session =  super.retrieveSession(sessionKey);
        if(request != null && sessionId != null){
            request.setAttribute(sessionId.toString(), session);  //将session塞入request中，下次访问session是直接从request中取。
        }
        return session;
    }

}
