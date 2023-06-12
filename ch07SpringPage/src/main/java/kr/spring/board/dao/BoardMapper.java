package kr.spring.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.board.vo.BoardFavVO;
import kr.spring.board.vo.BoardReplyVO;
import kr.spring.board.vo.BoardVO;

@Mapper
public interface BoardMapper {
	//부모글
	public List<BoardVO> selectList(Map<String,Object> map);
	public int selectRowCount(Map<String,Object> map);
	@Insert("INSERT INTO spboard (board_num,title,content,uploadfile,filename,ip,mem_num) "
			+ "VALUES (spboard_seq.nextval,#{title},#{content},#{uploadfile},#{filename},"
			+ "#{ip},#{mem_num})")
	public void insertBoard(BoardVO board);
	@Select("SELECT * FROM spboard b JOIN spmember m USING(mem_num) JOIN spmember_detail d "
			+ "USING(mem_num) WHERE b.board_num=#{board_num}")
	public BoardVO selectBoard(Integer board_num);
	@Update("UPDATE spboard SET hit=hit+1 WHERE board_num=#{board_num}")
	public void updateHit(Integer board_num);
	public void updateBoard(BoardVO board);
	@Delete("DELETE FROM spboard WHERE board_num=#{board_num}")
	public void deleteBoard(Integer board_num);
	@Update("Update spboard SET uploadfile='',filename='' WHERE board_num=#{board_num}")
	public void deleteFile(Integer board_num);
	
	//좋아요
	@Select("SELECT * FROM spboard_fav WHERE board_num=#{board_num} AND mem_num=#{mem_num}")
	public BoardFavVO selectFav(BoardFavVO fav);
	@Select("SELECT COUNT(*) FROM spboard_fav WHERE board_num=#{board_num}")
	public int selectFavCount(Integer board_num);
	@Insert("INSERT INTO spboard_fav (fav_num,board_num,mem_num) VALUES "
			+ "(spfav_seq.nextval,#{board_num},#{mem_num})")
	public void insertFav(BoardFavVO fav);
	@Delete("DELETE FROM spboard_fav WHERE fav_num=#{fav_num}")
	public void deleteFav(Integer fav_num);
	@Delete("DELETE FROM spboard_fav WHERE board_num=#{board_num}")
	public void deleteFavByBoardNum(Integer board_num);
	
	//댓글
	public List<BoardReplyVO> selectListReply(Map<String,Object> map);
	@Select("SELECT COUNT(*) FROM spboard_reply JOIN spmember USING (mem_num) WHERE board_num=#{board_num}")
	public int selectRowCountReply(Map<String,Object> map);
	@Select("SELECT * FROM spboard_reply WHERE re_num=#{re_num}")
	public BoardReplyVO selectReply(Integer re_num);
	@Insert("INSERT INTO spboard_reply (re_num,re_content,re_ip,board_num,mem_num) VALUES "
			+ "(spreply_seq.nextval,#{re_content},#{re_ip},#{board_num},#{mem_num})")
	public void insertReply(BoardReplyVO boardReply);
	@Update("UPDATE spboard_reply SET re_content=#{re_content},re_ip=#{re_ip},"
			+ "re_mdate=SYSDATE WHERE re_num=#{re_num}")
	public void updateReply(BoardReplyVO boardReply);
	
	public void deleteReply(BoardReplyVO boardReply);
	
	public void deleteReplyByBoardNum(Integer board_num);
	
}
