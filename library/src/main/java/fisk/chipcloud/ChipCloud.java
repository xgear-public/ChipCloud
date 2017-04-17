package fisk.chipcloud;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.fiskur.chipcloud.R;

@SuppressWarnings({"unused", "SameParameterValue"})
public class ChipCloud implements View.OnClickListener{

  private static final boolean USER_CLICK = true;
  private static final boolean AUTO_CHECK = false;

  public enum SelectMode{
    multi,
    single,
    mandatory,
    none
  }

  private final Context context;
  private final ViewGroup layout;
  private final SelectMode selectMode;
  private Typeface typeface = null;
  private StateListDrawable customDrawable = null;

  private ChipCloudConfig config = null;

  private ChipListener chipListener;
  private boolean ignoreAutoChecks = false;

  public ChipCloud(Context context, ViewGroup layout){
    this.context = context;
    this.layout = layout;
    selectMode = SelectMode.multi;
  }

  public ChipCloud(Context context, ViewGroup layout, ChipCloudConfig config) {
    this.context = context;
    this.layout = layout;
    selectMode = config.selectMode;
    this.config = config;
  }

  public void setListener(ChipListener chipListener){
    this.chipListener = chipListener;
  }

  public void setListener(ChipListener chipListener, boolean ignoreAutoChecks){
    this.chipListener = chipListener;
    this.ignoreAutoChecks = ignoreAutoChecks;
  }

  public <T> void addChips(T[] objects){
    for(T object : objects){
      addChip(object);
    }
  }

  public <T> void addChips(List<T> objects){
    for(T object : objects){
      addChip(object);
    }
  }

  public <T> void addChip(T object){
    ToggleChip toggleChip;
    int chipHeight;
    if(config.useInsetPadding){
      toggleChip = (ToggleChip) LayoutInflater.from(context).inflate(R.layout.inset_toggle_chip, layout, false);
      chipHeight = context.getResources().getDimensionPixelSize(R.dimen.inset_chip_height);
    }else{
      toggleChip = (ToggleChip) LayoutInflater.from(context).inflate(R.layout.toggle_chip, layout, false);
      chipHeight = context.getResources().getDimensionPixelSize(R.dimen.chip_height);
    }
    toggleChip.setLabel(object.toString());
    ConfigHelper.initialise(toggleChip, config);
    toggleChip.setHeight(chipHeight);
    toggleChip.setOnClickListener(this);
    layout.addView(toggleChip);
  }

  public void setChecked(int index){
    ToggleChip toggleChip = (ToggleChip) layout.getChildAt(index);
    check(toggleChip, true, AUTO_CHECK);
    if(selectMode == SelectMode.single || selectMode == SelectMode.mandatory){
      int childCount = layout.getChildCount();
      for (int i = 0; i < childCount; i++) {
        View child = layout.getChildAt(i);
        if (child != toggleChip) {
          ToggleChip otherChip = (ToggleChip) child;
          check(otherChip, false, AUTO_CHECK);
        }
      }
    }
  }

  public void setSelectedIndexes(int[] indexes){
    if(selectMode == SelectMode.single || selectMode == SelectMode.mandatory){
      return;
    }

    for (int index : indexes) {
      ToggleChip chip = (ToggleChip) layout.getChildAt(index);
      check(chip, true, AUTO_CHECK);
    }
  }

  public void deselectIndex(int index){
    ToggleChip toggleChip = (ToggleChip) layout.getChildAt(index);
    switch(selectMode){
      case multi:
      case single:
        check(toggleChip, false, AUTO_CHECK);
        break;
      default:
        //
    }
  }

  public String getLabel(int index){
    return ((ToggleChip) layout.getChildAt(index)).getText().toString();
  }

  @Override
  public void onClick(View view) {
    ToggleChip clickedChip = (ToggleChip) view;
    switch (selectMode) {
      case multi:
        check(clickedChip, !clickedChip.isChecked(), USER_CLICK);
        break;
      case single:
        check(clickedChip, !clickedChip.isChecked(), USER_CLICK);
        if(clickedChip.isChecked()) {
          int childCount = layout.getChildCount();
          for (int i = 0; i < childCount; i++) {
            View child = layout.getChildAt(i);
            if (child != clickedChip) {
              ToggleChip otherChip = (ToggleChip) child;
              check(otherChip, false, AUTO_CHECK);
            }
          }
        }
        break;
      case mandatory:
        if(!clickedChip.isChecked()){
          check(clickedChip, true, USER_CLICK);
          int childCount = layout.getChildCount();
          for (int i = 0; i < childCount; i++) {
            View child = layout.getChildAt(i);
            if (child != clickedChip) {
              ToggleChip otherChip = (ToggleChip) child;
              check(otherChip, false, AUTO_CHECK);
            }
          }
        }
        break;
      case none:
        if(chipListener != null) {
          chipListener.chipCheckedChange(layout.indexOfChild(clickedChip), clickedChip.isChecked(), USER_CLICK);
        }
        break;
      default:
        //do nothing
    }
  }

  private void check(ToggleChip toggleChip, boolean checked, boolean isUserClick){
    toggleChip.setChecked(checked);
    ConfigHelper.update(toggleChip, config);

    if(chipListener != null){
      if(!isUserClick && ignoreAutoChecks){
        return;
      }
      int index = layout.indexOfChild(toggleChip);
      chipListener.chipCheckedChange(index, checked, isUserClick);
    }
  }
}