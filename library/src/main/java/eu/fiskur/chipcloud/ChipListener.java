package eu.fiskur.chipcloud;

public interface ChipListener {
    void chipSelected(int index, Object object);
    void chipDeselected(int index, Object object);
}
