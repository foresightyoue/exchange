package com.huagu.vcoin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huagu.coa.common.cons.AppDB;
import com.huagu.coa.common.cons.MyQuery;
import com.huagu.coa.common.cons.Mysql;

import cn.cerc.jdb.core.Record;
import cn.cerc.jdb.mysql.BatchScript;
import cn.cerc.jdb.mysql.Transaction;
import cn.cerc.jdb.mysql.UpdateMode;
import net.sf.json.JSONObject;

@ServerEndpoint("/FrmChatWebSocket/{to}/{user}")
public class FrmChatWebSocket {
	private static final Logger log = LoggerFactory.getLogger(FrmChatWebSocket.class);
	private static Map<String, FrmChatWebSocket> webSocketMap = new HashMap<String, FrmChatWebSocket>();
	private String to = null;
	private String userCode = null;
	private Session session;

	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		// 发送消息
        /*
         * if (message.startsWith("msg")) { String msg = message.substring(4); send(msg,
         * session); }
         */
            send(message, session);

		/*
		 * // 历史记录 if (message.startsWith("history")) { long time =
		 * Long.parseLong(message.substring(8)); history(time, session); }
		 */
	}

	/**
	 * 发送消息的方法
	 * 
	 * @param message
	 * @param session
	 * @throws Exception
	 */
	public void send(String message, Session session) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Mysql handle = new Mysql();
        Date ss = new Date();
        String time = format.format(ss.getTime());
        JSONObject json = JSONObject.fromObject(message);
        JSONObject data = (JSONObject) json.get("data");
        JSONObject mine = (JSONObject) data.get("mine");
        JSONObject mito = (JSONObject) data.get("to");
        int userId = mine.getInt("id");
        String content = mine.getString("content");
        int toId = mito.getInt("id");
        String type = mito.getString("type");
        System.err.println(message);
		// 查看对方是否在线
        FrmChatWebSocket obj = webSocketMap.get(to);
		if (obj != null) {
			// 在线直接发送消息
            MyQuery ds = new MyQuery(handle);
            ds.add("select fId,FStatus,FContent,FReceiverId,FCreatorId,FCreateTime,type from %s", AppDB.fmessaget_);
            ds.setMaximum(1);
            ds.open();
            ds.add("FStatus", 0);
            ds.add("FContent", content);
            ds.add("FReceiverId", toId);
            ds.add("FCreatorId", userId);
            ds.add("FCreateTime", new Date());
            ds.add("type", type);
            ds.post();
			Session ses = obj.getSession();
            // ses.getBasicRemote().sendText(message);
            ses.getBasicRemote().sendText(JSONObject.fromObject(message).toString());
			//System.out.println("发送了一条消息！");
			log.info(to + "发送了一条消息！");
		} else {
            MyQuery ds = new MyQuery(handle);
            ds.add("select fId,FStatus,FContent,FReceiverId,FCreatorId,FCreateTime,type from %s", AppDB.fmessaget_);
            ds.open();
            ds.append();
            ds.setField("FStatus", 1);
            ds.setField("FContent", content);
            ds.setField("FReceiverId", toId);
            ds.setField("FCreatorId", userId);
            ds.setField("FCreateTime", new Date());
            ds.setField("type", type);
            ds.post();
			log.info(to + "发送消息失败了！");
		}

	}

	/**
	 * 连接打开
	 * 
	 * @param to
	 * @param user
	 * @param session
	 * @throws Exception
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "to") String to, @PathParam(value = "user") String user, Session session)
			throws Exception {
		// 数据保存
		this.to = to;
		this.userCode = user;
		this.session = session;
		webSocketMap.put(userCode, this);
        try (Mysql handle = new Mysql(); Transaction tx = new Transaction(handle)) {
            FrmChatWebSocket obj = webSocketMap.get(userCode);
            if (obj != null) {
                MyQuery ds = new MyQuery(handle);
                ds.add("select fId,FStatus,FContent,FReceiverId,FCreatorId,FCreateTime,type from %s", AppDB.fmessaget_);
                ds.add(" where FCreatorId = %s and FReceiverId =%s", to, userCode);
                ds.add(" and FStatus = %s", 1);
                ds.getDefaultOperator().setUpdateMode(UpdateMode.loose);
                ds.getDefaultOperator().setPrimaryKey("fId");
                ds.open();
                if (!ds.eof()) {
                    for (Record re : ds) {
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> mine = new HashMap<>();
                        Map<String, Object> to_ = new HashMap<>();
                        mine.put("username", re.getString("FCreatorId"));
                        mine.put("avatar", "http://tp1.sinaimg.cn/5619439268/180/40030060651/1");
                        mine.put("id", re.getString("FCreatorId"));
                        mine.put("mine", true);
                        mine.put("content", re.getString("FContent"));
                        to_.put("name", re.getString("FReceiverId"));
                        to_.put("type", re.getString("type"));
                        to_.put("avatar", "http://tp1.sinaimg.cn/5619439268/180/40030060651/1");
                        to_.put("id", re.getString("FReceiverId"));
                        data.put("mine", mine);
                        data.put("to", to_);
                        map.put("data", data);
                        BatchScript script = new BatchScript(handle);
                        script.add("update %s set", AppDB.fmessaget_);
                        script.add("FStatus = %s", 0);
                        script.add(" where fid= %d", re.getInt("fId"));
                        script.exec();
                        String message = JSONObject.fromObject(map).toString();
                        Session ses = obj.getSession();
                        ses.getBasicRemote().sendText(JSONObject.fromObject(message).toString());
                    }
                }
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    /**
     * 连接断开
     */
	@OnClose
	public void onClose() {
		webSocketMap.remove(userCode);
		System.out.println("Connection closed");
	}

	public Session getSession() {
		return session;
	}
}
