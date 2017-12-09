//package com.example.junhaozeng.testdesign.Activity;
//
//import android.content.Context;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.hardware.Camera;
//import android.os.Handler;
//import android.os.Message;
//import android.os.PowerManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.junhaozeng.testdesign.R;
//import com.example.junhaozeng.testdesign.Utils.ImageProcessing;
//import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//
//import java.io.IOException;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class HeartbeatActivity extends AppCompatActivity {
//    //曲线
//    private Timer timer = new Timer();
//    //Timer任务，与Timer配套使用
//    private TimerTask task;
//    private static int gx;
//    private static int j;
//    private static LineChart chart;
//    private static Integer i=0;
//    private static LineData data;
//    private static SharedPreferencesUtils sharedPreferencesUtils;
//
//
//    private static double flag=1;
//    private Handler handler;
//    private String title = "pulse";
//    private Context context;
//
//
//    //	private static final String TAG = "HeartRateMonitor";
//    private static final AtomicBoolean processing = new AtomicBoolean(false);
//    //Android手机预览控件
//    private static SurfaceView preview = null;
//    //预览设置信息
//    private static SurfaceHolder previewHolder = null;
//    //Android手机相机句柄
//    private static Camera camera = null;
//    private static Camera.Parameters parameters;
//    //private static View image = null;
//    private static TextView text = null;
//    private static TextView text1 = null;
//    private static TextView text2 = null;
//    private static Button startButton;
//    private static Button stopButton;
//    //private static PowerManager.WakeLock wakeLock = null;
//    private static int averageIndex = 0;
//    private static final int averageArraySize = 4;
//    private static final int[] averageArray = new int[averageArraySize];
//    private static int[] averagepref = new int [25];
//    private static int count =0;
//
//
//    public static enum TYPE {
//        GREEN, RED
//    };
//    //设置默认类型
//    private static TYPE currentType = TYPE.GREEN;
//    //获取当前类型
//    public static TYPE getCurrent() {
//        return currentType;
//    }
//    //心跳下标值
//    private static int beatsIndex = 0;
//    //心跳数组的大小
//    private static final int beatsArraySize = 3;
//    //心跳数组
//    private static final int[] beatsArray = new int[beatsArraySize];
//    //心跳脉冲
//    private static double beats = 0;
//    //开始时间
//    private static long startTime = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        sharedPreferencesUtils = new SharedPreferencesUtils(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_heartbeat);
//        initChart();
//        initConfig();
//    }
//
//    /**
//     * 初始化配置
//     */
//    private void initConfig() {
//        count=0;
//        //获取SurfaceView控件
//        preview = (SurfaceView) findViewById(R.id.camera_view);
//        previewHolder = preview.getHolder();
//        previewHolder.addCallback(surfaceCallback);
//        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        camera = Camera.open();
//        text = (TextView) findViewById(R.id.tv_heartbeat);
//        text1 = (TextView) findViewById(R.id.tv_pixel);
//        text2 = (TextView) findViewById(R.id.tv_pulse);
//        startButton = (Button) findViewById(R.id.heartstart);
//        startButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        stopButton = (Button) findViewById(R.id.heartstop);
//        stopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stop();
//            }
//        });
//        stopButton.setEnabled(false);
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        startTime = System.currentTimeMillis();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        camera.setPreviewCallback(null);
//        camera.stopPreview();
//        camera.release();
//        camera = null;
//        chart.clearValues();
//        i = 0;
//    }
//
//    /**
//     * 相机预览方法
//     * 这个方法中实现动态更新界面UI的功能，
//     * 通过获取手机摄像头的参数来实时动态计算平均像素值、脉冲数，从而实时动态计算心率值。
//     */
//    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
//        public void onPreviewFrame(byte[] data, Camera cam) {
//            if (data == null)
//                throw new NullPointerException();
//            Camera.Size size = cam.getParameters().getPreviewSize();
//            if (size == null)
//                throw new NullPointerException();
//            if (!processing.compareAndSet(false, true))
//                return;
//            int width = size.width;
//            int height = size.height;
//            //图像处理
//            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
//            gx = imgAvg;
//            text1.setText("average pixels: "+String.valueOf(imgAvg));
//            //像素平均值imgAvg,日志
//            //Log.i(TAG, "imgAvg=" + imgAvg);
//            if (imgAvg == 0 || imgAvg == 255) {
//                processing.set(false);
//                return;
//            }
//            else{
//                addEntry(i,imgAvg);
//                i++;
//            }
//            //计算平均值
//            int averageArrayAvg = 0;
//            int averageArrayCnt = 0;
//            for (int i = 0; i < averageArray.length; i++) {
//                if (averageArray[i] > 0) {
//                    averageArrayAvg += averageArray[i];
//                    averageArrayCnt++;
//                }
//            }
//            //计算平均值
//            int rollingAverage = (averageArrayCnt > 0)?(averageArrayAvg/averageArrayCnt):0;
//            TYPE newType = currentType;
//            if (imgAvg < rollingAverage) {
//                newType = TYPE.RED;
//                if (newType != currentType) {
//                    beats++;
//                    flag=0;
//                    text2.setText("number of pulses: "+String.valueOf(beats));
//                    //Log.e(TAG, "BEAT!! beats=" + beats);
//                }
//            } else if (imgAvg > rollingAverage) {
//                newType = TYPE.GREEN;
//            }
//            if (averageIndex == averageArraySize)
//                averageIndex = 0;
//            averageArray[averageIndex] = imgAvg;
//            averageIndex++;
//            // Transitioned from one state to another to the same
//            if (newType != currentType) {
//                currentType = newType;
//            }
//            //获取系统结束时间（ms）
//            long endTime = System.currentTimeMillis();
//            double totalTimeInSecs = (endTime - startTime) / 1000d;
//            if (totalTimeInSecs >= 2) {
//                double bps = (beats / totalTimeInSecs);
//                int dpm = (int) (bps * 60d);
//                if (dpm < 30 || dpm > 180||imgAvg<200) {
//                    //获取系统开始时间（ms）
//                    startTime = System.currentTimeMillis();
//                    //beats心跳总数
//                    beats = 0;
//                    processing.set(false);
//                    return;
//                }
//                //Log.e(TAG, "totalTimeInSecs=" + totalTimeInSecs + " beats="+ beats);
//                if (beatsIndex == beatsArraySize)
//                    beatsIndex = 0;
//                beatsArray[beatsIndex] = dpm;
//                beatsIndex++;
//                int beatsArrayAvg = 0;
//                int beatsArrayCnt = 0;
//                for (int i = 0; i < beatsArray.length; i++) {
//                    if (beatsArray[i] > 0) {
//                        beatsArrayAvg += beatsArray[i];
//                        beatsArrayCnt++;
//                    }
//                }
//                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
//                text.setText("Heartbeat rate: "+String.valueOf(beatsAvg)+"  zhi:"+String.valueOf(beatsArray.length)
//                        +"    "+String.valueOf(beatsIndex)+"    "+String.valueOf(beatsArrayAvg)+"    "+String.valueOf(beatsArrayCnt));
//                //获取系统时间（ms）
//                startTime = System.currentTimeMillis();
//                beats = 0;
//                if(count<averagepref.length){
//                    averagepref [count]= beatsAvg;
//                    count++;
//                }
//                else{
//                    int averagesum=0;
//                    for (int i =0;i<averagepref.length;i++){
//                        averagesum= averagesum+averagepref[i];
//                    }
//                    int secondAverage;
//                    secondAverage=averagesum/averagepref.length;
//                    sharedPreferencesUtils.setParam("heartbeat",secondAverage);
//                    System.exit(0);
//                }
//            }
//            processing.set(false);
//        }
//    };
//
//    /**
//     * 预览回调接口
//     */
//    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
//        //创建时调用
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            try {
//                camera.setPreviewDisplay(previewHolder);
//                camera.setPreviewCallback(previewCallback);
//            } catch (Throwable t) {
//                // Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
//            }
//        }
//        //当预览改变的时候回调此方法
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
//            parameters = camera.getParameters();
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
//            if (size != null) {
//                parameters.setPreviewSize(size.width, size.height);
//                // Log.d(TAG, "Using width=" + size.width + " height="	+ size.height);
//            }
//            camera.setParameters(parameters);
//        }
//        //销毁的时候调用
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            // Ignore
//        }
//    };
//
//    /**
//     * 获取相机最小的预览尺寸
//     * @param width
//     * @param height
//     * @param parameters
//     * @return
//     */
//    private static Camera.Size getSmallestPreviewSize(int width, int height,
//                                                      Camera.Parameters parameters) {
//        Camera.Size result = null;
//        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
//            if (size.width <= width && size.height <= height) {
//                if (result == null) {
//                    result = size;
//                } else {
//                    int resultArea = result.width * result.height;
//                    int newArea = size.width * size.height;
//                    if (newArea < resultArea)
//                        result = size;
//                }
//            }
//        }
//        return result;
//    }
//
//    private void initChart(){
//        chart = (LineChart) findViewById(R.id.chart);
//        chart.setData(new LineData());
//    }
//
//    private static void addEntry(int x,int y){
//        data = chart.getData();
//        ILineDataSet set = data.getDataSetByIndex(0);
//        if(set == null){
//            set = createSet();
//            data.addDataSet(set);
//        }
//        set.addEntry(new Entry(x,y));
//        data.notifyDataChanged();
//        chart.notifyDataSetChanged();
//
//        chart.setVisibleXRangeMaximum(40); //change number later
//
//        chart.moveViewToX(data.getEntryCount() - 40); //change number later
//
//
//    }
//
//    private static LineDataSet createSet() {
//
//        LineDataSet set = new LineDataSet(null, "DataSet 1");
//        YAxis leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
//        YAxis rightAxis = chart.getAxis(YAxis.AxisDependency.RIGHT);
//        set.setLineWidth(2.5f);
//        set.setDrawCircles(false);
//        set.setHighLightColor(Color.rgb(190, 190, 190));
//        set.setDrawValues(false);
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        rightAxis.setEnabled(false);
//        leftAxis.setAxisMinimum(245f);
//        leftAxis.setAxisMaximum(255f);
//
//        set.setValueTextSize(10f);
//        return set;
//    }
//
//    private void start() throws IOException {
//
//        i = 0;
//        chart.clearValues();
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        camera.setParameters(parameters);
//        camera.startPreview();
//        startButton.setEnabled(false);
//        stopButton.setEnabled(true);
//
//    }
//
//    private static void stop(){
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//        camera.setParameters(parameters);
//        camera.stopPreview();
//        stopButton.setEnabled(false);
//        startButton.setEnabled(true);
//    }
//}
//
////public class HeartbeatActivity extends AppCompatActivity {
////    //曲线
////    private Timer timer = new Timer();
////    //Timer任务，与Timer配套使用
////    private TimerTask task;
////    private static int gx;
////    private static int j;
////
////    private static double flag=1;
////    private Handler handler;
////    private String title = "pulse";
////    // private XYSeries series;
////    // private XYMultipleSeriesDataset mDataset;
////    // private GraphicalView chart;
////    // private XYMultipleSeriesRenderer renderer;
////    private Context context;
////    private int addX = -1;
////    double addY;
////    int[] xv = new int[300];
////    int[] yv = new int[300];
////    int[] hua=new int[]{9,10,11,12,13,14,13,12,11,10,9,8,7,6,7,8,9,10,11,10,10};
////
////    //	private static final String TAG = "HeartRateMonitor";
////    private static final AtomicBoolean processing = new AtomicBoolean(false);
////    //Android手机预览控件
////    private static SurfaceView preview = null;
////    //预览设置信息
////    private static SurfaceHolder previewHolder = null;
////    //Android手机相机句柄
////    private static Camera camera = null;
////    //private static View image = null;
////    private static TextView text = null;
////    private static TextView text1 = null;
////    private static TextView text2 = null;
////    private static PowerManager.WakeLock wakeLock = null;
////    private static int averageIndex = 0;
////    private static final int averageArraySize = 4;
////    private static final int[] averageArray = new int[averageArraySize];
////
////    /**
////     * 类型枚举
////     * @author liuyazhuang
////     *
////     */
////    public static enum TYPE {
////        GREEN, RED
////    };
////    //设置默认类型
////    private static TYPE currentType = TYPE.GREEN;
////    //获取当前类型
////    public static TYPE getCurrent() {
////        return currentType;
////    }
////    //心跳下标值
////    private static int beatsIndex = 0;
////    //心跳数组的大小
////    private static final int beatsArraySize = 3;
////    //心跳数组
////    private static final int[] beatsArray = new int[beatsArraySize];
////    //心跳脉冲
////    private static double beats = 0;
////    //开始时间
////    private static long startTime = 0;
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_heartbeat);
////        initConfig();
////    }
////
////    /**
////     * 初始化配置
////     */
////    private void initConfig() {
////        //曲线
////        // context = getApplicationContext();
////
////        //这里获得main界面上的布局，下面会把图表画在这个布局里面
////        // LinearLayout layout = (LinearLayout)findViewById(R.id.pulse_graph);
////
////        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
////        // series = new XYSeries(title);
////
////        //创建一个数据集的实例，这个数据集将被用来创建图表
////        // mDataset = new XYMultipleSeriesDataset();
////
////        //将点集添加到这个数据集中
////        // mDataset.addSeries(series);
////
////        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
////        // int color = Color.GREEN;
////        // PointStyle style = PointStyle.CIRCLE;
////        // renderer = buildRenderer(color, style, true);
////
////        //设置好图表的样式
////        // setChartSettings(renderer, "X", "Y", 0, 300, 4, 16, Color.WHITE, Color.WHITE);
////
////        //生成图表
////        // chart = ChartFactory.getLineChartView(context, mDataset, renderer);
////
////        //将图表添加到布局中去
////        // layout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
////
////        //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
//////        handler = new Handler() {
//////            @Override
//////            public void handleMessage(Message msg) {
//////                //        		刷新图表
//////                updateChart();
//////                super.handleMessage(msg);
//////            }
//////        };
//////
//////        task = new TimerTask() {
//////            @Override
//////            public void run() {
//////                Message message = new Message();
//////                message.what = 1;
//////                handler.sendMessage(message);
//////            }
//////        };
////
//////        timer.schedule(task, 1,20);           //曲线
////        //获取SurfaceView控件
////        preview = (SurfaceView) findViewById(R.id.camera_view);
////        previewHolder = preview.getHolder();
////        previewHolder.addCallback(surfaceCallback);
////        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
////        //		image = findViewById(R.id.image);
////        text = (TextView) findViewById(R.id.tv_heartbeat);
////        text1 = (TextView) findViewById(R.id.tv_pixel);
////        text2 = (TextView) findViewById(R.id.tv_pulse);
////        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
////        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
////    }
////
////    //	曲线
//////    @Override
//////    public void onDestroy() {
//////        //当结束程序时关掉Timer
//////        timer.cancel();
//////        super.onDestroy();
//////    };
////
////    /**
////     * 创建图表
////     * @param color
////     * @param style
////     * @param fill
////     * @return
////     */
//////    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
//////        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//////
//////        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
//////        XYSeriesRenderer r = new XYSeriesRenderer();
//////        r.setColor(Color.RED);
////////		r.setPointStyle(null);
////////		r.setFillPoints(fill);
//////        r.setLineWidth(1);
//////        renderer.addSeriesRenderer(r);
//////        return renderer;
//////    }
////
////    /**
////     * 设置图标的样式
////     * @param renderer
////     * @param xTitle：x标题
////     * @param yTitle：y标题
////     * @param xMin：x最小长度
////     * @param xMax：x最大长度
////     * @param yMin:y最小长度
////     * @param yMax：y最大长度
////     * @param axesColor：颜色
////     * @param labelsColor：标签
////     */
//////    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
//////                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
//////        //有关对图表的渲染可参看api文档
//////        renderer.setChartTitle(title);
//////        renderer.setXTitle(xTitle);
//////        renderer.setYTitle(yTitle);
//////        renderer.setXAxisMin(xMin);
//////        renderer.setXAxisMax(xMax);
//////        renderer.setYAxisMin(yMin);
//////        renderer.setYAxisMax(yMax);
//////        renderer.setAxesColor(axesColor);
//////        renderer.setLabelsColor(labelsColor);
//////        renderer.setShowGrid(true);
//////        renderer.setGridColor(Color.GREEN);
//////        renderer.setXLabels(20);
//////        renderer.setYLabels(10);
//////        renderer.setXTitle("Time");
//////        renderer.setYTitle("mmHg");
//////        renderer.setYLabelsAlign(Paint.Align.RIGHT);
//////        renderer.setPointSize((float) 3 );
//////        renderer.setShowLegend(false);
//////    }
////
////    /**
////     * 更新图标信息
////     */
//////    private void updateChart() {
//////
//////        //设置好下一个需要增加的节点
//////        if(flag==1)
//////            addY=10;
//////        else{
////////			addY=250;
//////            flag=1;
//////            if(gx<200){
//////                if(hua[20]>1){
//////                    Toast.makeText(this, "请用您的指尖盖住摄像头镜头！", Toast.LENGTH_SHORT).show();
//////                    hua[20]=0;}
//////                hua[20]++;
//////                return;}
//////            else
//////                hua[20]=10;
//////            j=0;
//////
//////        }
//////        if(j<20){
//////            addY=hua[j];
//////            j++;
//////        }
////
////        //移除数据集中旧的点集
////        // mDataset.removeSeries(series);
////
////        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
//////        int length = series.getItemCount();
//////        int bz=0;
//////        //addX = length;
//////        if (length > 300) {
//////            length = 300;
//////            bz=1;
//////        }
//////        addX = length;
//////        //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
//////        for (int i = 0; i < length; i++) {
//////            xv[i] = (int) series.getX(i) -bz;
//////            yv[i] = (int) series.getY(i);
//////        }
//////
//////        //点集先清空，为了做成新的点集而准备
//////        series.clear();
//////        mDataset.addSeries(series);
//////        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
//////        //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
//////        series.add(addX, addY);
//////        for (int k = 0; k < length; k++) {
//////            series.add(xv[k], yv[k]);
//////        }
//////        //在数据集中添加新的点集
//////        //mDataset.addSeries(series);
//////
//////        //视图更新，没有这一步，曲线不会呈现动态
//////        //如果在非UI主线程中，需要调用postInvalidate()，具体参考api
//////        chart.invalidate();
//////    } //曲线
////
////
////    @Override
////    public void onConfigurationChanged(Configuration newConfig) {
////        super.onConfigurationChanged(newConfig);
////    }
////
////    @Override
////    public void onResume() {
////        super.onResume();
////        wakeLock.acquire();
////        camera = Camera.open();
////        startTime = System.currentTimeMillis();
////    }
////
////    @Override
////    public void onPause() {
////        super.onPause();
////        wakeLock.release();
////        camera.setPreviewCallback(null);
////        camera.stopPreview();
////        camera.release();
////        camera = null;
////    }
////
////
////    /**
////     * 相机预览方法
////     * 这个方法中实现动态更新界面UI的功能，
////     * 通过获取手机摄像头的参数来实时动态计算平均像素值、脉冲数，从而实时动态计算心率值。
////     */
////    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
////        public void onPreviewFrame(byte[] data, Camera cam) {
////            if (data == null)
////                throw new NullPointerException();
////            Camera.Size size = cam.getParameters().getPreviewSize();
////            if (size == null)
////                throw new NullPointerException();
////            if (!processing.compareAndSet(false, true))
////                return;
////            int width = size.width;
////            int height = size.height;
////            //图像处理
////            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),height,width);
////            gx=imgAvg;
////            text1.setText("average pixels: "+String.valueOf(imgAvg));
////            //像素平均值imgAvg,日志
////            //Log.i(TAG, "imgAvg=" + imgAvg);
////            if (imgAvg == 0 || imgAvg == 255) {
////                processing.set(false);
////                return;
////            }
////            //计算平均值
////            int averageArrayAvg = 0;
////            int averageArrayCnt = 0;
////            for (int i = 0; i < averageArray.length; i++) {
////                if (averageArray[i] > 0) {
////                    averageArrayAvg += averageArray[i];
////                    averageArrayCnt++;
////                }
////            }
////            //计算平均值
////            int rollingAverage = (averageArrayCnt > 0)?(averageArrayAvg/averageArrayCnt):0;
////            TYPE newType = currentType;
////            if (imgAvg < rollingAverage) {
////                newType = TYPE.RED;
////                if (newType != currentType) {
////                    beats++;
////                    flag=0;
////                    text2.setText("number of pulses: "+String.valueOf(beats));
////                    //Log.e(TAG, "BEAT!! beats=" + beats);
////                }
////            } else if (imgAvg > rollingAverage) {
////                newType = TYPE.GREEN;
////            }
////
////            if (averageIndex == averageArraySize)
////                averageIndex = 0;
////            averageArray[averageIndex] = imgAvg;
////            averageIndex++;
////
////            // Transitioned from one state to another to the same
////            if (newType != currentType) {
////                currentType = newType;
////                //image.postInvalidate();
////            }
////            //获取系统结束时间（ms）
////            long endTime = System.currentTimeMillis();
////            double totalTimeInSecs = (endTime - startTime) / 1000d;
////            if (totalTimeInSecs >= 2) {
////                double bps = (beats / totalTimeInSecs);
////                int dpm = (int) (bps * 60d);
////                if (dpm < 30 || dpm > 180||imgAvg<200) {
////                    //获取系统开始时间（ms）
////                    startTime = System.currentTimeMillis();
////                    //beats心跳总数
////                    beats = 0;
////                    processing.set(false);
////                    return;
////                }
////                //Log.e(TAG, "totalTimeInSecs=" + totalTimeInSecs + " beats="+ beats);
////                if (beatsIndex == beatsArraySize)
////                    beatsIndex = 0;
////                beatsArray[beatsIndex] = dpm;
////                beatsIndex++;
////                int beatsArrayAvg = 0;
////                int beatsArrayCnt = 0;
////                for (int i = 0; i < beatsArray.length; i++) {
////                    if (beatsArray[i] > 0) {
////                        beatsArrayAvg += beatsArray[i];
////                        beatsArrayCnt++;
////                    }
////                }
////                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
////                text.setText("Heartbeat rate: "+String.valueOf(beatsAvg)+"  zhi:"+String.valueOf(beatsArray.length)
////                        +"    "+String.valueOf(beatsIndex)+"    "+String.valueOf(beatsArrayAvg)+"    "+String.valueOf(beatsArrayCnt));
////                //获取系统时间（ms）
////                startTime = System.currentTimeMillis();
////                beats = 0;
////            }
////            processing.set(false);
////        }
////    };
////
////    /**
////     * 预览回调接口
////     */
////    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
////        //创建时调用
////        @Override
////        public void surfaceCreated(SurfaceHolder holder) {
////            try {
////                camera.setPreviewDisplay(previewHolder);
////                camera.setPreviewCallback(previewCallback);
////            } catch (Throwable t) {
////                // Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
////            }
////        }
////        //当预览改变的时候回调此方法
////        @Override
////        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
////            Camera.Parameters parameters = camera.getParameters();
////            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
////            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
////            if (size != null) {
////                parameters.setPreviewSize(size.width, size.height);
////                //				Log.d(TAG, "Using width=" + size.width + " height="	+ size.height);
////            }
////            camera.setParameters(parameters);
////            camera.startPreview();
////        }
////        //销毁的时候调用
////        @Override
////        public void surfaceDestroyed(SurfaceHolder holder) {
////            // Ignore
////        }
////    };
////
////    /**
////     * 获取相机最小的预览尺寸
////     * @param width
////     * @param height
////     * @param parameters
////     * @return
////     */
////    private static Camera.Size getSmallestPreviewSize(int width, int height,
////                                                      Camera.Parameters parameters) {
////        Camera.Size result = null;
////        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
////            if (size.width <= width && size.height <= height) {
////                if (result == null) {
////                    result = size;
////                } else {
////                    int resultArea = result.width * result.height;
////                    int newArea = size.width * size.height;
////                    if (newArea < resultArea)
////                        result = size;
////                }
////            }
////        }
////        return result;
////    }
////}
package com.example.junhaozeng.testdesign.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junhaozeng.testdesign.Maths.Fft;
import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.DbManager;
import com.example.junhaozeng.testdesign.Utils.ImageProcessing;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

