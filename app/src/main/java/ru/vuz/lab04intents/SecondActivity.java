package ru.vuz.lab04intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends Activity {
    private static final String TAG = "SecondActivityIntents";
    public static final String EXTRA_BOOK_ID = "bookId";
    public static final String EXTRA_BOOK_TITLE = "bookTitle";
    public static final String EXTRA_BOOK_AUTHOR = "bookAuthor";
    public static final String EXTRA_BOOK_GENRE = "bookGenre";
    public static final String EXTRA_BOOK_DESCRIPTION = "bookDescription";
    public static final String EXTRA_READER_REQUEST = "readerRequest";
    public static final String RESULT_BOOK_TITLE = "resultBookTitle";
    public static final String RESULT_UPDATED_NOTE = "resultUpdatedNote";

    private static final String KEY_BOOK_ID = "bookId";
    private static final String KEY_BOOK_TITLE = "bookTitle";
    private static final String KEY_BOOK_AUTHOR = "bookAuthor";
    private static final String KEY_BOOK_GENRE = "bookGenre";
    private static final String KEY_BOOK_DESCRIPTION = "bookDescription";
    private static final String KEY_READER_REQUEST = "readerRequest";
    private static final String KEY_UPDATED_NOTE = "updatedNote";

    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;
    private String bookDescription;
    private String readerRequest;
    private EditText updatedNoteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_second);

        updatedNoteEditText = findViewById(R.id.updatedNoteEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        if (savedInstanceState == null) {
            readIntentData();
        } else {
            restoreState(savedInstanceState);
        }

        bindBookData();
        saveButton.setOnClickListener(view -> returnUpdatedNote());
        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BOOK_ID, bookId);
        outState.putString(KEY_BOOK_TITLE, bookTitle);
        outState.putString(KEY_BOOK_AUTHOR, bookAuthor);
        outState.putString(KEY_BOOK_GENRE, bookGenre);
        outState.putString(KEY_BOOK_DESCRIPTION, bookDescription);
        outState.putString(KEY_READER_REQUEST, readerRequest);
        outState.putString(KEY_UPDATED_NOTE, updatedNoteEditText.getText().toString());
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreState(savedInstanceState);
        bindBookData();
        Log.d(TAG, "onRestoreInstanceState");
    }

    private void readIntentData() {
        bookId = getIntent().getIntExtra(EXTRA_BOOK_ID, 0);
        bookTitle = getIntent().getStringExtra(EXTRA_BOOK_TITLE);
        bookAuthor = getIntent().getStringExtra(EXTRA_BOOK_AUTHOR);
        bookGenre = getIntent().getStringExtra(EXTRA_BOOK_GENRE);
        bookDescription = getIntent().getStringExtra(EXTRA_BOOK_DESCRIPTION);
        readerRequest = getIntent().getStringExtra(EXTRA_READER_REQUEST);
    }

    private void restoreState(Bundle savedInstanceState) {
        bookId = savedInstanceState.getInt(KEY_BOOK_ID, 0);
        bookTitle = savedInstanceState.getString(KEY_BOOK_TITLE, "");
        bookAuthor = savedInstanceState.getString(KEY_BOOK_AUTHOR, "");
        bookGenre = savedInstanceState.getString(KEY_BOOK_GENRE, "");
        bookDescription = savedInstanceState.getString(KEY_BOOK_DESCRIPTION, "");
        readerRequest = savedInstanceState.getString(KEY_READER_REQUEST, "");
        updatedNoteEditText.setText(savedInstanceState.getString(KEY_UPDATED_NOTE, ""));
    }

    private void bindBookData() {
        TextView bookIdTextView = findViewById(R.id.bookIdTextView);
        TextView bookTitleTextView = findViewById(R.id.bookTitleTextView);
        TextView bookAuthorTextView = findViewById(R.id.bookAuthorTextView);
        TextView bookGenreTextView = findViewById(R.id.bookGenreTextView);
        TextView bookDescriptionTextView = findViewById(R.id.bookDescriptionTextView);
        TextView receivedRequestTextView = findViewById(R.id.receivedRequestTextView);

        String displayedRequest = readerRequest == null || readerRequest.trim().isEmpty()
                ? getString(R.string.empty_request)
                : readerRequest;

        bookIdTextView.setText(getString(R.string.book_identifier, bookId));
        bookTitleTextView.setText(bookTitle);
        bookAuthorTextView.setText(getString(R.string.book_author, bookAuthor));
        bookGenreTextView.setText(getString(R.string.book_genre, bookGenre));
        bookDescriptionTextView.setText(bookDescription);
        receivedRequestTextView.setText(getString(R.string.received_request, displayedRequest));
    }

    private void returnUpdatedNote() {
        String updatedNote = updatedNoteEditText.getText().toString().trim();
        if (updatedNote.isEmpty()) {
            updatedNote = getString(R.string.default_updated_note);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_BOOK_TITLE, bookTitle);
        resultIntent.putExtra(RESULT_UPDATED_NOTE, updatedNote);
        setResult(RESULT_OK, resultIntent);
        Log.d(TAG, "setResult RESULT_OK");
        finish();
    }
}
