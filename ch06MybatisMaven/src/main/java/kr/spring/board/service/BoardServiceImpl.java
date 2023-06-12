package kr.spring.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.spring.board.dao.BoardMapper;
import kr.spring.board.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public void insertBoard(BoardVO board) {
		boardMapper.insertBoard(board);
	}

	@Override
	public int getBoardCount() {
		return boardMapper.getBoardCount();
	}

	@Override
	public List<BoardVO> getBoardList(Map<String, Object> map) {
		return boardMapper.getBoardList(map);
	}

	@Override
	public BoardVO getBoard(int num) {
		return boardMapper.getBoard(num);
	}

	@Override
	public void updateBoard(BoardVO board) {
		boardMapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(int num) {
		boardMapper.deleteBoard(num);
	}

}
