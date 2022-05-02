package com.board.controller;

import com.board.domain.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.board.service.BoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/board/write.do")

    /*
    * @RequestParam : view 에서 전달받은 파라미터를 처리
    * 게시글 리스트 페이지에서 게시글 등록 페이지로 이동하면 idx는 null로 전송됨
    * 하지만, 게시글 상세 페이지에서 수정하기 버튼을 누르면 컨트롤러로 게시글 idx가 파라미터로 전송된다.
    * 컨트롤러는 전달받은 idx를 getBoardDetail 메서드의 인자로 전달.
    * 새로운 게시글을 등록할 경우에는 idx가 필요하지 않기 때문에 required 속성을 false로 지정.
    * 필수(required) 속성은 default 값이 true이며, required 속성을 false로 지정하지 않으면 idx가 파라미터로 전송되지 않았을 때 오류가 발생
    * */

    public String openBoardWrite(@RequestParam(value = "idx", required = false) Long idx, Model model) {
        if (idx == null) {
            model.addAttribute("board", new BoardDTO());
        } else {
            BoardDTO board = boardService.getBoardDetail(idx);
            if (board == null) {
                return "redirect:/board/list.do";
            }
            model.addAttribute("board", board);
        }

        return "board/write";
    }

    @PostMapping(value = "/board/register.do")
    public String registerBoard(final BoardDTO params) {
        try {
            boolean isRegistered = boardService.registerBoard(params);
            if (isRegistered == false) {
                // TODO => 게시글 등록에 실패하였다는 메시지를 전달
            }
        } catch (DataAccessException e) {
            // TODO => 데이터베이스 처리 과정에 문제가 발생하였다는 메시지를 전달

        } catch (Exception e) {
            // TODO => 시스템에 문제가 발생하였다는 메시지를 전달
        }

        return "redirect:/board/list.do";
    }

    @GetMapping(value = "/board/list.do")
    public String openBoardList(Model model) {
        List<BoardDTO> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);

        return "board/list";
    }

    @GetMapping(value = "/board/view.do")
    public String openBoardDetail(@RequestParam(value = "idx", required = false) Long idx, Model model) {
        if (idx == null) {
            // TODO => 올바르지 않은 접근이라는 메시지를 전달하고, 게시글 리스트로 리다이렉트
            return "redirect:/board/list.do";
        }

        BoardDTO board = boardService.getBoardDetail(idx);
        if (board == null || "Y".equals(board.getDeleteYn())) {
            // TODO => 없는 게시글이거나, 이미 삭제된 게시글이라는 메시지를 전달하고, 게시글 리스트로 리다이렉트
            return "redirect:/board/list.do";
        }
        model.addAttribute("board", board);

        return "board/view";
    }
}