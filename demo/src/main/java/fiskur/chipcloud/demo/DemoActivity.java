package fiskur.chipcloud.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import eu.fiskur.chipcloud.ChipCloud;

public class DemoActivity extends AppCompatActivity {

  private static final String TAG = "DemoActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo);

    final ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud_a);
    String[] demoLabelsA = new String[]{"UY Scuti", "WOH G64", "RW Cephei", "Westerlund 1-26", "V354 Cephei", "KY Cygni", "VY Canis Majoris"};
    chipCloud.addChips(demoLabelsA);

    Button deleteButton = (Button) findViewById(R.id.delete_selected_button);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        int selectedIndex = chipCloud.getSelectedIndex();
        chipCloud.deleteChip(selectedIndex);
      }
    });
  }
}
