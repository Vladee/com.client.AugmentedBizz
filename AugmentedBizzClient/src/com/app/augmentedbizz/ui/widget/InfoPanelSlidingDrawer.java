package com.app.augmentedbizz.ui.widget;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.application.status.ApplicationStateListener;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity.TargetIndicator;
import com.app.augmentedbizz.ui.MainActivity;

public class InfoPanelSlidingDrawer extends SlidingDrawer implements OnDrawerOpenListener, OnDrawerCloseListener, ApplicationStateListener {
	/**
	 * Possible values for state indication in the info panel on the left side
	 * @author Vladi
	 *
	 */
	public enum StateIndicatorValue {
		BLUE(R.drawable.blue_indicator),//=> searching for image targets
		RED(R.drawable.red_indicator),//=> scanning barcode, loading model, loading indicators
		GREEN(R.drawable.green_indicator);//=> loaded and augmented
		
		private int resourceId;
		
		StateIndicatorValue(int resourceId) {
			this.resourceId = resourceId;
		}
		
		private int getResourceId() {
			return resourceId;
		}
	}
	
	private MainActivity mainActivity;
	private StateIndicatorValue indicatorValue = StateIndicatorValue.BLUE;
	private ImageView imageViewStateIndicator = null;
	private TextView textViewInfoText = null;
	private LinearLayout linearLayoutActionSymbol = null;
	private LinearLayout linearLayoutDetailView = null;
	private boolean locked = false;
	
