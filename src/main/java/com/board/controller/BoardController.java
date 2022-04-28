package com.board.controller;

import com.board.domain.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.board.service.BoardService;
import org.springframework.web.bind.annotation.RequestParam;

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
}