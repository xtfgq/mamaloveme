package com.netlab.loveofmum.huanxin;

public class ChatMsgEntity {
	private static final String TAG = ChatMsgEntity.class.getSimpleName();
	private String name;
	private String date;
	private String text;
	private String time;
	private String headurl;
	private String content;
	private int  msgtype;
	public int getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	private boolean isComMeg = true;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isComMeg() {
		return isComMeg;
	}
	   public boolean getMsgType() {
	        return isComMeg;
	    }
	    public void setMsgType(boolean isComMsg) {
	    	isComMeg = isComMsg;
	    }
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public ChatMsgEntity() {
	}
	public ChatMsgEntity(String name, String date, String text, boolean isComMeg) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
		this.isComMeg = isComMeg;
	}
@Override
public String toString() {
	return "ChatMsgEntity [name="+name+",date="+date+",text="+text+",time="+time+",isComMeg="+isComMeg+"]";
}


}