	public InfoPanelSlidingDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mainActivity = (MainActivity)getContext();
	}
	
	public InfoPanelSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mainActivity = (MainActivity)getContext();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		//load attributes
		imageViewStateIndicator = (ImageView)findViewById(R.id.imageViewInfoPanelIndicator);
		textViewInfoText = (TextView)findViewById(R.id.textViewInfoPanel);
		linearLayoutActionSymbol = (LinearLayout)findViewById(R.id.linearLayoutInfoPanelActionSymbol);
		linearLayoutDetailView = (LinearLayout)findViewById(R.id.linearLayoutInfoContent);
		
		//add listeners
		setOnDrawerOpenListener(this);
		setOnDrawerCloseListener(this);
		
		//lock the info panel
		lockDetailView();
		
		//register a state listener
		mainActivity.getAugmentedBizzApplication().getApplicationStateManager().addApplicationStateListener(this);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mainActivity.getAugmentedBizzApplication().getApplicationStateManager().removeApplicationStateListener(this);
	}
	
	/**
	 * Updates the state indicator in the info panel. Should be called on state changes inside the state management.
	 * 
	 * @param value New state indicator value
	 */
	private void setStateIndicatorValue(StateIndicatorValue value) {
		imageViewStateIndicator.setImageResource(value.getResourceId());
	}
	
	/**
	 * @return The current state indicator value on the left side of the info panel
	 */
	public StateIndicatorValue getStateIndicatorValue() {
		return indicatorValue;
	}
	
	/**
	 * Sets the info text in the info panel including some format arguments if needed
	 * 
	 * @param stringResourceId The resource id of the string retrievable from R.string.<id>
	 * @param formatArgs Optional format arguments as strings which can be passed if the string resource contains them
	 */
	private void setInfoText(int stringResourceId, String... formatArgs) {
		textViewInfoText.setText(getContext().getString(stringResourceId, (Object[])formatArgs));
	}
	
	/**
	 * Shows the progress bar loader as the action symbol on the right side of the info panel.
	 * This works only if the sliding drawer and detail view is locked and closed.
	 */
	private void showLoadingSymbol() {
		if(!locked) {
			return;
		}
		if(!(linearLayoutActionSymbol.getChildAt(0) instanceof ProgressBar)) {
			removeActionSymbol();
			ProgressBar loader = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
			linearLayoutActionSymbol.addView(loader);
		}
	}
	
	/**
	 * Shows the arrow up symbol as the action symbol on the right side
	 */
	private void showArrowUpActionSymbol() {
		if(locked) {
			return;
		}
		removeActionSymbol();
		ImageView arrowImage = new ImageView(getContext());
		arrowImage.setImageResource(R.drawable.slider_up);
		linearLayoutActionSymbol.addView(arrowImage);
		setInfoText(R.string.infoTapDetailView);
	}
	
	/**
	 * Shows the arrow down symbol as the action symbol on the right side
	 */
	private void showArrowDownActionSymbol() {
		if(locked) {
			return;
		}
		removeActionSymbol();
		ImageView arrowImage = new ImageView(getContext());
		arrowImage.setImageResource(R.drawable.slider_down);
		linearLayoutActionSymbol.addView(arrowImage);
		setInfoText(R.string.infoTapCameraView);
	}
	
	/**
	 * Removes all current actions
	 */
	public void removeActionSymbol() {
		linearLayoutActionSymbol.removeAllViews();
	}

	@Override
	public void onDrawerClosed() {
		if(!locked) {
			bringToFront();
			showArrowUpActionSymbol();
		}
	}

	@Override
	public void onDrawerOpened() {
		if(!locked) {
			bringToFront();
			showArrowDownActionSymbol();
		}
	}
	
	/**
	 * Returns whether the sliding drawer is unlocked and the detail view is shown or not
	 * 
	 * @return true, if details view is currently shown
	 */
	public boolean isDetailViewShown() {
		return isOpened();
	}
	
	/**
	 * Checks whether the info panel is locked or not
	 * 
	 * @return true, if the info panel is locked and the detail view is unavailable and hidden
	 */
	public boolean isPanelLocked() {
		return locked;
	}
	
	/**
	 * Unlocks the detail view and sliding drawer so that the user can tap or drag the info panel and see the details
	 */
	private void unlockDetailView() {
		if(locked) {
			locked = false;
			unlock();
			showArrowUpActionSymbol();
		}
	}
	
	/**
	 * Hides the detail view and locks the sliding drawer so that a tap or drag of the info panel is disabled
	 */
	private void lockDetailView() {
		if(!locked) {
			locked = true;
			close();
			lock();
			setDetailViewContent(null);
			removeActionSymbol();
		}
	}
	
	/**
	 * Sets the content of the detail view
	 * 
	 * @param view The view to set as the detail view content. null removes the current detail view only.
	 */
	private void setDetailViewContent(View view) {
		linearLayoutDetailView.removeAllViews();
		if(view != null) {
			linearLayoutDetailView.addView(view);
		}
	}
	
	/**
	 * Creates a list view based upon a list of target indicators.
	 * 
	 * @param indicators List of target indicators.
	 * @return A generated list view.
	 */
	private ListView createListViewFromIndicators(List<TargetIndicator> indicators) {
		ListView indicatorInfoList = new ListView(getContext());
		
		int size = indicators == null ? 0 : indicators.size();
		String[] descriptionStrings = new String[size];
		for(int i = 0; i < size; ++i) {
			descriptionStrings[i] = indicators.get(i).getDescription();
		}
		ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(getContext(), R.layout.detail_listitem, descriptionStrings) {
			@Override
			public boolean areAllItemsEnabled() {
				return false;
			}
			
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		};
		
		indicatorInfoList.setAdapter(descriptionAdapter);
		
		return indicatorInfoList;
	}

	@Override
	public void onApplicationStateChange(ApplicationState lastState, ApplicationState nextState) {
		final ApplicationFacade facade = (ApplicationFacade)mainActivity.getAugmentedBizzApplication();
		switch(nextState) {
			case EXITING:
			case UNINITIATED:
			case DEINITIALIZING:
				setVisibility(GONE);
				break;
			case INITIALIZING:
				setVisibility(VISIBLE);
				break;
			case TRACKING:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.BLUE);
				setInfoText(R.string.infoCaptureTarget);
				removeActionSymbol();
				break;
			case TRACKED:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.BLUE);
				setInfoText(R.string.infoTargetFoundScanning);
				removeActionSymbol();
				break;
			case SCANNED:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.RED);
				setInfoText(R.string.infoScannedLoadingData);
				showLoadingSymbol();
				break;
			case LOADING:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.RED);
				setInfoText(R.string.infoScannedLoadingData);
				showLoadingSymbol();
				break;
			case SHOWING_CACHE:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.RED);
				setInfoText(R.string.infoModelCachedCheckingUpdates);
				showLoadingSymbol();
				break;
			case LOADING_INDICATORS:
				lockDetailView();
				setStateIndicatorValue(StateIndicatorValue.RED);
				setInfoText(R.string.infoRetrievingIndicators, facade.getDataManager().getCurrentTarget().getTargetName());
				showLoadingSymbol();
				break;
			case SHOWING:
				unlockDetailView();
				setStateIndicatorValue(StateIndicatorValue.GREEN);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						setDetailViewContent(
								createListViewFromIndicators(facade.getDataManager().getCurrentIndicators()));
					}
				}, 200);
				
		}
	}
}
