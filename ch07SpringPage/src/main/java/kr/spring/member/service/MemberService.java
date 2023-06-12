package kr.spring.member.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.spring.member.vo.MemberVO;

public interface MemberService {
	public void insertMember(MemberVO member);
	public MemberVO selectCheckMember(String id);
	public MemberVO selectMember(Integer mem_num);
	public void updateMember(MemberVO member);
	public void updatePassword(MemberVO member);
	public void deleteMember(Integer mem_num);
	
	//프로필 이미지 업데이트
	public void updateProfile(MemberVO member);
	
	//자동로그인
	public void updateAu_id(@Param("au_id") String au_id, @Param("id") String id);
	public MemberVO selectAu_id(String au_id);
	public void deleteAu_id(int mem_num);
	
	//회원관리 - 관리자
	public List<MemberVO> selectList(Map<String,Object> map);
	public int selectRowCount(Map<String,Object> map);
	public void updateByAdmin(MemberVO memberVO);
}
