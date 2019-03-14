package moe.nyako.NIMS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class EditTags extends NFCRead {

    LinearLayout lowerTableLayout;


    //绘制标题栏按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_tags, menu);
        return true;
    }

    //标题栏按钮事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download)
            getXlsx();
        else if (item.getItemId() == R.id.action_upload)
            sendData();
        return super.onOptionsItemSelected(item);
    }

    //下载 xlsx 文件，调用方法
    public void getXlsx() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://ifnya.com:2333/getXlsx?oname=" + TagLists.adminOrgName));
        startActivity(intent);
        Toast.makeText(this, "已开始下载", Toast.LENGTH_SHORT).show();
    }

    //发送数据
    public void sendData() {
        try {
            JSONObject data = new JSONObject();

            //添加机构名
            data.put("orgName", TagLists.adminOrgName);

            //添加修改的标签名
            JSONArray tags = new JSONArray();
            tags.put(((EditText) findViewById(R.id.key1)).getText().toString());
            tags.put(((EditText) findViewById(R.id.key2)).getText().toString());
            tags.put(((EditText) findViewById(R.id.key3)).getText().toString());
            data.put("tags", tags);

            //添加修改内容
            JSONArray change = new JSONArray();
            for (int i = 1; i < lowerTableLayout.getChildCount(); i++) {
                String id = ((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(0)).getText().toString();
                if (!id.equals("")) {
                    JSONObject aChange = new JSONObject();

                    //添加学号
                    aChange.put("id", id);

                    //添加值变化
                    JSONArray value = new JSONArray();
                    value.put(((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(1)).getText().toString());
                    value.put(((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(2)).getText().toString());
                    value.put(((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(3)).getText().toString());
                    aChange.put("value", value);

                    change.put(aChange);
                }
            }
            data.put("change", change);

            //POST 修改
            if (MainActivity.api.editTags(data))
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();


        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);

        nfcInit();

        //设置标题
        setTitle(TagLists.adminOrgName);

        //底部表格
        lowerTableLayout = findViewById(R.id.lTable);

        //设置文本框内容关联性
        ((EditText) findViewById(R.id.key1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextView) findViewById(R.id.title1)).setText(s);
            }
        });
        ((EditText) findViewById(R.id.key2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextView) findViewById(R.id.title2)).setText(s);
            }
        });
        ((EditText) findViewById(R.id.key3)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextView) findViewById(R.id.title3)).setText(s);
            }
        });
        ((EditText) findViewById(R.id.val1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 1; i < lowerTableLayout.getChildCount(); i++)
                    ((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(1)).setText(s);
            }
        });
        ((EditText) findViewById(R.id.val2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 1; i < lowerTableLayout.getChildCount(); i++)
                    ((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(2)).setText(s);
            }
        });
        ((EditText) findViewById(R.id.val3)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 1; i < lowerTableLayout.getChildCount(); i++)
                    ((EditText) ((TableRow) lowerTableLayout.getChildAt(i)).getChildAt(3)).setText(s);
            }
        });

        //底部默认有一空行
        addLineLower("", 0);
    }

    //NFC 读取到学号，将学号放入倒数第一行之前
    @Override
    protected void output(String out) {
        addLineLower(out, 1);
    }

    //在下方数据区添加一行，插入到倒数第 pos 行前
    public void addLineLower(String id, int pos) {
        //新的一行
        TableRow editLine = new TableRow(this);
        lowerTableLayout.addView(editLine, lowerTableLayout.getChildCount() - pos);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT, 1);
        //学号输入框
        EditText name = new EditText(this);
        name.setHint("学号");
        name.setText(id);
        name.setLayoutParams(layoutParams);
        editLine.addView(name);
        //输入后，始终保证最后一行是空行
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!((TextView) ((TableRow) lowerTableLayout.getChildAt(lowerTableLayout.getChildCount() - 1)).getChildAt(0)).getText().toString().equals(""))
                    addLineLower("", 0);
            }
        });

        //三个 Tag 值输入框
        EditText value1 = new EditText(this);
        value1.setHint("值");
        value1.setText(((EditText) findViewById(R.id.val1)).getText().toString());
        value1.setLayoutParams(layoutParams);
        editLine.addView(value1);

        EditText value2 = new EditText(this);
        value2.setHint("值");
        value2.setText(((EditText) findViewById(R.id.val2)).getText().toString());
        value2.setLayoutParams(layoutParams);
        editLine.addView(value2);

        EditText value3 = new EditText(this);
        value3.setHint("值");
        value3.setText(((EditText) findViewById(R.id.val3)).getText().toString());
        value3.setLayoutParams(layoutParams);
        editLine.addView(value3);
    }
}
