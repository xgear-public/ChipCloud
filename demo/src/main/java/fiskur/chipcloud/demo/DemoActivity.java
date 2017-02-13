package fiskur.chipcloud.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import eu.fiskur.chipcloud.ChipCloud;

public class DemoActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo);

    ChipCloud chipCloudA = (ChipCloud) findViewById(R.id.chip_cloud_a);
    String[] demoLabelsA = new String[]{"UY Scuti", "WOH G64", "RW Cephei", "Westerlund 1-26", "V354 Cephei", "KY Cygni", "VY Canis Majoris"};
    chipCloudA.addChips(demoLabelsA);
  }
}
