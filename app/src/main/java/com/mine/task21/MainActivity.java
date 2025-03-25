package com.mine.task21;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner, spinnerFrom, spinnerTo;
    private EditText inputValue;
    private Button convertButton;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        categorySpinner = findViewById(R.id.categorySpinner);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultView = findViewById(R.id.resultView);

        // Set up category spinner adapter
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
            this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set an initial adapter for unit spinners (default to Length)
        setUnitSpinners("Length");

        // Listen for category changes to update unit options
        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                setUnitSpinners(selectedCategory);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // Set click listener for convert button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromUnit = spinnerFrom.getSelectedItem().toString();
                String toUnit = spinnerTo.getSelectedItem().toString();
                String input = inputValue.getText().toString();

                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double value = Double.parseDouble(input);
                    double result = convertValue(value, fromUnit, toUnit, categorySpinner.getSelectedItem().toString());
                    resultView.setText("Converted Value: " + result);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Updates the source and destination unit spinners based on the selected category.
     */
    private void setUnitSpinners(String category) {
        int arrayResId;
        switch (category) {
            case "Length":
                arrayResId = R.array.length_units;
                break;
            case "Weight":
                arrayResId = R.array.weight_units;
                break;
            case "Temperature":
                arrayResId = R.array.temp_units;
                break;
            default:
                arrayResId = R.array.length_units;
        }
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
            this, arrayResId, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);
    }

    /**
     * Converts the input value from the given source unit to the target unit
     * based on the selected category.
     */
    private double convertValue(double value, String fromUnit, String toUnit, String category) {
        if (category.equals("Length")) {
            return convertLength(value, fromUnit, toUnit);
        } else if (category.equals("Weight")) {
            return convertWeight(value, fromUnit, toUnit);
        } else if (category.equals("Temperature")) {
            return convertTemperature(value, fromUnit, toUnit);
        }
        return 0;
    }

    private double convertLength(double value, String fromUnit, String toUnit) {
        // Convert to base unit: centimeters
        double valueInCm = 0;
        switch (fromUnit) {
            case "Inch":
                valueInCm = value * 2.54;
                break;
            case "Cm":
                valueInCm = value;
                break;
            case "Foot":
                valueInCm = value * 30.48;
                break;
            case "Yard":
                valueInCm = value * 91.44;
                break;
            case "Mile":
                valueInCm = value * 160934;
                break;
            case "Km":
                valueInCm = value * 100000;
                break;
        }
        // Convert from centimeters to target unit
        switch (toUnit) {
            case "Inch":
                return valueInCm / 2.54;
            case "Cm":
                return valueInCm;
            case "Foot":
                return valueInCm / 30.48;
            case "Yard":
                return valueInCm / 91.44;
            case "Mile":
                return valueInCm / 160934;
            case "Km":
                return valueInCm / 100000;
        }
        return 0;
    }

    private double convertWeight(double value, String fromUnit, String toUnit) {
        // Convert to base unit: kilograms
        double valueInKg = 0;
        switch (fromUnit) {
            case "Pound":
                valueInKg = value * 0.453592;
                break;
            case "Kg":
                valueInKg = value;
                break;
            case "Ounce":
                valueInKg = value * 0.0283495;
                break;
            case "G":
                valueInKg = value / 1000.0;
                break;
            case "Ton":
                valueInKg = value * 907.185;
                break;
        }
        // Convert from kilograms to target unit
        switch (toUnit) {
            case "Pound":
                return valueInKg / 0.453592;
            case "Kg":
                return valueInKg;
            case "Ounce":
                return valueInKg / 0.0283495;
            case "G":
                return valueInKg * 1000;
            case "Ton":
                return valueInKg / 907.185;
        }
        return 0;
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals("Celsius")) {
            if (toUnit.equals("Fahrenheit")) {
                return (value * 1.8) + 32;
            } else if (toUnit.equals("Kelvin")) {
                return value + 273.15;
            }
        } else if (fromUnit.equals("Fahrenheit")) {
            if (toUnit.equals("Celsius")) {
                return (value - 32) / 1.8;
            } else if (toUnit.equals("Kelvin")) {
                double celsius = (value - 32) / 1.8;
                return celsius + 273.15;
            }
        } else if (fromUnit.equals("Kelvin")) {
            if (toUnit.equals("Celsius")) {
                return value - 273.15;
            } else if (toUnit.equals("Fahrenheit")) {
                double celsius = value - 273.15;
                return (celsius * 1.8) + 32;
            }
        }
        return value;
    }
}
