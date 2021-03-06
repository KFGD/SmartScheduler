package cnu.mobilesoftware.smartscheduler.KFGD_MemoUI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cnu.mobilesoftware.smartscheduler.DBHelper;
import cnu.mobilesoftware.smartscheduler.R;

public class EditMemoDialog extends Dialog {

    Context ownerActivity;
    Memo memo;
    EditText editText;
    IRefresh refreshOfActivity;

    private EditMemoDialog(Context context) {
        super(context);
    }

    public EditMemoDialog(Context context, Memo memo, IRefresh refreshOfActivity) {
        super(context);
        ownerActivity = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.memo = memo;
        this.refreshOfActivity = refreshOfActivity;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_memo);
        editText = (EditText)findViewById(R.id.edit);
        if(null != memo.getContent()){
            editText.setText(memo.getContent());
        }

        ImageButton insertMemoButton = (ImageButton)findViewById(R.id.btn_save);
        ImageButton cancelDialogBtn = (ImageButton) findViewById(R.id.btn_delete);

        insertMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveMemo();
            }
        });

        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelDialog();
            }
        });
        editText.addTextChangedListener(new DialogTextWatcher());



    }

    private void onSaveMemo(){
        String content = editText.getText().toString();
        if(content.length() == 0) {
            Toast.makeText(ownerActivity, "메모를 입력해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(-1 == memo.getID()){
            memo.setContent(content);
            DBHelper.getInstance().insertMemoInDB(memo);
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }else{
            memo.setContent(content);
            DBHelper.getInstance().updateMemoInDB(memo);
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }
    }

    private void onCancelDialog(){
        if(-1 == memo.getID()){
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }else{
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }
    }

    public void onDeleteMemoButton(){
        if(-1 == memo.getID()){
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }else{
            DBHelper.getInstance().deleteMemoinDB(memo);
            refreshOfActivity.Refresh();
            EditMemoDialog.this.dismiss();
        }
    }

    public class DialogTextWatcher implements TextWatcher {

        String previousText = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            previousText = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(4<=editText.getLineCount()){
                editText.setText(previousText.substring(0, previousText.length()-1));
                editText.setSelection(previousText.length());
                Toast.makeText(ownerActivity, "최대 3줄까지 입력가능합니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
