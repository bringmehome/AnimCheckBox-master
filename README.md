### 给几个参考的，可以弹窗

### 画图界面,包含打勾和叉叉
```java
package com.kingdomcares.Dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kingdomcares.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DrawSuccessView extends View {

    private boolean isSuccess = false;

    // 绘制圆弧的最大进度
    private int mProgress= 0;

    //绘制圆弧的进度值
    private int progress = 0;

    // 控制画线的速度
    int fast = 3;

    //打勾的起点
    int checkStartX;

    //线1的x轴增量
    private int line1X = 0;

    //线1的y轴增量
    private int line1Y = 0;

    //线2的x轴增量
    private int line2X = 0;

    //线2的y轴增量
    private int line2Y = 0;

    int step = 2;

    //线的宽度
    private int lineThick = 4;

    //获取圆心的x坐标
    int center;

    //圆弧半径
    int radius;

    //定义的圆弧的形状和大小的界限
    RectF rectF;

    Paint paint;

    //控件大小
    float totalWidth;

    //线水平最大增量
    int maxLineIncrement;
    //打叉的起点
    int line1StartX;
    int line2StartX;
    int lineStartY;

    boolean secLineInited = false;

    public DrawSuccessView(Context context) {
        super(context);
        //init();
    }

    public DrawSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Pattern p = Pattern.compile("\\d*");

        Matcher m = p.matcher(attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width"));

        if (m.find()) {
            totalWidth = Float.valueOf(m.group());
        }

        totalWidth = DisplayUtils.dp2px(context, totalWidth);
        maxLineIncrement = (int) (totalWidth * 2 / 5);
        init();
    }

    public DrawSuccessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //init();
    }

    void init() {
        paint = new Paint();

        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.login_btn));

        //设置圆弧的宽度
        paint.setStrokeWidth(lineThick);

        //设置圆弧为空心
        paint.setStyle(Paint.Style.STROKE);

        //消除锯齿
        paint.setAntiAlias(true);

        //获取圆心的x坐标
        center = (int) (totalWidth / 2);

        //圆弧半径
        radius = (int) (totalWidth / 2) - lineThick;

        checkStartX = (int) (center - totalWidth / 5);

        //打叉的起点
        line1StartX = (int) (center + totalWidth / 5);
        lineStartY = (int) (center - totalWidth / 5);
        line2StartX = (int) (center - totalWidth / 5);

        rectF = new RectF(center - radius,
                center - radius,
                center + radius,
                center + radius);
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (progress < 100 && progress <= mProgress)
            progress += step;

        //根据进度画圆弧
        canvas.drawArc(rectF, 235, -360 * progress / 100, false, paint);

        //先等圆弧画完，画对勾
        if (progress >= 100) {

            // 划勾
            if(isSuccess) {
                if (line1X < radius / 3) {
                    line1X += step + fast;
                    line1Y += step + fast;
                }

                //画第一根线
                canvas.drawLine(checkStartX, center, checkStartX + line1X, center + line1Y, paint);

                if (line1X >= radius / 3) {

                    if (!secLineInited) {
                        line2X = line1X;
                        line2Y = line1Y;

                        secLineInited = true;
                    }

                    line2X += step + fast;
                    line2Y -= step + fast;

                    //画第二根线
                    canvas.drawLine(checkStartX + line1X - lineThick / 2, center + line1Y, checkStartX + line2X, center + line2Y, paint);
                }
            }
            // 划叉
            else{
                if (line1X < maxLineIncrement) {
                    line1X += step + fast;
                    line1Y += step + fast;
                }

                //画第一根线
                canvas.drawLine(line1StartX, lineStartY, line1StartX - line1X, lineStartY + line1Y, paint);

                if (line1X >= maxLineIncrement) {

                    line2X += step + fast;
                    line2Y += step + fast;

                    //画第二根线
                    canvas.drawLine(line2StartX, lineStartY, line2StartX + line2X, lineStartY + line2Y, paint);

                    Log.d("---canvas---", line2StartX+"/"+lineStartY+"/"+(line2StartX+line2X)+"/"+(lineStartY + line2Y));
                }
            }
        }

        //每隔6毫秒界面刷新
        if (line2X <= maxLineIncrement)
            postInvalidateDelayed(10);
    }

    /**
     * 设置画到什么程度
     * @param progress
     */
    public void setProgress(int progress){
        this.mProgress = progress;
    }

    /**
     * 画对勾还是叉叉
     * @param isSuccess
     */
    public void setFigure(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

}
```

### 调用界面
```java
package com.kingdomcares.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.kingdomcares.R;

/**
 * Created by SIN on 17/7/23.
 * 控制画圈圈的进度
 *
    new Thread(new Runnable() {

        @Override
        public void run() {
            for(int i = 0; i<101;i++){
                try {
                    sb.setPro(i);
                    Thread.sleep(30);//每一秒输出一次

                    if(i == 100){
                        Thread.sleep(1000);
                        builder.dismiss();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }).start();

 -------------------

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             sb.setPercent(100);

             sb.setFigure(false);
             builder.show();
             try{
                 Thread.sleep(1800);
                 builder.dismiss();
             }catch (Exception e){

             }
         }
     }, 1000);
 *
 */

public class SuccessDialog extends Dialog {


    public SuccessDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        View layout;
        DrawSuccessView ds;
        boolean mIsSuccess = false;
        boolean _DISMISS = false;

        SuccessDialog mDialog;

        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public SuccessDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SuccessDialog dialog = new SuccessDialog(context, R.style.Dialog);
            layout = inflater.inflate(R.layout.success_view_circle, null);

            dialog.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            /*
             * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
             * 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER);

            /*
             * lp.x与lp.y表示相对于原始位置的偏移.
             * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
             * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
             * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
             * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
             * 当参数值包含Gravity.CENTER_HORIZONTAL时
             * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
             * 当参数值包含Gravity.CENTER_VERTICAL时
             * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
             * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
             * Gravity.CENTER_VERTICAL.
             *
             * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
             * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
             * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
             */
//            lp.x = 100; // 新位置X坐标
//            lp.y = 100; // 新位置Y坐标
//            lp.width = 300; // 宽度
//            lp.height = 300; // 高度
//            lp.alpha = 0.7f; // 透明度

            // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
            // dialog.onWindowAttributesChanged(lp);
            dialogWindow.setAttributes(lp);

            ds = (DrawSuccessView) layout.findViewById(R.id.drawsuccessid);

            init();

            mDialog = dialog;

            return dialog;
        }

        private void init(){
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for(int i = 0; i<101;i++){
                        try {
                            setPercent(i);
                            Thread.sleep(38);//每一秒输出一次

                            if(i == 100 || _DISMISS){
                                ds.setFigure(mIsSuccess);
                                i = 100;
                                setPercent(i);
                                Thread.sleep(1800);
                                mDialog.dismiss();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }

        /**
         * 设置图的绘制速度
         * @param progress
         */
        public void setPercent(int progress){
            ds.setProgress(progress);
        }

        public void setFigure(boolean isSuccess){
            this._DISMISS = true;
            this.mIsSuccess = isSuccess;
        }

    }

}


```