public class HeartbeatActivity extends AppCompatActivity {
    //曲线
    private Timer timer = new Timer();
    //Timer任务，与Timer配套使用
    //private TimerTask task;
    private static int gx;
    private static int j;
    private static LineChart chart;
    private static Integer i=0;
    private static LineData data;
    private static SharedPreferencesUtils sharedPreferencesUtils;


    private static double flag=1;
    private Handler handler;
    private String title = "pulse";
    //private XYSeries series;
    // private XYMultipleSeriesDataset mDataset;
    // private GraphicalView chart;
    // private XYMultipleSeriesRenderer renderer;
    private Context context;


    //	private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    //Android手机预览控件
    private static SurfaceView preview = null;
    //预览设置信息
    private static SurfaceHolder previewHolder = null;
    //Android手机相机句柄
    private static Camera camera = null;
    private static Camera.Parameters parameters;
    //private static View image = null;
    private static TextView text = null;
    private static TextView text1 = null;
    private static TextView text2 = null;
    private static Button startButton;
    private static Button stopButton;
    private static PowerManager.WakeLock wakeLock = null;
    private static int averageIndex = 0;
    // private static final int averageArraySize = 4;
    //private static final int[] averageArray = new int[averageArraySize];
    //private static int[] averagepref = new int [25];
    private static int count =0;
    private static DbManager dbManager;
    //private static int averageBeat;

