package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Displays the login screen for the player
 */

public class LogInScreen extends AppCompatActivity {

    /**
     * The file holding the user info
     */
    private File userFile;

    /**
     * User input for email field
     */
    private EditText emailField;

    /**
     * User input for password field
     */
    private EditText passwordField;

    /**
     * Sign up button
     */
    private Button signUpButton;

    /**
     * Log in button
     */
    private Button logInButton;

    /**
     * Firebase Storage reference to root of storage
     */

    private StorageReference userRef;

    /**
     * Loads users file from Firebase storage and
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // loads already created users to userManager
        setContentView(R.layout.login_screen);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        userRef = storage.getReferenceFromUrl("gs://gamecenter-g0503.appspot.com/users.txt");

        initializeUserFile();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signUpButton = findViewById(R.id.SignUpButton);
        logInButton = findViewById(R.id.LogInButton);

        addLogInButtonListener();
        addSignUpButtonListener();
    }

    /**
     * Initialize a user file.
     */

    private void initializeUserFile() {
        try {
            final File localFile = File.createTempFile("users", "txt");
            userRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    userFile = localFile;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LogInScreen.this, "unable to retrieve", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add sign up button listener to log in screen
     */
    private void addSignUpButtonListener() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startSignUp();
            }
        });
    }


    /**
     * Sign up a new user if the email inputted has not been previously registered.
     */
    private void startSignUp() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LogInScreen.this, "Fields are empty", Toast.LENGTH_LONG).show();
        } else {
            if (email.contains(":") || password.contains(":")) {
                Toast.makeText(LogInScreen.this, "Invalid character input " +
                        "(char \":\" is not allowed ", Toast.LENGTH_LONG).show();
            } else {
                if (trySignUp(email, password)) {
                    Toast.makeText(LogInScreen.this, "Successfully signed in!", Toast.LENGTH_LONG).show();
                    switchToGM(email);
                } else {
                    Toast.makeText(LogInScreen.this, "Username already exists", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Perform log in function
     */

    private void startLogIn() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LogInScreen.this, "Fields are empty", Toast.LENGTH_LONG).show();
        } else {
            if (email.contains(":") || password.contains(":")) {
                Toast.makeText(LogInScreen.this, "Invalid character input " +
                        "(char \":\" is not allowed ", Toast.LENGTH_LONG).show();
            } else {
                if (!tryLogIn(email, password)) {
                    Toast.makeText(LogInScreen.this, "Invalid login", Toast.LENGTH_LONG).show();
                } else {
                    switchToGM(email);
                }
            }
        }
    }

    /**
     * Attempt a log in with username and password
     *
     * @param username user's inputted username
     * @param password user's inputted password
     * @return whether log in was successful
     */

    private boolean tryLogIn(String username, String password) {
        String line;
        boolean check = false;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(userFile));

            while ((line = br.readLine()) != null) {
                if ((getUsernameFromLine(line).equals(username)) &&
                        (Addons.checkString(password, getPasswordFromLine(line)))) {
                    check = true;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Exception", "File not found: " + e.toString());
        } catch (NullPointerException e) {
            Toast.makeText(LogInScreen.this, "Unable to connect to Database", Toast.LENGTH_LONG).show();
        }
        return check;
    }

    /**
     * Attempt a sign up with email and password
     *
     * @param username user's inputted email
     * @param password user's inputted password
     * @return whether log in was successful
     */

    private boolean trySignUp(String username, String password) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(userFile));

            String line;
            while ((line = br.readLine()) != null) {
                if (getUsernameFromLine(line).equals(username)) {
                    return false;
                }
            }
            FileWriter fw = new FileWriter(userFile, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.printf("\n" + username + ":" + Addons.stringToSHA256(password));

            userRef.putFile(Uri.fromFile(userFile));

            pw.close();
            br.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Exception", "File not found: " + e.toString());
        } catch (NullPointerException e) {
            Toast.makeText(LogInScreen.this, "Unable to connect to Database", Toast.LENGTH_LONG).show();
        }
        return false; // SHOULD NEVER REACH.
    }

    /**
     * Retrieve username from save file
     *
     * @param line line from file with user credentials
     * @return user's username
     */

    private String getUsernameFromLine(String line) {
        int index = line.indexOf(":");
        if (index == -1) {
            return "";
        }
        return line.substring(0, index);
    }

    /**
     * Retrieve password from the save file
     *
     * @param line line from file with user password
     * @return user's inputted password
     */
    private String getPasswordFromLine(String line) {
        int index = line.indexOf(":");
        if (index == -1) {
            return "";
        }
        return line.substring(index + 1).trim();
    }

    /**
     * Add log in button listener to log in screen
     */
    private void addLogInButtonListener() {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogIn();
            }
        });
    }

    /**
     * Switch to the GameManager view to launch game centre.
     * Adapted from: https://zocada.com/using-intents-extras-pass-data-activities-android-beginners-guide/
     */
    private void switchToGM(String sessionUsername) {
        Intent gmIntent = new Intent(LogInScreen.this, GameManager.class);
        gmIntent.putExtra("USERNAME", sessionUsername);
        startActivity(gmIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
}