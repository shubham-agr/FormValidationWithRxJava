package com.example.formvalidationwithrxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formvalidationwithrxjava.utils.ValidationManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding4.widget.RxTextView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Function3;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etAddress;
    private MaterialButton btnSubmit;
    Observable<Boolean> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setOnClickListener();
        initObservable();
    }

    private void initObservable() {

        /**
         * NOTES: skip(1) is an operator in RxJava which allows us to skip the first value that we receive.
         * **/
        Observable<String> nameObservable = RxTextView.textChanges(etName).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Throwable {
                return charSequence.toString();
            }
        });

        Observable<String> emailObservable = RxTextView.textChanges(etEmail).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Throwable {
                return charSequence.toString();
            }
        });

        Observable<String> addressObservable = RxTextView.textChanges(etAddress).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Throwable {
                return charSequence.toString();
            }
        });

        observable = Observable.combineLatest(
                nameObservable,
                emailObservable,
                addressObservable,
                new Function3<String, String, String, Boolean>() {
                    @Override
                    public Boolean apply(String name, String email, String address) throws Throwable {
                        return isValidForm(name, email, address);
                    }
                }
        );

        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                Log.d("sugam", "Inside Subscribe==>" + aBoolean);
                updateButton(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private Boolean isValidForm(String name, String email, String address) {

        boolean validName = !name.isEmpty();

        if (!validName) {
            etName.setError("Please enter valid name");
        }
        boolean validEmail = ValidationManager.validateEmail(email);
        if (!validEmail) {
            etEmail.setError("Please enter valid email");
        }
        boolean validAddress = !address.isEmpty();
        if (!validAddress) {
            etAddress.setError("Please enter valid address");
        }
        return validName && validEmail && validAddress;
    }


    public void updateButton(boolean valid) {
        if (valid) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }

    }

    private void setOnClickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);

        btnSubmit = findViewById(R.id.btnSubmit);
    }
}