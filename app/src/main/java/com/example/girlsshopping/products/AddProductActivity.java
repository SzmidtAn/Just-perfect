package com.example.girlsshopping.products;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.girlsshopping.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    String photoString;
    ViewTarget<ImageView, Bitmap> ph;
    Product product;
    int photo;
    private static final int CAMERA_REQUEST_CODE = 1234;
    private Uri fileUri;
    private File file;
    public static final int PICK_IMAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 999;


    int i;
    ImageView imageView;
    Button button;

    Drawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        Spinner categorySpinner = findViewById(R.id.expense_category);
        Spinner sizeSpinner = findViewById(R.id.productSize);
        Spinner brandSpinner = findViewById(R.id.productBrand);
        Spinner conditionSpinner = findViewById(R.id.productCondition);


        ProductCategory[] categories = ProductCategory.values();
        ArrayAdapter<ProductCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, categories);
        categorySpinner.setAdapter(adapter);

        // Spinner Drop down elements
        List<String> size = new ArrayList<String>();
        size.add("XS");
        size.add("S");
        size.add("M ");
        size.add("L");
        size.add("XL");

        // Creating adapter for spinner
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, size);
        // Drop down layout style - list view with radio button
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sizeSpinner.setAdapter(sizeAdapter);


        // Spinner Drop down elements
        List<String> brand = new ArrayList<String>();
        brand.add("Reserved");
        brand.add("H&M");
        brand.add("Nike");
        brand.add("Adidas");
        brand.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brand);
        // Drop down layout style - list view with radio button
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        brandSpinner.setAdapter(brandAdapter);


     // Spinner Drop down elements
        List<String> condition = new ArrayList<String>();
        condition.add("Nowe");
        condition.add("Dobry");
        condition.add("Idealny");
        condition.add("Mocno używane");

        // Creating adapter for spinner
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, condition);
        // Drop down layout style - list view with radio button
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        conditionSpinner.setAdapter(conditionAdapter);




        Button newExpenseButton = findViewById(R.id.add_expense);

        newExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpense();
            }
        });

    }

    private void addNewExpense() {
        EditText nameEditText = findViewById(R.id.expense_name);
        EditText descriptionEditText = findViewById(R.id.product_description);
        EditText priceEditText = findViewById(R.id.expense_price);
        Spinner categorySpinner = findViewById(R.id.expense_category);
        Spinner sizeSpinner = findViewById(R.id.productSize);
        Spinner brandSpinner = findViewById(R.id.productBrand);
        Spinner conditionSpinner = findViewById(R.id.productCondition);


        String title = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String size = sizeSpinner.getSelectedItem().toString();
        String brand = brandSpinner.getSelectedItem().toString();
        String condition = conditionSpinner.getSelectedItem().toString();


        ProductCategory category = (ProductCategory) categorySpinner.getSelectedItem();

        product = new Product( title,  description,  category, price, photoString, size, brand, condition);

        ProductRepository.addProduct(product);



        onStart();
        Toast.makeText(getApplicationContext(), "Artykuł został dodany poprawnie", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void makePhotoButtonPressed(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();



            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoString);


            startActivityForResult(intent, PICK_IMAGE);
        }
    }




    private File createImageFile() {

        String randomName = UUID.randomUUID().toString();

        File storageDir = new File(getExternalFilesDir(null), "wydatex_photos");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = null;
        image = new File(storageDir, randomName);

        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageView=findViewById(R.id.photo);

            fileUri=data.getData();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> resolvedIntentActivities = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;

            }


            photoString=fileUri.toString();





            Glide.with(this).asBitmap().load(photoString).centerCrop().into(imageView);





        }
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        photoString = directory.getAbsolutePath();
        return directory.getAbsolutePath();
    }

}