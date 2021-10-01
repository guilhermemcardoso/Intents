package com.example.intents;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<String> requisicaoPermissaoActivityLauncher;
    ActivityMainBinding activityMainBinding;
    ActivityResultLauncher<Intent> imagePickResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.mainTb.appTb.setTitle(MainActivity.this.getClass().getSimpleName());
        activityMainBinding.mainTb.appTb.setSubtitle(getIntent().getAction());
        setSupportActionBar(activityMainBinding.mainTb.appTb);

        requisicaoPermissaoActivityLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedida -> {
            if(!concedida) {
                requisitarPermissaoLigacao();
            } else {
                discarTelefone();
            }
        });

        imagePickResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = null;
                        if (data != null) {
                            selectedImageUri = data.getData();
                        }
                        if (null != selectedImageUri) {
                            activityMainBinding.parameterIv.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.callMi:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requisitarPermissaoLigacao();
                    } else {
                        discarTelefone();
                    }
                }
                break;
            case R.id.dialMi:
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel: ".concat(activityMainBinding.parameterEt.getText().toString())));
                startActivity(dial);
                break;
            case R.id.exitMi:
                finish();
                break;
            case R.id.actionMi:
                Intent action = new Intent(this, ActionActivity.class).putExtra(Intent.EXTRA_TEXT, activityMainBinding.parameterEt.getText().toString());
                startActivity(action);
                break;
            case R.id.viewMi:
                Intent browser = new Intent(Intent.ACTION_VIEW);
                String url = activityMainBinding.parameterEt.getText().toString();
                if(!url.contains("http")) {
                    url = "http://".concat(activityMainBinding.parameterEt.getText().toString());
                }
                browser.setData(Uri.parse(url));
                startActivity(browser);
                break;
            case R.id.pickMi:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickResultLauncher.launch(intent);
                break;
            case R.id.chooserMi:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, activityMainBinding.parameterEt.getText().toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            default:
                break;
        }

        return true;
    }

    private void discarTelefone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: ".concat(activityMainBinding.parameterEt.getText().toString())));
        startActivity(intent);
    }

    private void requisitarPermissaoLigacao() {
        requisicaoPermissaoActivityLauncher.launch(Manifest.permission.CALL_PHONE);
    }
}