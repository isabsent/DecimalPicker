# DecimalPicker
Extends [ElegantNumberButton](https://github.com/ashik94vc/ElegantNumberButton) to handle floating point numbers.
## Gradle

  `compile 'com.github.isabsent:decimalpicker:0.0.10'`
## Maven

    <dependency>
      <groupId>com.github.isabsent</groupId>
      <artifactId>decimalpicker</artifactId>
      <version>0.0.10</version>
      <type>pom</type>
    </dependency>
    
## Usage

        <com.github.isabsent.decimalpicker.DecimalPicker
            android:id="@+id/decimal_picker"
            android:layout_marginTop="24dp"
            android:layout_marginBottom="24dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            picker:beforeDecimal="3"
            picker:afterDecimal="2"
            picker:initialNumber="0"
            picker:finalNumber="100"
            picker:textSize="24sp"/>
