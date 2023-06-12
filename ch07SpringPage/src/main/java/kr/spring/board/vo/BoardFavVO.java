package kr.spring.board.vo;

public class BoardFavVO {
	private int fav_num;
	private int board_num;
	private int mem_num;
	
	
	public int getFav_num() {
		return fav_num;
	}
	public void setFav_num(int fav_num) {
		this.fav_num = fav_num;
	}
	public int getBoard_num() {
		return board_num;
	}
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	public int getMem_num() {
		return mem_num;
	}
	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}
	@Override
	public String toString() {
		return "BoardFavVO [fav_num=" + fav_num + ", board_num=" + board_num + ", mem_num=" + mem_num + "]";
	}
	
}
