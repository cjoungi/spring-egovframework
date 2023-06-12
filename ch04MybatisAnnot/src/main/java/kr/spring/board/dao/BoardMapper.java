package kr.spring.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.board.vo.BoardVO;

public interface BoardMapper {
	@Insert("INSERT INTO aboard (num,writer,title,passwd,content,reg_date)"
			   + "VALUES (aboard_seq.nextval,#{writer},#{title},#{passwd},#{content},SYSDATE)")
	public void insertBoard(BoardVO vo);
	/*어노테이션이나 xml 설정 둘중에 1개만*/
	@Select("SELECT COUNT(*) FROM aboard")
	public int getBoardCount();
	public List<BoardVO> getBoardList(Map<String,Object> map);
	@Select("SELECT * FROM aboard WHERE num=#{num}")
	public BoardVO getBoard(int num);
	@Update("UPDATE aboard SET writer=#{writer},title=#{title},content=#{content} WHERE num=#{num}")
	public void updateBoard(BoardVO board);
	@Delete("DELETE FROM aboard WHERE num=#{num}")
	public void deleteBoard(int num);
}
