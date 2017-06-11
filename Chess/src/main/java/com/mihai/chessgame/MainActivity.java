package com.mihai.chessgame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mihai.chessgame.BT_Connexion.BluetoothConnectionService;
import com.mihai.chessgame.BT_Connexion.DeviceListAdapter;
import com.mihai.chessgame.model.ChessPieceModel;
import com.mihai.chessgame.model.ChessTableModel;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Display display;
    Point sizeS;
    int widthScreen, heightScreen;






    private int whiteColor;
    private TextView text1;
    private int blackColor;
    private Point size;
    private Size screenSize;
    private ImageButton backgroundButon;
    private RelativeLayout.LayoutParams  paramsBk;





    private static final String TAG = "MainActivity";
    BluetoothAdapter mBluetooth_Adapter;
    Button btn_EnableDisable_Discoverable;

    BluetoothConnectionService mBluetooth_Connection;
    Button btnStart_Connection;
    Button btn_Send;
    StringBuilder _messages;
    TextView incoming_Messages;
    EditText et_Send;
    private static final UUID MY_UUIDINSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
   /*private static final UUID MY_UUID_INSECURE =
                 UUID.fromString("79161f1a-41f3-11e7-a919-92ebcb67fe33");*/

    BluetoothDevice mBT_Device;

    public ArrayList<BluetoothDevice> mBT_Devices = new ArrayList<>();

    public DeviceListAdapter mDeviceList_Adapter;

    ListView lvNew_Devices;







    private ImageButton[][] imageButtons = new ImageButton[8][8];
    private int backgroundColor;
    private RelativeLayout markedLayout;
    private Point selectedMarkerPosition;
    private int gamephase;
    ChessPieceModel selectedPieceModel;
    ImageButton selectedImageButton;
    ChessTableModel  chessTableModel;
    private Size squareSize;
    private int playerTurn;

    public boolean isHost;

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private FrameLayout frameLayout;
    private RelativeLayout gameLayout;
    private LinearLayout buttonsLL;
    private LinearLayout viewsLL;
    public boolean isMultiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        isMultiplayer = intent.getBooleanExtra("isMultiplayer", false);
        Log.d("isMultiplayer: ", ""+isMultiplayer);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //overridePendingTransition(0,0);




        if(isMultiplayer){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Multiplayer Connection");
            builder.setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    isHost = false;
                }
            });
            builder.setNegativeButton("CREATE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    isHost=true;
                    ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
                    progress.show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }




        Button btnONOFF = (Button) findViewById(R.id.On_OFF);
        btn_EnableDisable_Discoverable = (Button) findViewById(R.id.enableDiscover);
        lvNew_Devices = (ListView) findViewById(R.id.listNewDevices);
        mBT_Devices = new ArrayList<>();
        btnStart_Connection = (Button) findViewById(R.id.startConnexionBT);
        btn_Send = (Button) findViewById(R.id.buttonSend);
        et_Send = (EditText) findViewById(R.id.edit_Text);
        incoming_Messages = (TextView)findViewById(R.id.incoming_Message);
        _messages = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetooth_Adapter = BluetoothAdapter.getDefaultAdapter();

        lvNew_Devices.setOnItemClickListener(MainActivity.this);

        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        });

        btnStart_Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] bytes = et_Send.getText().toString().getBytes(Charset.defaultCharset());
                mBluetooth_Connection.write(bytes);
                et_Send.setText("");
            }
        });




















        display = getWindowManager().getDefaultDisplay();
        sizeS = new Point();
        display.getSize(sizeS);
        widthScreen = sizeS.x;
        heightScreen = sizeS.y;

        frameLayout = (FrameLayout)this.findViewById(R.id.frameLayout);
        gameLayout = (RelativeLayout)this.findViewById(R.id.gameLayout);
        text1= (TextView) findViewById(R.id.text3);

        mVisible = true;
        selectedMarkerPosition = new Point(-1,-1);

        whiteColor = Color.parseColor("#dadfe1");
        blackColor = Color.parseColor("#6c7a89");
        backgroundColor = Color.parseColor("#ffffff");
        //backgroundColor = Color.parseColor("#d9d9d9");

        playerTurn = 0;

        drawChessBoard();
        drawLabels();
        updateTurnLabel();
    }





    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetooth_Adapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetooth_Adapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBT_Devices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceList_Adapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBT_Devices);
                lvNew_Devices.setAdapter(mDeviceList_Adapter);
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBT_Device = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MainActivity.this, MainMenu.class);
        intent.setAction(Intent.ACTION_SEND);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theMessage");

            _messages.append(text + "\n");
            incoming_Messages.setText(_messages);
        }
    };


    public void startConnection(){
        startBTConnection(mBT_Device,MY_UUIDINSECURE);
    }
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");
        mBluetooth_Connection.startClient(device,uuid);
    }

    public void enableDisableBT(){
        if(mBluetooth_Adapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetooth_Adapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetooth_Adapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetooth_Adapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }
    public void btn_EnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetooth_Adapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
    }

    public void btn_Discover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetooth_Adapter.isDiscovering()){
            mBluetooth_Adapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            checkBTPermissions();

            mBluetooth_Adapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetooth_Adapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetooth_Adapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    private void checkBTPermissions() {
        {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, "Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += ContextCompat.checkSelfPermission(this, "Manifest.permission.ACCESS_COARSE_LOCATION");
                if (permissionCheck != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
                }
            }
            else {
                Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetooth_Adapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBT_Devices.get(i).getName();
        String deviceAddress = mBT_Devices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBT_Devices.get(i).createBond();

            mBT_Device = mBT_Devices.get(i);
            mBluetooth_Connection = new BluetoothConnectionService(MainActivity.this);
        }
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
    }








    public void replay(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog;
        final View dialogView = getLayoutInflater().inflate(R.layout.new_game_layout, null);
        Button positiveButton = (Button)dialogView.findViewById(R.id.buttonYes);
        Button negativeButton = (Button)dialogView.findViewById(R.id.buttonCancel);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        if(widthScreen < 900){
            alertDialog.getWindow().setLayout(550, 300);
        }
       positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chessTableModel.arrangeDefaultPieces();
                updateTableBoardImage();
                playerTurn = 0;
                gamephase = 0;
                //Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();


            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });
    }

    public  Drawable getDrawable(String name) {
        Context context = this;
        int resourceId = context.getResources().getIdentifier(name, "drawable", this.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    private void changePlayerTurn(){
        if (playerTurn == 0){
            playerTurn =1;
        }
        else{
            playerTurn =0;
        }
    }

    private void drawLabels(){
        text1.setText("");
    }

    private void updateTurnLabel(){
        if(playerTurn == 0){
            text1.setText("YOUR TURN");
            text1.setTextColor(Color.WHITE);

            if(chessTableModel.isCheckMate(ChessPieceModel.PieceColor.PIECE_WHITE)){

                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.checkmate_lost, null);
                dialog.setView(dialogView);
                final AlertDialog alert = dialog.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alert.show();

                final Handler handler  = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                        }
                    }
                };

                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 1900);

            }else

            if(chessTableModel.isCheck(ChessPieceModel.PieceColor.PIECE_WHITE)){

                alertCheck();
            }
        }
        else{
            text1.setText("PC TURN");
            text1.setTextColor(Color.BLACK);
            if(chessTableModel.isCheckMate(ChessPieceModel.PieceColor.PIECE_BLACK)){
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.checkmate, null);
                dialog.setView(dialogView);
                final AlertDialog alert = dialog.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alert.show();

                final Handler handler  = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                        }
                    }
                };

                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 1900);
            }
            else
            if(chessTableModel.isCheck(ChessPieceModel.PieceColor.PIECE_BLACK)){
                alertCheck();
            }
        }

    }

    private void performAutoMove(ChessTableModel chessTableModel){

        if(playerTurn == 1){

            // getting All Moves from the Table
            List<Move> moveList = new ArrayList<Move>();
            List<Move> bestMoveList = new ArrayList<Move>();

            // 1st STEP
            for(int i = 0;i<8;i++) {
                for (int j = 0; j < 8; j++) {


                    ChessPieceModel pieceModel = chessTableModel.getChessPieceModelAtPoint(new Point(i,j));
                    if(pieceModel!= null && pieceModel.getPieceColor() == ChessPieceModel.PieceColor.PIECE_BLACK) {
                        List<Point> points = pieceModel.listAllPossibleMovesForThisPiece(chessTableModel);
                        for (Point point : points) {
                            boolean moveAllowed = true;


                            if (pieceModel != null) {
                                ChessTableModel table2 = new ChessTableModel();
                                table2.setChessTableModel(chessTableModel.cloneTable());
                                table2.performMove(pieceModel.getPosition(), new Point(point.x, point.y));
                                if (table2.isCheck(pieceModel.getPieceColor())) {
                                    moveAllowed = false;
                                }
                                //   int valueWhite = table2.tableValue(ChessPieceModel.PieceColor.PIECE_WHITE);
                                //  int valueBlack = table2.tableValue(ChessPieceModel.PieceColor.PIECE_BLACK);
                                //  float rapport = (float) valueBlack /((float) (valueWhite+ valueBlack));
                                float rapportMin2 = 1.0f;
                                // float rapportMax3 = 0.0f;

                                if (moveAllowed) {
                                    // 2nd STEP


                                    for(int i2  = 0; i2<8;i2++ ){
                                        for(int j2 = 0;j2<8;j2++){
                                            ChessPieceModel pieceModel2 = table2.getChessPieceModelAtPoint(new Point(i2,j2));
                                            if(pieceModel2!= null && pieceModel2.getPieceColor() == ChessPieceModel.PieceColor.PIECE_WHITE) {
                                                List<Point> points2 = pieceModel2.listAllPossibleMovesForThisPiece(table2);

                                                for (Point point2 : points2) {
                                                    boolean moveAllowed2 = true;

                                                    if (pieceModel2 != null) {
                                                        ChessTableModel table3 = new ChessTableModel();
                                                        table3.setChessTableModel(table2.cloneTable());
                                                        table3.performMove(pieceModel2.getPosition(), new Point(point2.x, point2.y));
                                                        if (table3.isCheck(pieceModel.getPieceColor())) {
                                                            moveAllowed2 = false;
                                                        }
                                                        if(moveAllowed2) {
                                                            int valueWhite2 = table3.tableValue(ChessPieceModel.PieceColor.PIECE_WHITE);
                                                            int valueBlack2 = table3.tableValue(ChessPieceModel.PieceColor.PIECE_BLACK);
                                                            float rapport2 = (float) valueBlack2 / ((float) (valueWhite2 + valueBlack2));
                                                            if (rapport2 < rapportMin2) {
                                                                rapportMin2 = rapport2;
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Move move = new Move(pieceModel.getPosition(), point, rapportMin2);
                                    moveList.add(move);
                                    // Log.d(TAG,">> " + move.toString());

                                }
                            }

                        }



                    }


                }
            }
            // selecting the move from the list
            if(moveList.size()>0) {
                Random random = new Random();
                Move selectedMove = null;


                float max = 0;
                for(Move move : moveList){
                    if(move.getValue() > max  ){

                        max = move.getValue();
                    }
                }
                for(Move move : moveList) {
                    if(move.getValue() == max){
                        bestMoveList.add(move);
                    }
                }
                selectedMove = bestMoveList.get(Math.abs(random.nextInt()%bestMoveList.size()));
                Log.d(TAG, "Selected Move " + selectedMove.toString());
                chessTableModel.performMove(selectedMove.getP1(), selectedMove.getP2());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changePlayerTurn();
                        updateTurnLabel();
                        updateTableBoardImage();
                    }
                });

            }
            Log.d(TAG,"Numarul de mutari" + moveList.size()+"");

            // end Move
        }
    }
    private void drawChessBoard(){

        chessTableModel = new ChessTableModel();
        gamephase = 0;
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        backgroundButon = new ImageButton(this);
        screenSize = new Size(size.x, size.y);
        paramsBk = new RelativeLayout.LayoutParams(screenSize.getWidth(), screenSize.getHeight());
        backgroundButon.setLayoutParams(paramsBk);
        backgroundButon.setBackground(null);
        gameLayout.addView(backgroundButon);


        backgroundButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gamephase = 0;
                if(markedLayout != null) {
                    markedLayout.setVisibility(View.GONE);
                }
            }
        });

        squareSize = new Size(widthScreen/8, widthScreen/8);

        Point offset;
        if(widthScreen <1000) {
            offset = new Point(1, (int) (heightScreen / 4.5));
        }
        else{
            offset = new Point(1, (int) heightScreen/2);
        }
        //Drawable back = getResources().getDrawable(R.drawable.background_pic);

        gameLayout.setBackgroundColor(Color.WHITE);

        for(int i = 0; i< 8; i++){

            for(int j=0; j< 8; j++){
                int color = whiteColor;

                if((i+j)%2 == 1) {
                    color = blackColor;
                }
                final RelativeLayout relativeLayout = new RelativeLayout(this);
                final RelativeLayout markerLayout = new RelativeLayout(this);

                final ImageButton b1 = new ImageButton(this);

                imageButtons[i][j] = b1;
                relativeLayout.setBackgroundColor((color));
                b1.setBackgroundColor(color);

                RelativeLayout.LayoutParams  params = new RelativeLayout.LayoutParams(squareSize.getWidth(), squareSize.getHeight());
                params.leftMargin = (i*squareSize.getWidth() + offset.x);
                params.topMargin = (j*squareSize.getHeight()+ offset.y);
                relativeLayout.setLayoutParams(params);
                relativeLayout.addView(b1);

                gameLayout.addView(relativeLayout);
                final int x = i;
                final int y = j;

                int markerColor = Color.parseColor("#cf000f");
                int markerOffset = (int)widthScreen/240;

                View leftBorderMarker = new View(this);
                leftBorderMarker.setLayoutParams(new RelativeLayout.LayoutParams(markerOffset,squareSize.getHeight()));
                leftBorderMarker.setBackgroundColor(markerColor);
                markerLayout.addView(leftBorderMarker);

                View rightBorderMarker = new View(this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(markerOffset,squareSize.getHeight());
                layoutParams.leftMargin= squareSize.getWidth() -markerOffset;

                View topBorderMarker = new View(this);
                topBorderMarker.setLayoutParams( new RelativeLayout.LayoutParams(squareSize.getWidth(), markerOffset));
                topBorderMarker.setBackgroundColor(markerColor);
                markerLayout.addView(topBorderMarker);

                View bottomBorderMarker = new View(this);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(squareSize.getHeight(), markerOffset);
                bottomBorderMarker.setLayoutParams(layoutParams1);
                layoutParams1.topMargin= squareSize.getWidth() -markerOffset;
                bottomBorderMarker.setBackgroundColor(markerColor);

                rightBorderMarker.setLayoutParams(layoutParams);
                rightBorderMarker.setBackgroundColor(markerColor);

                markerLayout.addView(rightBorderMarker);
                markerLayout.addView(bottomBorderMarker);

                markerLayout.setVisibility(View.GONE);

                relativeLayout.addView(markerLayout);

                final int c = color;
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ChessPieceModel pieceModel = chessTableModel.getChessTableModel()[y][x];
                        if ((gamephase == 1 && pieceModel != null && selectedPieceModel.getPieceColor()==pieceModel.getPieceColor()))
                        {
                            gamephase = 0;
                        }
                        if(gamephase==0 && pieceModel!= null){

                            if((playerTurn == 0 && pieceModel.getPieceColor()== ChessPieceModel.PieceColor.PIECE_WHITE)
                                    || (playerTurn == 1 && pieceModel.getPieceColor() == ChessPieceModel.PieceColor.PIECE_BLACK))
                            {
                                selectedMarkerPosition.x = x;
                                selectedMarkerPosition.y = y;

                                if (markedLayout != null) {
                                    markedLayout.setVisibility(View.GONE);
                                }
                                markedLayout = markerLayout;
                                markerLayout.setVisibility(View.VISIBLE);
                                gamephase = 1;
                                selectedPieceModel = pieceModel;
                                selectedImageButton = b1;
                                Log.d("Selected piece tyoe: ", selectedPieceModel.getPieceType().toString()+"");
                            }
                        }else
                        if((gamephase == 1 &&pieceModel== null)|| ((gamephase == 1 && pieceModel != null &&
                                selectedPieceModel.canCaptureAnotherPiece(pieceModel)))){
                            int x1 = selectedPieceModel.getPosition().x;
                            int y1 = selectedPieceModel.getPosition().y;
                         /*   int x2 = y;
                            int y2 = x;
                            String moveData = ""+x1+""+y1+""+x2+""+y2;*/
                            //Log.d("Signature: ", moveData);
                            Log.d("Selec: ","sss "+ selectedPieceModel.getPieceType().toString()+"");

                            if(selectedPieceModel.listAllPossibleMovesForThisPiece(chessTableModel).contains(new Point(x,y))) {
                                boolean moveAllowed = true;

                                ChessTableModel table2 = new ChessTableModel();
                                table2.setChessTableModel(chessTableModel.cloneTable());
                                table2.performMove(selectedPieceModel.getPosition(), new Point(x, y));
                                if (table2.isCheck(selectedPieceModel.getPieceColor())) {
                                    moveAllowed = false;
                                }

                                if (moveAllowed) {

                                    chessTableModel.performMove(selectedMarkerPosition, new Point(x, y));

                                    //startAnimation(b1);

                                    updateTableBoardImage();

                                    gamephase = 0;
                                    changePlayerTurn();

                                    if (markedLayout != null) {
                                        markedLayout.setVisibility(View.GONE);
                                    }
                                    updateTurnLabel();

                                    // perform Oponent Move
                                    Thread thread = new Thread(){
                                        public void run(){
                                            performAutoMove(chessTableModel);
                                        }
                                    };
                                    thread.start();
                                }
                            }

                            else{
                                // wrong move
                                gamephase = 0;

                                if (markedLayout != null) {
                                    markedLayout.setVisibility(View.GONE);
                                }
                                alertWrongMove();
                            }
                        }else{
                            if ((gamephase == 1 && pieceModel != null && selectedPieceModel.getPieceColor()==pieceModel.getPieceColor()))
                            {
                                gamephase = 0;
                            }
                        }
                    }
                });
            }
        }
        updateTableBoardImage();
    }

    private void updateTableBoardImage() {
        int valWhite = chessTableModel.tableValue(ChessPieceModel.PieceColor.PIECE_WHITE);
        int valBlack = chessTableModel.tableValue(ChessPieceModel.PieceColor.PIECE_BLACK);
        Log.d(TAG," RAPORT W:"+ valWhite+" B: "+valBlack);
        for(int i = 0;i<8;i++)
            for(int j = 0; j<8;j++)
            {
                final ChessPieceModel pieceModel = chessTableModel.getChessPieceModelAtPoint(new Point(i,j));
                ImageButton imageButton= imageButtons[i][j];
                //  ImageButton b1 = listImageButtons.get(i*7+j);
                if(pieceModel!= null)
                {
                    Drawable dr = getDrawable(pieceModel.imageName());
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, squareSize.getWidth(),squareSize.getHeight(), true));

                    imageButton.setImageDrawable(d);
                }else
                {
                    imageButton.setImageDrawable(getDrawable("void_img"));
                }
            }
    }

    private void alertWrongMove(){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.alert_wrong_move, null);
        alertDialog.setView(dialogView);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 750);
    }

    private void alertCheck(){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.alert_check, null);
        alertDialog.setView(dialogView);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 900);
    }
}
