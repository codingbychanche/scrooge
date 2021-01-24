package berthold.scrooge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import berthold.filedialogtool.FileDialog;

public class ActivityExportData extends AppCompatActivity implements FragmentDialogYesNo.getDataFromFragment {

    // Data Base
    private int keyOfChallengeToExport;

    // UI
    ImageButton saveCsvLocally;
    ImageButton sendByMail;
    EditText csvSepparator;
    ProgressBar pBar;

    // Req codes
    private final static int SAVE_FILE = 1;
    private final static int CONFIRM_CSV_SAVE_LOC = 2;
    private final static int CONFIRM_CSV_OVERWRITE = 3;

    private boolean SHOW_CONFIRM_SAVE_AT = false;
    private boolean SHOW_CONFIRM_OVERWRITE = false;

    private static int NOT_REQUIERED = -1;

    // Filesystem
    private String path;

    // Async tasks
    private SaveFile saveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csvdialog);

        Bundle extra = getIntent().getExtras();
        keyOfChallengeToExport = extra.getInt("keyOfChallengeToExport", 0);

        // UI
        saveCsvLocally = (ImageButton) findViewById(R.id.save_csv_localy);
        sendByMail = (ImageButton) findViewById(R.id.export_csv_by_mail);
        csvSepparator = (EditText) findViewById(R.id.csv_separator);
        pBar = (ProgressBar) findViewById(R.id.progress);

        // Actions
        saveCsvLocally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FileDialog.class);
                i.putExtra(FileDialog.MY_TASK_FOR_TODAY_IS, FileDialog.SAVE_FILE);
                i.putExtra("path", "/");
                startActivityForResult(i, SAVE_FILE);
            }
        });

        // Send text by mail
        sendByMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String csvTable=tableToCsv(csvSepparator.getText().toString());
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FunWithRegex: Take this :-)");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "-");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // @rem:Bug api level >11. When starting fragment before 'onResume' was called:@@
        // @rem:java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState@@
        // @rem:This is a workaround for this bug!@@
        // @rem:toDo Insert link for see: ... stack overflow..@@
        if (SHOW_CONFIRM_SAVE_AT) {
            showConfirmDialog(CONFIRM_CSV_SAVE_LOC,
                    FragmentDialogYesNo.SHOW_WITH_EDIT_TEXT,
                    getResources().getString(R.string.confirm_dialog_save_at),
                    getResources().getString(R.string.confirm_save_at),
                    getResources().getString(R.string.cancel_save));
        }

        if (SHOW_CONFIRM_OVERWRITE) {
            showConfirmDialog(CONFIRM_CSV_OVERWRITE,
                    FragmentDialogYesNo.SHOW_AS_YES_NO_DIALOG,
                    (getResources().getString(R.string.confirm_dialog_overwrite) + " \n" + path),
                    getResources().getString(R.string.confirm_save_at),
                    getResources().getString(R.string.cancel_save));
        }
    }

    /**
     * Show confirm dialog
     *
     * @param reqCode
     * @param kindOfDialog
     * @param dialogText
     * @param confirmButton
     * @param cancelButton
     */
    private void showConfirmDialog(int reqCode, int kindOfDialog, String dialogText, String confirmButton, String cancelButton) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentDialogYesNo fragmentDeleteRegex =
                FragmentDialogYesNo.newInstance(reqCode, kindOfDialog, NOT_REQUIERED, null, dialogText, confirmButton, cancelButton);
        fragmentDeleteRegex.show(fm, "fragment_dialog");
    }

    /**
     * Callback from confirm dialog
     * Existing '*.csv' file will be overwritten or a new file will be created
     * depending on the users choice in previously shown confirm dialog.
     *
     * @param reqCode
     * @param data
     * @param filename
     * @param buttonPressed
     */
    @Override
    public void getDialogInput(int reqCode, int data, String filename, String buttonPressed) {

        File f = new File(path + "/" + filename);

        if (reqCode == CONFIRM_CSV_SAVE_LOC) {
            if (buttonPressed.equals(FragmentDialogYesNo.BUTTON_OK_PRESSED)) {

                // If filename does not exist, save file....
                if (!f.exists())
                    saveCsv(path + "/" + filename);
                else {
                    // ... If filename exists, append '_NEW' to filename. Do not overwrite
                    // existing file.
                    String newFilename = filename + "_NEW";
                    saveCsv(path + "/" + newFilename);
                    Toast.makeText(getApplicationContext(), "Die Datei " + filename + " gibt es schon. Neue Datei unter " + newFilename + " abgelegt.", Toast.LENGTH_LONG).show();
                }

            } else
                // Cancel button pressed, do nothing.....
                Toast.makeText(getApplicationContext(), "Nichts gespeichert....", Toast.LENGTH_LONG).show();
        }
        // If file was picked, overwrite
        if (reqCode == CONFIRM_CSV_OVERWRITE) {
            saveCsv(path);
        }
        SHOW_CONFIRM_OVERWRITE = false;
        SHOW_CONFIRM_SAVE_AT = false;
    }

    /**
     * Callback for {@link FileDialog}
     *
     * @param reqCode
     * @param resCode
     * @param data
     */
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, reqCode, data);

        Log.v("RETURNED", "hi");

        if (resCode == RESULT_OK && reqCode == SAVE_FILE) {
            if (data.hasExtra("path")) {

                path = data.getExtras().getString("path");

                // When saving a file:
                // Check if just the folder was picked, in that case you have to take care
                // that a filename will be assigned or:
                // if a folder and an existing file was selected, in that case you may want to
                // check if the user wants the selected file to be overwritten...
                String returnStatus = data.getExtras().getString(FileDialog.RETURN_STATUS);

                if (returnStatus != null) {

                    // File was picked, ask user if he want's to overwrite it
                    if (returnStatus.equals(FileDialog.FOLDER_AND_FILE_PICKED)) {
                        SHOW_CONFIRM_OVERWRITE = true;
                        SHOW_CONFIRM_SAVE_AT = false;
                    }

                    // Just the folder, ask user for filename
                    if (returnStatus.equals(FileDialog.JUST_THE_FOLDER_PICKED)) {
                        SHOW_CONFIRM_OVERWRITE = false;
                        SHOW_CONFIRM_SAVE_AT = true;
                    }
                }
            }
        }
    }

    /*
     * Save CSV
     */
    private void saveCsv(String path) {
        //String csvTable=tableToCsv(csvSepparator.getText().toString());
        if (saveFile != null) saveFile.cancel(true);
        saveFile = new SaveFile("-", getApplicationContext(), pBar, path);
        saveFile.execute();
    }
}