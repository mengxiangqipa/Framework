package com.framework.customviews;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 2013.11.06 updated by xiexiaojian
 * Support that LinearLayouts or RadioButtons can be all MyRadioGroupForAll's childrens,
 * we can display more rows and columns at the same time.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class is used to create a multiple-exclusion scope for a set of radio
 * buttons. Checking one radio button that belongs to a radio group unchecks any
 * previously checked radio button within the same group.
 * </p>
 *
 * <p>
 * Intially, all of the radio buttons are unchecked. While it is not possible to
 * uncheck a particular radio button, the radio group can be cleared to remove
 * the checked state.
 * </p>
 *
 * <p>
 * The selection is identified by the unique id of the radio button as defined
 * in the XML layout file.
 * </p>
 *
 * <p>
 * <strong>XML Attributes</strong>
 * </p>
 * <p>
 * </p>
 * <p>
 * Also see {@link LinearLayout.LayoutParams
 * LinearLayout.LayoutParams} for layout attributes.
 * </p>
 *
 * @see RadioButton
 *
 */
public class MyRadioGroupForAll extends LinearLayout {
	// holds the checked id; the selection is empty by default
	private int mCheckedId = -1;
	// tracks children radio buttons checked state
	private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
	// when true, mOnCheckedChangeListener discards events
	private boolean mProtectFromCheckedChange = false;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private PassThroughHierarchyChangeListener mPassThroughListener;
	List<RadioButton> list = new ArrayList<RadioButton>();

