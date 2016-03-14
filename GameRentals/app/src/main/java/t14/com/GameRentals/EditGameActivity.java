package t14.com.GameRentals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yourui on 3/3/16.
 */
public class EditGameActivity extends Activity {
    private Game game;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_item);
        currentUser = UserController.getCurrentUser();

        final EditText gameNameEdit = (EditText) findViewById(R.id.editGameNameEditText);
        final EditText gameDescriptionEdit = (EditText) findViewById(R.id.editGameDescriptionText);
        final Button saveButton = (Button) findViewById((R.id.editGameSaveButton));
        final Button deleteButton = (Button) findViewById((R.id.editGameDeleteButton));
        final Button cancelButton = (Button) findViewById(R.id.editGameCancelButton);
        final Button returnButton = (Button) findViewById(R.id.editGameReturnButton);
        final TextView statusLabel = (TextView) findViewById(R.id.editGameStatus);
        final TextView borrowerName = (TextView) findViewById(R.id.borrowerName);
        final TextView borrowerNameLabel = (TextView) findViewById(R.id.borrowerNameLabel);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        returnButton.setVisibility(View.INVISIBLE);
        returnButton.setClickable(false);
        borrowerName.setVisibility(View.INVISIBLE);
        borrowerNameLabel.setVisibility(View.INVISIBLE);

        //TODO:Doesn't always get correct game, need to fix logic
        game = (Game) getIntent().getSerializableExtra("Game");
        statusLabel.setText(game.getStatusString());
        if(game.getStatus() == GameController.STATUS_BORROWED){
            returnButton.setVisibility(View.VISIBLE);
            returnButton.setClickable(true);
            borrowerName.setVisibility(View.VISIBLE);
            borrowerNameLabel.setVisibility(View.VISIBLE);
            //borrowerName.setText(game.getBorrower().getUserName());
            borrowerName.setText("Borrow name will go here when not NULL");
        }

        gameNameEdit.setText(game.getGameName());
        gameDescriptionEdit.setText(game.getDescription());

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for(int i = 0; i < currentUser.getMyGames().getSize(); i++){
                    if (currentUser.getMyGames().getGame(i).getGameName().equals(game.getGameName()))
                        game = currentUser.getMyGames().getGame(i);
                }
                game.setGameName(gameNameEdit.getText().toString());
                game.setDescription(gameDescriptionEdit.getText().toString());
                updateServer();
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adb.setMessage("Are you sure you want to delete this game?");
                adb.setCancelable(true);
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Make this remove from server
                        UserController.getCurrentUser().getMyGames().removeGame(game);
                        updateServer();
                        finish();
                    }
                });
                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.show();
            }
        });
        //Only shows if item has borrowed status, and is used to indicate that game has been returned
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setStatus(GameController.STATUS_AVAILABLE);
                statusLabel.setText(game.getStatusString());
                updateServer();
            }
        });
    }

    public void updateServer(){
        ElasticSearchUsersController.EditUserTask ese = new ElasticSearchUsersController.EditUserTask();
        ese.execute(currentUser);
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

}

