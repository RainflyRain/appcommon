package com.friean.appcommon.common;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author friean
 */
@SuppressWarnings("unused")
public class Tools {
    public static final int RADIUS = 10;
    public static final int STROKE = 5;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean isValidContext (Context c){
        Activity a = (Activity)c;
        return !a.isFinishing();
    }

    /**
     * 获取箭头图片
     *
     * @param color 色值
     */
    public static Bitmap getRow(int color) {
        int scale = RADIUS / 10;
        Bitmap output = Bitmap.createBitmap(RADIUS * 2, RADIUS * 2,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        // 画直线
        Path linePath = new Path();
        linePath.moveTo(0, RADIUS);
        linePath.lineTo(2 * RADIUS, RADIUS);
        linePath.close();
        canvas.drawPath(linePath, paint);
        // 画箭头
        // paint.setStyle(Style.FILL);
        Path rowPath = new Path();
        rowPath.moveTo(2 * RADIUS - 6 * scale, RADIUS - 6 * scale);
        rowPath.lineTo(2 * RADIUS, RADIUS);
        rowPath.lineTo(2 * RADIUS - 6 * scale, RADIUS + 6 * scale);
        // rowPath.close();
        canvas.drawPath(rowPath, paint);
        return output;
    }

    /**
     * 把图片转换成圆形
     * <p/>
     * param bitmapimg bitmap参数
     * return 返回值
     */
    public static void transforCircleBitmap(Bitmap bitmap, ImageView imageView) {
        if (bitmap == null)
            return;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 取小的
        int radius = width > height ? height / 2 : width / 2;
        Bitmap output = Bitmap.createBitmap(radius * 2, radius * 2,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        int left = 0, top = 0;
        int right = width, bottom = height;
        if (width > height) {
            left = width / 2 - radius;
            right = width / 2 + radius;
        } else if (width < height) {
            top = height / 2 - radius;
            bottom = height / 2 + radius;
        }
        Rect src = new Rect(left, top, right, bottom);// 截取原始图片的地方
        Rect dst = new Rect(0, 0, 2 * radius, 2 * radius);

        paint.setAntiAlias(true);
        canvas.drawCircle(radius, radius, radius - STROKE, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, src, dst, paint);

        setCircle(imageView, output, canvas, radius);
    }

    /**
     * 画边框
     * <p/>
     * param width
     * param height
     * return
     */
    private static void setCircle(ImageView imageView, Bitmap output,
                                  Canvas canvas, int radius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE);
        paint.setColor(Color.parseColor("#FFCCCCCC"));
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        canvas.drawCircle(radius, radius, radius - STROKE, paint);
        imageView.setImageBitmap(output);
    }


    /**
     * 验证手机格式
     *
     * @param phone 手机号码
     * @return 返回值
     */
    public static boolean phoneFormat(String phone) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(phone) && phone.matches(telRegex);
    }

    /**
     * drawable 转bitmap
     */
    public static Bitmap drawbleToBitmap(Drawable drawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * 输入字符中是否只包含英文和数字
     *
     * @param str 输入字符
     * @return 返回值
     */
    public static boolean strFormat(String str) {
        String regex = "^[A-Za-z0-9]+$";
        return str.matches(regex);
    }

    /**
     * 字符串长度大于num
     *
     * @param str 字符串
     * @param num 数量
     * @return true or false
     */
    public static boolean strNum(String str, int num) {
        return str.length() >= num;
    }

    /**
     * 验证码格式校验
     *
     * @param str 字符串
     * @param num 个数
     * @return 返回值
     */
    public static boolean strCode(String str, int num) {
        if (str.length() == num) {
            String regex = "^[0-9]+$";
            return str.matches(regex);
        }
        return false;
    }

    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email 用户输入的邮箱地址
     * @return 是否合法
     */

    public static boolean emailFormat(String email){
        boolean flag;
        try{
            String check = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }


    /**
     * 从raw 目录下获取字符串
     *
     * @param context 上下文
     * @param r       对应文件
     * @return 字符串
     */
    public static String readRawFile(Context context, int r) {
        InputStream inputStream = context.getResources().openRawResource(r);
        String result = null;
        try {
            result = URLDecoder.decode(getString(inputStream), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从流中获取字符串
     *
     * @param inputStream 输入流
     * @return 字符串
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        assert inputStreamReader != null;
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从Assets目录获取String数据
     *
     * @param context        上下文
     * @param assetsFileName 文件名
     * @return 返回字符串
     */
    @SuppressWarnings("unused")
    private String readAssetsFile(Context context, String assetsFileName) {
        String rtnStr;
        try {
            InputStream is = context.getAssets().open(assetsFileName);
            rtnStr = getString(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return rtnStr;
    }

    /**
     * 只显示年月的日期选择器
     * @return DatePickerDialog
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static DatePickerDialog createDialogWithoutDateField(Context context,DatePickerDialog.OnDateSetListener listener,int year,int month) {
        DatePickerDialog dpd = new DatePickerDialog(context, listener, year, month-1, 1);
        dpd.getDatePicker().setMaxDate(new Date().getTime());
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return dpd;
    }



    /**
     *限制edit的最大长度
     * @param context context
     * @param string edit的内容
     * @param maxLength 最大长度
     * @param edit edit
     * @param name toast内容
     * @return true or false
     */
   public static boolean editLength(Context context,String string,int maxLength,EditText edit,String name){
       if (string.length() > maxLength) {
           Toast.makeText(context,name+"字数不能超过"+maxLength,Toast.LENGTH_SHORT).show();
           String str = string.substring(0, maxLength);
           edit.setText(str);
           edit.requestFocus();
           edit.setSelection(str.length());
       } else {
           int length = edit.getText().toString().trim().length();
       }
       return true;
   }

    /**
     * 以最省内存的方式读取本地资源的图片
     */
    @SuppressWarnings("deprecation")
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     *系统当前时间	目标时间
     *
     * 如果返回1 当前时间 > 指定时间
     * 如果返回-1 当前时间 < 指定时间
     */
    public static int compare_date(String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 =  df.parse(df.format(new Date()));
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

}
