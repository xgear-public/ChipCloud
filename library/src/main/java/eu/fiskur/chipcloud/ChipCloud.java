package eu.fiskur.chipcloud;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class ChipCloud extends FlowLayout implements ChipListener {

  private Context context;
  private int chipHeight;
  private int selectedColor = -1;
  private int selectedFontColor = -1;
  private int unselectedColor = -1;
  private int unselectedFontColor = -1;
  private int selectTransitionMS = 750;
  private int deselectTransitionMS = 500;
  private boolean singleChoice;

  private ChipListener chipListener;

  public ChipCloud(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public ChipCloud(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChipCloud, 0, 0);

    try {
      selectedColor = a.getColor(R.styleable.ChipCloud_selectedColor, -1);
      selectedFontColor = a.getColor(R.styleable.ChipCloud_selectedFontColor, -1);
      unselectedColor = a.getColor(R.styleable.ChipCloud_deselectedColor, -1);
      unselectedFontColor = a.getColor(R.styleable.ChipCloud_deselectedFontColor, -1);
      selectTransitionMS = a.getInt(R.styleable.ChipCloud_selectTransitionMS, 750);
      deselectTransitionMS = a.getInt(R.styleable.ChipCloud_deselectTransitionMS, 500);
      singleChoice = a.getBoolean(R.styleable.ChipCloud_singleChoice, true);
    } finally {
      a.recycle();
    }

    init();
  }

  private void init() {
    chipHeight = (int) (28 * getResources().getDisplayMetrics().density + 0.5f);
    singleChoice = true;
  }

  public void setSelectedColor(int selectedColor) {
    this.selectedColor = selectedColor;
  }

  public void setSelectedFontColor(int selectedFontColor) {
    this.selectedFontColor = selectedFontColor;
  }

  public void setUnselectedColor(int unselectedColor) {
    this.unselectedColor = unselectedColor;
  }

  public void setUnselectedFontColor(int unselectedFontColor) {
    this.unselectedFontColor = unselectedFontColor;
  }

  public void setSelectTransitionMS(int selectTransitionMS) {
    this.selectTransitionMS = selectTransitionMS;
  }

  public void setDeselectTransitionMS(int deselectTransitionMS) {
    this.deselectTransitionMS = deselectTransitionMS;
  }

  public void setSingleChoice(boolean singleChoice) {
    this.singleChoice = singleChoice;
    if (singleChoice) {
      for (int i = 0; i < getChildCount(); i++) {
        Chip chip = (Chip) getChildAt(i);
        chip.deselect();
      }
    }
  }

  public void setChipListener(ChipListener chipListener) {
    this.chipListener = chipListener;
  }

  public void addChip(String label) {
    Chip chip = new Chip.ChipBuilder().index(getChildCount())
        .label(label)
        .selectedColor(selectedColor)
        .selectedFontColor(selectedFontColor)
        .unselectedColor(unselectedColor)
        .unselectedFontColor(unselectedFontColor)
        .selectTransitionMS(selectTransitionMS)
        .deselectTransitionMS(deselectTransitionMS)
        .chipHeight(chipHeight)
        .chipListener(this)
        .build(context);

    addView(chip);
  }

  public void setSelectedChip(int index) {
    Chip chip = (Chip) getChildAt(index);
    chip.select();
  }

  @Override public void chipSelected(int index) {

    if (singleChoice) {
      for (int i = 0; i < getChildCount(); i++) {
        Chip chip = (Chip) getChildAt(i);
        if (i != index) {
          chip.deselect();
        }
      }
    }

    if (chipListener != null) {
      chipListener.chipSelected(index);
    }
  }

  @Override public void chipDeselected(int index) {
    if (chipListener != null) {
      chipListener.chipDeselected(index);
    }
  }

  public boolean isSelected(int index) {
    if (index > 0 && index < getChildCount()) {
      Chip chip = (Chip) getChildAt(index);
      return chip.isSelected();
    }
    return false;
  }

  //Apparently using the builder pattern to configure an object has been labelled a 'Bloch Builder'.
  public static class Configure {
    private ChipCloud chipCloud;
    private int selectedColor = -1;
    private int selectedFontColor = -1;
    private int deselectedColor = -1;
    private int deselectedFontColor = -1;
    private int selectTransitionMS = 750;
    private int deselectTransitionMS = 500;
    private boolean singleChoice = true;
    private ChipListener chipListener;

    public Configure chipCloud(ChipCloud chipCloud) {
      this.chipCloud = chipCloud;
      return this;
    }

    public Configure selectedColor(int selectedColor) {
      this.selectedColor = selectedColor;
      return this;
    }

    public Configure selectedFontColor(int selectedFontColor) {
      this.selectedFontColor = selectedFontColor;
      return this;
    }

    public Configure deselectedColor(int deselectedColor) {
      this.deselectedColor = deselectedColor;
      return this;
    }

    public Configure deselectedFontColor(int deselectedFontColor) {
      this.deselectedFontColor = deselectedFontColor;
      return this;
    }

    public Configure selectTransitionMS(int selectTransitionMS) {
      this.selectTransitionMS = selectTransitionMS;
      return this;
    }

    public Configure deselectTransitionMS(int deselectTransitionMS) {
      this.deselectTransitionMS = deselectTransitionMS;
      return this;
    }

    public Configure singleChoice(boolean singleChoice) {
      this.singleChoice = singleChoice;
      return this;
    }

    public Configure chipListener(ChipListener chipListener) {
      this.chipListener = chipListener;
      return this;
    }

    public void build() {
      chipCloud.setSelectedColor(selectedColor);
      chipCloud.setSelectedFontColor(selectedFontColor);
      chipCloud.setUnselectedColor(deselectedColor);
      chipCloud.setUnselectedFontColor(deselectedFontColor);
      chipCloud.setSelectTransitionMS(selectTransitionMS);
      chipCloud.setDeselectTransitionMS(deselectTransitionMS);
      chipCloud.setSingleChoice(singleChoice);
      chipCloud.setChipListener(chipListener);
    }
  }
}