	/**
	 * {@inheritDoc}
	 */
	public MyRadioGroupForAll(Context context) {
		super(context);
		setOrientation(VERTICAL);
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	public MyRadioGroupForAll(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TypedArray attributes = context.obtainStyledAttributes(
		// attrs, com.android.internal.R.styleable.RadioGroup,
		// com.android.internal.R.attr.radioButtonStyle, 0);
		//
		// int value =
		// attributes.getResourceId(R.styleable.RadioGroup_checkedButton,
		// View.NO_ID);
		// if (value != View.NO_ID) {
		// mCheckedId = value;
		// }
		// final int index =
		// attributes.getInt(com.android.internal.R.styleable.RadioGroup_orientation,
		// VERTICAL);
		// setOrientation(index);
		//
		// attributes.recycle();
		// init();

		mCheckedId = View.NO_ID;
		// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
		final int index = VERTICAL;
		setOrientation(index);
		// //////////////////////////////////////////////////////////////////
		init();
	}

	private void init() {
		mChildOnCheckedChangeListener = new CheckedStateTracker();
		mPassThroughListener = new PassThroughHierarchyChangeListener();
		super.setOnHierarchyChangeListener(mPassThroughListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
		// the user listener is delegated to our pass-through listener
		mPassThroughListener.mOnHierarchyChangeListener = listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		// checks the appropriate radio button as requested in the XML file
		if (mCheckedId != -1) {
			mProtectFromCheckedChange = true;
			setCheckedStateForView(mCheckedId, true);
			mProtectFromCheckedChange = false;
			setCheckedId(mCheckedId);
		}
	}

	@Override
	public void addView(final View child, int index,
						ViewGroup.LayoutParams params) {
		// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
		// MLog.i( "addViewaddViewaddView");
		dealOnChecked(child);
		super.addView(child, index, params);
	}

	// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
	// //////////////////////////////////////////////////////////////////�¼ӷ���
	/**
	 * <pre>
	 * �õ��������ʱradioButton��OnTouchEvent����
	 * @param child
	 */
	private List<RadioButton> dealOnChecked(final View child) {
		if (child instanceof RadioButton) {
			// ((RadioButton) child).setChecked(true);
			dealRadioButtonSetChecked((RadioButton) child);
			// MLog.i( "111");
			// if (mOnCheckedChangeListener != null) {
			// MLog.i( "222");
			// mOnCheckedChangeListener.onCheckedChanged(
			// MyRadioGroupForAll.this, child.getId());
			// MLog.i( "333");
			// }
			if (((RadioButton) child).isChecked()) {
				dealRadioButtonSetChecked((RadioButton) child);
				mProtectFromCheckedChange = true;
				if (mCheckedId != -1) {
					setCheckedStateForView(mCheckedId, false);
				}
				mProtectFromCheckedChange = false;
				setCheckedId(((RadioButton) child).getId());
			}
			list.add((RadioButton) child);
		} else if (child instanceof ViewGroup) {
			int childCount = ((ViewGroup) child).getChildCount();
			for (int i = 0; i < childCount; i++) {
				View view = ((ViewGroup) child).getChildAt(i);
				if (view instanceof RadioButton) {
					final RadioButton button = (RadioButton) view;
					dealRadioButtonSetChecked(button);
					// MLog.i( "444");
					// if (mOnCheckedChangeListener != null) {
					// MLog.i( "555");
					// mOnCheckedChangeListener
					// .onCheckedChanged(
					// MyRadioGroupForAll.this,
					// button.getId());
					// MLog.i( "666");
					// }
					list.add(button);
					if (button.isChecked()) {
						dealRadioButtonSetChecked(button);
						mProtectFromCheckedChange = true;
						if (mCheckedId != -1) {
							setCheckedStateForView(mCheckedId, false);
						}
						mProtectFromCheckedChange = false;
						setCheckedId(button.getId());
					}
				} else if (view instanceof ViewGroup) {
					dealOnChecked(((ViewGroup) child).getChildAt(i));
				}
			}
		}
		return list;
	}

	// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
	// //////////////////////////////////////////////////////////////////�¼ӷ���
	// RadioGroup��������Ӳ�Viewgroup����:��linearlay��relativelayout
	/**
	 * �õ������radionButton
	 *
	 * @param view
	 * @param radioButton
	 */
	private void findRadioButton(View view, RadioButton radioButton) {
		if (view instanceof RadioButton) {
			if (view == radioButton) {
				// do nothing
			} else {
				((RadioButton) view).setChecked(false);
			}
		} else {
			View child;
			int radioCount = ((ViewGroup) view).getChildCount();
			// MLog.i( "radioCount:" + radioCount);
			for (int i = 0; i < radioCount; i++) {
				child = ((ViewGroup) view).getChildAt(i);
				if (child instanceof RadioButton) {
					if (child == radioButton) {
						// do nothing
					} else {
						((RadioButton) child).setChecked(false);
					}
				} else if (child instanceof ViewGroup) {
					findRadioButton(((ViewGroup) view).getChildAt(i),
							radioButton);
				}
			}
		}

	}

	// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
	// //////////////////////////////////////////////////////////////////�¼ӷ���
	/**
	 * ���ʱradioButton��setChecked(true)����
	 *
	 * @param radioButton
	 */
	private void dealRadioButtonSetChecked(RadioButton radioButton) {
		int radioCount = getChildCount();
		for (int i = 0; i < radioCount; i++) {
			findRadioButton((getChildAt(i)), radioButton);
		}
	}

	// //////////////////////////////////////////////////////////////////
	/**
	 * <p>
	 * Sets the selection to the radio button whose identifier is passed in
	 * parameter. Using -1 as the selection identifier clears the selection;
	 * such an operation is equivalent to invoking {@link #clearCheck()}.
	 * </p>
	 *
	 * @param id
	 *            the unique id of the radio button to select in this group
	 *
	 * @see #getCheckedRadioButtonId()
	 * @see #clearCheck()
	 */
	public void check(int id) {
		// don't even bother
		if (id != -1 && (id == mCheckedId)) {
			return;
		}

		if (mCheckedId != -1) {
			setCheckedStateForView(mCheckedId, false);
		}

		if (id != -1) {
			setCheckedStateForView(id, true);
		}

		setCheckedId(id);
	}

	private void setCheckedId(int id) {
		mCheckedId = id;
		if (mOnCheckedChangeListener != null) {
			mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
		}
	}

	private void setCheckedStateForView(int viewId, boolean checked) {
		View checkedView = findViewById(viewId);
		if (checkedView != null && checkedView instanceof RadioButton) {
			((RadioButton) checkedView).setChecked(checked);
		}
	}

	/**
	 * <p>
	 * Returns the identifier of the selected radio button in this group. Upon
	 * empty selection, the returned value is -1.
	 * </p>
	 *
	 * @return the unique id of the selected radio button in this group
	 *
	 * @see #check(int)
	 * @see #clearCheck()
	 *
	 * @attr ref android.R.styleable#RadioGroup_checkedButton
	 */
	public int getCheckedRadioButtonId() {
		return mCheckedId;
	}

	/**
	 * <p>
	 * Clears the selection. When the selection is cleared, no radio button in
	 * this group is selected and {@link #getCheckedRadioButtonId()} returns
	 * null.
	 * </p>
	 *
	 * @see #check(int)
	 * @see #getCheckedRadioButtonId()
	 */
	public void clearCheck() {
		check(-1);
	}

	/**
	 * <p>
	 * Register a callback to be invoked when the checked radio button changes
	 * in this group.
	 * </p>
	 *
	 * @param listener
	 *            the callback to call on checked state change
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@SuppressLint("NewApi")
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(MyRadioGroupForAll.class.getName());
	}

	@SuppressLint("NewApi")
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(MyRadioGroupForAll.class.getName());
	}

	/**
	 * <p>
	 * This set of layout parameters defaults the width and the height of the
	 * children to {@link #WRAP_CONTENT} when they are not specified in the XML
	 * file. Otherwise, this class ussed the value read from the XML file.
	 * </p>
	 *
	 * <p>
	 * Attributes} for a list of all child view attributes that this class
	 * supports.
	 * </p>
	 *
	 */
	public static class LayoutParams extends LinearLayout.LayoutParams {
		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(int w, int h) {
			super(w, h);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(int w, int h, float initWeight) {
			super(w, h, initWeight);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(ViewGroup.LayoutParams p) {
			super(p);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		/**
		 * <p>
		 * Fixes the child's width to
		 * {@link ViewGroup.LayoutParams#WRAP_CONTENT} and the
		 * child's height to
		 * {@link ViewGroup.LayoutParams#WRAP_CONTENT} when not
		 * specified in the XML file.
		 * </p>
		 *
		 * @param a
		 *            the styled attributes set
		 * @param widthAttr
		 *            the width attribute to fetch
		 * @param heightAttr
		 *            the height attribute to fetch
		 */
		@Override
		protected void setBaseAttributes(TypedArray a, int widthAttr,
										 int heightAttr) {

			if (a.hasValue(widthAttr)) {
				width = a.getLayoutDimension(widthAttr, "layout_width");
			} else {
				width = WRAP_CONTENT;
			}

			if (a.hasValue(heightAttr)) {
				height = a.getLayoutDimension(heightAttr, "layout_height");
			} else {
				height = WRAP_CONTENT;
			}
		}
	}

	/**
	 * <p>
	 * Interface definition for a callback to be invoked when the checked radio
	 * button changed in this group.
	 * </p>
	 */
	public interface OnCheckedChangeListener {
		/**
		 * <p>
		 * Called when the checked radio button has changed. When the selection
		 * is cleared, checkedId is -1.
		 * </p>
		 *
		 * @param group
		 *            the group in which the checked radio button has changed
		 * @param checkedId
		 *            the unique identifier of the newly checked radio button
		 */
		public void onCheckedChanged(MyRadioGroupForAll group, int checkedId);
	}

	private class CheckedStateTracker implements
			CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// prevents from infinite recursion
			if (mProtectFromCheckedChange) {
				return;
			}

			mProtectFromCheckedChange = true;
			if (mCheckedId != -1) {
				setCheckedStateForView(mCheckedId, false);
			}
			mProtectFromCheckedChange = false;

			int id = buttonView.getId();
			setCheckedId(id);
		}
	}

	/**
	 * <p>
	 * A pass-through listener acts upon the events and dispatches them to
	 * another listener. This allows the table layout to set its own internal
	 * hierarchy change listener without preventing the user to setup his.
	 * </p>
	 */
	private class PassThroughHierarchyChangeListener implements
			OnHierarchyChangeListener {
		private OnHierarchyChangeListener mOnHierarchyChangeListener;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onChildViewAdded(View parent, View child) {
			// MLog.i( "onChildViewAdded");
			if (parent == MyRadioGroupForAll.this
					&& child instanceof RadioButton) {
				int id = child.getId();
				// generates an id if it's missing
				if (id == View.NO_ID) {
					id = child.hashCode();
					child.setId(id);
				}
				((RadioButton) child)
						.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
			}
			// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
			// //////////////////////////////////////////////////////////////////�¼ӷ���
			else if (parent == MyRadioGroupForAll.this
					&& child instanceof ViewGroup) {
				int len = 0;
				List<RadioButton> list_rb = new ArrayList<RadioButton>();
				list_rb = dealOnChecked(child);
				if (list_rb != null) {
					len = list_rb.size();
				}
				for (int i = 0; i < len; i++) {
					list_rb.get(i).setOnCheckedChangeListener(
							mChildOnCheckedChangeListener);
				}
			}
			// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
			if (mOnHierarchyChangeListener != null) {
				mOnHierarchyChangeListener.onChildViewAdded(parent, child);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onChildViewRemoved(View parent, View child) {
			if (parent == MyRadioGroupForAll.this
					&& child instanceof RadioButton) {
				((RadioButton) child).setOnCheckedChangeListener(null);
			}
			// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�
			// //////////////////////////////////////////////////////////////////�¼ӷ���
			else if (parent == MyRadioGroupForAll.this
					&& child instanceof ViewGroup) {
				int len = 0;
				List<RadioButton> list_rb = new ArrayList<RadioButton>();
				list_rb = dealOnChecked(child);
				if (list_rb != null) {
					len = list_rb.size();
				}
				for (int i = 0; i < len; i++) {
					list_rb.get(i).setOnCheckedChangeListener(
							mChildOnCheckedChangeListener);
				}
			}
			// //////////////////////////////////////////////////////////////////���Դ���޸Ĺ�

			if (mOnHierarchyChangeListener != null) {
				mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
			}
		}
	}
}
