package com.app.augmentedbizz.ui.widget;

import com.app.augmentedbizz.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class InfoPanelSlidingDrawer extends SlidingDrawer implements OnDrawerOpenListener, OnDrawerCloseListener
{
	/**
	 * Possible values for state indication in the info panel on the left side
	 * @author Vladi
	 *
	 */
	public enum StateIndicatorValue
	{
		BLUE(R.drawable.blue_indicator),//=> searching for image targets
		RED(R.drawable.red_indicator),//=> scanning barcode, loading model, loading indicators
		GREEN(R.drawable.green_indicator);//=> loaded and augmented
		
		private int resourceId;
		
		StateIndicatorValue(int resourceId)
		{
			this.resourceId = resourceId;
		}
		
		private int getResourceId()
		{
			return resourceId;
		}
	}
	
	private StateIndicatorValue indicatorValue = StateIndicatorValue.BLUE;
	private ImageView imageViewStateIndicator = null;
	private TextView textViewInfoText = null;
	private LinearLayout linearLayoutActionSymbol = null;
	private boolean locked = false;
	
	public InfoPanelSlidingDrawer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public InfoPanelSlidingDrawer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		
		//load attributes
		imageViewStateIndicator = (ImageView)findViewById(R.id.imageViewInfoPanelIndicator);
		textViewInfoText = (TextView)findViewById(R.id.textViewInfoPanel);
		linearLayoutActionSymbol = (LinearLayout)findViewById(R.id.linearLayoutInfoPanelActionSymbol);
		
		//add listeners
		setOnDrawerOpenListener(this);
		setOnDrawerCloseListener(this);
		
		//lock the info panel
		lockDetailView();
	}
	
	/**
	 * Updates the state indicator in the info panel. Should be called on state changes inside the state management.
	 * 
	 * @param value New state indicator value
	 */
	public void setStateIndicatorValue(StateIndicatorValue value)
	{
		imageViewStateIndicator.setImageResource(value.getResourceId());
	}
	
	/**
	 * @return The current state indicator value on the left side of the info panel
	 */
	public StateIndicatorValue getStateIndicatorValue()
	{
		return indicatorValue;
	}
	
	/**
	 * Sets the info text in the info panel including some format arguments if needed
	 * 
	 * @param stringResourceId The resource id of the string retrievable from R.string.<id>
	 * @param formatArgs Optional format arguments as strings which can be passed if the string resource contains them
	 */
	public void setInfoText(int stringResourceId, String... formatArgs)
	{
		textViewInfoText.setText(getContext().getString(stringResourceId, (Object[])formatArgs));
	}
	
	/**
	 * Shows the progress bar loader as the action symbol on the right side of the info panel.
	 * This works only if the sliding drawer and detail view is locked and closed.
	 */
	public void showLoadingSymbol()
	{
		if(!locked)
		{
			return;
		}
		
		removeActionSymbol();
		ProgressBar loader = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
		linearLayoutActionSymbol.addView(loader);
	}
	
	/**
	 * Shows the arrow up symbol as the action symbol on the right side
	 */
	private void showArrowUpActionSymbol()
	{
		if(locked)
		{
			return;
		}
		removeActionSymbol();
		ImageView arrowImage = new ImageView(getContext());
		arrowImage.setImageResource(R.drawable.slider_up);
		linearLayoutActionSymbol.addView(arrowImage);
	}
	
	/**
	 * Shows the arrow down symbol as the action symbol on the right side
	 */
	private void showArrowDownActionSymbol()
	{
		if(locked)
		{
			return;
		}
		removeActionSymbol();
		ImageView arrowImage = new ImageView(getContext());
		arrowImage.setImageResource(R.drawable.slider_down);
		linearLayoutActionSymbol.addView(arrowImage);
	}
	
	/**
	 * Removes all current actions
	 */
	public void removeActionSymbol()
	{
		linearLayoutActionSymbol.removeAllViews();
	}

	@Override
	public void onDrawerClosed()
	{
		if(!locked)
		{
			showArrowUpActionSymbol();
		}
	}

	@Override
	public void onDrawerOpened()
	{
		if(!locked)
		{
			showArrowDownActionSymbol();
		}
	}
	
	/**
	 * Returns whether the sliding drawer is unlocked and the detail view is shown or not
	 * 
	 * @return true, if details view is currently shown
	 */
	public boolean isDetailViewShown()
	{
		return isOpened();
	}
	
	/**
	 * Unlocks the detail view and sliding drawer so that the user can tap or drag the info panel and see the details
	 */
	public void unlockDetailView()
	{
		if(locked)
		{
			locked = false;
			unlock();
			showArrowUpActionSymbol();
		}
	}
	
	/**
	 * Hides the detail view and locks the sliding drawer so that a tap or drag of the info panel is disabled
	 */
	public void lockDetailView()
	{
		if(!locked)
		{
			locked = true;
			close();
			lock();
			removeActionSymbol();
		}
	}
	
}
