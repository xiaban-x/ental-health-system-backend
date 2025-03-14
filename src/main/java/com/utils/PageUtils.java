package com.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;

	// 总记录数
	private long total;
	// 每页记录数
	private long pageSize;
	// 总页数
	private long totalPage;
	// 当前页数
	private long currPage;
	// 列表数据
	private List<?> list;

	/**
	 * 分页
	 * 
	 * @param list       列表数据
	 * @param totalCount 总记录数
	 * @param pageSize   每页记录数
	 * @param currPage   当前页数
	 */
	public PageUtils(List<?> list, long totalCount, long pageSize, long currPage) {
		this.list = list;
		this.total = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (long) Math.ceil((double) totalCount / pageSize);
	}

	/**
	 * 分页构造函数，使用 MyBatis-Plus 的分页对象
	 */
	public PageUtils(Page<?> page) {
		this.list = page.getRecords();
		this.total = page.getTotal();
		this.pageSize = page.getSize();
		this.currPage = page.getCurrent();
		this.totalPage = page.getPages();
	}

	/**
	 * 空数据的分页
	 */
	public PageUtils(Map<String, Object> params) {
		// 生成 Page 对象并初始化
		Page<?> page = new Page<>(
				Long.valueOf(params.get("page").toString()),
				Long.valueOf(params.get("limit").toString()));
		new PageUtils(page);
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	// 额外的分页转换方法
	public static <T> PageUtils convert(Page<T> page) {
		return new PageUtils(page);
	}
}