    public static enum TYPE {
        GREEN, RED
    };
    //设置默认类型
    // private static TYPE currentType = TYPE.GREEN;
    //获取当前类型
    // public static TYPE getCurrent() {
    //   return currentType;
    // }
    //心跳下标值
    // private static int beatsIndex = 0;
    //心跳数组的大小
    // private static final int beatsArraySize = 3;
    //心跳数组
    //private static final int[] beatsArray = new int[beatsArraySize];
    //心跳脉冲
    //private static double beats = 0;
    //开始时间
    private static long startTime = 0;

    private static double bufferAvgB=0;
    private static ArrayList<Double> GreenAvgList=new ArrayList<Double>();
    private static ArrayList<Double> RedAvgList=new ArrayList<Double>();
    private static ArrayList<Double> BlueAvgList = new ArrayList<Double>();
    private static int counter = 0;
    private static double SamplingFreq;
    private static int Beats=0;
    private static int o2=0;

    private static double Stdr=0;
    private static double Stdb=0;
    private static double sumred=0;
    private static double sumblue=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartbeat);
        initChart();
        initConfig();
        initDbManager();

    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        count=0;
        //获取SurfaceView控件
        preview = (SurfaceView) findViewById(R.id.camera_view);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        camera = Camera.open();

        //		image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.tv_heartbeat);
        text1 = (TextView) findViewById(R.id.tv_pixel);
        text2 = (TextView) findViewById(R.id.tv_pulse);

        startButton = (Button) findViewById(R.id.heartstart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stopButton = (Button) findViewById(R.id.heartstop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        stopButton.setEnabled(false);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();

        startTime = System.currentTimeMillis();

    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        chart.clearValues();
        i=0;


    }


    private static int oxygen_saturation;
    /**
     * 相机预览方法
     * 这个方法中实现动态更新界面UI的功能，
     * 通过获取手机摄像头的参数来实时动态计算平均像素值、脉冲数，从而实时动态计算心率值。
     */
    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera cam) {
            //if data or size == null ****
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            //Atomically sets the value to the given updated value if the current value == the expected value.
            if (!processing.compareAndSet(false, true)) return;

            //put width + height of the camera inside the variables
            int width = size.width;
            int height = size.height;

            double GreenAvg=0;
            double RedAvg=0;
            double BlueAvg;

            GreenAvg=ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), height, width,3); //1 stands for red intensity, 2 for blue, 3 for green
            RedAvg=ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), height, width,1); //1 stands for red intensity, 2 for blue, 3 for green
            sumred = sumred + RedAvg;
            BlueAvg=ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), height, width,2); //1 stands for red intensity, 2 for blue, 3 for green
            sumblue = sumblue + BlueAvg;
            GreenAvgList.add(GreenAvg);
            RedAvgList.add(RedAvg);
            BlueAvgList.add(BlueAvg);



            ++counter;;
            if(counter>=30) {

                int RedEntry = (int) ceil(RedAvg);
                addEntry(i, RedEntry);
                i++;


            }

            if (RedAvg < 200) {
                //inc=0;
                //ProgP=inc;
                counter=0;
                //ProgHeart.setProgress(ProgP);
                processing.set(false);
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d; //to convert time to seconds
            if (totalTimeInSecs >= 15) { //when 30 seconds of measuring passes do the following " we chose 30 seconds to take half sample since 60 seconds is normally a full sample of the heart beat

                Double[] Green = GreenAvgList.toArray(new Double[GreenAvgList.size()]);
                Double[] Red = RedAvgList.toArray(new Double[RedAvgList.size()]);
                Double[] Blue = BlueAvgList.toArray(new Double[BlueAvgList.size()]);

                SamplingFreq =  (counter/totalTimeInSecs); //calculating the sampling frequency

                double HRFreq = Fft.FFT(Green, counter, SamplingFreq); // send the green array and get its fft then return the amount of heartrate per second
                double bpm=(int)ceil(HRFreq*60);
                double HR1Freq = Fft.FFT(Red, counter, SamplingFreq);  // send the red array and get its fft then return the amount of heartrate per second
                double bpm1=(int)ceil(HR1Freq*60);
                //double meanr = sumred/counter;
                //double meanb = sumblue/counter;

                //for(int k=0;i<counter-1;k++){

//                    Double bufferb = Blue[k];
//
//                    Stdb = Stdb + ((bufferb - meanb)*(bufferb - meanb));
//
//                    Double bufferr = Red[k];
//
//                    Stdr = Stdr + ((bufferr - meanr)*(bufferr - meanr));
//
//                }
//
//                double varr = sqrt(Stdr/(counter-1));
//                double varb = sqrt(Stdb/(counter-1));
//
//                double R = (varr/meanr)/(varb/meanb);
//
//                double spo2 = 100-5*(R);


                // The following code is to make sure that if the heartrate from red and green intensities are reasonable
                // take the average between them, otherwise take the green or red if one of them is good

                if((bpm > 60 || bpm < 200) )
                {
                    if((bpm1 > 60 || bpm1 < 200)) {

                        bufferAvgB = (bpm+bpm1)/2;
                    }
                    else{
                        bufferAvgB = bpm;
                    }
                }
                else if((bpm1 > 60 || bpm1 < 200)){
                    bufferAvgB = bpm1;
                }

                if (bufferAvgB < 60 || bufferAvgB > 200) { //if the heart beat wasn't reasonable after all reset the progresspag and restart measuring
                    //inc=0;
                    //ProgP=inc;
                    //ProgHeart.setProgress(ProgP);
                    //mainToast = Toast.makeText(getApplicationContext(), "Measurement Failed", Toast.LENGTH_SHORT);
                    //mainToast.show();
                    startTime = System.currentTimeMillis();
                    counter=0;
                    processing.set(false);
                    return;
                }

                Beats=(int)bufferAvgB;
                //o2=(int)spo2;
            }
            //To check if we got a good red intensity to process if not return to the condition and set it again until we get a good red intensity

            if(Beats != 0 ){ //if beasts were reasonable stop the loop and send HR with the username to results activity and finish this activity
                //Intent i=new Intent(HeartRateProcess.this,HeartRateResult.class);
                //i.putExtra("bpm", Beats);
                //i.putExtra("Usr", user);
                //startActivity(i);
                //finish();
            }


            if(RedAvg!=0){ //increment the progresspar

                //ProgP=inc++/34;
                //ProgHeart.setProgress(ProgP);}

                //keeps taking frames tell 30 seconds
                processing.set(false);

            }
        }};

    /**
     * 预览回调接口
     */
    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        //创建时调用
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);

            } catch (Throwable t) {
                // Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
            }
        }
        //当预览改变的时候回调此方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                //				Log.d(TAG, "Using width=" + size.width + " height="	+ size.height);
            }
            camera.setParameters(parameters);
        }
        //销毁的时候调用
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    /**
     * 获取相机最小的预览尺寸
     * @param width
     * @param height
     * @param parameters
     * @return
     */
    private static Camera.Size getSmallestPreviewSize(int width, int height,
                                                      Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea)
                        result = size;
                }
            }
        }
        return result;
    }

    private void initChart(){
        chart = (LineChart) findViewById(R.id.chart);
        chart.setData(new LineData());
    }

    private static void addEntry(int x,int y){
        data=chart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);
        if(set == null){
            set = createSet();
            data.addDataSet(set);
        }


        set.addEntry(new Entry(x,y));
        data.notifyDataChanged();
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(40);//change number later

        chart.moveViewToX(data.getEntryCount() - 40);//change number later


    }

    private static LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        YAxis leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis rightAxis = chart.getAxis(YAxis.AxisDependency.RIGHT);
        set.setLineWidth(2.5f);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        rightAxis.setEnabled(false);
        //      leftAxis.setAxisMinimum(200f);
        //      leftAxis.setAxisMaximum(300f);

        set.setValueTextSize(10f);
        return set;
    }

    private void start() throws IOException {

        i=0;
        count=0;
//        for(int k=0;k<averagepref.length;k++){
//            averagepref[k]=0;
//        }
        chart.clearValues();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

    }

    private static void stop(){

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
        stopButton.setEnabled(false);
        startButton.setEnabled(true);

        sharedPreferencesUtils.setParam("heart_beat",Beats);
        sharedPreferencesUtils.setParam("heart_time",System.currentTimeMillis());
        save();
        text.setText("Heartbeat rate: "+String.valueOf(Beats)+"Spo2 "+String.valueOf(o2));


    }

    private static String getDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private void initDbManager() {
        dbManager = new DbManager(this);
    }

    private static void  save() {

        String dbHeart = dbManager.readHeartRecord(getDate());
        if (dbHeart.equals(null)) {
            dbManager.insertHeartRecord(getDate(), "true");
        } else  {
            // TODO:
            // create a set method in DbManager
            // set the date and step
            dbManager.updateHeartRecord(getDate(), "true");
        }
    }



}
