package com.korea.basic2.notepage;

import com.korea.basic2.notebook.Notebook;
import com.korea.basic2.notebook.NotebookRepository;
import com.korea.basic2.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotePageController {


    private final NotePageService notePageService;
    private final NotebookService notebookService;

    @RequestMapping("/")
    public String main(Model model) {
        List<Notebook> notebookList = notebookService.getParentNotebookList();
        if(notebookList.isEmpty()){
            notebookService.saveDefaultNotebook();
            return "redirect:/";
        }
        Notebook targetNotebook = notebookList.get(0);

        List<NotePage> notePageList = notePageService.getNotePageListByNotebook(targetNotebook);
        if(notePageList.isEmpty()){
            notePageService.saveDefaultNotePage(targetNotebook);
            return "redirect:/";
        }

        model.addAttribute("notePageList", notePageList);
        model.addAttribute("targetNotePage", notePageList.get(0));
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", targetNotebook);

        return "main";
    }

    @PostMapping("/write")
    public String write(Long notebookId) {
        Notebook notebook = notebookService.getNotebookById(notebookId);
        notePageService.saveDefaultNotePage(notebook);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        NotePage notePage = notePageService.getNotePageById(id);
        List<NotePage> notePageList = notePageService.getNotePageListByNotebook(notePage.getNotebook());
        List<Notebook> notebookList = notebookService.getParentNotebookList();

        model.addAttribute("targetNotePage", notePage);
        model.addAttribute("notePageList", notePageList);
        model.addAttribute("notebookList", notebookList);
        model.addAttribute("targetNotebook", notePage.getNotebook());

        return "main";
    }

    @PostMapping("/update")
    public String update(Long id, String title, String content) {
        NotePage notePage = notePageService.getNotePageById(id);

        if(title.trim().length()==0){
            title="제목없음";
        }

        notePage.setTitle(title);
        notePage.setContent(content);

        notePageService.save(notePage);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete")
    public String delete(Long id){
        notePageService.deleteById(id);
        return "redirect:/";
    }
}
