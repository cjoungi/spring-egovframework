package kr.spring.board.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.spring.board.vo.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO{
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public void insertBoard(BoardVO board) {
		         //BoardMapper의 insert 태그 id와 일치
		sqlSession.insert("insertBoard",board);
		
	}

	@Override
	public int getBoardCount() {
		Integer count = sqlSession.selectOne("getBoardCount");
		return count;
	}

	@Override
	public List<BoardVO> getBoardList(Map<String, Object> map) {
		
		return sqlSession.selectList("getBoardList",map);
	}

	@Override
	public BoardVO getBoard(int num) {
		return sqlSession.selectOne("getBoard",num);
	}

	@Override
	public void updateBoard(BoardVO board) {
		sqlSession.update("updateBoard",board);
	}

	@Override
	public void deleteBoard(int num) {
		sqlSession.delete("deleteBoard",num);
	}

}
