package com.vision2020.ui.views.dialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
public abstract class BaseListDialog extends Dialog implements ItemCallBack.IndexClick {
	ItemCallBack.IndexClick indexClick;
	Snackbar mSnackbar;
	private int contentView;
	private Context context;
	private int mWidth, mHeight;

	private ItemCallBack.IndexClick interfaceInstance;
	
	
	protected BaseListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	public BaseListDialog(@NonNull Context context, int themeResId) {
		super(context, themeResId);
	}
	
	public BaseListDialog(@NonNull Context context) {
		super(context);
	}
	
	@NonNull
    public abstract ItemCallBack.IndexClick getInterfaceInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		WindowManager.LayoutParams windowManager = this.getWindow().getAttributes();
		setContentView(getContentView());
		context = getContext();
		indexClick = getInterfaceInstance();
		getDefaults();
		onCreateStuff();
		windowManager.width = WindowManager.LayoutParams.MATCH_PARENT;
		windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(windowManager);
	}
	
	protected abstract void onCreateStuff();
	
	public abstract int getContentView();
	
	void getDefaults() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowmanager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
		mWidth = displayMetrics.widthPixels;
		mHeight = displayMetrics.heightPixels;
		Log.e("Height = ", mHeight + " width " + mWidth);
	}
	
	/*protected void showInternetAlert(@NonNull View view) {
		mSnackbar = Snackbar.make(view, R.string.errorInternet, Snackbar.LENGTH_SHORT);
		mSnackbar.show();
	}*/

	public  interface Callback{
		void selected(int pos);
	}
}

