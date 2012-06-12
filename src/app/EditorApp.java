package app;

import io.FileType;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.MorphableModel;
import model.MorphableModelBuilder;

import editor.Editor;

public class EditorApp {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		MorphableModel mm = MorphableModelBuilder.LoadDirectory("data", FileType.PLY);

		Editor editor = new Editor(mm);
	}

}
