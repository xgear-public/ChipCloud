package fisk.chipcloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.fiskur.chipcloud.R;

public class ChipCloud implements View.OnClickListener{

  private static final boolean USER_CLICK = true;
  private static final boolean AUTO_CHECK = false;

  public enum SelectMode{
    multi,
    single,
    mandatory,
    none
  }

  private SelectMode selectMode;

  private final Context context;
  private final ViewGroup layout;

  private ChipListener chipListener;
  private boolean ignoreAutoChecks = false;

  public ChipCloud(Context context, ViewGroup layout){
    this.context = context;
    this.layout = layout;
    selectMode = SelectMode.multi;
  }

  public ChipCloud(Context context, ViewGroup layout, SelectMode selectMode){
    this.context = context;
    this.layout = layout;
    this.selectMode = selectMode;
  }

  public void setListener(ChipListener chipListener){
    this.chipListener = chipListener;
  }

  public void setListener(ChipListener chipListener, boolean ignoreAutoChecks){
    this.chipListener = chipListener;
    this.ignoreAutoChecks = ignoreAutoChecks;
  }

  public void addChips(Object[] objects){
    for(Object object : objects){
      addChip(object);
    }
  }

  public void addChips(List<Object> objects){
    for(Object object : objects){
      addChip(object);
    }
  }

  public void addChip(Object object){
    ToggleChip toggleChip = (ToggleChip) LayoutInflater.from(context).inflate(R.layout.toggle_chip, null);
    toggleChip.setLabel(object.toString());
    int chipHeight = context.getResources().getDimensionPixelSize(R.dimen.chip_height);
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
      default:
        //do nothing
    }
  }

  private void check(ToggleChip toggleChip, boolean checked, boolean isUserClick){
    toggleChip.setChecked(checked);
    int index = layout.indexOfChild(toggleChip);
    if(chipListener != null){
      if(!isUserClick && ignoreAutoChecks){
        return;
      }
      chipListener.chipCheckedChange(index, checked, isUserClick);
    }
  }
}
