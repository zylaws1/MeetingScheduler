package com.example.xinshen.comp2100_meetingschedule.main;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
//import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.example.xinshen.comp2100_meetingschedule.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 课表显示View
 *
 * @author shallcheek
 */
public class MeetingSchedulerView extends LinearLayout {
    /**
     * 配色数组
     */
    public static int colors[] = {R.drawable.select_label_san,
            R.drawable.select_label_er, R.drawable.select_label_si,
            R.drawable.select_label_wu, R.drawable.select_label_liu,
            R.drawable.select_label_qi, R.drawable.select_label_ba,
            R.drawable.select_label_jiu, R.drawable.select_label_sss,
            R.drawable.select_label_se, R.drawable.select_label_yiw,
            R.drawable.select_label_sy, R.drawable.select_label_yiwu,
            R.drawable.select_label_yi, R.drawable.select_label_wuw};
    private final static int START = 0;
    //最大节数
    public final static int MAX_NUM_IN_DAY = 15;
    //显示到星期几
    public final static int WEEKNUM = 7;
    /**
     * 单个View高度
     */
    private final static int TIME_TABLE_HEIGHT = 50;
    /**
     * 线的高度
     */
    private final static int TIME_TABLE_LINE_HEIGHT = 2;
    private final static int LEFT_TITLE_WIDTH = 20;
    private final static int WEEK_TITLE_HEIGHT = 30;
    /**
     * 第一行的星期显示
     */
    private LinearLayout mHorizontalWeekLayout;
    private LinearLayout mVerticalWeekLaout;
    private String[] mWeekTitle = {"Mon", "Tues", "Wedn", "Thur", "Fri", "Sat", "Sun"};
    public static String[] colorStr = new String[50];
    int colorNum = 0;
    private List<MeetingModel> mListTimeTable = new ArrayList<>();

    private Context mContext;

    public MeetingSchedulerView(Context context) {
        super(context);
    }

