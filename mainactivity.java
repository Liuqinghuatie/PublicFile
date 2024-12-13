public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button startButton;
    private Button pauseButton;
    private Button continueButton;
    private Button hideButton;

    private int progress = 0;
    private boolean isRunning = false;
    private boolean isVisible = true;

    private Handler handler = new Handler();

    private Object lock = new Object();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lock) {
                while (progress < 100 && isRunning) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progress++;
                    progressBar.setProgress(progress);

                    if (progress == 100) {
                        handler.removeCallbacks(runnable);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        continueButton = findViewById(R.id.continueButton);
        hideButton = findViewById(R.id.hideButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoading();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseLoading();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueLoading();
            }
        });

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    hideProgressBar();
                } else {
                    showProgressBar();
                }
            }
        });
    }

    private void startLoading() {
        synchronized (lock) {
            if (!isRunning) {
                progress = 0;
                progressBar.setProgress(progress);
                isRunning = true;
                handler.post(runnable);
            }
        }
    }

    private void pauseLoading() {
        synchronized (lock) {
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }

    private void continueLoading() {
        synchronized (lock) {
            isRunning = true;
            handler.post(runnable);
        }
    }

    private void hideProgressBar() {
        isVisible = false;
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        isVisible = true;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pauseLoading();
        hideProgressBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseLoading();
        hideProgressBar();
    }
}
//获取log
//for /f "delims=" %i in ('powershell -command "(Get-Date).ToString('yyyyMMddHHmmss')"') do (adb logcat -b all > ".\%i.log")
