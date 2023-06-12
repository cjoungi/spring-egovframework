package kr.spring.ch11.controller;

public class PageRank {
	private int rank;
	private String page;
	
	public PageRank() {}

	public PageRank(int rank, String page) {
		super();
		this.rank = rank;
		this.page = page;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
}
