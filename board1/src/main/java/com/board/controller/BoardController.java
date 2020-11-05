package com.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.board.domain.BoardVO;
import com.board.domain.MemberVO;
import com.board.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	@Inject
	private BoardService service;

	// 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {

		List list = null;
		list = service.list();
		model.addAttribute("list", list);
	}

//게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWirte(HttpSession session, Model model) throws Exception {
		Object loginInfo = session.getAttribute("member");

		if(loginInfo == null) {
		 model.addAttribute("msg", false);
		}
	}

	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String posttWirte(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception {
		service.write(vo, mpRequest);
		
		return "redirect:/board/list";
	}
	//비회원 게시물 작성 페이지 이동
	@RequestMapping(value = "/openwrite", method = RequestMethod.GET)
	public void getRegister() throws Exception {
	}
	
	//비회원 게시물 작성
	@RequestMapping(value = "/openwrite", method = RequestMethod.POST)
	public String postRegister(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception {
		System.out.println("뭐가들어있으려나??? "+vo);
		service.openwrite(vo, mpRequest);

		return "redirect:/board/list";
	}
	// 게시물 조회
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String getView(@RequestParam("bnumber") int bnumber, Model model) throws Exception {
		BoardVO vo = service.view(bnumber);
		System.out.println("테스트 : "+vo);
		if(vo.getAsecret() == 1) {
		model.addAttribute("view", vo);
		System.out.println("비공개글");
		List<Map<String, Object>> fileList = service.selectFileList(vo.getBnumber());
		model.addAttribute("file", fileList);
		return "board/openpassword";
		
		}else {
			model.addAttribute("view", vo);
			List<Map<String, Object>> fileList = service.selectFileList(vo.getBnumber());
			model.addAttribute("file", fileList);
			System.out.println("공개글");
			
		}
		return "redirect:/board/view?bnumber="+vo.getBnumber();
	}

	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void getModify(@RequestParam("bnumber") int bnumber, Model model) throws Exception {

		BoardVO vo = service.view(bnumber);

		model.addAttribute("view", vo);
		
		List<Map<String, Object>> fileList = service.selectFileList(vo.getBnumber());
		model.addAttribute("file", fileList);
	}

	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postModify(BoardVO vo,  @RequestParam(value="fileNoDel[]") String[] files,
			 @RequestParam(value="fileNameDel[]") String[] fileNames,
			 MultipartHttpServletRequest mpRequest) throws Exception {

		service.modify(vo, files, fileNames, mpRequest);

		return "redirect:/board/view?bnumber=" + vo.getBnumber();
		
	}

	// 게시물 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(@RequestParam("bnumber") int bnumber) throws Exception {

		service.delete(bnumber);

		return "redirect:/board/list";
	}
	
	//첨부파일 다운
	@RequestMapping(value="/fileDown")
	public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception{
		Map<String, Object> resultMap = service.selectFileInfo(map);
		String storedFileName = (String) resultMap.get("STORED_FILE_NAME");
		String originalFileName = (String) resultMap.get("ORG_FILE_NAME");
		
		// 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File("C:\\mp\\file\\"+storedFileName));
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition",  "attachment; fileName=\""+URLEncoder.encode(originalFileName, "UTF-8")+"\";");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
	}
	
	

	/*
	 * // 게시물 목록 + 페이징 추가 게시물 총 갯수
	 * 
	 * @RequestMapping(value = "/listpage", method = RequestMethod.GET) public void
	 * getListPage(Model model) throws Exception {
	 * 
	 * List list = null; list = service.list(); model.addAttribute("list", list); }
	 * 
	 * // 게시물 목록 + 페이징 추가
	 * 
	 * @RequestMapping(value = "/listPage", method = RequestMethod.GET) public void
	 * getListPage(Model model, @RequestParam("num") int num) throws Exception {
	 * 
	 * // 게시물 총 갯수 int count = service.count();
	 * 
	 * // 한 페이지에 출력할 게시물 갯수 int postNum = 10;
	 * 
	 * // 하단 페이징 번호 ([ 게시물 총 갯수 ÷ 한 페이지에 출력할 갯수 ]의 올림) int pageNum =
	 * (int)Math.ceil((double)count/postNum);
	 * 
	 * // 출력할 게시물 int displayPost = (num - 1) * postNum;
	 * 
	 * List list = null; list = service.listPage(displayPost, postNum);
	 * model.addAttribute("list", list); model.addAttribute("pageNum", pageNum); }
	 */
}