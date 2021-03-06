package com.board.dao;

import java.util.List;
import java.util.Map;

import com.board.domain.BoardVO;

public interface BoardDAO {

	// 게시물 목록
	public List list() throws Exception;

	// 게시물 작성
	public void write(BoardVO vo) throws Exception;

	// 게시물 조회
	public BoardVO view(int bnumber) throws Exception;

	// 게시물 수정
	public void modify(BoardVO vo) throws Exception;

	// 게시뮬 삭제
	public void delete(int bnumber) throws Exception;

	// 게시물 총 갯수
	public int count() throws Exception;

	// 첨부파일 업로드
	public void insertFile(Map<String, Object> map) throws Exception;

	// 첨부파일 조회
	public List<Map<String, Object>> selectFileList(int bnumber) throws Exception;

	// 첨부파일 다운
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception;
	
	// 첨부파일 수정
		public void updateFile(Map<String, Object> map) throws Exception;
		
	// 게시물 목록 + 페이징
	public List listPage(int displayPost, int postNum) throws Exception;
	
	//공개글 작성
	public void openwrite(BoardVO vo) throws Exception;

}