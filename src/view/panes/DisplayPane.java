package view.panes;

import javafx.scene.text.Text;

import java.io.File;

/**
 * Created by Anca on 3/16/2017.
 */
public class DisplayPane extends MainPane {
    private File file;

    public DisplayPane(){
        addToolbar(true);
        this.setCenter(new Text("display"));
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
