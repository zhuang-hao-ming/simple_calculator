package com.example.csuha.mycalculator;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText;
    private RecyclerView resultRecyclerView;
    private ImageButton delButton;
    private ImageButton leftButton;
    private ImageButton rightButton;


    private List<ResultItem> resultItemList;

    private ResultItemsAdapter resultItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initEvent();

    }

    /**
     * 初始化一些事件响应函数
     */
    private void initEvent() {
        // 删除按钮短按
        this.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        // 删除按钮长按
        this.delButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputEditText.setText(null);
                return true;
            }
        });

        // 左移短按
        this.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
            }
        });

        // 左移长按
        this.leftButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputEditText.setSelection(0, 0);
                return true;
            }
        });

        // 右移短按
        this.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
            }
        });

        // 右移长按
        this.rightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int textLength = inputEditText.getText().length();
                inputEditText.setSelection(textLength, textLength);
                return true; // 吸收事件
            }
        });

        // 使得文本框不响应点击事件
        // 不显示键盘（manifest中设置）
        // 显示cursor
        inputEditText.requestFocus();
        inputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 初始化view
     */
    private void init() {
        // init view
        inputEditText = (EditText) findViewById(R.id.input_edit_text);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler_view);
        delButton = (ImageButton) findViewById(R.id.button_del);
        leftButton = (ImageButton) findViewById(R.id.button_left);
        rightButton = (ImageButton) findViewById(R.id.button_right);
        // init data set
        resultItemList = new ArrayList<ResultItem>();

        // init adapter
        resultItemAdapter = new ResultItemsAdapter(this.resultItemList);

        // config recycler view
        resultRecyclerView.setHasFixedSize(true);

        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultRecyclerView.setAdapter(resultItemAdapter);


    }



    /**
     * 三角函数按钮点击事件响应函数
     * @param v
     */
    public void trigButtonPressed(View v) {
        PopupMenu triangleMenu = new PopupMenu(this, v);
        triangleMenu.inflate(R.menu.menu_triangle);
        triangleMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                inputEditText.getText().insert(inputEditText.getSelectionStart(), item.getTitle());
                return true;
            }
        });
        triangleMenu.show();
    }

    /**
     * 普通按钮点击事件响应函数
     * @param view
     */
    public void buttonPressed(View view) {
        Button btn = (Button)view;
        inputEditText.getText().insert(inputEditText.getSelectionStart(), btn.getText());
    }

    /**
     * =按钮点击事件响应函数
     * 计算结果
     * @param view
     */
    public void calculateResult(View view) {
        String input = inputEditText.getText().toString();
        if (input.isEmpty()) {
            return;
        }

        try {
            doCalculate(input);
            inputEditText.setText(null);
        } catch (Exception e) {
            if (e.getMessage().isEmpty()) {
                CharSequence msg = (CharSequence)e;
                Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                CharSequence msg = e.getMessage();
                Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    /**
     * 计算结果辅助函数
     * @param input
     * @throws Exception
     */
    private void doCalculate(String input) throws Exception{
        String result = Maths.doMath(input, this);

        ResultItem r = new ResultItem(input, result);
        int sizeOfResultItemList = this.resultItemList.size();
        this.resultItemList.add(r);
        this.resultItemAdapter.notifyItemInserted(sizeOfResultItemList); // 通知view数据改变，重新绘制必要内容
        this.resultRecyclerView.scrollToPosition(sizeOfResultItemList);
    }
}
