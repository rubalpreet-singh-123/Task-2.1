package com.example.assignment1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinnerSource = findViewById(R.id.spinnerSource);
        Spinner spinnerDestination = findViewById(R.id.spinnerDestination);
        EditText editValue = findViewById(R.id.editValue);
        Button btnConvert = findViewById(R.id.btnConvert);
        TextView txtResult = findViewById(R.id.txtResult);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);


        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueStr = editValue.getText().toString();
                String sourceUnit = spinnerSource.getSelectedItem().toString();
                String destUnit = spinnerDestination.getSelectedItem().toString();

                if (valueStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sourceUnit.equals(destUnit)) {
                    txtResult.setText("Source and destination units are the same!");
                    return;
                }

                try {
                    double value = Double.parseDouble(valueStr);
                    double result = convertUnits(value, sourceUnit, destUnit);
                    txtResult.setText(String.format("%.4f %s = %.4f %s", value, sourceUnit, result, destUnit));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    txtResult.setText("Conversion not supported!");
                }
            }
        });
    }



    private double convertUnits(double value, String source, String dest) {

        if (isLengthUnit(source) && isLengthUnit(dest)) {
            double valueInCm = toCentimeter(value, source);
            return fromCentimeter(valueInCm, dest);
        }

        if (isWeightUnit(source) && isWeightUnit(dest)) {
            double valueInKg = toKilogram(value, source);
            return fromKilogram(valueInKg, dest);
        }

        if (isTemperatureUnit(source) && isTemperatureUnit(dest)) {
            return convertTemperature(value, source, dest);
        }
        throw new IllegalArgumentException("Conversion not supported");
    }

    private boolean isLengthUnit(String unit) {
        return unit.equals("Inch") || unit.equals("Foot") || unit.equals("Yard") || unit.equals("Mile") ||
                unit.equals("Centimeter") || unit.equals("Kilometer");
    }

    private boolean isWeightUnit(String unit) {
        return unit.equals("Pound") || unit.equals("Ounce") || unit.equals("Ton") ||
                unit.equals("Gram") || unit.equals("Kilogram");
    }

    private boolean isTemperatureUnit(String unit) {
        return unit.equals("Celsius") || unit.equals("Fahrenheit") || unit.equals("Kelvin");
    }


    private double toCentimeter(double value, String unit) {
        switch (unit) {
            case "Inch": return value * 2.54;
            case "Foot": return value * 30.48;
            case "Yard": return value * 91.44;
            case "Mile": return value * 160934;
            case "Centimeter": return value;
            case "Kilometer": return value * 100000;
            default: throw new IllegalArgumentException("Unknown length unit");
        }
    }

    private double fromCentimeter(double value, String unit) {
        switch (unit) {
            case "Inch": return value / 2.54;
            case "Foot": return value / 30.48;
            case "Yard": return value / 91.44;
            case "Mile": return value / 160934;
            case "Centimeter": return value;
            case "Kilometer": return value / 100000;
            default: throw new IllegalArgumentException("Unknown length unit");
        }
    }


    private double toKilogram(double value, String unit) {
        switch (unit) {
            case "Pound": return value * 0.453592;
            case "Ounce": return value * 0.0283495;
            case "Ton": return value * 907.185;
            case "Gram": return value / 1000;
            case "Kilogram": return value;
            default: throw new IllegalArgumentException("Unknown weight unit");
        }
    }

    private double fromKilogram(double value, String unit) {
        switch (unit) {
            case "Pound": return value / 0.453592;
            case "Ounce": return value / 0.0283495;
            case "Ton": return value / 907.185;
            case "Gram": return value * 1000;
            case "Kilogram": return value;
            default: throw new IllegalArgumentException("Unknown weight unit");
        }
    }


    private double convertTemperature(double value, String source, String dest) {
        double celsius;
        if (source.equals("Celsius")) {
            celsius = value;
        } else if (source.equals("Fahrenheit")) {
            celsius = (value - 32) / 1.8;
        } else if (source.equals("Kelvin")) {
            celsius = value - 273.15;
        } else {
            throw new IllegalArgumentException("Unknown temperature unit");
        }

        if (dest.equals("Celsius")) {
            return celsius;
        } else if (dest.equals("Fahrenheit")) {
            return celsius * 1.8 + 32;
        } else if (dest.equals("Kelvin")) {
            return celsius + 273.15;
        } else {
            throw new IllegalArgumentException("Unknown temperature unit");
        }
    }
}
