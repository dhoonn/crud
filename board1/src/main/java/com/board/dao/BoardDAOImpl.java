package com.board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.board.domain.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Inject
	private SqlSession sql;

	private static String namespace = "com.board.mappers.board";

	// 게시물 목록
	@Override
	public List list() throws Exception {

		return sql.selectList(namespace + ".list");
	}

//게시물 작성
	@Override
	public void write(BoardVO vo) throws Exception {
		sql.insert(namespace + ".write", vo);

	}

//공개글 작성
	@Override
	public void openwrite(BoardVO vo) throws Exception {
		sql.insert(namespace + ".openwrite", vo);
		}
		
//게시물 조회
	public BoardVO view(int bnumber) throws Exception {
		System.out.println("dbn" + bnumber);
		return sql.selectOne(namespace + ".view", bnumber);
	}

//게시물 수정
	@Override
	public void modify(BoardVO vo) throws Exception {
		sql.update(namespace + ".modify", vo);
	}

//게시물 삭제
	public void delete(int bnumber) throws Exception {
		sql.delete(namespace + ".delete", bnumber);
	}

//게시물 총 갯수
	@Override
	public int count() throws Exception {
		return sql.selectOne(namespace + ".count");
	}

//첨부파일 업로드
	@Override
	public void insertFile(Map<String, Object> map) throws Exception {
		sql.insert(namespace + ".insertFile", map);
	}

// 첨부파일 조회
	@Override
	public List<Map<String, Object>> selectFileList(int bnumber) throws Exception {
		return sql.selectList(namespace +".selectFileList", bnumber);
	}
	
	// 첨부파일 다운로드
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		return sql.selectOne(namespace +".selectFileInfo", map);
	}
	
	@Override
	public void updateFile(Map<String, Object> map) throws Exception {
		
		sql.update(namespace +".updateFile", map);
	}
	
	
	
	// 게시물 목록 + 페이징

	@Override
	public List listPage(int displayPost, int postNum) throws Exception {

		HashMap data = new HashMap();

		data.put("displayPost", displayPost);
		data.put("postNum", postNum);

		return sql.selectList(namespace + ".listPage", data);
	}

}