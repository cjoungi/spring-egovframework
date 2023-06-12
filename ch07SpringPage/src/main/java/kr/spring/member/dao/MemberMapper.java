package kr.spring.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.MemberVO;

@Mapper
public interface MemberMapper {
	//회원관리 - 일반회원
	@Select("SELECT spmember_seq.nextval FROM dual")
	public int selectMem_num();
	@Insert("INSERT INTO spmember (mem_num,id,nick_name) VALUES (#{mem_num},#{id},#{nick_name})")
	public void insertMember(MemberVO member);
	@Insert("INSERT INTO spmember_detail (mem_num,name,passwd,phone,email,zipcode,address1,address2,hobby) VALUES "
			+ "(#{mem_num},#{name},#{passwd},#{phone},#{email},#{zipcode},#{address1},#{address2},#{hobby})")
	public void insertMember_detail(MemberVO member);
	@Select("SELECT m.mem_num,m.id,m.auth,m.au_id,d.passwd,m.nick_name,d.email FROM spmember m "
			+ "LEFT OUTER JOIN spmember_detail d ON m.mem_num=d.mem_num WHERE m.id=#{id}")
	public MemberVO selectCheckMember(String id);
	@Select("SELECT * FROM spmember m JOIN spmember_detail d ON m.mem_num=d.mem_num "
			+ "WHERE m.mem_num=#{mem_num}")
	public MemberVO selectMember(Integer mem_num);
	@Update("UPDATE spmember SET nick_name=#{nick_name} WHERE mem_num=#{mem_num}")
	public void updateMember(MemberVO member);
	@Update("UPDATE spmember_detail SET name=#{name},phone=#{phone},email=#{email},zipcode=#{zipcode},"
			+ "address1=#{address1},address2=#{address2},hobby=#{hobby},modify_date=SYSDATE WHERE mem_num=#{mem_num}")
	public void updateMember_detail(MemberVO member);
	@Update("UPDATE spmember_detail SET passwd=#{passwd} WHERE mem_num=#{mem_num}")
	public void updatePassword(MemberVO member);
	@Update("UPDATE spmember SET auth=0 WHERE mem_num=#{mem_num}")
	public void deleteMember(Integer mem_num);
	@Delete("DELETE FROM spmember_detail WHERE mem_num=#{mem_num}")
	public void deleteMember_detail(Integer mem_num);
	
	//프로필 이미지 업데이트
	@Update("UPDATE spmember_detail SET photo=#{photo},photo_name=#{photo_name} WHERE mem_num=#{mem_num}")
	public void updateProfile(MemberVO member);
	
	//자동로그인
	@Update("UPDATE spmember SET au_id=#{au_id} WHERE id=#{id}")
	public void updateAu_id(@Param("au_id") String au_id, @Param("id") String id);
	@Select("SELECT m.mem_num,m.id,m.auth,m.au_id,d.passwd,m.nick_name,d.email "
			+ "FROM spmember m JOIN spmember_detail d ON m.mem_num=d.mem_num "
			+ "WHERE m.au_id=#{au_id}")
	public MemberVO selectAu_id(String au_id);
	
	@Update("UPDATE spmember SET au_id='' WHERE mem_num=#{mem_num}")
	public void deleteAu_id(int mem_num);
	
	//회원관리 - 관리자
	public List<MemberVO> selectList(Map<String,Object> map);
	public int selectRowCount(Map<String,Object> map);
	@Update("UPDATE spmember SET auth=#{auth} WHERE mem_num=#{mem_num}")
	public void updateByAdmin(MemberVO memberVO);
}
