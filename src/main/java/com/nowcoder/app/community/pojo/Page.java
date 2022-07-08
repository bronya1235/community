package com.nowcoder.app.community.pojo;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-20:36
 * @Description com.nowcoder.app.community.pojo
 * @Function 用于实现分页
 * 分页的具体实现过程：
 * 1、客户端需要传给服务器的数据，当前页码，和每页显示多少条，数据库就能查询到对应数据了
 * 2、服务器需要反馈给客户端的数据，一共有多少条数据，同时把访问路径也传过去方面复用
 */
public class Page {
	private int curPage=1;//当前页
	private int limit=10;//每页显示上限
	private int rows;//总数据上限
	private String path;//每个按钮所使用的访问路径

	/*
	 * 生成get 和 set方法，其中对set方法需要做一定的处理，避免用户乱输入
	 * */
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		if (curPage >= 1) {
			this.curPage = curPage;
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if (limit >= 1 && limit <= 100) {
			this.limit = limit;
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/*
	 * 需要额外补充一些方法
	 *
	 * */

	/**
	 * 获取当前页的起始行
	 *
	 * @return
	 */
	public int getOffset() {
		return (curPage - 1) * limit;
	}

	/**
	 * 计算总共有多少页
	 *
	 * @return
	 */
	public int getTotalPage() {
		return (rows - 1) / limit + 1;
	}

	/**
	 * 获取起始页码
	 *
	 * @return
	 */
	public int getStart() {
		int from = curPage - 2;
		return Math.max(from, 1);
	}

	public int getEnd() {
		int end = curPage + 2;
		return Math.min(end, getTotalPage());
	}

}
