package com.korea.basic2.notepage;

import com.korea.basic2.notebook.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotePageRepository extends JpaRepository<NotePage, Long> {
    List<NotePage> findByNotebook(Notebook targetNotebook);
}
