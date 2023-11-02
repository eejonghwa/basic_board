package com.korea.basic2.notebook;

import com.korea.basic2.notepage.NotePage;
import com.korea.basic2.notepage.NotePageRepository;
import com.korea.basic2.notepage.NotePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/notebook")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final NotePageService notePageService;

    @RequestMapping("/{notebookId}")
    public String select(@PathVariable("notebookId") Long notebookId, Model model) {

        Notebook notebook = notebookService.getNotebookById(notebookId);
        List<Notebook> notebookList = notebookService.getParentNotebookList();

        model.addAttribute("notebookList", notebookList);
        model.addAttribute("notePageList", notebook.getNotePageList());
        model.addAttribute("targetNotePage", notebook.getNotePageList().get(0));
        model.addAttribute("targetNotebook", notebook);

        return "main";
    }

    @RequestMapping("/")
    public String main(Model model) {
        List<NotePage> notePageList = notePageService.getNotePageList();
        List<Notebook> notebookList = notebookService.getParentNotebookList();
        if (notebookList.isEmpty()) {
            notebookService.saveDefaultNotebook();
            return "redirect:/";
        }

        if (notePageList.isEmpty()) {
            notePageService.saveDefaultNotePage(notebookList.get(0));
            return "redirect:/";
        }
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("notePageList", notePageList);
        model.addAttribute("targetNotePage", notePageList.get(0));
        model.addAttribute("targetNotebook", notebookList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write() {
        Notebook notebook = notebookService.saveDefaultNotebook();
        notePageService.saveDefaultNotePage(notebook);
        return "redirect:/notebook/" +notebook.getId();
    }

    @PostMapping("/add-group")
    public String addGroup(Long notebookId){
        Notebook notebook = notebookService.saveGroupNotebook(notebookId);
        notePageService.saveDefaultNotePage(notebook);
        return "redirect:/";
    }
}
