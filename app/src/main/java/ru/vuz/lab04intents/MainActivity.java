package ru.vuz.lab04intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivityIntents";
    private static final int REQUEST_BOOK_DETAILS = 4004;
    private static final String KEY_READER_REQUEST = "readerRequest";
    private static final String KEY_SELECTED_BOOK_INDEX = "selectedBookIndex";
    private static final String KEY_LAST_RESULT = "lastResult";

    private final String[] bookTitles = {
            "Мастер и Маргарита",
            "1984",
            "451 градус по Фаренгейту",
            "Три товарища"
    };

    private final String[] bookAuthors = {
            "Михаил Булгаков",
            "Джордж Оруэлл",
            "Рэй Брэдбери",
            "Эрих Мария Ремарк"
    };

    private final String[] bookGenres = {
            "Роман, сатира, мистика",
            "Антиутопия",
            "Научная фантастика, антиутопия",
            "Роман"
    };

    private final String[] bookDescriptions = {
            "Книга о свободе, любви, выборе и ответственности, соединяющая московскую сатиру и библейскую линию.",
            "Роман о тоталитарном обществе, контроле информации и личной свободе человека.",
            "История мира, где книги запрещены, а чтение становится способом сохранить память и мышление.",
            "Роман о дружбе, любви и попытке сохранить человеческое достоинство после войны."
    };

    private EditText readerRequestEditText;
    private TextView selectedBookTextView;
    private TextView resultTextView;
    private ListView bookListView;
    private int selectedBookIndex = 0;
    private String lastResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        readerRequestEditText = findViewById(R.id.readerRequestEditText);
        selectedBookTextView = findViewById(R.id.selectedBookTextView);
        resultTextView = findViewById(R.id.resultTextView);
        bookListView = findViewById(R.id.bookListView);
        Button openSecondButton = findViewById(R.id.openSecondButton);
        Button openWebButton = findViewById(R.id.openWebButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                bookTitles
        );
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedBookIndex = position;
            updateSelectedBook();
        });

        if (savedInstanceState != null) {
            String restoredRequest = savedInstanceState.getString(KEY_READER_REQUEST, "");
            selectedBookIndex = savedInstanceState.getInt(KEY_SELECTED_BOOK_INDEX, 0);
            lastResult = savedInstanceState.getString(KEY_LAST_RESULT, "");
            readerRequestEditText.setText(restoredRequest);
        }

        updateSelectedBook();
        updateResult();
        openSecondButton.setOnClickListener(view -> openSecondActivity());
        openWebButton.setOnClickListener(view -> openBookInBrowser());
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
        outState.putString(KEY_READER_REQUEST, readerRequestEditText.getText().toString());
        outState.putInt(KEY_SELECTED_BOOK_INDEX, selectedBookIndex);
        outState.putString(KEY_LAST_RESULT, lastResult);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String restoredRequest = savedInstanceState.getString(KEY_READER_REQUEST, "");
        selectedBookIndex = savedInstanceState.getInt(KEY_SELECTED_BOOK_INDEX, 0);
        lastResult = savedInstanceState.getString(KEY_LAST_RESULT, "");
        readerRequestEditText.setText(restoredRequest);
        updateSelectedBook();
        updateResult();
        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_BOOK_DETAILS) {
            return;
        }

        if (resultCode == RESULT_OK && data != null) {
            String bookTitle = data.getStringExtra(SecondActivity.RESULT_BOOK_TITLE);
            String updatedNote = data.getStringExtra(SecondActivity.RESULT_UPDATED_NOTE);
            lastResult = getString(R.string.result_received, bookTitle, updatedNote);
            updateResult();
            Log.d(TAG, "onActivityResult: result received");
        } else {
            lastResult = getString(R.string.result_cancelled);
            updateResult();
            Log.d(TAG, "onActivityResult: cancelled");
        }
    }

    private void updateSelectedBook() {
        bookListView.setItemChecked(selectedBookIndex, true);
        selectedBookTextView.setText(getString(R.string.selected_book, bookTitles[selectedBookIndex]));
    }

    private void updateResult() {
        if (lastResult == null || lastResult.trim().isEmpty()) {
            resultTextView.setText(R.string.result_empty);
        } else {
            resultTextView.setText(lastResult);
        }
    }

    private void openSecondActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(SecondActivity.EXTRA_BOOK_ID, selectedBookIndex + 1);
        intent.putExtra(SecondActivity.EXTRA_BOOK_TITLE, bookTitles[selectedBookIndex]);
        intent.putExtra(SecondActivity.EXTRA_BOOK_AUTHOR, bookAuthors[selectedBookIndex]);
        intent.putExtra(SecondActivity.EXTRA_BOOK_GENRE, bookGenres[selectedBookIndex]);
        intent.putExtra(SecondActivity.EXTRA_BOOK_DESCRIPTION, bookDescriptions[selectedBookIndex]);
        intent.putExtra(SecondActivity.EXTRA_READER_REQUEST, readerRequestEditText.getText().toString());
        startActivityForResult(intent, REQUEST_BOOK_DETAILS);
    }

    private void openBookInBrowser() {
        String query = bookTitles[selectedBookIndex] + " " + bookAuthors[selectedBookIndex];
        Uri uri = Uri.parse("https://books.google.com/books?q=" + Uri.encode(query));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.open_web_chooser)));
            Log.d(TAG, "implicit ACTION_VIEW intent started");
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.no_browser_found, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "no activity for implicit intent", exception);
        }
    }
}
