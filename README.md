# DecimalPicker
Extends [ElegantNumberButton](https://github.com/ashik94vc/ElegantNumberButton) to handle floating point numbers.
## Gradle

  `compile 'com.github.isabsent:decimalpicker:0.0.7'`
## Maven

    <dependency>
      <groupId>com.github.isabsent</groupId>
      <artifactId>decimalpicker</artifactId>
      <version>0.0.7</version>
      <type>pom</type>
    </dependency>
    
## Usage in .xml

    <com.github.isabsent.decimalpicker.DecimalPicker
        android:id="@+id/decimal_picker"
        picker:beforeDecimal="3"
        picker:afterDecimal="2"
        picker:initialNumber="1"
        picker:finalNumber="100"
        android:layout_marginTop="24dp"
        android:layout_marginBottom="24dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="24sp"/>
            
## Usage in the code

    DecimalPicker decimalPicker = (DecimalPicker) view.findViewById(R.id.decimal_picker);
    decimalPicker.setRange(0.0, 100.0);
    decimalPicker.setBeforeDecimal(3);
    decimalPicker.setAfterDecimal(2);
