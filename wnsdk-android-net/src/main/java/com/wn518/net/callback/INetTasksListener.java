package com.wn518.net.callback;



/**
 * 请求回调
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author LiLong
* @date 2014-10-24 下午06:04:33 
* @UpdateData 2014-10-24 下午06:04:33 by_
 */
public interface INetTasksListener
{       
	  public void onLoading(long total, long current);
	  public void onSuccess(Object result, int taskflag);
	  public void onFailure(Throwable error, String msg,int taskflag);
	  public void onTaskStart();
}
