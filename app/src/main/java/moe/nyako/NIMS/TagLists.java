package moe.nyako.NIMS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TagLists extends AppCompatActivity {

    public static String adminOrgName = "";
    ArrayList<TableLayout> tableLayouts = new ArrayList<>();
    ArrayList<CardView> cards = new ArrayList<>();
    ArrayList<ImageButton> pinButtons = new ArrayList<>();
    ArrayList<Boolean> favors = new ArrayList<>();
    LinearLayout linearLayout;
    Boolean showall = false;

    //绘制标题栏按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tag_lists, menu);
        return true;
    }

    //标题栏按钮事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (!showall) {
                for (ImageButton pinButton : pinButtons) {
                    LinearLayout title = (LinearLayout) ((LinearLayout) cards.get(pinButtons.indexOf(pinButton)).getChildAt(0)).getChildAt(0);
                    title.addView(pinButton, 0);
                }

                for (CardView card : cards)
                    if (linearLayout.indexOfChild(card) == -1)
                        linearLayout.addView(card);

                item.setTitle("仅显示收藏");
                item.setIcon(R.drawable.btn_show_favor);
                showall = true;
            } else {
                for (ImageButton pinButton : pinButtons)
                    ((LinearLayout) pinButton.getParent()).removeView(pinButton);

                for (CardView card : cards)
                    if (!favors.get(cards.indexOf(card)))
                        linearLayout.removeView(card);

                item.setTitle("显示所有");
                item.setIcon(R.drawable.btn_show_all);
                showall = false;
            }
        } catch (Exception e) {
            Toast.makeText(TagLists.this, e.toString(), Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_lists);

        try {
            linearLayout = findViewById(R.id.cards);
            setTitle(MainActivity.userIdString);

            //允许主线程网络访问
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //获取 Tag 列表
            JSONArray jsonArray = MainActivity.api.getTags(MainActivity.userIdString);

            for (int i = 0; i < jsonArray.length(); i++) {
                //创建卡片
                CardView card = new CardView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 20, 10, 20);
                card.setLayoutParams(lp);
                card.setRadius(30);
                cards.add(card);

                //卡片中所有内容
                LinearLayout allInCard = new LinearLayout(this);
                lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(30, 30, 30, 30);
                allInCard.setOrientation(LinearLayout.VERTICAL);
                allInCard.setLayoutParams(lp);
                card.addView(allInCard);

                //卡片标题栏
                LinearLayout cardTitle = new LinearLayout(this);
                cardTitle.setOrientation(LinearLayout.HORIZONTAL);
                cardTitle.setMinimumHeight(36);
                allInCard.addView(cardTitle);

                JSONObject object = jsonArray.getJSONObject(i);

                //显示收藏按钮
                ImageButton pinButton = new ImageButton(this);
                pinButtons.add(pinButton);
                if (object.getInt("favor") == 0) {
                    favors.add(false);
                    pinButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_pin));
                } else {
                    favors.add(true);
                    pinButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_unpin));

                    linearLayout.addView(card);
                }
                pinButton.setBackground(null);
                pinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int res = MainActivity.api.favor(MainActivity.userIdString,
                                ((TextView) ((LinearLayout) v.getParent()).getChildAt(1)).getText().toString().substring(2));
                        if (res == 0) {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.btn_pin));
                            Toast.makeText(TagLists.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                            favors.set(cards.indexOf(v.getParent().getParent().getParent()), false);
                        } else if (res == 1) {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.btn_unpin));
                            Toast.makeText(TagLists.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            favors.set(cards.indexOf(v.getParent().getParent().getParent()), true);
                        } else
                            Toast.makeText(TagLists.this, "操作失败", Toast.LENGTH_SHORT).show();
                    }
                });
                pinButton.setMinimumWidth(36);

                //显示组织名称
                TextView orgNameTextView = new TextView(this);
                orgNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));
                orgNameTextView.setText("  " + object.getString("orgName"));
                orgNameTextView.setTextSize(26);
                orgNameTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                orgNameTextView.setGravity(Gravity.CENTER_VERTICAL);
                cardTitle.addView(orgNameTextView);

                //显示管理按钮
                ImageButton adminButton = new ImageButton(this);
                adminButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_edit));
                adminButton.setBackground(null);
                adminButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TagLists.this, EditTags.class);
                        if (showall)
                            adminOrgName = ((TextView) ((LinearLayout) v.getParent()).getChildAt(1)).getText().toString().substring(2);
                        else
                            adminOrgName = ((TextView) ((LinearLayout) v.getParent()).getChildAt(0)).getText().toString().substring(2);
                        startActivity(intent);
                    }
                });
                adminButton.setMinimumWidth(36);
                if (object.getInt("admin") == 0)
                    adminButton.setVisibility(View.INVISIBLE);
                cardTitle.addView(adminButton);

                //显示展开按钮
                ImageButton expandButton = new ImageButton(this);
                expandButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_up));
                expandButton.setBackground(null);
                expandButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout linearLayoutInCard = (LinearLayout) v.getParent().getParent();
                        if (linearLayoutInCard.getChildCount() == 2) {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.btn_down));
                            linearLayoutInCard.removeViewAt(1);
                        } else {
                            ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.btn_up));
                            int theTable = cards.indexOf((CardView) linearLayoutInCard.getParent());
                            linearLayoutInCard.addView(tableLayouts.get(theTable));
                        }
                    }
                });
                adminButton.setMinimumWidth(36);
                cardTitle.addView(expandButton);

                //卡片数据表
                TableLayout table = new TableLayout(this);
                TableRow blankTableRow = new TableRow(this);
                blankTableRow.setMinimumHeight(24);
                table.addView(blankTableRow);
                allInCard.addView(table);
                tableLayouts.add(table);

                JSONArray array = object.getJSONArray("tag");

                for (int j = 0; j < array.length(); j++) {
                    JSONObject tag = array.getJSONObject(j);

                    TableRow tableRow = new TableRow(this);

                    TextView name = new TextView(this);
                    TextView value = new TextView(this);

                    name.setText("    " + tag.keys().next());
                    value.setText(tag.getString(tag.keys().next()));

                    name.setTextSize(20);
                    value.setTextSize(20);

                    name.setWidth(600);
                    value.setWidth(300);

                    tableRow.addView(name);
                    tableRow.addView(value);

                    table.addView(tableRow);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

