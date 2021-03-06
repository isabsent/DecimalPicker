package com.github.isabsent.decimalpicker;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalPicker extends RelativeLayout {
    private Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private OnClickListener mListener;
    private double initialNumber, finalNumber, lastNumber, currentNumber;
    private int beforeDecimal, afterDecimal;
    private String format;
    private EditText editText;
    private OnValueChangeListener onValueChangeListener;

    public DecimalPicker(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public DecimalPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public DecimalPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    //https://stackoverflow.com/questions/3282390/add-floating-point-value-to-android-resources-values
    private void initView(){
        View view = inflate(context, R.layout.decimal_picker, this);
        final Resources res = getResources();
        final int defaultColor = res.getColor(R.color.colorPrimaryDark);
        final int defaultTextColor = res.getColor(R.color.colorAccent);
        final Drawable defaultDrawable = res.getDrawable(R.drawable.decimal_picker_shape);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DecimalPicker, styleAttr, 0);

        beforeDecimal = a.getInt(R.styleable.DecimalPicker_beforeDecimal, 7);
        afterDecimal = a.getInt(R.styleable.DecimalPicker_afterDecimal, 3);
        initialNumber = a.getInt(R.styleable.DecimalPicker_initialNumber, 0);
        finalNumber = a.getInt(R.styleable.DecimalPicker_finalNumber, Integer.MAX_VALUE);
        float textSize = a.getDimension(R.styleable.DecimalPicker_textSize, 18);
        int color = a.getColor(R.styleable.DecimalPicker_backgroundColor, defaultColor);
        int textColor = a.getColor(R.styleable.DecimalPicker_textColor, defaultTextColor);
        Drawable drawable = a.getDrawable(R.styleable.DecimalPicker_backgroundDrawable);

        Button buttonMinus = (Button) view.findViewById(R.id.subtract_btn);
        Button buttonPlus = (Button) view.findViewById(R.id.add_btn);

        editText = (EditText) view.findViewById(R.id.number_counter);
        editText.setFilters(new InputFilter[]{new DecimalInputFilter(beforeDecimal, afterDecimal)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                double valueDouble = -1;
                try {
                    valueDouble = parseDouble(value.isEmpty() ? "0" : value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (valueDouble >= 0 && valueDouble <= finalNumber){
                    lastNumber = currentNumber;
                    currentNumber = valueDouble;
                    callListener(DecimalPicker.this);
                } else if (valueDouble > finalNumber){//https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
                    lastNumber = currentNumber;
                    currentNumber = finalNumber;
                    s.clear();
                    s.insert(0, String.valueOf(finalNumber));
                }
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    String num = ((EditText) v).getText().toString();
                    setNumber(num, true);
                }
                return false;
            }
        });

        LinearLayout mLayout = (LinearLayout) view.findViewById(R.id.decimal_picker_layout);

        buttonMinus.setTextColor(textColor);
        buttonPlus.setTextColor(textColor);
        editText.setTextColor(textColor);
        buttonMinus.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        buttonPlus.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        if (drawable == null)
            drawable = defaultDrawable;

        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
            if (Build.VERSION.SDK_INT > 16)
                mLayout.setBackground(drawable);
            else
                mLayout.setBackgroundDrawable(drawable);
        }

        editText.setText(String.valueOf(initialNumber));

        currentNumber = initialNumber;
        lastNumber = initialNumber;

        buttonMinus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View mView) {
                double num = parseDouble(editText.getText().toString());
                setNumber(String.valueOf(num - 1)/*, true*/);
            }
        });
        buttonPlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View mView) {
                double num = parseDouble(editText.getText().toString());
                setNumber(String.valueOf(num + 1)/*, true*/);
            }
        });
        a.recycle();
    }

    private void callListener(View view){
        if (mListener != null)
            mListener.onClick(view);

        if (onValueChangeListener != null && lastNumber != currentNumber)
            onValueChangeListener.onValueChange(this, lastNumber, currentNumber);
    }

    public String getNumber(){
        return String.valueOf(currentNumber);
    }

    public void setNumber(String number) {
        try {
            double n = parseDouble(number);
            if (n > finalNumber)
                n = finalNumber;

            if (n < initialNumber)
                n = initialNumber;

            if (format != null) {
                String num = String.format(getCurrentLocale(), format, n);
                num = removeTrailingZeroes(num);
                editText.setText(num);
            } else
                editText.setText(String.valueOf(number));

            lastNumber = currentNumber;
            currentNumber = parseDouble(editText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private double parseDouble(String str) throws NumberFormatException {
        return Double.parseDouble(str.replace(",","."));
    }

    private String removeTrailingZeroes(String num) {
        NumberFormat nf = NumberFormat.getInstance();
        if (nf instanceof DecimalFormat) {
            DecimalFormatSymbols sym = ((DecimalFormat) nf).getDecimalFormatSymbols();
            char decSeparator = sym.getDecimalSeparator();
            String[] split = num.split((decSeparator == '.' ? "\\" : "") + String.valueOf(decSeparator));
            if (split.length == 2 && split[1].replace("0", "").isEmpty())
                num = split[0];
        }
        return num;
    }

    public void setNumber(String number, boolean notifyListener){
        setNumber(number);
        if (notifyListener)
            callListener(this);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener){
        this.onValueChangeListener = onValueChangeListener;
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnValueChangeListener {
        void onValueChange(DecimalPicker view, double oldValue, double newValue);
    }

    public void setRange(int startingNumber, int endingNumber) {
        initialNumber = startingNumber;
        finalNumber = endingNumber;
    }

    public void setFormat(String format){
        this.format = format;
    }

    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            return getResources().getConfiguration().locale;
        }
    }

    public void setBeforeDecimal(int beforeDecimal) {
        this.beforeDecimal = beforeDecimal;
    }

    public void setAfterDecimal(int afterDecimal) {
        this.afterDecimal = afterDecimal;
    }
}
