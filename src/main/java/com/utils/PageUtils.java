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
	private Integer total;
	// 每页记录数
	private Integer pageSize;
	// 总页数
	private Integer totalPage;
	// 当前页数
	private Integer currPage;
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
	public PageUtils(List<?> list, Integer totalCount, Integer pageSize, Integer currPage) {
		this.list = list;
		this.total = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
	}

	/**
	 * 分页构造函数，使用 MyBatis-Plus 的分页对象
	 */
	public PageUtils(Page<?> page) {
		this.list = page.getRecords();
		this.total = (int) page.getTotal();
		this.pageSize = (int) page.getSize();
		this.currPage = (int) page.getCurrent();
		this.totalPage = (int) page.getPages();
	}

	/**
	 * 空数据的分页
	 */
	public PageUtils(Map<String, Object> params) {
		// 生成 Page 对象并初始化
		Page<?> page = new Page<>(
				Integer.valueOf(params.get("page").toString()),
				Integer.valueOf(params.get("limit").toString()));
		new PageUtils(page);
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	// 额外的分页转换方法
	public static <T> PageUtils convert(Page<T> page) {
		return new PageUtils(page);
	}
}