    public MeetingSchedulerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initView();
        invalidate();
    }

    /**
     * 横的分界线
     *
     * @return
     */
    private View getWeekHorizontalLine() {
        View line = new View(getContext());
        line.setBackgroundColor(getResources().getColor(R.color.view_line));
        line.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, TIME_TABLE_LINE_HEIGHT));
        return line;
    }

    /**
     * 竖向分界线
     *
     * @return
     */
    private View getWeekVerticalLine() {
        View line = new View(mContext);
        line.setBackgroundColor(getResources().getColor(R.color.view_line));
        line.setLayoutParams(new ViewGroup.LayoutParams((TIME_TABLE_LINE_HEIGHT), dip2px(WEEK_TITLE_HEIGHT)));
        return line;
    }


    private void initView() {
        removeAllViews();
        mHorizontalWeekLayout = new LinearLayout(getContext());
        mHorizontalWeekLayout.setOrientation(HORIZONTAL);
        mVerticalWeekLaout = new LinearLayout(getContext());
        mVerticalWeekLaout.setOrientation(HORIZONTAL);

        //表格
        for (int i = 0; i <= WEEKNUM; i++) {
            if (i == 0) {
                layoutLeftNumber();
            } else {
                layoutWeekTitleView(i);
                layoutContentView(i);
            }

            mVerticalWeekLaout.addView(createTableVerticalLine());
            mHorizontalWeekLayout.addView(getWeekVerticalLine());
        }
        addView(mHorizontalWeekLayout);
        addView(getWeekHorizontalLine());
        addView(mVerticalWeekLaout);
    }


    private View createTableVerticalLine() {
        View l = new View(getContext());
        l.setLayoutParams(new ViewGroup.LayoutParams(TIME_TABLE_LINE_HEIGHT, dip2px(TIME_TABLE_HEIGHT * MAX_NUM_IN_DAY) + (MAX_NUM_IN_DAY - 2) * TIME_TABLE_LINE_HEIGHT));
        l.setBackgroundColor(getResources().getColor(R.color.view_line));
        return l;
    }

    private void layoutContentView(int week) {
        List<MeetingModel> weekClassList = findWeekClassList(week);
        //添加
        LinearLayout mLayout = createWeekTimeTableView(weekClassList, week);
        mLayout.setOrientation(VERTICAL);
        mLayout.setLayoutParams(new ViewGroup.LayoutParams((getViewWidth() - dip2px(20)) / WEEKNUM, LayoutParams.MATCH_PARENT));
        mLayout.setWeightSum(1);
        mVerticalWeekLaout.addView(mLayout);
    }

    /**
     * 遍历出星期1~7的课表
     * 再进行排序
     *
     * @param week 星期
     */

    private List<MeetingModel> findWeekClassList(int week) {
        List<MeetingModel> list = new ArrayList<>();
        for (MeetingModel meetingModel : mListTimeTable) {
            if (meetingModel.getDay() == week) {
                list.add(meetingModel);
            }
        }

        Collections.sort(list, new Comparator<MeetingModel>() {
            @Override
            public int compare(MeetingModel o1, MeetingModel o2) {
                return o1.getStar_hour() - o2.getStar_hour();
            }
        });

        return list;
    }

    private void layoutWeekTitleView(int weekNumber) {
        TextView weekText = new TextView(getContext());
        weekText.setTextColor(getResources().getColor(R.color.schedule_text_color));
        weekText.setWidth(((getViewWidth() - dip2px(LEFT_TITLE_WIDTH))) / WEEKNUM);
        weekText.setHeight(dip2px(WEEK_TITLE_HEIGHT));
        weekText.setGravity(Gravity.CENTER);
        weekText.setTextSize(16);
        weekText.setText(mWeekTitle[weekNumber - 1]);
        mHorizontalWeekLayout.addView(weekText);
    }


    private void layoutLeftNumber() {
        //课表出的0,0格子 空白的
        TextView mTime = new TextView(mContext);
        mTime.setLayoutParams(new ViewGroup.LayoutParams(dip2px(LEFT_TITLE_WIDTH), dip2px(WEEK_TITLE_HEIGHT)));
        mHorizontalWeekLayout.addView(mTime);

        //绘制1~MAXNUM
        LinearLayout numberView = new LinearLayout(mContext);
        numberView.setLayoutParams(new ViewGroup.LayoutParams(dip2px(LEFT_TITLE_WIDTH), dip2px(MAX_NUM_IN_DAY * TIME_TABLE_HEIGHT) + MAX_NUM_IN_DAY * 2));
        numberView.setOrientation(VERTICAL);
        for (int j = 1; j <= MAX_NUM_IN_DAY; j++) {
            TextView number = createNumberView(j);
            numberView.addView(number);
            numberView.addView(getWeekHorizontalLine());
        }
        mVerticalWeekLaout.addView(numberView);
    }


    private TextView createNumberView(int j) {
        TextView number = new TextView(getContext());
        number.setLayoutParams(new ViewGroup.LayoutParams(dip2px(LEFT_TITLE_WIDTH), dip2px(TIME_TABLE_HEIGHT)));
        number.setGravity(Gravity.CENTER);
        number.setTextColor(getResources().getColor(R.color.schedule_text_color));
        number.setTextSize(14);
        number.setText(String.valueOf(j + 7) + ":00");
        return number;
    }

    private int getViewWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 绘制空白
     *
     * @param count 数量
     * @param week  星期
     * @param start 用着计算下标
     */
    private View addBlankView(int count, final int week, final int start) {
        LinearLayout blank = new LinearLayout(getContext());
        blank.setOrientation(VERTICAL);
        for (int i = 1; i < count; i++) {
            final View classView = new View(getContext());
            classView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(TIME_TABLE_HEIGHT)));
            blank.addView(classView);
            blank.addView(getWeekHorizontalLine());
            final int num = i;
            //这里可以处理空白处点击添加课表
            classView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "No meeting at the moment", Toast.LENGTH_LONG).show();
                }
            });
            classView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog alert_add = new AlertDialog.Builder(getContext())
                            .setTitle("Add a new meeting?")
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                                    transaction.replace(R.id.main_linear, MainActivity.addNewMeetingFragment);
                                    transaction.commit();
                                }
                            })

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            })
                            .create();
                    alert_add.show();
                    return true;
                }
            });

        }
        return blank;
    }

    /**
     * 星期一到星期天的课表
     *
     * @param weekList 每天的课程列表
     * @param week     周
     */
    private LinearLayout createWeekTimeTableView(List<MeetingModel> weekList, int week) {
        LinearLayout weekTableView = new LinearLayout(getContext());
        weekTableView.setOrientation(VERTICAL);
        int size = weekList.size();
        if (weekList.isEmpty()) {
            weekTableView.addView(addBlankView(MAX_NUM_IN_DAY + 1, week, 0));
        } else {
            for (int i = 0; i < size; i++) {
                MeetingModel tableModel = weekList.get(i);
                if (i == 0) {
                    //添加的0到开始节数的空格
                    weekTableView.addView(addBlankView(tableModel.getStar_hour() - 7, week, 0));
                    weekTableView.addView(createClassView(tableModel));
                } else if (weekList.get(i).getStar_hour() - 7 - (weekList.get(i - 1).getEnd_time() - 7) > 0) {
                    //填充
                    weekTableView.addView(addBlankView(weekList.get(i).getStar_hour() - 7 - (weekList.get(i - 1).getEnd_time() - 7), week, weekList.get(i - 1).getEnd_time() - 7));
                    weekTableView.addView(createClassView(weekList.get(i)));
                }
                //绘制剩下的空白
                if (i + 1 == size) {
                    weekTableView.addView(addBlankView(MAX_NUM_IN_DAY - (weekList.get(i).getEnd_time() - 7) + 1, week, weekList.get(i).getEnd_time() - 7));
                }
            }
        }
        return weekTableView;
    }

    /**
     * 获取单个课表View 也可以自定义我这个
     *
     * @param model 数据类型
     * @return
     */
    @SuppressWarnings("deprecation")
    private View createClassView(final MeetingModel model) {
        LinearLayout mTimeTableView = new LinearLayout(getContext());
        mTimeTableView.setOrientation(VERTICAL);
        int num = (model.getEnd_time() - model.getStar_hour());
        mTimeTableView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px((num + 1) * TIME_TABLE_HEIGHT) + (num + 1) * TIME_TABLE_LINE_HEIGHT));

        TextView mTimeTableNameView = new TextView(getContext());
        mTimeTableNameView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px((num + 1) * TIME_TABLE_HEIGHT) + (num) * TIME_TABLE_LINE_HEIGHT));

        mTimeTableNameView.setTextColor(getContext().getResources().getColor(
                android.R.color.white));
        mTimeTableNameView.setTextSize(15);
        mTimeTableNameView.setGravity(Gravity.CENTER);
        mTimeTableNameView.setText(model.getName() + "(" + model.getRoom() + ")");

        mTimeTableView.addView(mTimeTableNameView);
        mTimeTableView.addView(getWeekHorizontalLine());

        mTimeTableView.setBackgroundDrawable(getContext().getResources()
                .getDrawable(colors[getColorNum(model.getName())]));
        mTimeTableView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), model.getName() + " from " + model.getStart_time_str() + " at "
                        + model.getRoom() + "," + model.getVenue(), Toast.LENGTH_LONG).show();
            }
        });
        mTimeTableView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.setmTitleBarInactive();
                MainActivity.meetingInfoFragment.setMeetingModel(model);
                FragmentTransaction transaction = MainActivity.mFraManager.beginTransaction();
                transaction.replace(R.id.main_linear, MainActivity.meetingInfoFragment);
                transaction.commit();
                //Toast.makeText(getContext(), "long clicked class", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return mTimeTableView;
    }

    /**
     * 转换dp
     *
     * @param dpValue
     * @return
     */
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public void setTimeTable(List<MeetingModel> mlist) {
        this.mListTimeTable = mlist;
        for (MeetingModel meetingModel : mlist) {
            addTimeName(meetingModel.getName());
        }
        initView();
        invalidate();
    }

    public void refreshTable() {
        setTimeTable(mListTimeTable);
    }

    /**
     * 输入课表名循环判断是否数组存在该课表 如果存在输出true并退出循环 如果不存在则存入colorSt[20]数组
     *
     * @param name
     */
    private void addTimeName(String name) {
        boolean isRepeat = true;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                isRepeat = true;
                break;
            } else {
                isRepeat = false;
            }
        }
        if (!isRepeat) {
            colorStr[colorNum] = name;
            colorNum++;
        }
    }


    /**
     * 获取数组中的课程名
     *
     * @param name
     * @return
     */
    public static int getColorNum(String name) {
        int num = 0;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                num = i;
            }
        }
        return num;
    }
}
