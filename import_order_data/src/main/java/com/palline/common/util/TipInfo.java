package com.palline.common.util;
/**
 * 页面提示信息
 * @author gmm
 *
 */
public class TipInfo {

	private String msg;
	private String info;
	public TipInfo(String msg,String info){
		this.msg=msg;
		this.info=info;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
