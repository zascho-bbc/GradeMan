package ch.oliver.grademan.listener;

import android.view.View;
import android.widget.Toast;

import ch.oliver.grademan.activity.NewGradeDialogFragment;
import ch.oliver.grademan.model.Fach;
import ch.oliver.grademan.model.Note;
import ch.oliver.grademan.database.NoteDAO;

/**
 * Created by nilso on 11.09.2016.
 */
public class AddGradeListener implements View.OnClickListener {
    private NewGradeDialogFragment gradeDialogFragment;

    public AddGradeListener(NewGradeDialogFragment gradeDialogFragment) {
        this.gradeDialogFragment = gradeDialogFragment;
    }

    @Override
    public void onClick(View v) {
        if (gradeDialogFragment.getSpinner().getSelectedItem().toString().equals("Fach wählen")) {
            Toast.makeText(v.getContext(), "Wählen Sie bitte ein Fach aus", Toast.LENGTH_SHORT).show();
        } else {
            Fach fach = (Fach) gradeDialogFragment.getSpinner().getSelectedItem();
            NoteDAO ndao=new NoteDAO(v.getContext());
            Note note= new Note();

            note.setFach_id(fach.getId_fach());
            note.setNote(Float.parseFloat(gradeDialogFragment.getSeekArcText().getText().toString()));
            note.setThema("Test");
            note.setGewichtung(Float.parseFloat(gradeDialogFragment.getSeekArcTextW().getText().toString().substring(0,gradeDialogFragment.getSeekArcTextW().getText().toString().length()-1)));

            ndao.noteerstellen(note);

            Toast.makeText(v.getContext(), "Note erfolgreich zu "+fach.getName()+" hinzugefügt", Toast.LENGTH_SHORT).show();
            gradeDialogFragment.dismiss();
        }
    }
}